import { CirclesRepository } from "../data/CirclesRepository";
import { CirclesService } from "./CirclesService";
import { CricleMetaModel, EventModel, EventMetaModel } from "../api/CircleApi";

/**
 * Circles Service Implementation
 */
export class CirclesServiceImpl implements CirclesService {
  private circlesRepo : CirclesRepository;

  /**
   * Creates a new instance
   * @param {CirclesRepository} circlesRepo
   */
  constructor(circlesRepo: CirclesRepository) {
    this.circlesRepo = circlesRepo;
  }

  /**
   * @param {string} circleId ID of the circle to fetch
   * @return {CricleMetaModel} metadata for the circle if found
   */
  public async getCircleById(
    circleId: string
  ): Promise<CricleMetaModel> {
    if (!circleId) {
      throw new Error("invalid circleId");
    }
    return this.circlesRepo.getCircleMeta(circleId);
  }

  /**
   * @param {string} circleId ID of the circe to fetch events for
   * @return {Array<EventModel>} array of events in the circle
   */
  public async getEventList(
    circleId: string
  ): Promise<Array<EventModel>> {
    if (!circleId) {
      throw new Error("invalid circleId");
    }
    return this.circlesRepo.getCircleEvents(circleId);
  }

  /**
   * @param {string} circleId ID of the circle which contains the event
   * @param {string} eventId ID of the circle to fetch
   */
  public async getEventMeta(
    circleId: string,
    eventId: string
  ): Promise<EventMetaModel> {
    if (!circleId) {
      throw new Error("invalid circleID");
    }
    if (!eventId) {
      throw new Error("invalid eventId");
    }
    return this.circlesRepo.getCircleEventMeta(circleId, eventId);
  }
}
