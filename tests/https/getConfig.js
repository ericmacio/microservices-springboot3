const https = require('https');

const httpClient = require('./httpClient');
const args = process.argv.slice(2);
const item = args[0];
if (typeof(item) === 'undefined') { console.log('ERROR: item is missing'); process.exit(); }
console.log('item: ' + item);
const user = 'dev-usr';
const pwd = 'dev-pwd';
const host = `https://${user}:${pwd}@localhost:8443`;
const method = 'GET';
const url = `/config/${item}/docker`;
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
    console.log('Data: ' + JSON.stringify(data));
}
test();