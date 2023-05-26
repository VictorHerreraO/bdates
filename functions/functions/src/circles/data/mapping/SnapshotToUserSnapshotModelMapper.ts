import { Mapper } from "../../../core/mapping/Mapper";
import { UserSnapshotModel } from "../../api/CircleApi";

/**
 * Maps the given object to a UserSnapshotModel
 */
export class SnapshotToUserSnapshotModelMapper implements
  Mapper<any, UserSnapshotModel> {
  /**
   * @param {any} value object to map
   * @return {UserSnapshotModel} model with data in object
   */
  map(value: any): UserSnapshotModel {
    const userSnapshot: UserSnapshotModel = {
      ...value,
    };
    return userSnapshot;
  }
}
