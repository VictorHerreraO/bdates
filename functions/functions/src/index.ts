/**
 * Import function triggers from their respective submodules:
 *
 * import {onCall} from "firebase-functions/v2/https";
 * import {onDocumentWritten} from "firebase-functions/v2/firestore";
 *
 * See a full list of supported triggers at https://firebase.google.com/docs/functions
 */

import * as functions from "firebase-functions";
import * as logger from "firebase-functions/logger";

// Start writing functions
// https://firebase.google.com/docs/functions/typescript

const DEFAULT_REGION = "us-west2"
const DEFAULT_MAX_INSTANCES = 10

const https = functions.region(DEFAULT_REGION).runWith({maxInstances: DEFAULT_MAX_INSTANCES}).https;

export const helloWorld = https.onRequest((request, response) => {
  logger.info("Hello logs!", {structuredData: true});
  response.send("Hello from Firebase!");
});
