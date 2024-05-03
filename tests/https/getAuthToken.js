const https = require('https');

const httpClient = require('./httpClient');
const client_id = 'writer';
const client_secret = 'secret-writer';
const scope = 'product:read product:write';
const host = `https://${client_id}:${client_secret}@localhost:8443`;
const method = 'POST'
const url = '/oauth2/token';
const data = `grant_type=client_credentials&scope=${scope}`;
const headers = {'Content-Type': 'application/x-www-form-urlencoded'};
const httpsAgent = new https.Agent({rejectUnauthorized: false});
const requestData = {method, url, host, headers, httpsAgent, data};
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