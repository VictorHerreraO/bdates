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
