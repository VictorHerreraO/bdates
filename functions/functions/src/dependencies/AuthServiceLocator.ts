/* eslint-disable max-len */
/* eslint-disable require-jsdoc */
import { Reference } from "firebase-admin/database";
import { AuthRepositoryImpl } from "../auth/data/AuthRepositoryImpl";
import { SnapshotToAuthCredentialsModelMapper } from "../auth/data/mapping/SnapshotToAuthCredentialsModelMapper";
import { references } from "../firebase/References";
import { AuthService } from "../auth/service/AuthService";
import { AuthRepository } from "../auth/data/AuthRepository";
import { AuthServiceImpl } from "../auth/service/AuthServiceImpl";
import { usersServiceLocator } from "./UsersServiceLocator";

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
      usersServiceLocator.getUsersRepository()
    );
  }
}

export const authServiceLocator = new AuthServiceLocator();

