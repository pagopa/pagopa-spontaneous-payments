import http from 'k6/http';
import { check } from 'k6';
import { generateFakeFiscalCode, randomString } from './modules/helpers.js';

export let options = {
	summaryTrendStats: ['avg', 'min', 'med', 'max', 'p(95)', 'p(99)', 'p(99.99)', 'count'],
	stages: [
		{ duration: '1m', target: 50 }, // simulate ramp-up of traffic from 1 to 50 users over 1 minutes.
	],
	thresholds: {
		http_req_failed: ['rate<0.01'], // http errors should be less than 1%
		http_req_duration: ['p(99)<1000'], // 99% of requests must complete below 1000ms
		'http_req_duration{gpsMethod:CreateSpontaneousPayment}': ['p(95)<1000'], // threshold on creation API requests only
		'http_req_duration{gpdMethod:DeleteDebtPosition}': ['p(95)<1000'], // threshold on delete API requests only
	},
};



export default function() {

	var gpsUrlBasePath = `${__ENV.GPS_BASE_URL}`
	var gpdUrlBasePath = `${__ENV.GPD_BASE_URL}`
	var creditor_institution_code = `${__ENV.ORGANIZATION_FISCAL_CODE}`
	var delete_debt_position = `${__ENV.DELETE_DEBT_POSITION}`



	const debtor_fiscal_code = generateFakeFiscalCode(75);
	const full_name = randomString(15, "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
	const phone_number = randomString(10, "0123456789");
	const amount = randomString(5, "123456789");

	// Create a new spontaneous payment.
	var tag = {
		gpsMethod: "CreateSpontaneousPayment",
	};

	var url = `${gpsUrlBasePath}/organizations/${creditor_institution_code}/spontaneouspayments`;

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
						{ "name": "amount", "value": amount }
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
		'status is 201': (_r) => r.status === 201,
	}, tag);

    // If flag delete_debt_position is set to true the debit position is deleted after being created
	if (r.status === 201 && delete_debt_position === "true") {

		let iupd = r.json()["iupd"];

		// Delete the newly created debt position.
		tag = {
			gpdMethod: "DeleteDebtPosition",
		};

		url = `${gpdUrlBasePath}/organizations/${creditor_institution_code}/debtpositions/${iupd}`;

		r = http.del(url, params);

		console.log("DeleteDebtPosition call - creditor_institution_code " + creditor_institution_code + ", iupd " + iupd);

		check(r, {
			'status is 200': (_r) => r.status === 200,
		}, tag);

	}


}
