// 1. init code (once per VU)
// prepares the script: loading files, importing modules, and defining functions

import {check, sleep} from 'k6';
import {SharedArray} from 'k6/data';


// read configuration
// note: SharedArray can currently only be constructed inside init code
// according to https://k6.io/docs/javascript-api/k6-data/sharedarray
const varsArray = new SharedArray('vars', function () {
    return JSON.parse(open(`./${__ENV.VARS}`)).environment;
});
// workaround to use shared array (only array should be used)
const vars = varsArray[0];
const rootUrl = `${vars.host}/${vars.basePath}`;


export function setup() {
    // 2. setup code (once)
    // The setup code runs, setting up the test environment (optional) and generating data
    // used to reuse code for the same VU

    // precondition is moved to default fn because in this stage
    // __VU is always 0 and cannot be used to create env properly
}

function precondition(params, id) {
    const tempId = `ORG${id}`;

    // remove the organization if it already exists
    let response = deleteOrganization(rootUrl, params, id);
    let key = `initial step for organization ${getOrganizationCode(id)}`;
    check(response, {
        [key]: (r) => r.status === 200 || r.status === 404,
    });

    // TODO add service in to DB

}

function postcondition(params, id) {
    const tempId = `STATION${id}`;

    // remove creditor institution and broker used in the test
    let response = deleteCreditorInstitution(rootUrl, params, tempId);
    let key = `final step for organization-ec ${getOrganizationCode(tempId)} / ${getCiCode(tempId)}`;
    check(response, {
        [key]: (r) => r.status === 200 || r.status === 404,
    });

    response = deleteBroker(rootUrl, params, tempId);
    key = `final step for organization-broker ${getOrganizationCode(id)} / ${getBrokerCode(tempId)}`;
    check(response, {
        [key]: (r) => r.status === 200 || r.status === 404,
    });
}

export default function (data) {
    // 3. VU code (once per iteration, as many times as the test options require)
    // VU code runs in the default() function, running for as long and as many times as the options define.
    const token = vars.env === "local" ? "-" : getJWTToken(vars.tenantId, vars.clientId, vars.clientSecret, vars.resource);

    const params = {
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        },
    };

    const tempId = `STATION${__VU}`;

    precondition(params, __VU);


    // Create organization
    var response = createOrganization(rootUrl, params, __VU, getBrokerCode(tempId));
    check(response, {
        'createOrganization': (r) => r.status === 201,
    });

    // Get organization
    response = getOrganization(rootUrl, params, __VU);
    check(response, {
        'getOrganization': (r) => r.status === 200,
    });

    // Update organization
    response = updateOrganization(rootUrl, params, __VU, getBrokerCode(tempId));
    check(response, {
        'updateOrganization': (r) => r.status === 200,
    });


    sleep(0.5)


    // Delete organization
    response = deleteOrganization(rootUrl, params, __VU);
    check(response, {
        'deleteOrganization': (r) => r.status === 200,
    });

    postcondition(params, __VU);
}

export function teardown(data) {
    // 4. teardown code (once per script)
    // The teardown code runs, postprocessing data and closing the test environment.

    // postcondition is moved to default fn because in this stage
    // __VU is always 0 and cannot be used to create env properly

}
