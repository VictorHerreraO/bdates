export interface EventModel {
  id: string,
  name: string,
  year: number | undefined | null,
  day_of_month: number,
  month_of_year: number,
  updated_date?: number,
  deleted?: boolean,
}
