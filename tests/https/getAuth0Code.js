const https = require('https');
const httpClient = require('./httpClient');

const host = 'https://dev-3688dd2vsqyyzvw6.us.auth0.com';
const method = 'GET'
const client_id = 'cHtDVkTsb7KwpR65G6LJPV421W6awGOq';
const audience = 'https://localhost:8443/composite';
const redirect_uri = 'https://my.redirect.uri';
const scope = 'openid email product:write';
const responseType = 'code';
const state = 845361;
const url = `/authorize?audience=${audience}&response_type=${responseType}&client_id=${client_id}&redirect_uri=${redirect_uri}&scope=${scope}&state=${state}`;
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
