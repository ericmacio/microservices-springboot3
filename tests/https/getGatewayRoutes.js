const https = require('https');

const httpClient = require('./httpClient');
const host = 'http://localhost:8443';
const method = 'GET';
const url = '/actuator/gateway/routes';
const headers = {};
const httpsAgent = {} = new https.Agent({rejectUnauthorized: false});
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
    var routes = [];
    data.forEach(route => {
        routes.push({routeid: route.route_id, uri: route.uri});
    })
    console.log('Data: ' + JSON.stringify(routes));
}
test();