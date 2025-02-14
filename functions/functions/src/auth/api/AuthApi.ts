export interface AuthCredentialsModel {
  id: string,
  email: string,
  pwdHash: string,
  userId: string
}

export interface UserTokenPairModel {
  auth: string,
  refresh: string
}

export interface AccessTokenModel {
  readonly issuer: string,
  readonly subject: string,
  readonly expirationTime: number,
  readonly issuedAt: number,
}
