import { DataSnapshot } from "firebase-admin/database";
import { Mapper } from "../../../core/mapping/Mapper";
import { EventMetaModel } from "../../api/EventTypes";

/**
 * Maps the given DataSnapshot to a CircleMetaModel
 */
export class SnapshotToEventMetaModelMapper implements
  Mapper<DataSnapshot, EventMetaModel> {
  /**
   * @param {DataSnapshot} value snapshot to map
   * @return {EventMetaModel} model with data in snapshot
   */
  map(value: DataSnapshot): EventMetaModel {
    const data = value.val();
    const eventMetaModel: EventMetaModel = {
      id: value.key!,
      created_date: data.created_date,
      created_by: data.created_by,
      updated_date: data.created_date,
      updated_by: data.updated_by,
    };
    return eventMetaModel;
  }
}
