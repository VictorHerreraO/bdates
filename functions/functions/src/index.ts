/*
 * https://blog.logrocket.com/rest-api-firebase-cloud-functions-typescript-firestore/
 */
import * as functions from "firebase-functions";
import * as express from "express";
import { circlesController } from "./circles/controller/CirclesController";
import { morganMiddleware } from "./core/logging/MorganMiddleware";

const DEFAULT_REGION = "us-west2";
const DEFAULT_MAX_INSTANCES = 10;

const app = express();

// Logging
app.use(morganMiddleware);

// Functions config
const https = functions
  .region(DEFAULT_REGION)
  .runWith({ maxInstances: DEFAULT_MAX_INSTANCES })
  .https;

app.use("/circles", circlesController);

exports.app = https.onRequest(app);
