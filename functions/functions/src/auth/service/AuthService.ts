import { UserTokenPairModel } from "../api/AuthApi";

export interface AuthService {
  registerUser(
    email: string,
    password: string,
  ): Promise<UserTokenPairModel>
}
