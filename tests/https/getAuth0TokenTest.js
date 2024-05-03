const https = require('https');

const httpClient = require('./httpClient');
const grant_type = 'client_credentials';
const client_id = '7H552dfTUKJ1WviwOvgYQAwGuGhwyio1';
const client_secret = 'HVnvVXFF8jUbfRFem25YjbG7K4rjlWDzZMBTZXOo64lovUn6T33WfCDeGw0Y556Q';
const audience = 'https://dev-3688dd2vsqyyzvw6.us.auth0.com/api/v2/';
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