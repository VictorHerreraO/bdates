import { DataSnapshot } from "firebase-admin/database";
import { Mapper } from "../../../core/mapping/Mapper";
import { AuthCredentialsModel } from "../../api/AuthApi";

/**
 * Maps the given DataSnapshot to an AuthCredentialsModel
 */
export class SnapshotToAuthCredentialsModelMapper implements
  Mapper<DataSnapshot, AuthCredentialsModel> {
  /**
   * @param {DataSnapshot} value snapshot to map
   * @return {AuthCredentialsModel} model with data in snapshot
   */
  map(value: DataSnapshot): AuthCredentialsModel {
    const data = value.val();
    const model : AuthCredentialsModel = {
      id: value.key!,
      email: data.email,
      pwdHash: data.pwdHash,
      userId: data.userId,
    };
    return model;
  }
}
