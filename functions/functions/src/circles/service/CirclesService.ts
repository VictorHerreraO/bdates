import {
  CricleMetaModel, EventMetaModel, EventModel,
} from "../api/CircleApi";

export interface CirclesService {
  getCircleById(circleId: string): Promise<CricleMetaModel>

  getEventList(circleId: string): Promise<Array<EventModel>>

  getEventMeta(circleId: string, eventId: string): Promise<EventMetaModel>
}
