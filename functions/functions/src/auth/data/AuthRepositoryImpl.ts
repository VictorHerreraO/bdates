/* eslint-disable max-len */
import { DataSnapshot, Reference } from "firebase-admin/database";
import { AuthCredentialsModel } from "../api/AuthApi";
import { AuthRepository } from "./AuthRepository";
import { Mapper } from "../../core/mapping/Mapper";
import { Logger } from "../../core/logging/Logger";

/**
 * Auth Repository Implementation
*/
export class AuthRepositoryImpl implements AuthRepository {
  private authRef: Reference;
  private authCredentialsMapper: Mapper<DataSnapshot, AuthCredentialsModel>;

  /**
   * Creates a new instance
   * @param {Reference} authRef reference to the auth database location
   * @param {Mapper<DataSnapshot, AuthCredentialsModel>} authCredentialsModelMapper,
   */
  constructor(
    authRef: Reference,
    authCredentialsModelMapper: Mapper<DataSnapshot, AuthCredentialsModel>,
  ) {
    this.authRef = authRef;
    this.authCredentialsMapper = authCredentialsModelMapper;
  }

  /**
   * @param {string} authId ID of the auth credentials to fetch
   */
  async getAuthCredentialsModel(authId: string): Promise<AuthCredentialsModel> {
    Logger.debug(`looking for credentials ${authId}`);
    const snapshot = await this.authCredentialsRef(authId).once("value");

    if (!snapshot.exists()) {
      throw new Error(`no credentials found with id ${authId}`);
    }

    Logger.info("credentials found");
    const model = this.authCredentialsMapper.map(snapshot);
    return model;
  }

  /**
   * @param {AuthCredentialsModel} model model to be updated
   */
  async updateAuthCredentialsModel(
    model: AuthCredentialsModel
  ): Promise<void> {
    const authId = model.id;
    Logger.debug(`looking for credentials ${authId}`);
    const snapshot = await this.authCredentialsRef(authId).once("value");

    const data: any = model;
    delete data.id;

    try {
      snapshot.ref.set(data);
      Logger.debug("crdentials updated");
    } catch (error) {
      Logger.error(error);
      throw new Error(`unable to set credentials for ${authId}`);
    }
  }

  /**
   * @param {string} authId ID of the auth credentials
   * @return {Reference} reference to the auth credentials
   */
  private authCredentialsRef(authId: string): Reference {
    return this.authRef.child(authId);
  }
}
