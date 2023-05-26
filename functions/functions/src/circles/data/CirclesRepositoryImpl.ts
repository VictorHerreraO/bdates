import { CirclesRepository } from "./CirclesRepository";
import { CricleMetaModel, EventModel, EventMetaModel } from "../api/CircleApi";
import { DataSnapshot, Reference } from "firebase-admin/database";
import { Logger } from "../../core/logging/Logger";
import { Mapper } from "../../core/mapping/Mapper";

const REF_META = "meta";
const REF_EVENTS = "events";
const REF_EVENTS_META = "events_meta";

/**
 * Circle DAO implementation
 */
export class CirclesRepositoryImpl implements CirclesRepository {
  private circlesRef: Reference;
  private circleMetaModelMapper: Mapper<DataSnapshot, CricleMetaModel>;
  private eventModelMapper: Mapper<DataSnapshot, EventModel>;
  private eventMetaModelMapper: Mapper<DataSnapshot, EventMetaModel>;

  /**
   * Creates a new instance
   * @param {Reference} circlesRef reference to the circles database location
   * @param {Mapper<DataSnapshot, CricleMetaModel>} circleMetaModelMapper
   * @param {Mapper<DataSnapshot, EventModel>} eventModelMapper
   * @param {Mapper<DataSnapshot, EventMetaModel>} eventMetaModelMapper
   */
  constructor(
    circlesRef: Reference,
    circleMetaModelMapper: Mapper<DataSnapshot, CricleMetaModel>,
    eventModelMapper: Mapper<DataSnapshot, EventModel>,
    eventMetaModelMapper: Mapper<DataSnapshot, EventMetaModel>,
  ) {
    this.circlesRef = circlesRef;
    this.circleMetaModelMapper = circleMetaModelMapper;
    this.eventModelMapper = eventModelMapper;
    this.eventMetaModelMapper = eventMetaModelMapper;
  }


  /**
   * @param {string} circleId ID of the circle to fetch
   * @return {CricleMetaModel} metadata for the circle if found
   */
  async getCircleMeta(
    circleId: string
  ): Promise<CricleMetaModel> {
    Logger.debug("looking for circle " + circleId);
    const snapshot = await this.circleMetaRef(circleId).once("value");

    if (!snapshot.exists()) {
      throw new Error(`no circle found with id ${circleId}`);
    }

    Logger.info("circle found");
    const model = this.circleMetaModelMapper.map(snapshot);
    return model;
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
   * @return {Reference} reference to the event meta data
   */
  private eventMetaRef(circleId: string, eventId: string): Reference {
    return this.circlesRef
      .child(circleId)
      .child(REF_EVENTS_META)
      .child(eventId);
  }
}
