const https = require('https');

const httpClient = require('./httpClient');
const grant_type = 'client_credentials';
const client_id = 'cHtDVkTsb7KwpR65G6LJPV421W6awGOq';
const client_secret = 'FWWdowPAMEcrz4iBNWpiiC5KD7dKyPmKmvx03poFTaHpS-G7rsjJzTaW1Txq4PXr';
const audience = 'https://localhost:8443/composite';
//const scope = 'product:write';
const host = 'https://dev-3688dd2vsqyyzvw6.us.auth0.com';
const method = 'POST'
const url = '/oauth/token';
const data = {
    grant_type,
    client_id,
    client_secret,
    audience
};
const headers = {'Content-Type': 'application/json'};
//const httpsAgent = new https.Agent({rejectUnauthorized: false});
const requestData = {method, url, host, headers, data};
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