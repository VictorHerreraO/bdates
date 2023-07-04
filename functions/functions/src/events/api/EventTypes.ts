import { UserId } from "../../core/api/CommonTypes";

export interface EventModel {
  id: string,
  name: string,
  year: number | undefined | null,
  day_of_month: number,
  month_of_year: number,
  updated_date?: number,
  deleted?: boolean,
}

export interface EventMetaModel {
  id: string,
  created_date: number,
  created_by: UserId,
  updated_date?: number,
  updated_by?: UserId,
}
