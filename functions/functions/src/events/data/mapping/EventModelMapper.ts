import { EventModel } from "../../api/EventTypes";
import { Mapper } from "../../../core/mapping/Mapper";

export type EventModelMapper = Mapper<any, EventModel>

const DELETED_STRING = "";
const DELETED_NUMBER = -1;

/**
 * Maps the given object to a EventModel
 */
export class EventModelMapperImpl implements EventModelMapper {
  /**
   * @param {any} value snapshot to map
   * @param {string} id ID for this model
   * @return {EventModel} model with data in snapshot
   */
  map(value: any, id?: string): EventModel {
    if (value.deleted) {
      // Stub for deleted events
      return {
        id: id!,
        deleted: true,
        name: DELETED_STRING,
        day_of_month: DELETED_NUMBER,
        month_of_year: DELETED_NUMBER,
        year: undefined,
      };
    }

    return {
      id: id!,
      name: value.name || "",
      year: value.year || undefined,
      day_of_month: value.day_of_month || 0,
      month_of_year: value.month_of_year || 0,
      // Skip updated_date
    };
  }
}
