import {
  CircleMetaModel,
  CircleTier,
  EventMetaModel,
  EventModel,
} from "../api/CircleApi";
import { CirclesRepository } from "../data/CirclesRepository";
import { CirclesService } from "./CirclesService";
import { UsersRepository } from "../../users/data/UsersRepository";

/**
 * Circles Service Implementation
 */
export class CirclesServiceImpl implements CirclesService {
  private circlesRepo: CirclesRepository;
  private usersRepo: UsersRepository;

  /**
   * Creates a new instance
   * @param {CirclesRepository} circlesRepo
   * @param {UsersRepository} usersRepo
   */
  constructor(
    circlesRepo: CirclesRepository,
    usersRepo: UsersRepository
  ) {
    this.circlesRepo = circlesRepo;
    this.usersRepo = usersRepo;
  }

  /**
   * @param {string} name name for the new circle
   * @param {string} ownerId ID of the user who will own and administrate
   * the new circle
   * @param {CircleTier} tier tier for this circle. FREE by default
   */
  async createCircle(
    name: string,
    ownerId: string,
    tier?: string
  ): Promise<CircleMetaModel> {
    const safeName = (name || "").trim();
    if (!safeName) {
      throw new Error("name is required for circle");
    }

    const owner = await this.usersRepo.getUserModel(ownerId);
    // TODO: perform any validations for the user

    // TODO: perform validations on circle tier

    const now = new Date().valueOf();
    const newCircle = await this.circlesRepo.saveCircleMeta({
      id: "",
      name: safeName,
      owner: owner.id,
      admins: {},
      event_count: 0,
      created_date: now,
      updated_date: now,
      tier: CircleTier.FREE,
    });

    owner.circles.push(newCircle.id);
    this.usersRepo.updateUserModel(owner);

    return newCircle;
  }

  /**
   * @param {string} circleId ID of the circle to fetch
   * @return {CircleMetaModel} metadata for the circle if found
   */
  public async getCircleById(
    circleId: string
  ): Promise<CircleMetaModel> {
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
