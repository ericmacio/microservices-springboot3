const httpClient = require('./httpClient');
const host = 'http://localhost:8090';
const method = 'GET';
const urlComposite = '/composite/';
const headers = {};
const args = process.argv.slice(2);
const productId = args[0];
if (typeof(productId) === 'undefined') { console.log('ERROR: productId is missing'); process.exit(); }
console.log('productId: ' + productId);
const url = urlComposite + productId;
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