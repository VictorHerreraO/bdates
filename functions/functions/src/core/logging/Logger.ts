/*
 * https://cloud.google.com/logging/docs/samples/logging-winston-quickstart
 */
// Imports the Google Cloud client library for Winston
import { LoggingWinston } from "@google-cloud/logging-winston";
import winston = require("winston");

const loggingWinston = new LoggingWinston();

// Create a Winston logger that streams to Cloud Logging
// Logs will be written to: "projects/YOUR_PROJECT_ID/logs/winston_log"
const Logger = winston.createLogger({
  level: "debug",
  transports: [
    new winston.transports.Console(),
    // Add Cloud Logging
    loggingWinston,
  ],
});

export { Logger };
