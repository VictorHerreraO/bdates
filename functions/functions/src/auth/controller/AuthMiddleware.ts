import { authServiceLocator } from "../../dependencies/AuthServiceLocator";
import {
  NextFunction,
  Request,
  Response,
} from "express";
import { ServiceErrorResponse } from "../../core/api/ResponseApi";
import { UnauthorizedError } from "../../core/api/ServiceErrorApi";

const AUTH_BEARER = "bearer";

/**
 * Intercepts requests and validates that a bearer token is included
 * in the authorization header. If a token is present and is valid
 * request chain request continues, else request is fullfiled with an
 * error
 * @param {Request} request
 * @param {Response} response
 * @param {NextFunction} next
 */
export function authenticateRequest(
  request: Request,
  response: Response,
  next: NextFunction
): void {
  try {
    const authHeader = request.headers.authorization || "";
    const [authType = "", token = ""] = authHeader.split(" ");

    if (!authHeader || authType.toLowerCase() !== AUTH_BEARER || !token) {
      throw new UnauthorizedError("Invalid authentication credentials");
    }

    const tokenService = authServiceLocator.getJWTService();
    const contents = tokenService.validateToken(token);

    request.user = {
      id: contents.subject,
      name: "",
    };

    next();
  } catch (error: any) {
    response.status(error.code || 500).json(
      new ServiceErrorResponse(error.message, error.code)
    );
  }
}
