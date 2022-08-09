const assert = require('assert')
const {Given, When, Then} = require('@cucumber/cucumber')
const {randomIban, randomName, randomSegregationCode, randomRemittanceInformation} = require("./common");
const {createSpontaneousPayment, deleteOrganization, createOrganization, createOrganizationService} = require("./gps_client");

let responseToCheck;
let organization;
let service


// Given -> create the organization 777777
Given('the organization creates the creditor institution {string}', async function (idOrg) {
    // precondition
    await deleteOrganization(idOrg);

    responseToCheck = await createOrganization(idOrg, {companyName: idOrg});
    assert.strictEqual(responseToCheck.status, 201);
    // save data
    organization = responseToCheck.data;
    organization.code = idOrg;
});


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
    // save data
    organization = responseToCheck.data;
});

Given('the organization tries to enroll the {string} on a not registered creditor institution {string}', async function (idService,idOrg) {
    // precondition -> if already exist delete the org 
    await deleteOrganization(idOrg);

    service = {
        serviceId: idService,
        iban: randomIban(),
        officeName: randomName(),
        segregationCode: randomSegregationCode(),   
        remittanceInformation: randomRemittanceInformation()
    };
    // save data
    organization.fiscalCode = idOrg;
});




// When

When('the organization enrolls the creditor institution on the service {string}', async function (idService) {
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

When('the organization tries to create a spontaneous payment with a property {string} not configured to service', async function (propName) {
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
				[{ "name": propName, "value": "1000" }]
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
