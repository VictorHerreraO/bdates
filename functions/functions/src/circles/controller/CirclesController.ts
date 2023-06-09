import {
  circlesServiceLocator,
} from "../../dependencies/CirclesServiceLocator";
import { Logger } from "../../core/logging/Logger";
import { Request, Response, Router as expressRouter } from "express";
import { authenticateRequest } from "../../auth/controller/AuthMiddleware";
import { ServiceErrorResponse } from "../../core/api/ResponseApi";
import { authenticateCircleAdmin } from "./CirclesMiddleware";

export const circlesController = expressRouter();
const circlesService = circlesServiceLocator.getCirclesService();
const errorMapper = circlesServiceLocator
  .getErrorToServiceErrorResponseMapper();

circlesController.post(
  "/",
  authenticateRequest,
  async (request: Request, response: Response) => {
    const userId = request.userId;
    const params = request.body;
    try {
      if (!userId) {
        throw new Error("no user found on request");
      }

      const model = await circlesService.createCircle(params.name, userId);

      response.json(model);
    } catch (error: any) {
      response.status(error.code || 500).json(
        new ServiceErrorResponse(error.message, error.code || 500)
      );
    }
  }
);

circlesController.get(
  "/:circleId",
  async (request: Request, response: Response) => {
    const params = request.params;
    try {
      const model = await circlesService.getCircleById(params.circleId);
      response.json(model);
    } catch (error: any) {
      Logger.error(error);
      response.status(500).json({ error: error.message });
    }
  }
);

circlesController.post(
  "/:circleId/events",
  authenticateRequest,
  authenticateCircleAdmin,
  async (request: Request, response: Response) => {
    const circleId = request.params.circleId;
    const userId = request.userId;
    const params = request.body;
    try {
      if (!userId) {
        throw new Error("no user found on request");
      }

      const model = await circlesService.createEvent(circleId, userId, params);
      Logger.debug("model is:", model);
      response.json(model);
    } catch (error: any) {
      Logger.error("unable to create event:", error);
      const errorResponse = errorMapper.map(error);
      response.status(errorResponse.code).json(errorResponse);
    }
  }
);

circlesController.get(
  "/:circleId/events",
  async (request: Request, response: Response) => {
    const params = request.params;
    try {
      const models = await circlesService.getEventList(params.circleId);
      response.json({ data: models });
    } catch (error: any) {
      Logger.error(error);
      response.status(500).json({ error: error.message });
    }
  }
);

circlesController.put(
  "/:circleId/events/:eventId",
  authenticateRequest,
  authenticateCircleAdmin,
  async (request: Request, response: Response) => {
    const circleId = request.params.circleId;
    const eventId = request.params.eventId;
    const userId = request.userId;
    const params = request.body;
    try {
      if (!userId) {
        throw new Error("no user found on request");
      }

      await circlesService.updateEvent(
        circleId,
        eventId,
        userId,
        params
      );
      Logger.debug("model updated");
      response.status(204).send();
    } catch (error: any) {
      Logger.error("unable to update event:", error);
      const errorResponse = errorMapper.map(error);
      response.status(errorResponse.code).json(errorResponse);
    }
  }
);

circlesController.delete(
  "/:circleId/events/:eventId",
  authenticateRequest,
  authenticateCircleAdmin,
  async (request: Request, response: Response) => {
    const circleId = request.params.circleId;
    const eventId = request.params.eventId;
    const userId = request.userId;
    try {
      if (!userId) {
        throw new Error("no user found on request");
      }

      await circlesService.deleteEvent(
        circleId,
        eventId,
      );
      Logger.debug(`model deleted by ${userId}`);
      response.status(204).send();
    } catch (error: any) {
      Logger.error("unable to delete event:", error);
      const errorResponse = errorMapper.map(error);
      response.status(errorResponse.code).json(errorResponse);
    }
  }
);

circlesController.get(
  "/:circleId/events/:eventId/meta",
  async (request: Request, response: Response) => {
    const params = request.params;
    try {
      const model = await circlesService.getEventMeta(
        params.circleId,
        params.eventId
      );
      response.json(model);
    } catch (error: any) {
      Logger.error(error);
      response.status(500).json({ error: error.message });
    }
  }
);
