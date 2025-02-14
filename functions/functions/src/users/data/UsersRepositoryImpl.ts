import { Logger } from "../../core/logging/Logger";
import { Reference } from "firebase-admin/database";
import { SnapshotToUserModelMapper } from "./mapping/SnapshotToUserModelMapper";
import { UserModel } from "../api/UserApi";
import { UsersRepository } from "./UsersRepository";

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

  /**
   * @param {string} userId ID of the user to fetched
   * @return {UserModel} user model if found
   */
  async getUserModel(userId: string): Promise<UserModel> {
    Logger.debug("looking for user " + userId);
    const snapshot = await this.userRef(userId).once("value");

    if (!snapshot.exists()) {
      throw new Error(`no user found with id ${userId}`);
    }

    Logger.info("user found");
    const model = this.userModelMapper.map(snapshot);
    return model;
  }

  /**
   * @param {UserModel} model model to be saved
   */
  async updateUserModel(model: UserModel): Promise<void> {
    Logger.debug("updating new user");
    const snapshot = await this.userRef(model.id).once("value");
    if (!snapshot.exists()) {
      throw new Error(`no user found with id ${model.id}`);
    }

    const data = this.userModelMapper.reverseMap(model);
    try {
      delete data.id;
      snapshot.ref.set(data);
      Logger.debug("user updated");
    } catch (error) {
      Logger.error(error);
      throw new Error(`unable to update user with id ${model.id}`);
    }
  }

  /**
   * @param {string} userId ID of the user
   * @return {Reference} reference to the user
   */
  private userRef(userId: string): Reference {
    return this.usersRef.child(userId);
  }
}
