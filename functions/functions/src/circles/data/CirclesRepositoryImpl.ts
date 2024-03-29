import { CirclesRepository, EventCount } from "./CirclesRepository";
import { CircleMetaModel } from "../api/CircleApi";
import { Reference, ServerValue } from "firebase-admin/database";
import { Logger } from "../../core/logging/Logger";
import {
  SnapshotToCircleMetaModelMapper,
} from "./mapping/SnapshotToCircleMetaModelMapper";
import { ModelUpdate } from "../../core/api/CommonTypes";

const REF_META = "meta";

/**
 * Circle DAO implementation
 */
export class CirclesRepositoryImpl implements CirclesRepository {
  private circlesRef: Reference;
  // TODO: change to interface
  private circleMetaModelMapper: SnapshotToCircleMetaModelMapper;

  /**
   * Current time millis
   */
  private get currentMillis(): number {
    return new Date().valueOf();
  }

  /**
   * Creates a new instance
   * @param {Reference} circlesRef reference to the circles database location
   * @param {SnapshotToCircleMetaModelMapper} circleMetaModelMapper
   */
  constructor(
    circlesRef: Reference,
    circleMetaModelMapper: SnapshotToCircleMetaModelMapper,
  ) {
    this.circlesRef = circlesRef;
    this.circleMetaModelMapper = circleMetaModelMapper;
  }

  /**
   * @param {CircleMetaModel} model model to be saved
   * @return {CircleMetaModel} model saved
   */
  async saveCircleMeta(model: CircleMetaModel): Promise<CircleMetaModel> {
    Logger.debug("registering new circle meta");
    const data = this.circleMetaModelMapper.reverseMap(model);
    const circleRef = await this.circlesRef.push();

    await this.circleMetaRef(circleRef.key!).set(data);

    model.id = circleRef.key!;
    model.updated_date = this.currentMillis;
    Logger.debug(`new circle registered with id: ${model.id}`);

    return model;
  }

  /**
   * @param {string} circleId ID of the circle to fetch
   * @return {CircleMetaModel} metadata for the circle if found
   */
  async getCircleMeta(
    circleId: string
  ): Promise<CircleMetaModel> {
    Logger.debug("looking for circle " + circleId);
    const snapshot = await this.circleMetaRef(circleId).once("value");

    if (!snapshot.exists()) {
      throw new Error(`no circle found with id ${circleId}`);
    }

    Logger.info("circle found");
    const model = this.circleMetaModelMapper.map(snapshot, circleId);
    return model;
  }

  /**
   * @param {string} circleId  ID of the circle
   * @param {EventCount} count update type to be done
   */
  async updateCircleEventCount(
    circleId: string,
    count: EventCount,
  ): Promise<void> {
    await this.circleMetaRef(circleId).update({
      event_count: ServerValue.increment(count),
      updated_date: this.currentMillis,
    } as CircleMetaModelUpdate);

    Logger.debug("event count updated");
  }

  /**
   * @param {string} circleId ID of the circle
   */
  async updateCircleUpdateDate(circleId: string): Promise<void> {
    await this.circleMetaRef(circleId).update({
      updated_date: this.currentMillis,
    } as CircleMetaModelUpdate);
    Logger.debug("event count updated");
  }

  /**
   * @param {string} circleId ID of the circle
   * @return {Reference} reference to the circle meta
   */
  private circleMetaRef(circleId: string): Reference {
    return this.circlesRef
      .child(circleId)
      .child(REF_META);
  }
}

type CircleMetaModelUpdate = ModelUpdate<CircleMetaModel>
