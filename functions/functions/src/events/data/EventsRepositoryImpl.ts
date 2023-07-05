import { EventModel } from "../api/EventTypes";
import { EventsRepository } from "./EventsRepository";
import { Logger } from "../../core/logging/Logger";
import { Reference, DataSnapshot } from "firebase-admin/database";
import { ModelUpdate, UserId } from "../../core/api/CommonTypes";
import { EventModelMapper } from "./mapping/EventModelMapper";
import { CirclesRepository, EventCount } from "../../circles/data/CirclesRepository";

const REF_EVENTS = "events";

/**
 * Events repository implementation
 */
export class EventsRepositoryImpl implements EventsRepository {
  private circlesRef: Reference;
  private circlesRepo: CirclesRepository;
  private eventModelMapper: EventModelMapper;

  /**
   * Current time millis
   */
  private get currentMillis(): number {
    return new Date().valueOf();
  }

  /**
   * Creates a new instance
   * @param {Reference} circlesRef reference to the circles database location
   * @param {CirclesRepository} circlesRepo Circles Repository
   * @param {EventModelMapper} eventModelMapper
   */
  constructor(
    circlesRef: Reference,
    circlesRepo: CirclesRepository,
    eventModelMapper: EventModelMapper,
  ) {
    this.circlesRef = circlesRef;
    this.circlesRepo = circlesRepo;
    this.eventModelMapper = eventModelMapper;
  }

  /**
   * @param {string} circleId ID of the circe to fetch events for
   * @param {number?} sinceTimestamp return events modified after
   * the given timestamp
   * @return {Array<EventModel>} array of events in the circle
   */
  async getAllEvents(
    circleId: string,
    sinceTimestamp?: number,
  ): Promise<EventModel[]> {
    Logger.debug("looking for circle " + circleId);

    let snapshot: DataSnapshot;
    if (sinceTimestamp) {
      const sortKey: keyof EventModel = "updated_date";
      snapshot = await this.eventRef(circleId)
        .orderByChild(sortKey)
        .startAt(sinceTimestamp)
        .once("value");
    } else {
      snapshot = await this.eventRef(circleId).once("value");
    }

    if (!snapshot.exists()) {
      if (sinceTimestamp) {
        Logger.debug(`no updated events found since ${sinceTimestamp}`);
      } else {
        Logger.debug("no events found in circle");
      }
      return [];
    }

    const events: Array<EventModel> = [];
    // If all events were requested (sinceTimestamp != true)
    //   then filter out deleted events
    const removeDeleted = !sinceTimestamp;
    snapshot.forEach((child: DataSnapshot) => {
      const event = this.eventModelMapper.map(
        child.val(),
        child.key!,
      );

      if (removeDeleted && event.deleted) return;

      events.push(event);
    });

    return events;
  }

  /**
   * @param {string} circleId ID of the circle to which this event belongs
   * @param {UserId} creator ID of the user who created this event
   * @param {EventModel} model model to be saved
   * @return {EventModel} model saved
   */
  async saveEvent(
    circleId: string,
    creator: UserId,
    model: EventModel
  ): Promise<EventModel> {
    const now = this.currentMillis;
    model.updated_date = now;

    const eventRef = await this.eventRef(circleId).push();
    const eventId = eventRef.key!;
    const data = model as any;
    delete data.id;

    await eventRef.set(data);
    model.id = eventId;

    Logger.debug(`new event registered with id: ${eventId}`);

    await this.circlesRepo.updateCircleEventCount(
      circleId,
      EventCount.INCREASE
    );

    return model;
  }

  /**
   * @param {string} circleId ID of the circle to which this event belongs
   * @param {UserId} editor ID of the user who edits this event
   * @param {EventModel} model model to be saved
   * @return {void}
   */
  async updateEvent(
    circleId: string,
    editor: UserId,
    model: EventModel
  ): Promise<void> {
    const eventId = model.id;
    const eventSnapshot = await this.eventRef(circleId, eventId).once("value");
    if (!eventSnapshot.exists()) {
      throw new Error(`no event found with id ${eventId}`);
    }

    const now = this.currentMillis;
    model.updated_date = now;

    const data = model as any;
    delete data.id;

    await eventSnapshot.ref.set(data);

    Logger.debug("event updated!");
  }


  /**
   * @param {string} circleId ID of the circle to which this event belongs
   * @param {string} eventId ID of the event to be deleted
   * @return {void}
   */
  async deleteEvent(
    circleId: string,
    eventId: string,
  ): Promise<void> {
    const eventSnapshot = await this.eventRef(circleId, eventId).once("value");
    if (!eventSnapshot.exists()) {
      throw new Error(`no event found with id ${eventId}`);
    }

    const now = this.currentMillis;
    await eventSnapshot.ref.update({
      "name": null,
      "day_of_month": null,
      "month_of_year": null,
      "year": null,
      "deleted": true,
      "updated_date": now,
    } as EventModelUpdate);

    Logger.debug("event deleted!");

    await this.circlesRepo.updateCircleEventCount(
      circleId,
      EventCount.DECREASE
    );
  }

  /**
   * @param {string} circleId ID of the circle
   * @param {string?} eventId ID of the event. If falsy then returns a
   *  reference to all circle events
   * @return {Reference} reference to the event data
   */
  private eventRef(circleId: string, eventId?: string): Reference {
    const ref = this.circlesRef
      .child(circleId)
      .child(REF_EVENTS);
    if (eventId) {
      return ref.child(eventId);
    } else {
      return ref;
    }
  }
}

type EventModelUpdate = ModelUpdate<EventModel>
