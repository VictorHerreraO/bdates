import { UserId } from "../../core/api/CommonTypes";

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

export enum CircleTier {
    FREE
}
