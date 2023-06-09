import { DataSnapshot } from "firebase-admin/database";
import { CircleTier, CircleMetaModel } from "../../api/CircleApi";
import {
  SnapshotToUserSnapshotModelMapper,
} from "./SnapshotToUserSnapshotModelMapper";
import { BaseMapperImpl } from "../../../core/mapping/BaseMapperImpl";

/**
 * Maps the given DataSnapshot to a CircleMetaModel
 */
export class SnapshotToCircleMetaModelMapper extends
  BaseMapperImpl<DataSnapshot, CircleMetaModel> {
  private userSnapshotMapper: SnapshotToUserSnapshotModelMapper;

  /**
   * Creates a new instance
   * @param {SnapshotToUserSnapshotModelMapper} userSnapshotMapper
   */
  constructor(userSnapshotMapper: SnapshotToUserSnapshotModelMapper) {
    super();
    this.userSnapshotMapper = userSnapshotMapper;
  }

  /**
   * @param {DataSnapshot} value snapshot to map
   * @param {string} id id to use for model
   * @return {CircleMetaModel} model with data in snapshot
   */
  map(value: DataSnapshot, id?: string): CircleMetaModel {
    const data = value.val();
    const model: CircleMetaModel = {
      id: id || value.key!,
      name: data.name || "",
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
