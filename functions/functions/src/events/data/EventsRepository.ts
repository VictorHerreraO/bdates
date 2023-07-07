import { EventModel } from "../api/EventTypes";
import { UserId } from "../../core/api/CommonTypes";

export interface EventsRepository {
  getAllEvents(
    circleId: string,
    sinceTimestamp?: number,
  ): Promise<Array<EventModel>>

  saveEvent(
    circleId: string,
    creator: UserId,
    model: EventModel,
  ): Promise<EventModel>

  updateEvent(
    circleId: string,
    model: EventModel,
  ): Promise<void>

  deleteEvent(
    circleId: string,
    eventId: string,
  ): Promise<void>
}
