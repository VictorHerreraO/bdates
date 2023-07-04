import { CircleMetaModel } from "../api/CircleApi";

export interface CirclesRepository {
  saveCircleMeta(model: CircleMetaModel): Promise<CircleMetaModel>

  getCircleMeta(circleId: string): Promise<CircleMetaModel>
}
