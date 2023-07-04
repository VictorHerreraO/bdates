import { DataSnapshot } from "firebase-admin/database";
import { Mapper } from "../../../core/mapping/Mapper";
import { EventModel } from "../../api/EventTypes";

/**
 * Maps the given DataSnapshot to a EventModel
 */
export class SnapshotToEventModelMapper implements
Mapper<DataSnapshot, EventModel> {
  /**
   * @param {DataSnapshot} value snapshot to map
   * @return {EventModel} model with data in snapshot
   */
  map(value: DataSnapshot): EventModel {
    const data = value.val();
    const eventModel: EventModel = {
      id: value.key!,
      name: data.name || "",
      year: data.year || undefined,
      day_of_month: data.day_of_month || 0,
      month_of_year: data.month_of_year || 0,
    };
    return eventModel;
  }
}
