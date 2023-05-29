import { UserModel } from "../api/UserApi";

export interface UsersRepository {
  saveUserModel(model: UserModel): Promise<UserModel>
}
