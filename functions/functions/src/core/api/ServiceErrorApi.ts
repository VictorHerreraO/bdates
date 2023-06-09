/**
 * Base Service Error class
 */
export abstract class HttpError extends Error {
  abstract readonly code: number;
  readonly cause?: Error;

  /**
   * Creates a new instance
   * @param {string?} message error message
   * @param {Error?} cause cause for this error
   */
  constructor(message?: string, cause?: Error) {
    super(message);
    this.cause = cause;
  }
}

/**
 * 400 Bad Request
 */
export class BadRequestError extends HttpError {
  code = 400;
}

/**
 * 401 Unauthorized
 */
export class UnauthorizedError extends HttpError {
  code = 401;
}

/**
 * 403 Forbidden
 */
export class ForbiddenError extends HttpError {
  code = 403;
}

/**
 * 404 Not Found
 */
export class NotFoundError extends HttpError {
  code = 404;
}

/**
 * 500 Internal Server Error
 */
export class InternalServerError extends HttpError {
  code = 500;
}

