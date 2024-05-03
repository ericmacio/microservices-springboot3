const httpClient = require('./httpClient');
const host = 'http://localhost:8090';
const method = 'POST';
const url = '/composite';
const headers = {ContentType: 'application/json'};
const data = {
  "name": "string",
  "weight": 0,
  "recommendations": [
    {
      "recommendationId": 2,
      "author": "Eric",
      "rate": 0,
      "content": "string"
    }
  ],
  "reviews": [
    {
      "reviewId": 2,
      "author": "Eric",
      "subject": "string",
      "content": "string"
    }
  ],
  "serviceAddresses": {
    "cmp": "string",
    "pro": "string",
    "rev": "string",
    "rec": "string"
  }
}
const args = process.argv.slice(2);
const productId = args[0];
if (typeof(productId) === 'undefined') { console.log('ERROR: productId is missing'); process.exit();}
console.log('productId: ' + productId);
data.productId = productId;
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