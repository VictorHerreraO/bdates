/* eslint-disable require-jsdoc */
/* eslint-disable max-len */
import { Reference } from "firebase-admin/database";
import { references } from "../firebase/References";
import { UsersRepository } from "../users/data/UsersRepository";
import { UsersRepositoryImpl } from "../users/data/UsersRepositoryImpl";
import { SnapshotToUserModelMapper } from "../users/data/mapping/SnapshotToUserModelMapper";

export class UsersServiceLocator {
  private _snapshotToUserMapper: SnapshotToUserModelMapper | undefined;
  getSnapshotToUserMapper(): SnapshotToUserModelMapper {
    return this._snapshotToUserMapper ??= new SnapshotToUserModelMapper();
  }

  private _usersReference: Reference | undefined;
  getUsersReference(): Reference {
    return this._usersReference ??= (references.users);
  }

  private _usersRepository: UsersRepository | undefined;
  getUsersRepository(): UsersRepository {
    return this._usersRepository ??= new UsersRepositoryImpl(
      this.getUsersReference(),
      this.getSnapshotToUserMapper()
    );
  }
}

export const usersServiceLocator = new UsersServiceLocator();
