// @ts-check

const config = {
  endpoint: process.env.COSMOS_URI,
  key: process.env.COSMOS_KEY,
  databaseId: "db",
  containerId: "services",
  partitionKey: { kind: "Hash", paths: ["/transferCategory"] }
};

module.exports = config;