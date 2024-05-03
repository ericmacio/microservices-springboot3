const https = require('https');
const httpClient = require('./httpClient');
const auth = require('./auth');

const caller = 'deleteCompositeProduct';
const args = process.argv.slice(2);
const productId = args[0];
if (typeof (productId) === 'undefined') { console.log('ERROR: productId is missing'); process.exit(); }
const type = 'writer';
const host = 'https://localhost:8443';
const method = 'DELETE';
const url = `/composite/${productId}`;

const deleteCompositeProduct = async () => {
    const accessToken = await auth.getAccessToken(type);
    if (typeof (accessToken) != 'undefined') {
        const headers = { 'Content-Type': 'application/json', Authorization: 'Bearer ' + accessToken };
        const httpsAgent = new https.Agent({ rejectUnauthorized: false });
        const requestData = { method, url, host, headers, httpsAgent };
        try {
            const result = await httpClient.sendRequest(requestData);
            if (!result.ok) {
                console.log(`${caller}: ERROR: httpClient.sendRequest result is ko`);
            } else {
                console.log(`${caller}: Success`);
                console.log(`${caller}: Data: ${JSON.stringify(result.data)}`);
            }
        } catch (error) {
            console.log(`${caller}: ERROR: httpClient.sendRequest failed: ${error}`);
        }
    } else {
        console.log(`${caller}: ERROR: cannot get access token`);
    }
}
deleteCompositeProduct();