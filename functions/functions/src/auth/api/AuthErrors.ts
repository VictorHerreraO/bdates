/**
 * Error response to specify that the requested resource requires
 * a valid authentication
 */
export class UnauthenticatedError extends Error {
  readonly code = 401;
  readonly cause: Error | undefined;

  /**
   * Creates a new instance
   * @param {string} message error message
   * @param {Error | undefined} cause cause for this error
   */
  constructor(message: string, cause: Error | undefined = undefined) {
    super(message);
    this.cause = cause;
  }
}
