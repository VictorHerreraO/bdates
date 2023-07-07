import { authenticateRequest } from "../../auth/controller/AuthMiddleware";
import { eventsController } from "../../events/controller/EventsController";
import { getCirclesService } from "../../dependencies/CirclesServiceLocator";
import { Logger } from "../../core/logging/Logger";
import { Request, Response, Router as expressRouter } from "express";
import { ServiceErrorResponse } from "../../core/api/ResponseApi";

export const circlesController = expressRouter();

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

      const model = await getCirclesService().createCircle(params.name, userId);

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
      const model = await getCirclesService().getCircleById(params.circleId);
      response.json(model);
    } catch (error: any) {
      Logger.error(error);
      response.status(500).json({ error: error.message });
    }
  }
);

circlesController.use(eventsController);
