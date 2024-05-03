const https = require('https');

const httpClient = require('./httpClient');
const host = 'https://u:p@localhost:8443';
const method = 'GET';
const url = '/eureka/apps';
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
    //console.log('data: ' + JSON.stringify(data));
    if(typeof(data) != 'undefined' && typeof(data.applications) != 'undefined') {
        const applications = data.applications.application;
        //console.log('applications: ' + JSON.stringify(applications));
        const instances = applications.map(app => app.instance)
        var instanceId = [];
        instances.forEach(instance => {
            instance.forEach(i => instanceId.push({ instanceId: i.instanceId, ipAddr: i.ipAddr }));
        })
    console.log('instance: ' + JSON.stringify(instanceId));
    } else {
        console.log('ERROR: data.applications undefined')
    }
}
test();
