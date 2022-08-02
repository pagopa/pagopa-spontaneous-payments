import http from 'k6/http';
import { check } from 'k6';
import { SharedArray } from 'k6/data';

import { generateFakeFiscalCode, randomString } from './modules/helpers.js';

import { createCreditorInstitutionEnrollment, getCiCode} from "./modules/gps_client.js";

let myOptions = JSON.parse(open(__ENV.TEST_TYPE));
export let options = myOptions;

// read configuration
// note: SharedArray can currently only be constructed inside init code
// according to https://k6.io/docs/javascript-api/k6-data/sharedarray
const varsArray = new SharedArray('vars', function () {
	return JSON.parse(open(`./${__ENV.VARS}`)).environment;
});
// workaround to use shared array (only array should be used)
const vars = varsArray[0];
const rootUrl = `${vars.host}/${vars.basePath}`;
const delete_debt_position = `${vars.deleteDebtPosition}`;
const gpdUrlBasePath = `${vars.gpdBaseUrl}`;

const creditor_institution_code = `organizationNew`;

export function setup() {
	// 2. setup code (once)
	// The setup code runs, setting up the test environment (optional) and generating data
	// used to reuse code for the same VU
	const params = {
		headers: {
			'Content-Type': 'application/json'
		},
	};
	const response = createCreditorInstitutionEnrollment(rootUrl, params, creditor_institution_code);

    console.log(`setup ... ${response.status}`);

    check(response, {
        "status is 201 or 409": (response) => (response.status === 201 || response.status === 409),
    });

	// precondition is moved to default fn because in this stage
	// __VU is always 0 and cannot be used to create env properly
}

function precondition() {
	// no pre conditions
}

function postcondition() {
	// no post conditions
}

export default function() {

	const debtor_fiscal_code = generateFakeFiscalCode(75);
	const full_name = randomString(15, "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
	const phone_number = randomString(10, "0123456789");
	const amount = randomString(5, "123456789");
	const description = "ukraine donation";
    //	getCiCode(__VU);


	// Create a new spontaneous payment.
	var tag = {
		gpsMethod: "CreateSpontaneousPayment",
	};

	var url = `${rootUrl}/${creditor_institution_code}/spontaneouspayments`;

	var payload = JSON.stringify(
		{
			"debtor":
			{
				"type": "F",
				"fiscalCode": debtor_fiscal_code,
				"fullName": full_name,
				"streetName": "street name",
				"civicNumber": "7",
				"postalCode": "00100",
				"city": "Rome",
				"province": "RM",
				"region": null,
				"country": null,
				"email": "mockEmail@mock.it",
				"phone": phone_number
			},
			"service":
			{
				"id": "id-servizio-1",
				"properties":
					[
						{ "name": "amount", "value": amount },
						{ "name": "description", "value": description },
					]
			}
		}
	);


	var params = {
		headers: {
			'Content-Type': 'application/json'
		},
	};

	var r = http.post(url, payload, params);

	console.log("CreateSpontaneousPayment call - creditor_institution_code " + creditor_institution_code + ", Status " + r.status);

	check(r, {
		'CreateSpontaneousPayment status is 201': (_r) => r.status === 201,
	}, tag);

    // If flag delete_debt_position is set to true the debit position is deleted after being created
	if (r.status === 201 && delete_debt_position === "true") {

		let iupd = r.json().iupd;

		// Delete the newly created debt position.
		tag = {
			gpdMethod: "DeleteDebtPosition",
		};

		url = `${gpdUrlBasePath}/organizations/${creditor_institution_code}/debtpositions/${iupd}`;

		r = http.del(url, params);

		console.log("DeleteDebtPosition call - creditor_institution_code " + creditor_institution_code + ", iupd " + iupd + ", Status " + r.status);

		check(r, {
			"DeleteDebtPosition status is 200": (_r) => r.status === 200,
		}, tag);

	}


}
