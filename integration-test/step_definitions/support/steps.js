const {Given, When, Then} = require('@cucumber/cucumber')
const {getServices} = require("./gps_client");
const assert = require("assert");


Given('GPS running', async function () {
    const response = await getServices(); // TODO use health check
    assert.strictEqual(response.status, 200);
});
