const assert = require('assert')
const {Given, When, Then} = require('@cucumber/cucumber')
const {randomIban, randomName, randomSegregationCode, randomRemittanceInformation} = require("./common");
const {
    createOrganization, createOrganizationService, getOrganization, deleteOrganizationService,
    getOrganizationService, updateOrganization, updateOrganizationService, deleteOrganization
} = require("./gps_client");

let responseToCheck;
let organization;
let service;


// Given

Given('the organization {string}', async function (idOrg) {
    // precondition
    await deleteOrganization(idOrg);

    responseToCheck = await createOrganization(idOrg, {companyName: idOrg});
    assert.strictEqual(responseToCheck.status, 201);
    // save data
    organization = responseToCheck.data;
    organization.code = idOrg;
});

Given('the organization {string} with the service {string}', async function (idOrg, idService) {
    // precondition
    await deleteOrganization(idOrg);

    // save data
    service = {
        serviceId: idService,
        iban: randomIban(),
        officeName: randomName(),
        segregationCode: randomSegregationCode(),   
        remittanceInformation: randomRemittanceInformation()
    };
    // call
    responseToCheck = await createOrganization(idOrg, {
        companyName: idOrg,
        enrollments: [service]
    });
    assert.strictEqual(responseToCheck.status, 201);
    // save data
    organization = responseToCheck.data;
});


// When
When('the organization enrolls in the service {string}', async function (idService) {
    // save data
    service = {
        serviceId: idService,
        iban: randomIban(),
        officeName: randomName(),
        segregationCode: randomSegregationCode(),   
        remittanceInformation: randomRemittanceInformation()
        };
    // call
    responseToCheck = await createOrganizationService(organization.fiscalCode, idService, service);
    // save data
    organization = responseToCheck.data;
});

When('the organization deletes the service {string}', async function (idService) {
    service.serviceId = idService
    responseToCheck = await deleteOrganizationService(organization.fiscalCode, idService);
    assert.strictEqual(responseToCheck.status, 200);
});


When('the organization changes the service IBAN with {string}', async function (iban) {
    responseToCheck = await updateOrganizationService(organization.fiscalCode, service.serviceId, {
        iban: iban,
        officeName: service.officeName,
        segregationCode: service.segregationCode,
        remittanceInformation: service.remittanceInformation
    })
    assert.strictEqual(responseToCheck.status, 200);
    // save data
    organization = responseToCheck.data
});

When('the organization set the status to {string}', async function (status) {
    responseToCheck = await updateOrganization(organization.fiscalCode, {
        companyName: organization.companyName,
        status: status
    })
    assert.strictEqual(responseToCheck.status, 200);
    // save data
    organization = responseToCheck.data
});

// Then

Then(/^the service is listed in the organization's details$/, async function () {
    responseToCheck = await getOrganization(organization.fiscalCode);
    assert.strictEqual(responseToCheck.status, 200);
    assert.deepStrictEqual(responseToCheck.data.enrollments, organization.enrollments)
    // save data
    organization = responseToCheck.data
});

Then('the organization gets the status code {int}', function (status) {
    assert.strictEqual(responseToCheck.status, status);
});

Then(/^the service is not found for the organization$/, async function () {
    responseToCheck = await getOrganizationService(organization.fiscalCode, service.serviceId);
    assert.strictEqual(responseToCheck.status, 404);
});

Then('the service for the organization has the IBAN {string}', async function (iban) {
    responseToCheck = await getOrganizationService(organization.fiscalCode, service.serviceId);
    assert.strictEqual(responseToCheck.status, 200);
    assert.strictEqual(responseToCheck.data.iban, iban);

});

Then('the status is {string} in the organization\'s details', async function (status) {
    responseToCheck = await getOrganization(organization.fiscalCode);
    assert.strictEqual(responseToCheck.status, 200);
    assert.strictEqual(responseToCheck.data.status, status);
});
