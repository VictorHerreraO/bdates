const regexEmail = /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i;


/**
 * Common validations
 */
export abstract class Validations {
  /**
   * @param {string} email email string to validate
   * @return {boolean} `true` if string is a valid email, `false` otherwise
   */
  public static isValidEmail(email: string): boolean {
    return regexEmail.test(email);
  }


  /**
   * @param {string} email email string to validate
   * @return {boolean} `true` if string is **not** a valid email,
   * `false` otherwise
   */
  public static isInvalidEmail(email: string): boolean {
    return !this.isValidEmail(email);
  }

  /**
   * @param {string} password password string to validate
   * @return {boolean} `true` if string is a valid password, `false` otherwise
   */
  public static isValidPassword(password: string): boolean {
    return password.length >= 6;
  }


  /**
   * @param {string} password password string to validate
   * @return {boolean} `true` if string is **not** a valid password,
   * `false` otherwise
   */
  public static isInvalidPassword(password: string): boolean {
    return !this.isValidPassword(password);
  }
}
