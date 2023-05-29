import { CricleMetaModel } from "../../circles/api/CircleApi";

export interface UserModel {
  id: string,
  name: string,
  authId: string,
  circles: Array<CricleMetaModel>,
}
