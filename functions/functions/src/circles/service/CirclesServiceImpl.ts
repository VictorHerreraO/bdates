import {
  CircleMetaModel,
  CircleTier,
  EventMetaModel,
  EventModel,
  UserId,
} from "../api/CircleApi";
import { CirclesRepository } from "../data/CirclesRepository";
import { CirclesService, EventParams } from "./CirclesService";
import { UsersRepository } from "../../users/data/UsersRepository";
import { IllegalArgumentError } from "../../core/api/Error";

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
   * @param {string} circleId ID of the circle to which this event belongs
   * @param {UserId} creator ID of the user who creates this event
   * @param {EventParams} params properties of the event to be created
   */
  async createEvent(
    circleId: string,
    creator: UserId,
    params: EventParams
  ): Promise<EventModel> {
    if (!circleId) {
      throw new IllegalArgumentError("circleId");
    }

    const event = this.validateEventParams(params);
    return this.circlesRepo.saveEvent(circleId, creator, event);
  }

  /**
   * @param {string} circleId ID of the circle to which this event belongs
   * @param {string} eventId ID of the event to update
   * @param {string} editor ID of the user who edits this event
   * @param {EventParams} params poperties of the event to be updated
   * @return {void}
   */
  updateEvent(
    circleId: string,
    eventId: string,
    editor: UserId,
    params: EventParams,
  ): Promise<void> {
    if (!circleId || !eventId) {
      throw new IllegalArgumentError(!circleId ? "circleId" : "eventId");
    }

    const event = this.validateEventParams(params);
    event.id = eventId;
    return this.circlesRepo.updateEvent(circleId, editor, event);
  }

  /**
   * @param {string} circleId ID of the circle to which this event belongs
   * @param {string} eventId ID of the event to update
   * @return {void}
   */
  deleteEvent(
    circleId: string,
    eventId: string,
  ): Promise<void> {
    if (!circleId || !eventId) {
      throw new IllegalArgumentError(!circleId ? "circleId" : "eventId");
    }

    return this.circlesRepo.deleteEvent(circleId, eventId);
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

  /**
   * @param {EventParams} params
   * @return {EventModel} if provided params are valid
   */
  private validateEventParams(params: EventParams): EventModel {
    const safeName = (params.name || "").trim();
    if (!safeName) {
      throw new IllegalArgumentError("name");
    }

    const safeMonth = params.month_of_year || -1;
    if (safeMonth < 1 || safeMonth > 12) {
      throw new IllegalArgumentError("month_of_year", "> 0 && <= 12");
    }

    let safeYear: number | null = params.year || -1;
    if (safeYear < 1900 || safeYear > 2100) {
      safeYear = null;
    }

    const now = new Date();
    const referenceYear = safeYear || now.getFullYear();
    const daysInMonth = new Date(referenceYear, safeMonth, 0).getDate();

    const safeDay = params.day_of_month || -1;
    if (safeDay < 1 || safeDay > daysInMonth) {
      throw new IllegalArgumentError(
        "day_of_month",
        `> 0 && <= ${daysInMonth}`
      );
    }

    return {
      id: "",
      name: safeName,
      day_of_month: safeDay,
      month_of_year: safeMonth,
      year: safeYear,
    };
  }
}
