import { UserModel } from "../api/UserApi";

export interface UsersRepository {
  saveUserModel(model: UserModel): Promise<UserModel>

  getUserModel(userId: string): Promise<UserModel>

  updateUserModel(model: UserModel): Promise<void>
}
