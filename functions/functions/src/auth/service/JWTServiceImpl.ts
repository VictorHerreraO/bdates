import { AccessTokenModel } from "../api/AuthApi";
import { ApplicationConfig } from "../../core/api/ApplicationConfig";
import { JWTService } from "./JWTService";
import * as jwt from "jsonwebtoken";

// const TIME_ZONE = "America/Mexico_City";

/**
 * JWT Service Implementation
 */
export class JWTServiceImpl implements JWTService {
  private appConfig: ApplicationConfig;

  /**
   * Creates a new instance
   * @param {ApplicationConfig} appConfig current app config
   */
  constructor(appConfig: ApplicationConfig) {
    this.appConfig = appConfig;
  }

  /**
   * Creates a new access token
   * @param {string} userId user id to store on the token
   * @return {string} JWT access token as string
   */
  createAccessToken(userId: string): string {
    return jwt.sign(
      {},
      this.appConfig.jwtSecret,
      {
        issuer: this.appConfig.applicationName,
        subject: userId,
        expiresIn: this.getAccessTokenExpiration(),
      });
  }


  /**
   * Creates a new access token
   * @param {string} userId user id to store on the token
   * @return {string} JWT refresh token as string
   */
  createRefreshToken(userId: string): string {
    return jwt.sign(
      {},
      this.appConfig.jwtSecret,
      {
        issuer: this.appConfig.applicationName,
        subject: userId,
        expiresIn: this.getRefreshTokenExpiration(),
      });
  }

  /**
   * @param {string} token token to verify
   * @throws error if token is not valid
   */
  validateToken(token: string): AccessTokenModel {
    throw new Error("Method not implemented.");
  }

  /**
   * @return {number} duration of the access token in millis
   */
  private getAccessTokenExpiration(): string {
    if (this.appConfig.isDebug) {
      return "5m";
    } else {
      return "1h";
    }
  }

  /**
   * @return {number} duration of the access token in millis
   */
  private getRefreshTokenExpiration(): string {
    if (this.appConfig.isDebug) {
      return "10m";
    } else {
      return "30d";
    }
  }
}
