const assert = require('assert')
const {Given, When, Then} = require('@cucumber/cucumber')
const {createService, updateService, deleteService, getServices, getService} = require("./gps_client");

let responseToCheck;
let service;
let serviceIdToDelete;


// Given

Given('creates the default service with id {string}', async function (serviceId) {
    // prior cancellation to avoid dirty cases
	await deleteService(serviceId);
	
    service = {
        "id": serviceId,
        "name": "name_"+serviceId,
        "description": "description_"+serviceId,
        "transferCategory": "transferCategory_"+serviceId,
        "status": "ENABLED",
        "endpoint": "http://localhos:7071",
        "basePath": "/donations/paymentoptions",
        "properties": [
            {
                "name": "amount",
                "type": "NUMBER",
                "required": true
            }
        ]
    };
    
    responseToCheck = await createService(service);
    assert.strictEqual(responseToCheck.status, 201);
});

Given('the body of the request for a service with id {string}', async function (serviceId) {
	// prior cancellation to avoid dirty cases
	responseToCheck = await deleteService(serviceId);
	
    service = {
        "id": serviceId,
        "name": "name_"+serviceId,
        "description": "description_"+serviceId,
        "transferCategory": "transferCategory_"+serviceId,
        "status": "ENABLED",
        "endpoint": "http://localhos:7071",
        "basePath": "/donations/paymentoptions",
        "properties": [
            {
                "name": "amount",
                "type": "NUMBER",
                "required": true
            }
        ]
    };
});

Given('the bad body of the request for a service with id {string}', async function (serviceId) {
	// prior cancellation to avoid dirty cases
	responseToCheck = await deleteService(serviceId);
	
    service = {
        "id": serviceId,
        "name": "name_"+serviceId,
        "description": "description_"+serviceId,
        "transferCategory": "transferCategory_"+serviceId,
        "status": "ENABLED",
        // no mandatory parameters endpoint and basePath
        "properties": [
            {
                "name": "amount",
                "type": "NUMBER",
                "required": true
            }
        ]
    };
});

Given('the updated body of the request for a service with id {string}', async function (serviceId) {
	
    service = {
        "id": serviceId,
        "name": "UPD name_"+serviceId,
        "description": "UPD description_"+serviceId,
        "transferCategory": "UPD transferCategory_"+serviceId,
        "status": "ENABLED",
        "endpoint": "http://localhos:7071",
        "basePath": "/donations/paymentoptions",
        "properties": [
            {
                "name": "amount",
                "type": "NUMBER",
                "required": true
            }
        ]
    };
});

Given('the service to delete with id {string}', async function (serviceId) {
    serviceIdToDelete = serviceId;
});



// When

When('the client requests the creation of a service', async function () {
    responseToCheck = await createService(service);
});

When('the client requests a service update', async function () {
    responseToCheck = await updateService(service.id, service);
});

When('the client requests a service delete', async function () {
    responseToCheck = await deleteService(serviceIdToDelete);
});

When('the client get service {string}', async function (idService) {
    responseToCheck = await getService(idService);
});

When('the client get all services', async function () {
    responseToCheck = await getServices();
});

// Then

Then('the client receives status code of {int}', function (statusCode) {
    assert.strictEqual(responseToCheck.status, statusCode);
});
Then(/^the client retrieves the list of services$/, function () {
    assert.notStrictEqual(responseToCheck.data.length, 0);
});
