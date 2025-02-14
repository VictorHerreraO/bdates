export interface ErrorResponse {
  readonly code: number,
  readonly message: string
}

/**
 * Generic service error response
 */
export class ServiceErrorResponse implements ErrorResponse {
  code: number;
  message: string;

  /**
   * Creates a new instance
   * @param {string} message service error message
   * @param {number} code service error code. Defaults to `500`
   */
  constructor(message: string, code = 500) {
    this.message = message;
    this.code = code;
  }
}
