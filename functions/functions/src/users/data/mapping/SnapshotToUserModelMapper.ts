import { DataSnapshot } from "firebase-admin/database";
import { BaseMapperImpl } from "../../../core/mapping/BaseMapperImpl";
import { UserModel } from "../../api/UserApi";

/**
 * Maps the given DataSnapshot to a UserModel
 */
export class SnapshotToUserModelMapper extends
  BaseMapperImpl<DataSnapshot, UserModel> {
  /**
   * @param {DataSnapshot} value snapshot to map
   * @return {UserModel} model with data in snapshot
   */
  map(value: DataSnapshot): UserModel {
    const data = value.val();
    const model: UserModel = {
      id: value.key!,
      name: data.name,
      authId: data.authId,
      circles: data.circles || [],
    };
    return model;
  }
}
