import { CirclesRepository } from "./CirclesRepository";
import {
  CircleMetaModel,
  EventModel,
  EventMetaModel,
  UserId,
} from "../api/CircleApi";
import { DataSnapshot, Reference, ServerValue } from "firebase-admin/database";
import { Logger } from "../../core/logging/Logger";
import { Mapper } from "../../core/mapping/Mapper";
import {
  SnapshotToCircleMetaModelMapper,
} from "./mapping/SnapshotToCircleMetaModelMapper";

const REF_META = "meta";
const REF_EVENTS = "events";
const REF_EVENTS_META = "events_meta";

/**
 * Circle DAO implementation
 */
export class CirclesRepositoryImpl implements CirclesRepository {
  private circlesRef: Reference;
  // TODO: change to interface
  private circleMetaModelMapper: SnapshotToCircleMetaModelMapper;
  private eventModelMapper: Mapper<DataSnapshot, EventModel>;
  private eventMetaModelMapper: Mapper<DataSnapshot, EventMetaModel>;

  /**
   * Creates a new instance
   * @param {Reference} circlesRef reference to the circles database location
   * @param {SnapshotToCircleMetaModelMapper} circleMetaModelMapper
   * @param {Mapper<DataSnapshot, EventModel>} eventModelMapper
   * @param {Mapper<DataSnapshot, EventMetaModel>} eventMetaModelMapper
   */
  constructor(
    circlesRef: Reference,
    circleMetaModelMapper: SnapshotToCircleMetaModelMapper,
    eventModelMapper: Mapper<DataSnapshot, EventModel>,
    eventMetaModelMapper: Mapper<DataSnapshot, EventMetaModel>,
  ) {
    this.circlesRef = circlesRef;
    this.circleMetaModelMapper = circleMetaModelMapper;
    this.eventModelMapper = eventModelMapper;
    this.eventMetaModelMapper = eventMetaModelMapper;
  }

  /**
   * @param {CircleMetaModel} model model to be saved
   * @return {CircleMetaModel} model saved
   */
  async saveCircleMeta(model: CircleMetaModel): Promise<CircleMetaModel> {
    Logger.debug("registering new circle meta");
    const data = this.circleMetaModelMapper.reverseMap(model);
    const circleRef = await this.circlesRef.push();

    await this.circleMetaRef(circleRef.key!).set(data);

    model.id = circleRef.key!;
    Logger.debug(`new circle registered with id: ${model.id}`);

    return model;
  }

  /**
   * @param {string} circleId ID of the circle to fetch
   * @return {CircleMetaModel} metadata for the circle if found
   */
  async getCircleMeta(
    circleId: string
  ): Promise<CircleMetaModel> {
    Logger.debug("looking for circle " + circleId);
    const snapshot = await this.circleMetaRef(circleId).once("value");

    if (!snapshot.exists()) {
      throw new Error(`no circle found with id ${circleId}`);
    }

    Logger.info("circle found");
    const model = this.circleMetaModelMapper.map(snapshot, circleId);
    return model;
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
    Logger.debug("looking for circle " + circleId);
    const circleSnapshot = await this.circleMetaRef(circleId).once("value");

    if (!circleSnapshot.exists()) {
      throw new Error(`no circle found with id ${circleId}`);
    }
    Logger.info("circle found");

    const eventRef = await this.circleEventsRef(circleId).push();
    const eventId = eventRef.key!;
    const data = model as any;
    delete data.id;

    await eventRef.set(data);
    model.id = eventId;

    Logger.debug(`new event registered with id: ${eventId}`);

    const metaRef = this.eventMetaRef(circleId, eventId);
    const metaModel: EventMetaModel = {
      id: "",
      created_date: new Date().valueOf(),
      created_by: creator,
    };
    const metaData = metaModel as any;
    delete metaData.id;

    await metaRef.set(metaData);
    Logger.debug("event metadata created");

    await circleSnapshot.ref.update({
      event_count: ServerValue.increment(1),
    } as CircleMetaModelUpdate);

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
    Logger.debug("looking for circle " + circleId);
    const circleSnapshot = await this.circleMetaRef(circleId).once("value");

    if (!circleSnapshot.exists()) {
      throw new Error(`no circle found with id ${circleId}`);
    }
    Logger.info("circle found");

    const eventId = model.id;
    const eventSnapshot = await this.eventRef(circleId, eventId).once("value");
    if (!eventSnapshot.exists()) {
      throw new Error(`no event found with id ${eventId}`);
    }

    const data = model as any;
    delete data.id;

    await eventSnapshot.ref.set(data);

    Logger.debug("event updated!");

    const metaRef = this.eventMetaRef(circleId, eventId);
    await metaRef.update({
      updated_date: new Date().valueOf(),
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
    Logger.debug("looking for circle " + circleId);
    const circleSnapshot = await this.circleMetaRef(circleId).once("value");

    if (!circleSnapshot.exists()) {
      throw new Error(`no circle found with id ${circleId}`);
    }
    Logger.info("circle found");

    const eventSnapshot = await this.eventRef(circleId, eventId).once("value");
    if (!eventSnapshot.exists()) {
      throw new Error(`no event found with id ${eventId}`);
    }

    await eventSnapshot.ref.remove();
    await this.eventMetaRef(circleId, eventId).remove();

    await circleSnapshot.ref.update({
      event_count: ServerValue.increment(-1),
    } as CircleMetaModelUpdate);
  }

  /**
   * @param {string} circleId ID of the circe to fetch events for
   * @return {Array<EventModel>} array of events in the circle
   */
  async getCircleEvents(
    circleId: string
  ): Promise<EventModel[]> {
    Logger.debug("looking for circle " + circleId);
    const snapshot = await this.circleEventsRef(circleId).once("value");

    if (!snapshot.exists()) {
      throw new Error(`no circle found with id ${circleId}`);
    }

    const events: Array<EventModel> = [];
    snapshot.forEach((child: DataSnapshot) => {
      events.push(this.eventModelMapper.map(child));
    });
    return events;
  }

  /**
   * @param {string} circleId ID of the circle which contains the event
   * @param {string} eventId ID of the circle to fetch
   */
  async getCircleEventMeta(
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
   * @return {Reference} reference to the circle meta
   */
  private circleMetaRef(circleId: string): Reference {
    return this.circlesRef
      .child(circleId)
      .child(REF_META);
  }

  /**
   * @param {string} circleId ID of the circle
   * @return {Reference} reference to the events in this circle
   */
  private circleEventsRef(circleId: string): Reference {
    return this.circlesRef
      .child(circleId)
      .child(REF_EVENTS);
  }

  /**
   * @param {string} circleId ID of the circle
   * @param {string} eventId ID of the event
   * @return {Reference} reference to the event data
   */
  private eventRef(circleId: string, eventId: string): Reference {
    return this.circlesRef
      .child(circleId)
      .child(REF_EVENTS)
      .child(eventId);
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

type CircleMetaModelUpdate = {
  [key in keyof CircleMetaModel]: unknown
}

type EventMetaModelUpdate = {
  [key in keyof EventMetaModel]: unknown
}
