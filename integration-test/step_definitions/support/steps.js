const {Given, When, Then} = require('@cucumber/cucumber')
const {healthCheckInfo} = require("./gps_client");
const assert = require("assert");


Given('GPS running', async function () {
    const response = await healthCheckInfo();
    assert.strictEqual(response.status, 200);
});
