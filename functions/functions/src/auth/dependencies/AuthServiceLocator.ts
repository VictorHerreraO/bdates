/* eslint-disable max-len */
/* eslint-disable require-jsdoc */
import { Reference } from "firebase-admin/database";
import { AuthRepositoryImpl } from "../data/AuthRepositoryImpl";
import { SnapshotToAuthCredentialsModelMapper } from "../data/mapping/SnapshotToAuthCredentialsModelMapper";
import { references } from "../../firebase/References";
import { AuthService } from "../service/AuthService";
import { AuthRepository } from "../data/AuthRepository";
import { AuthServiceImpl } from "../service/AuthServiceImpl";

/**
 * Dependencies for the auth module
 */
export class AuthServiceLocator {
  private _snapshotToAuthCredentialsMapper: SnapshotToAuthCredentialsModelMapper | undefined;
  getSnapshotToAuthCredentialsMapper(): SnapshotToAuthCredentialsModelMapper {
    return this._snapshotToAuthCredentialsMapper ??= new SnapshotToAuthCredentialsModelMapper();
  }

  private _authReference: Reference | undefined;
  getAuthReference(): Reference {
    return this._authReference ??= (references.auth);
  }

  private _authRepository: AuthRepository | undefined;
  getAuthRepository(): AuthRepository {
    return this._authRepository ??= new AuthRepositoryImpl(
      this.getAuthReference(),
      this.getSnapshotToAuthCredentialsMapper(),
    );
  }

  private _authService: AuthService | undefined;
  getAuthService(): AuthService {
    return this._authService ??= new AuthServiceImpl(
      this.getAuthRepository(),
    );
  }
}

export const authServiceLocator = new AuthServiceLocator();

