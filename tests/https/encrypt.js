const https = require('https');

const httpClient = require('./httpClient');
const args = process.argv.slice(2);
const data = args[0];
if (typeof(data) === 'undefined') { console.log('ERROR: data is missing'); process.exit(); }
console.log('data: ' + data);
const user = 'dev-usr';
const pwd = 'dev-pwd';
const host = `https://${user}:${pwd}@localhost:8443`;
const method = 'POST';
const url = `/config/encrypt`;
const headers = {};
const httpsAgent = {} = new https.Agent({rejectUnauthorized: false});
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