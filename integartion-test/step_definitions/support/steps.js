const {Given, When, Then} = require('@cucumber/cucumber')
const {getServices} = require("./gps_client");
const assert = require("assert");


Given(/^GPS running$/, function () {
    const response = getServices(); // TODO use health check
    assert.strictEqual(response.status, 200);
});
