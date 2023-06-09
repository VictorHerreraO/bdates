import {
  NextFunction,
  Request,
  Response,
} from "express";
import {
  circlesServiceLocator,
} from "../../dependencies/CirclesServiceLocator";
import { ServiceErrorResponse } from "../../core/api/ResponseApi";
import {
  ForbiddenError,
  InternalServerError,
} from "../../core/api/ServiceErrorApi";
import { Logger } from "../../core/logging/Logger";


/**
 * Intercepts requests and validates that the request user is an administrator
 * of the to the desired circle. If the user is present and is a circle admin
 * the request chain request continues, else request is fullfiled with an
 * error
 *
 * @param {Request} request
 * @param {Response} response
 * @param {NextFunction} next
 */
export async function authenticateCircleAdmin(
  request: Request,
  response: Response,
  next: NextFunction) {
  try {
    const circleId = request.params.circleId;
    if (!circleId) {
      Logger.error(
        "no circle id found in request. Is middleware added in the right place?"
      );
      throw new InternalServerError("no circle id found on request");
    }
    const userId = request.userId;
    if (!userId) {
      Logger.error(
        "no user found in request. Has request being authenticated?"
      );
      throw new InternalServerError("no user present in request");
    }

    const circlesService = circlesServiceLocator.getCirclesService();
    const circle = await circlesService.getCircleById(circleId);
    const isAdmin = (
      circle.owner === userId ||
      circle.admins[userId] == true
    );

    if (!isAdmin) {
      throw new ForbiddenError("user has no admin access to this circle");
    }

    next();
  } catch (error: any) {
    const statusCode = error.code || 500;
    response.status(statusCode).json(
      new ServiceErrorResponse(error.message, statusCode)
    );
  }
}
