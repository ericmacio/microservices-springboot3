const httpClient = require('./httpClient');
const host = 'http://localhost:8090';
const method = 'GET';
const url = '/headerrouting';
const headers = {host: 'i.feel.lucky:8090' };
const requestData = {method, url, host, headers};
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