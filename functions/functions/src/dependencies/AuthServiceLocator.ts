/* eslint-disable max-len */
/* eslint-disable require-jsdoc */
import { ApplicationConfig } from "../core/api/ApplicationConfig";
import { applicationConfig } from "../firebase/Application";
import { AuthRepository } from "../auth/data/AuthRepository";
import { AuthRepositoryImpl } from "../auth/data/AuthRepositoryImpl";
import { AuthService } from "../auth/service/AuthService";
import { AuthServiceImpl } from "../auth/service/AuthServiceImpl";
import { JWTService } from "../auth/service/JWTService";
import { JWTServiceImpl } from "../auth/service/JWTServiceImpl";
import { Reference } from "firebase-admin/database";
import { references } from "../firebase/References";
import { SnapshotToAuthCredentialsModelMapper } from "../auth/data/mapping/SnapshotToAuthCredentialsModelMapper";
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
      this.getJWTService(),
      this.getAuthRepository(),
      usersServiceLocator.getUsersRepository()
    );
  }

  getApplicationConfig(): ApplicationConfig {
    return applicationConfig;
  }

  private _jwtService: JWTService | undefined;
  getJWTService(): JWTService {
    return this._jwtService ??= new JWTServiceImpl(
      this.getApplicationConfig(),
    );
  }
}

export const authServiceLocator = new AuthServiceLocator();

