import { UserTokenPairModel } from "../api/AuthApi";

export interface AuthService {
  registerUser(
    userName: string,
    email: string,
    password: string,
  ): Promise<UserTokenPairModel>

  loginUser(
    email: string,
    password: string
  ): Promise<UserTokenPairModel>
}
