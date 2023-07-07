/* eslint-disable max-len */
import { ApplicationConfig } from "../core/api/ApplicationConfig";
import * as admin from "firebase-admin";
import * as functions from "firebase-functions";

type Environment = {
  clientEmail: string,
  databaseURL: string,
  jwtSecret: string,
  privateKey: string,
  projectId: string,
}

// Add firebase functions config using the following:
// firebase functions:config:set env="$(cat env.json)"
// https://dev.to/rajeshkumaravel/google-firebase-functions-setting-and-accessing-environment-variable-1gn2
const env = functions.config().env as Environment;

admin.initializeApp({
  credential: admin.credential.cert({
    privateKey: env.privateKey.replace(/\\n/g, "\n"),
    projectId: env.projectId,
    clientEmail: env.clientEmail,
  }),
  databaseURL: env.databaseURL,
});

const db = admin.database();

const applicationConfig: ApplicationConfig = {
  isDebug: env.projectId.includes("-sandbox"),
  applicationName: env.projectId,
  jwtSecret: env.jwtSecret,
};

export { admin, db, applicationConfig };
