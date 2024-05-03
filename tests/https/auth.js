const httpClient = require('./httpClient');

const grantType = 'client_credentials';
const writerCredentials = {
    clientId: 'cHtDVkTsb7KwpR65G6LJPV421W6awGOq',
    clientSecret: 'FWWdowPAMEcrz4iBNWpiiC5KD7dKyPmKmvx03poFTaHpS-G7rsjJzTaW1Txq4PXr'
};
const readerCredentials = {
    clientId: 'YKRx4rnwEQmlnEPjuN0DM0k5sR3JGekC',
    clientSecret: 'gUKxEi7rOdPdNwSc2mljI7s_qD7SrzECaoTEUMRf4DKGYhep1217VLPHjJW3PChI'
};
const audience = 'https://localhost:8443/composite';
const host = 'https://dev-3688dd2vsqyyzvw6.us.auth0.com';
const method = 'POST'
const url = '/oauth/token';
const headers = {'Content-Type': 'application/json'};

exports.getAccessToken = async(type) => {
    const caller = 'getAccessToken';
    var credentials;
    if (type === 'writer')
        credentials = writerCredentials;
    else if (type === 'reader')
        credentials = readerCredentials;
    else {
        console.log(`${caller}: ERROR bad type: ${type}`);
        return;
    }
    const data = {
        grant_type: grantType,
        client_id: credentials.clientId,
        client_secret: credentials.clientSecret,
        audience
    };
    const requestData = {method, url, host, headers, data};
    try {
        const result = await httpClient.sendRequest(requestData);
        if(!result.ok) {
            console.log(`${caller}: ERROR: httpClient.sendRequest result is ko`);
        } else {
            const { access_token, scope, expires_in, token_type} = result.data;
            console.log(`access_token: ${access_token}`);
            console.log(`scope: ${scope}`);
            console.log(`expires_in: ${expires_in}`);
            console.log(`token_type: ${token_type}`);
            return access_token;
        }
    } catch(error) {
        console.log(caller + ': ERROR: httpClient.sendRequest failed: ' + error);
    }
}