export interface CircleMetaModel {
    id: string,
    name: string,
    owner: UserId,
    admins: { [key: UserId]: boolean },
    created_date: number,
    updated_date: number,
    tier: CircleTier,
    event_count: number,
}

export interface EventModel {
    id: string,
    name: string,
    year: number | undefined | null,
    day_of_month: number,
    month_of_year: number,
}

export interface EventMetaModel {
    id: string,
    created_date: number,
    created_by: UserId,
    updated_date?: number,
    updated_by?: UserId,
}

export enum CircleTier {
    FREE
}

export type UserId = string
