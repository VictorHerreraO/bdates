import { EventModel, EventMetaModel } from "../api/EventTypes";
import { EventsRepository } from "./EventsRepository";
import { Logger } from "../../core/logging/Logger";
import { Mapper } from "../../core/mapping/Mapper";
import { Reference, DataSnapshot } from "firebase-admin/database";
import { ModelUpdate, UserId } from "../../core/api/CommonTypes";

const REF_EVENTS = "events";
const REF_EVENTS_META = "events_meta";

/**
 * Events repository implementation
 */
export class EventsRepositoryImpl implements EventsRepository {
  private circlesRef: Reference;
  private eventModelMapper: Mapper<DataSnapshot, EventModel>;
  private eventMetaModelMapper: Mapper<DataSnapshot, EventMetaModel>;

  /**
   * Current time millis
   */
  private get currentMillis(): number {
    return new Date().valueOf();
  }

  /**
   * Creates a new instance
   * @param {Reference} circlesRef reference to the circles database location
   * @param {Mapper<DataSnapshot, EventModel>} eventModelMapper
   * @param {Mapper<DataSnapshot, EventMetaModel>} eventMetaModelMapper
   */
  constructor(
    circlesRef: Reference,
    eventModelMapper: Mapper<DataSnapshot, EventModel>,
    eventMetaModelMapper: Mapper<DataSnapshot, EventMetaModel>,
  ) {
    this.circlesRef = circlesRef;
    this.eventModelMapper = eventModelMapper;
    this.eventMetaModelMapper = eventMetaModelMapper;
  }

  /**
   * @param {string} circleId ID of the circe to fetch events for
   * @return {Array<EventModel>} array of events in the circle
   */
  async getAllEvents(
    circleId: string
  ): Promise<EventModel[]> {
    Logger.debug("looking for circle " + circleId);

    const fiveMinAgo = this.currentMillis - (1_000 * 60 * 5);
    const sortKey: keyof EventModel = "updated_date";
    const snapshot = await this.eventRef(circleId)
      .orderByChild(sortKey)
      .startAt(fiveMinAgo)
      .once("value");

    if (!snapshot.exists()) {
      throw new Error(`no updated events found since ${fiveMinAgo}`);
    }

    const events: Array<EventModel> = [];
    snapshot.forEach((child: DataSnapshot) => {
      events.push(this.eventModelMapper.map(child));
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

    const metaRef = this.eventMetaRef(circleId, eventId);
    const metaModel: EventMetaModel = {
      id: "",
      created_date: now,
      created_by: creator,
    };
    const metaData = metaModel as any;
    delete metaData.id;

    await metaRef.set(metaData);
    Logger.debug("event metadata created");

    // TODO: Replace with repository
    /*
    await circleSnapshot.ref.update({
      event_count: ServerValue.increment(1),
      updated_date: now,
    } as CircleMetaModelUpdate);
    */

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

    const metaRef = this.eventMetaRef(circleId, eventId);
    await metaRef.update({
      updated_date: now,
      updated_by: editor,
    } as EventMetaModelUpdate);
    Logger.debug("event metadata updated!");
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
      "deleted": true,
      "updated_date": now,
    } as EventModelUpdate);
    await this.eventMetaRef(circleId, eventId).update({
      "updated_date": now,
    } as EventMetaModelUpdate);

    // TODO: replace with repository
    /*
    await circleSnapshot.ref.update({
      event_count: ServerValue.increment(-1),
    } as CircleMetaModelUpdate);
    */
  }

  /**
   * @param {string} circleId ID of the circle which contains the event
   * @param {string} eventId ID of the circle to fetch
   */
  async getEventMeta(
    circleId: string,
    eventId: string
  ): Promise<EventMetaModel> {
    Logger.debug(`looking for event ${eventId} in circle ${circleId}`);
    const snapshot = await this.eventMetaRef(circleId, eventId).once("value");

    if (!snapshot.exists()) {
      throw new Error(
        `no event found with id ${eventId} in circle ${circleId}`
      );
    }

    const event = this.eventMetaModelMapper.map(snapshot);
    return event;
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

  /**
   * @param {string} circleId ID of the circle
   * @param {string} eventId ID of the event
   * @return {Reference} reference to the event meta data
   */
  private eventMetaRef(circleId: string, eventId: string): Reference {
    return this.circlesRef
      .child(circleId)
      .child(REF_EVENTS_META)
      .child(eventId);
  }
}

type EventMetaModelUpdate = ModelUpdate<EventMetaModel>

type EventModelUpdate = ModelUpdate<EventModel>
