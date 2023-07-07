import { AuthCredentialsModel } from "../api/AuthApi";

export interface AuthRepository {
  getAuthCredentialsModel(authId: string): Promise<AuthCredentialsModel>

  updateAuthCredentialsModel(
    model: AuthCredentialsModel
  ): Promise<void>
}
