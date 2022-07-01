const assert = require('assert')
const {Given, When, Then} = require('@cucumber/cucumber')
const {post, get, randomIban, randomName} = require("./common");

let responseToCheck;
let organization;
let enrollments;


// Given

Given('the organization {string}', async function (idOrg) {
    responseToCheck = await post(`/organizations/${idOrg}`, {companyName: idOrg});
    assert.strictEqual(responseToCheck.status, 201);
    organization = responseToCheck.data;
    organization.code = idOrg;
});

Given('the organization {string} with the service {string}', async function (idOrg, idService) {
    responseToCheck = await post(`/organizations/${idOrg}`, {companyName: idOrg,
        enrollments: [{
            serviceId: idService,
            iban: randomIban(),
            officeName: randomName()
        }]});
    assert.strictEqual(responseToCheck.status, 201);
    organization = responseToCheck.data;
    organization.code = idOrg;
});


// When
When('the organization enrolls in the service {string}', async function (idService) {
    responseToCheck = await post(`/organizations/${organization.code}/services/${idService}`, {
        iban: randomIban(),
        officeName: randomName()
    });
    assert.strictEqual(responseToCheck.status, 201);
    enrollments = responseToCheck.data;
});


// Then

Then(/^the organization is enrolled in the service$/, async function () {
    responseToCheck = await get(`/organizations/${organization.code}`);
    assert.strictEqual(responseToCheck.status, 200);
    assert.deepStrictEqual(responseToCheck.data.enrollments, enrollments)
});
