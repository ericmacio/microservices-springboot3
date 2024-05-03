const https = require('https');
const httpClient = require('./httpClient');

const host = 'https://localhost:8443';
const method = 'GET'
const client_id = 'reader';
const redirect_uri = 'https://my.redirect.uri';
const scope = 'product:read';
const state = 35725;
const url = `/oauth2/authorize?response_type=code&client_id=${client_id}&redirect_uri=${redirect_uri}&scope=${scope}&state=${state}`;
const httpsAgent = new https.Agent({rejectUnauthorized: false});
const headers = {};
console.log('url: ' + host + url);
const requestData = {method, url, host, headers, httpsAgent};
const callApi = async() => {
    const caller = 'callApi';
    try {
        const result = await httpClient.sendRequest(requestData);
        if(!result.ok) {
            console.log(caller + ': ERROR: httpClient.sendRequest result is ko');
        } else {
            return result.data;
        }
    } catch(error) {
        console.log(caller + ': ERROR: httpClient.sendRequest failed: ' + error);
    }
}
test = async() => {
    const data = await callApi();
    console.log('Data: ' + JSON.stringify(data));
}
test();
