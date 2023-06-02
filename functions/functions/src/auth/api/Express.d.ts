import { UserSnapshotModel } from "../../circles/api/CircleApi";

export { };

declare global {
  namespace Express {
    export interface Request {
      /**
       * User snapshot for this request if authenticated
       */
      user?: UserSnapshotModel;
    }
  }
}
