/* eslint-disable max-len */
import { ApplicationConfig } from "../core/api/ApplicationConfig";
import * as admin from "firebase-admin";
import * as functions from "firebase-functions";

// Add firebase functions config using the following:
// firebase functions:config:set private.key="YOUR API KEY" project.id="YOUR CLIENT ID" client.email="YOUR CLIENT EMAIL jwt.secret="JWT SECRET"
admin.initializeApp({
  credential: admin.credential.cert({
    privateKey: functions.config().private.key.replace(/\\n/g, "\n"),
    projectId: functions.config().project.id,
    clientEmail: functions.config().client.email,
  }),
  databaseURL: "https://bdates-sandbox-default-rtdb.firebaseio.com/",
});

const db = admin.database();

const applicationConfig: ApplicationConfig = {
  isDebug: true,
  applicationName: functions.config().project.id,
  jwtSecret: functions.config().jwt.secret,
};

export { admin, db, applicationConfig };
