import { DataSnapshot } from "firebase-admin/database";
import { Mapper } from "../../../core/mapping/Mapper";
import { CircleTier, CricleMetaModel } from "../../api/CircleApi";
import {
  SnapshotToUserSnapshotModelMapper,
} from "./SnapshotToUserSnapshotModelMapper";

/**
 * Maps the given DataSnapshot to a CircleMetaModel
 */
export class SnapshotToCircleMetaModelMapper implements
  Mapper<DataSnapshot, CricleMetaModel> {
  private userSnapshotMapper: SnapshotToUserSnapshotModelMapper;

  /**
   * Creates a new instance
   * @param {SnapshotToUserSnapshotModelMapper} userSnapshotMapper
   */
  constructor(userSnapshotMapper: SnapshotToUserSnapshotModelMapper) {
    this.userSnapshotMapper = userSnapshotMapper;
  }

  /**
   * @param {DataSnapshot} value snapshot to map
   * @return {CricleMetaModel} model with data in snapshot
   */
  map(value: DataSnapshot): CricleMetaModel {
    const data = value.val();
    const model: CricleMetaModel = {
      id: value.key!,
      owner: this.userSnapshotMapper.map(data.owner || {}),
      admins: (Array.isArray(data.admins) ? data.admins : []).map(
        (admin: any) => this.userSnapshotMapper.map(admin)
      ),
      created_date: data.created_date,
      updated_date: data.updated_date,
      tier: this.mapToCircleTier(data.tier || ""),
      event_count: data.event_count,
    };
    return model;
  }

  /**
   * @param {string} value string representation of the CircleTier value
   * @return {CircleTier} the mapped object
   */
  private mapToCircleTier(value: string): CircleTier {
    const tier = CircleTier.FREE;
    // TODO: map other tiers
    return tier;
  }
}
