const {post, get, put, del} = require("./common");

async function getOrganization(code) {
    await get(`/organizations/${code}`)
}

async function createOrganization(idOrg, body) {
    await post(`/organizations/${idOrg}`, body)
}

async function updateOrganization(idOrg, body) {
    await put(`/organizations/${idOrg}`, body)
}

async function deleteOrganization(idOrg) {
    await del(`/organizations/${idOrg}`)
}


async function getOrganizationService(idOrg, idService) {
    await get(`/organizations/${idOrg}/services/${idService}`)
}

async function createOrganizationService(idOrg, idService, body) {
    await post(`/organizations/${idOrg}/services/${idService}`, body)
}

async function updateOrganizationService(idOrg, idService, body) {
    await put(`/organizations/${idOrg}/services/${idService}`, body)
}

async function deleteOrganizationService(idOrg, idService) {
    await del(`/organizations/${idOrg}/services/${idService}`)
}


async function getServices() {
    await get(`/services`)
}

async function getService(idService) {
    await get(`/services/${idService}`)
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
