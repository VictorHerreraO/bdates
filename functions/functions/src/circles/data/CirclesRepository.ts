import {
  CircleMetaModel,
  EventMetaModel,
  EventModel,
  UserId,
} from "../api/CircleApi";

export interface CirclesRepository {
  saveCircleMeta(model: CircleMetaModel): Promise<CircleMetaModel>

  getCircleMeta(circleId: string): Promise<CircleMetaModel>

  saveEvent(
    circleId: string,
    creator: UserId,
    model: EventModel
  ): Promise<EventModel>

  updateEvent(
    circleId: string,
    editor: UserId,
    model: EventModel
  ): Promise<void>

  deleteEvent(
    circleId: string,
    eventId: string,
  ): Promise<void>

  getCircleEvents(circleId: string): Promise<Array<EventModel>>

  getCircleEventMeta(circleId: string, eventId: string): Promise<EventMetaModel>
}
