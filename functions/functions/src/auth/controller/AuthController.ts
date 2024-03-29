import { authServiceLocator } from "../../dependencies/AuthServiceLocator";
import { Request, Response, Router as expressRouter } from "express";
import { ServiceErrorResponse } from "../../core/api/ResponseApi";

export const authController = expressRouter();

authController.post(
  "/register",
  async (request: Request, response: Response) => {
    const params = request.body;
    const authService = authServiceLocator.getAuthService();
    try {
      const result = await authService.registerUser(
        params.userName,
        params.email,
        params.password
      );
      response.json(result);
    } catch (error: any) {
      response.status(500).json(
        new ServiceErrorResponse(error.message)
      );
    }
  }
);

authController.post(
  "/login",
  async (request: Request, response: Response) => {
    const params = request.body;
    const authService = authServiceLocator.getAuthService();
    try {
      const tokenPair = await authService.loginUser(
        params.email,
        params.password,
      );
      response.json(tokenPair);
    } catch (error: any) {
      response.status(500).json(
        new ServiceErrorResponse(error.message)
      );
    }
  }
);

authController.post(
  "/refresh",
  async (request: Request, response: Response) => {
    const params = request.body;
    const authService = authServiceLocator.getAuthService();
    try {
      const tokenPair = await authService.refreshTokens(
        params.token
      );
      response.json(tokenPair);
    } catch (error: any) {
      response.status(500).json(
        new ServiceErrorResponse(error.message)
      );
    }
  }
);

authController.post(
  "/password/reset",
  async (request: Request, response: Response) => {
    return response.status(500).json(
      new ServiceErrorResponse("Unsupported operation error")
    );
  }
);
