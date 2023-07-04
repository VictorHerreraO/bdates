import { UserId } from "../../core/api/CommonTypes";
import { EventMetaModel, EventModel } from "../api/EventTypes";

export interface EventsService {
  createEvent(
    circleId: string,
    creator: UserId,
    params: EventParams
  ): Promise<EventModel>

  updateEvent(
    circleId: string,
    eventId: string,
    editor: UserId,
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

  getEventMeta(circleId: string, eventId: string): Promise<EventMetaModel>
}

export type EventParams = {
  name?: string,
  year?: number,
  day_of_month?: number,
  month_of_year?: number,
}
