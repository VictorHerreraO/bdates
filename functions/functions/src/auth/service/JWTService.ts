import { AccessTokenModel } from "../api/AuthApi";

export interface JWTService {
  createAccessToken(userId: string): string

  createRefreshToken(userId: string): string

  validateToken(token: string): AccessTokenModel
}
