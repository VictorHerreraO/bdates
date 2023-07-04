import { EventModel, EventMetaModel } from "../api/EventTypes";
import { EventParams, EventsService } from "./EventsService";
import { EventsRepository } from "../data/EventsRepository";
import { IllegalArgumentError } from "../../core/api/Error";
import { UserId } from "../../core/api/CommonTypes";

/**
 * Circles Service Implementation
 */
export class EventsServiceImpl implements EventsService {
  private eventsRepo: EventsRepository;

  /**
   * Creates a new instance
   * @param {EventsRepository} eventsRepo
   */
  constructor(
    eventsRepo: EventsRepository,
  ) {
    this.eventsRepo = eventsRepo;
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
    return this.eventsRepo.saveEvent(circleId, creator, event);
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
    return this.eventsRepo.updateEvent(circleId, editor, event);
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

    return this.eventsRepo.deleteEvent(circleId, eventId);
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
    return this.eventsRepo.getAllEvents(circleId);
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
    return this.eventsRepo.getEventMeta(circleId, eventId);
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
