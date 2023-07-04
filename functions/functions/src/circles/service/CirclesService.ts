import { CircleMetaModel } from "../api/CircleApi";

export interface CirclesService {
  createCircle(
    name: string,
    ownerId: string,
    tier?: string,
  ): Promise<CircleMetaModel>

  getCircleById(circleId: string): Promise<CircleMetaModel>
}
