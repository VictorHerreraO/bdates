import { CricleMetaModel, EventMetaModel, EventModel } from "../api/CircleApi";

export interface CirclesRepository {
  getCircleMeta(circleId: string): Promise<CricleMetaModel>

  getCircleEvents(circleId: string): Promise<Array<EventModel>>

  getCircleEventMeta(circleId: string, eventId: string): Promise<EventMetaModel>
}
