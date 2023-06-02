import { CircleMetaModel, EventMetaModel, EventModel } from "../api/CircleApi";

export interface CirclesRepository {
  saveCircleMeta(model: CircleMetaModel): Promise<CircleMetaModel>

  getCircleMeta(circleId: string): Promise<CircleMetaModel>

  getCircleEvents(circleId: string): Promise<Array<EventModel>>

  getCircleEventMeta(circleId: string, eventId: string): Promise<EventMetaModel>
}
