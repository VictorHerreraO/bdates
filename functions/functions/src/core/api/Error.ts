/* eslint-disable max-len */
/**
 * Indicate that a method has been passed an illegal or inappropriate argument
 */
export class IllegalArgumentError extends Error {
  /**
   * Creates a new instance
   * @param {string} argName name of the argument
   * @param {string} argBoundaries description of the expected value for the argument. Defaults to 'not null or empty'
   */
  constructor(argName: string, argBoundaries?: string) {
    const boundaries = argBoundaries || "not null or empty";
    super(`illegal value for ${argName}, expected it to be ${boundaries}`);
  }
}
