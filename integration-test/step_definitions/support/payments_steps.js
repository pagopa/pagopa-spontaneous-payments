const assert = require('assert')
const {Given, When, Then} = require('@cucumber/cucumber')
const {randomIban, randomName, randomSegregationCode, randomRemittanceInformation} = require("./common");
const {createSpontaneousPayment, deleteOrganization, createOrganization} = require("./gps_client");

let responseToCheck;
let organization;
let service



// Given -> create the organization 777777 with an enrollment to service service-1

Given('the organization {string} with an enrollment to service {string}', async function (idOrg, idService) {
    // precondition -> if already exist delete the org with fiscal code 777777
    await deleteOrganization(idOrg);

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

When('the organization creates a spontaneous payment', async function () {
	let payment = {
		"debtor": {
			"type": "F",
			"fiscalCode": "debtorFiscalCode",
			"fullName": "debtorFullName",
			"streetName": null,
			"civicNumber": null,
			"postalCode": null,
			"city": null,
			"province": null,
			"region": null,
			"country": null,
			"email": "debtorEmail@debtor.it",
			"phone": null
		},
		"service": {
			"id": service.serviceId,
			"properties":
				[{ "name": "amount", "value": "1000" }]
		}
	}
    responseToCheck = await createSpontaneousPayment(organization.fiscalCode, payment);
});


// Then

Then('the organization gets the created status code {int}', function (statusCode) {
    assert.strictEqual(responseToCheck.status, statusCode);
});
Then('the organization gets the created Payment Position', function () {
    assert.strictEqual(responseToCheck.data.companyName, organization.fiscalCode);
});
