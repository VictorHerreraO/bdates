import { UserId } from "../../core/api/CommonTypes";

export { };

declare global {
  namespace Express {
    export interface Request {
      /**
       * ID of the user that made this request if authenticated
       */
      userId?: UserId;
    }
  }
}
