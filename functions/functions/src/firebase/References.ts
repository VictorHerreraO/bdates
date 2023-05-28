import { db } from "./Application";

const references = {
  circles: db.ref("circles"),
  auth: db.ref("auth"),
};

export { references };
