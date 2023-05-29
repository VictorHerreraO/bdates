import { Reference } from "firebase-admin/database";
import { UserModel } from "../api/UserApi";
import { UsersRepository } from "./UsersRepository";
import { SnapshotToUserModelMapper } from "./mapping/SnapshotToUserModelMapper";
import { Logger } from "../../core/logging/Logger";

/**
 * User Repository Implementation
 */
export class UsersRepositoryImpl implements UsersRepository {
  private usersRef: Reference;
  private userModelMapper: SnapshotToUserModelMapper;

  /**
   * Creates a new instance
   * @param {Reference} usersRef reference to the users database location
   * @param {SnapshotToUserModelMapper} userModelMapper
   */
  constructor(
    usersRef: Reference,
    userModelMapper: SnapshotToUserModelMapper
  ) {
    this.usersRef = usersRef;
    this.userModelMapper = userModelMapper as SnapshotToUserModelMapper;
  }

  /**
   * @param {UserModel} model model to be saved
   * @return {UserModel} model saved
   */
  async saveUserModel(model: UserModel): Promise<UserModel> {
    Logger.debug("registering new user");
    const data = this.userModelMapper.reverseMap(model);
    const userRef = await this.usersRef.push();

    await userRef.set(data);

    model.id = userRef.key!;
    Logger.debug(`new user registered with id: ${model.id}`);

    return model;
  }
}
