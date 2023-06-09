import { IllegalArgumentError } from "../api/Error";
import { ServiceErrorResponse } from "../api/ResponseApi";
import {
  BadRequestError,
  HttpError,
  InternalServerError,
} from "../api/ServiceErrorApi";
import { Mapper } from "./Mapper";

/**
 * Error mapping
 */
export class ErrorToServiceErrorResponseMapper implements
  Mapper<Error, ServiceErrorResponse> {
  /**
   * @param {Error} value
   * @param {string} id
   * @return {ServiceErrorResponse}
   */
  map(value: Error, id?: string | undefined): ServiceErrorResponse {
    let httpError: HttpError = new InternalServerError(value.message);

    if (value instanceof IllegalArgumentError) {
      httpError = new BadRequestError();
    }

    return new ServiceErrorResponse(value.message, httpError.code);
  }
}
