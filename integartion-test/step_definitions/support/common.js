const axios = require("axios");
const assert = require("assert");
const fs = require('fs');

let rawdata = fs.readFileSync('./config/properties.json');
let properties = JSON.parse(rawdata);
const gps_host = properties.gps_host;

function get(url) {
    return axios.get(gps_host + url)
         .then(res => {
             return res;
         })
         .catch(error => {
             assert.fail(error);
         });
}

function post(url, body) {
    return axios.post(gps_host + url, body)
        .then(res => {
            return res;
        })
        .catch(error => {
            assert.fail(error);
        });
}

function put(url, body) {
    return axios.put(gps_host + url, body)
        .then(res => {
            return res;
        })
        .catch(error => {
            assert.fail(error);
        });
}


function del(url) {
    return axios.delete(gps_host + url)
        .then(res => {
            return res;
        })
        .catch(error => {
            assert.fail(error);
        });
}

function randomIban() {
    return "IT" + (Math.round(Math.random() * 89999999) + 10000000);
}

function randomName() {
    return "Name_" + Math.floor(Math.random() * 100);
}

module.exports = {get, post, put, del, randomIban, randomName}
