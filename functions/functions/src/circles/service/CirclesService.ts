import {
  CircleMetaModel,
  EventMetaModel,
  EventModel,
  UserId,
} from "../api/CircleApi";

export interface CirclesService {
  createCircle(
    name: string,
    ownerId: string,
    tier?: string,
  ): Promise<CircleMetaModel>

  getCircleById(circleId: string): Promise<CircleMetaModel>

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

  getEventList(circleId: string): Promise<Array<EventModel>>


  getEventMeta(circleId: string, eventId: string): Promise<EventMetaModel>
}

export type EventParams = {
  name?: string,
  year?: number,
  day_of_month?: number,
  month_of_year?: number,
}
