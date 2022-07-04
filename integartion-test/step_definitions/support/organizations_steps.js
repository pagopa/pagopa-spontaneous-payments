const assert = require('assert')
const {Given, When, Then} = require('@cucumber/cucumber')
const {randomIban, randomName} = require("./common");
const {createOrganization, createOrganizationService, getOrganization, deleteOrganizationService} = require("./gps_client");

let responseToCheck;
let organization;
let enrollments;


// Given

Given('the organization {string}', async function (idOrg) {
    responseToCheck = createOrganization(idOrg, {companyName: idOrg});
    assert.strictEqual(responseToCheck.status, 201);
    organization = responseToCheck.data;
    organization.code = idOrg;
});

Given('the organization {string} with the service {string}', async function (idOrg, idService) {
    responseToCheck = createOrganization(idOrg, {
        companyName: idOrg,
        enrollments: [{
            serviceId: idService,
            iban: randomIban(),
            officeName: randomName()
        }]
    });
    assert.strictEqual(responseToCheck.status, 201);
    organization = responseToCheck.data;
    organization.code = idOrg;
});


// When
When('the organization enrolls in the service {string}', async function (idService) {
    responseToCheck = createOrganizationService(organization.code, idService, {
        iban: randomIban(),
        officeName: randomName()
    });
    assert.strictEqual(responseToCheck.status, 201);
    enrollments = responseToCheck.data;
});

When('the organization deletes the service {string}', function (idService) {
    responseToCheck = deleteOrganizationService(organization.code, idService);
    assert.strictEqual(responseToCheck.status, 200);
});


// Then

Then(/^the organization is enrolled in the service$/, async function () {
    responseToCheck = getOrganization(organization.code);
    assert.strictEqual(responseToCheck.status, 200);
    assert.deepStrictEqual(responseToCheck.data.enrollments, enrollments)
    organization = responseToCheck.data
});

Then('the organization gets the status code {int}', function (status) {
    assert.strictEqual(responseToCheck.status, status);
});

Then('the service is listed in the organization\'s details', function () {
    responseToCheck = getOrganization(organization.code);
    assert.strictEqual(responseToCheck.status, 200);
});
