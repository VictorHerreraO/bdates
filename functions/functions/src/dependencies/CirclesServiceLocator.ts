/* eslint-disable max-len */
/* eslint-disable require-jsdoc */
import { CirclesRepository } from "../circles/data/CirclesRepository";
import { CirclesRepositoryImpl } from "../circles/data/CirclesRepositoryImpl";
import { CirclesService } from "../circles/service/CirclesService";
import { CirclesServiceImpl } from "../circles/service/CirclesServiceImpl";
import { Reference } from "firebase-admin/database";
import { references } from "../firebase/References";
import { SnapshotToCircleMetaModelMapper } from "../circles/data/mapping/SnapshotToCircleMetaModelMapper";
import { usersServiceLocator } from "./UsersServiceLocator";

/**
 * Dependencies for the circles module
 */
export class CirclesServiceLocator {
  private _snapshotToCircleMetaModelMapper: SnapshotToCircleMetaModelMapper | undefined = undefined;
  getSnapshotToCircleMetaModelMapper(): SnapshotToCircleMetaModelMapper {
    return this._snapshotToCircleMetaModelMapper || (this._snapshotToCircleMetaModelMapper = new SnapshotToCircleMetaModelMapper());
  }

  private _circlesReference: Reference | undefined = undefined;
  getCirclesReference(): Reference {
    return this._circlesReference || (this._circlesReference = references.circles);
  }

  private _circlesRepository: CirclesRepository | undefined = undefined;
  getCirclesRepository(): CirclesRepository {
    return this._circlesRepository || (this._circlesRepository = new CirclesRepositoryImpl(
      this.getCirclesReference(),
      this.getSnapshotToCircleMetaModelMapper(),
    ));
  }

  private _circlesService: CirclesService | undefined = undefined;
  getCirclesService(): CirclesService {
    return this._circlesService || (this._circlesService = new CirclesServiceImpl(
      this.getCirclesRepository(),
      usersServiceLocator.getUsersRepository(),
    ));
  }
}

export const circlesServiceLocator = new CirclesServiceLocator();

export function getCirclesService(): CirclesService {
  return circlesServiceLocator.getCirclesService();
}
