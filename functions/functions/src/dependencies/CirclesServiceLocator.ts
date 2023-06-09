/* eslint-disable max-len */
/* eslint-disable require-jsdoc */
import { Reference } from "firebase-admin/database";
import { CirclesRepositoryImpl } from "../circles/data/CirclesRepositoryImpl";
import { SnapshotToCircleMetaModelMapper } from "../circles/data/mapping/SnapshotToCircleMetaModelMapper";
import { SnapshotToEventMetaModelMapper } from "../circles/data/mapping/SnapshotToEventMetaModelMapper";
import { SnapshotToEventModelMapper } from "../circles/data/mapping/SnapshotToEventModelMapper";
import { references } from "../firebase/References";
import { CirclesRepository } from "../circles/data/CirclesRepository";
import { CirclesServiceImpl } from "../circles/service/CirclesServiceImpl";
import { CirclesService } from "../circles/service/CirclesService";
import { usersServiceLocator } from "./UsersServiceLocator";
import { ErrorToServiceErrorResponseMapper } from "../core/mapping/ErrorToServiceErrorResponseMapper";

/**
 * Dependencies for the circles module
 */
export class CirclesServiceLocator {
  private _snapshotToCircleMetaModelMapper: SnapshotToCircleMetaModelMapper | undefined = undefined;
  getSnapshotToCircleMetaModelMapper(): SnapshotToCircleMetaModelMapper {
    return this._snapshotToCircleMetaModelMapper || (this._snapshotToCircleMetaModelMapper = new SnapshotToCircleMetaModelMapper());
  }

  private _snapshotToEventMetaMapper: SnapshotToEventMetaModelMapper | undefined = undefined;
  getSnapshotToEventMetaMapper(): SnapshotToEventMetaModelMapper {
    return this._snapshotToEventMetaMapper || (this._snapshotToEventMetaMapper = new SnapshotToEventMetaModelMapper());
  }

  private _snapshotToEventMapper: SnapshotToEventModelMapper | undefined = undefined;
  getSnapshotToEventMapper(): SnapshotToEventModelMapper {
    return this._snapshotToEventMapper || (this._snapshotToEventMapper = new SnapshotToEventModelMapper());
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
      this.getSnapshotToEventMapper(),
      this.getSnapshotToEventMetaMapper()
    ));
  }

  private _circlesService: CirclesService | undefined = undefined;
  getCirclesService(): CirclesService {
    return this._circlesService || (this._circlesService = new CirclesServiceImpl(
      this.getCirclesRepository(),
      usersServiceLocator.getUsersRepository(),
    ));
  }

  private _errorToServiceErrorResponseMapper: ErrorToServiceErrorResponseMapper | undefined;
  getErrorToServiceErrorResponseMapper(): ErrorToServiceErrorResponseMapper {
    return this._errorToServiceErrorResponseMapper || (this._errorToServiceErrorResponseMapper = new ErrorToServiceErrorResponseMapper());
  }
}

const circlesServiceLocator = new CirclesServiceLocator();

export { circlesServiceLocator };
