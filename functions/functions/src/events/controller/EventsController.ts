import {
  authenticateCircleAdmin,
} from "../../circles/controller/CirclesMiddleware";
import { authenticateRequest } from "../../auth/controller/AuthMiddleware";
import {
  eventsServiceLocator, getEventsService,
} from "../../dependencies/EventsServiceLocator";
import { Logger } from "../../core/logging/Logger";
import { Request, Response, Router as expressRouter } from "express";

export const eventsController = expressRouter();
const errorMapper = eventsServiceLocator.getErrorToServiceErrorResponseMapper();

eventsController.get(
  "/:circleId/events",
  async (request: Request, response: Response) => {
    const params = request.params;
    try {
      const models = await getEventsService().getEventList(params.circleId);
      response.json({ data: models });
    } catch (error: any) {
      Logger.error(error);
      response.status(500).json({ error: error.message });
    }
  }
);

eventsController.post(
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

      const model = await getEventsService().createEvent(
        circleId,
        userId,
        params
      );
      Logger.debug("model is:", model);
      response.json(model);
    } catch (error: any) {
      Logger.error("unable to create event:", error);
      const errorResponse = errorMapper.map(error);
      response.status(errorResponse.code).json(errorResponse);
    }
  }
);

eventsController.put(
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

      await getEventsService().updateEvent(
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

eventsController.delete(
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

      await getEventsService().deleteEvent(
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

eventsController.get(
  "/:circleId/events/:eventId/meta",
  async (request: Request, response: Response) => {
    const params = request.params;
    try {
      const model = await getEventsService().getEventMeta(
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
