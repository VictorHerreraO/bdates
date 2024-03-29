import {
  AccessTokenModel,
  AuthCredentialsModel,
  UserTokenPairModel,
} from "../api/AuthApi";
import { AuthRepository } from "../data/AuthRepository";
import { AuthService } from "./AuthService";
import { JWTService } from "./JWTService";
import { Logger } from "../../core/logging/Logger";
import { UsersRepository } from "../../users/data/UsersRepository";
import { Validations } from "./Validations";
import bcrypt = require("bcrypt");
import crypto = require("crypto");

const HASH_MD5 = "md5";
const DIGEST_HEX = "hex";
const SALT_ROUNDS = 10;

/**
 * Auth Service Implementation
 */
export class AuthServiceImpl implements AuthService {
  private tokenService: JWTService;
  private authRepo: AuthRepository;
  private usersRepo: UsersRepository;

  /**
   * Creates a new instance
   * @param {JWTService} tokenService
   * @param {AuthRepository} authRepo
   * @param {UsersRepository} usersRepo
   */
  constructor(
    tokenService: JWTService,
    authRepo: AuthRepository,
    usersRepo: UsersRepository
  ) {
    this.tokenService = tokenService;
    this.authRepo = authRepo;
    this.usersRepo = usersRepo;
  }

  /**
   * Registers the the given username, email and password combination as
   * a new user. Checks for existing auth credentials and creates new ones
   * if there are none found.
   *
   * Creates the user entry and returns a pair of JWT tokens for auth
   * @param {string} userName user name
   * @param {string} email user email
   * @param {string} password user password
   * @return {Promise<UserTokenPairModel>} JWT token pair for auth
   */
  public async registerUser(
    userName: string,
    email: string,
    password: string,
  ): Promise<UserTokenPairModel> {
    const safeName = (userName || "").trim();
    if (!safeName) {
      throw new Error(`"${safeName}" is not a valid user name`);
    }

    const safeEmail = this.trimAndLowerCaseString(email || "");
    if (Validations.isInvalidEmail(safeEmail)) {
      throw new Error(`"${safeEmail}" is not a valid email`);
    }

    const safePwd = (password || "").trim();
    if (Validations.isInvalidPassword(safePwd)) {
      throw new Error(`"${safePwd}" is not a valid password`);
    }

    const emailHash = this.md5HashString(safeEmail);
    Logger.debug(`email hash is: ${emailHash}`);

    let credentialsFound = false;
    try {
      await this.authRepo.getAuthCredentialsModel(emailHash);
      credentialsFound = true;
    } catch (error) {
      Logger.error(error);
    }
    if (credentialsFound) {
      throw new Error(`${safeEmail} is already registered`);
    }

    Logger.debug(`creating new user for ${safeName}`);
    const newUser = await this.usersRepo.saveUserModel({
      id: "",
      authId: emailHash,
      name: safeName,
      circles: [],
    });

    const pwdHash = this.bcryptHashString(safePwd);
    Logger.debug(`pwd hash is: ${pwdHash}`);

    const credentials: AuthCredentialsModel = {
      id: emailHash,
      email: safeEmail,
      pwdHash: pwdHash,
      userId: newUser.id,
    };
    Logger.debug("registering new auth credentials...");
    await this.authRepo.updateAuthCredentialsModel(credentials);

    Logger.debug("creating token pair for user...");
    const tokenPair: UserTokenPairModel = {
      auth: this.tokenService.createAccessToken(newUser.id),
      refresh: this.tokenService.createRefreshToken(newUser.id),
    };
    return tokenPair;
  }

  /**
   * Logins the current email and password combination. Retirns a pair of JWT
   * tokens if credentials are valid.
   * @param {string} email user email
   * @param {string} password user password
   * @return {Promise<UserTokenPairModel>} JWT token pair for auth
   */
  async loginUser(
    email: string,
    password: string
  ): Promise<UserTokenPairModel> {
    const safeEmail = this.trimAndLowerCaseString(email || "");
    if (Validations.isInvalidEmail(safeEmail)) {
      throw new Error(`"${safeEmail}" is not a valid email`);
    }

    const safePwd = (password || "").trim();
    if (Validations.isInvalidPassword(safePwd)) {
      throw new Error(`"${safePwd}" is not a valid password`);
    }

    const emailHash = this.md5HashString(safeEmail);
    Logger.debug(`email hash is: ${emailHash}`);

    let credentials: AuthCredentialsModel | undefined;
    try {
      credentials = await this.authRepo.getAuthCredentialsModel(emailHash);
    } catch (error) {
      Logger.debug("unable to get credentials", error);
      throw new Error("invalid username / password");
    }

    Logger.debug("checking user credentials");
    const areCredentialsValid = this.validatePassword(
      password,
      credentials.pwdHash
    );
    if (!areCredentialsValid) {
      throw new Error("invalid username / password");
    }

    Logger.debug("creating token pair for user...");
    const tokenPair: UserTokenPairModel = {
      auth: this.tokenService.createAccessToken(credentials.userId),
      refresh: this.tokenService.createRefreshToken(credentials.userId),
    };
    return tokenPair;
  }

  /**
   * Uses the current refresh token to issue a new pair of access + refresh
   * tokens.
   * @param {string} refreshToken current valid refresh token
   * @return {Promise<UserTokenPairModel>} JWT token pair for auth
   */
  async refreshTokens(refreshToken: string): Promise<UserTokenPairModel> {
    if (!refreshToken) {
      throw new Error("invalid refresh token");
    }

    let token: AccessTokenModel | undefined;
    try {
      token = this.tokenService.validateToken(refreshToken);
    } catch (error) {
      Logger.debug("unable to validate access token", error);
      throw new Error("invalid refresh token");
    }

    const tokenPair: UserTokenPairModel = {
      auth: this.tokenService.createAccessToken(token.subject),
      refresh: this.tokenService.createRefreshToken(token.subject),
    };
    return tokenPair;
  }

  /**
   * Trims and lower cases the given string
   * @param {string} valueString string to trim and lowercase
   * @return {string} trimed and lowercased string
   */
  private trimAndLowerCaseString(valueString: string): string {
    return valueString.trim().toLowerCase();
  }

  /**
   * Hashes the given string using MD5
   * @param {string} valueString string to hash
   * @return {string} MD5 hash value for the given string
   */
  private md5HashString(valueString: string): string {
    return crypto.createHash(HASH_MD5).update(valueString).digest(DIGEST_HEX);
  }

  /**
   * Hashes the given string using bcrypt
   * @param {string} valueString string to hash
   * @return {string} bcrypt hash value for the given string
   */
  private bcryptHashString(valueString: string): string {
    return bcrypt.hashSync(valueString, SALT_ROUNDS);
  }

  /**
   * @param {string} password user password
   * @param {string} hash password hash
   * @return {boolean} `true` if password matches hash, `false` otherwise
   */
  private validatePassword(password: string, hash: string): boolean {
    return bcrypt.compareSync(password, hash);
  }
}
