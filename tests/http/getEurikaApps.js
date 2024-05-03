const httpClient = require('./httpClient');
const host = 'http://localhost:8090';
const method = 'GET';
const url = '/eureka/apps';
const headers = {};
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
    const applications = data.applications.application;
    //console.log('applications: ' + JSON.stringify(applications));
    const instances = applications.map(app => app.instance)
    var instanceId = [];
    instances.forEach(instance => {
        instance.forEach(i => instanceId.push(i.instanceId));
    })
    console.log('instance: ' + JSON.stringify(instanceId));
}
test();