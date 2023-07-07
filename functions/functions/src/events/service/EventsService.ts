import { UserId } from "../../core/api/CommonTypes";
import { EventModel } from "../api/EventTypes";

export interface EventsService {
  createEvent(
    circleId: string,
    creator: UserId,
    params: EventParams
  ): Promise<EventModel>

  updateEvent(
    circleId: string,
    eventId: string,
    params: EventParams,
  ): Promise<void>

  deleteEvent(
    circleId: string,
    eventId: string,
  ): Promise<void>

  getEventList(
    circleId: string,
    sinceTimestamp?: string,
  ): Promise<Array<EventModel>>
}

export type EventParams = {
  name?: string,
  year?: number,
  day_of_month?: number,
  month_of_year?: number,
}
