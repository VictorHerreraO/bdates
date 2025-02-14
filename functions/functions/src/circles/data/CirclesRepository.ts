import { CircleMetaModel } from "../api/CircleApi";

export interface CirclesRepository {
  saveCircleMeta(model: CircleMetaModel): Promise<CircleMetaModel>

  getCircleMeta(circleId: string): Promise<CircleMetaModel>

  updateCircleEventCount(
    circleId: string,
    count: EventCount,
  ): Promise<void>

  updateCircleUpdateDate(
    circleId: string,
  ): Promise<void>
}

export enum EventCount {
  INCREASE = 1,
  DECREASE = -1
}
