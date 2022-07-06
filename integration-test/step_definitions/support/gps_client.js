const {post, get, put, del} = require("./common");

function getOrganization(code) {
    return get(`/organizations/${code}`)
}

function createOrganization(idOrg, body) {
    return post(`/organizations/${idOrg}`, body)
}

function updateOrganization(idOrg, body) {
    return put(`/organizations/${idOrg}`, body)
}

function deleteOrganization(idOrg) {
    return del(`/organizations/${idOrg}`)
}


function getOrganizationService(idOrg, idService) {
    return get(`/organizations/${idOrg}/services/${idService}`)
}

function createOrganizationService(idOrg, idService, body) {
    return post(`/organizations/${idOrg}/services/${idService}`, body)
}

function updateOrganizationService(idOrg, idService, body) {
    return put(`/organizations/${idOrg}/services/${idService}`, body)
}

function deleteOrganizationService(idOrg, idService) {
    return del(`/organizations/${idOrg}/services/${idService}`)
}


function getServices() {
    return get(`/services`)
}

function getService(idService) {
    return get(`/services/${idService}`)
}

module.exports = {
    getOrganization,
    createOrganization,
    updateOrganization,
    deleteOrganization,
    getOrganizationService,
    createOrganizationService,
    updateOrganizationService,
    deleteOrganizationService,
    getServices,
    getService,
}
