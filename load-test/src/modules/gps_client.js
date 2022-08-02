import http from 'k6/http';

const ciCode = "12345678";

export function getCiCode(id) {
	return ciCode + ('000'+id).slice(-3);
}

export function createCreditorInstitutionEnrollment(rootUrl, params, creditorInstitutionsId) {
	const url = `${rootUrl}/${creditorInstitutionsId}`

	const payload = {
        "companyName": "Comune di Lorem",
        "enrollments": [
            {
                "serviceId": "id-servizio-1",
                "officeName": "office-test",
                "iban": "iban-1",
                "segregationCode": "47",
                "remittanceInformation": "causale-1",
                "postalIban": null
            }
        ],
    }

	return http.post(url, JSON.stringify(payload), params);
}
