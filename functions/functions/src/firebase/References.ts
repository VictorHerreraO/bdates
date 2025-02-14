import { db } from "./Application";

const references = {
  circles: db.ref("circles"),
  auth: db.ref("auth"),
  users: db.ref("users"),
};

export { references };
