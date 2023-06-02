export interface CircleMetaModel {
    id: string,
    name: string,
    owner: UserSnapshotModel,
    admins: Array<UserSnapshotModel>,
    created_date: number,
    updated_date: number,
    tier: CircleTier,
    event_count: number,
}

export interface EventModel {
    id: string,
    name: string,
    year: number,
    day_of_month: number,
    month_of_year: number,
}

export interface EventMetaModel {
    id: string,
    created_date: number,
    created_by: UserSnapshotModel,
    updated_date: number,
    updated_by: UserSnapshotModel,
}

export interface UserSnapshotModel {
    id: string,
    name: string
}

export enum CircleTier {
    FREE
}
