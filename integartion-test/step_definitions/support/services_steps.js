const assert = require('assert')
const {Given, When, Then} = require('@cucumber/cucumber')
const {get} = require("./common");

let responseToCheck;
let service;


// Given

Given('some services in the DB', function () {
    // TODO
});


// When

When('the client get service {string}', async function (idService) {
    service = await get(`/services/${idService}`);
});


When('the client get all services', async function () {
    responseToCheck = await get('/services');
});


// Then

Then('the client receives status code of {int}', function (statusCode) {
    assert.strictEqual(responseToCheck.status, statusCode);
});

Then(/^the client retrieves the list of services$/, function () {
    assert.notStrictEqual(responseToCheck.data.length, 0);
});
