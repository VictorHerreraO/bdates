import {
  CircleMetaModel,
  EventMetaModel,
  EventModel,
} from "../api/CircleApi";

export interface CirclesService {
  createCircle(
    name: string,
    ownerId: string,
    tier?: string,
  ): Promise<CircleMetaModel>

  getCircleById(circleId: string): Promise<CircleMetaModel>

  getEventList(circleId: string): Promise<Array<EventModel>>

  getEventMeta(circleId: string, eventId: string): Promise<EventMetaModel>
}
