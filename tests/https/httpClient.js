const axios = require('axios');

exports.sendRequest = async(requestData) => {
	const caller = 'sendRequest';
	const url = requestData.host + requestData.url;
	//console.log(caller + ': Send request to server: ' + requestData.method + ' ' + url);
	var config = {
		method: requestData.method,
		url: url,
		headers: requestData.headers,
		maxContentLength: Infinity,
		maxBodyLength: Infinity,
		httpsAgent: requestData.httpsAgent
	};
	if(requestData.data) {
		config.data = requestData.data;
		//console.log('data: ' + JSON.stringify(config.data));
	} else if(requestData.form)
		config.data = requestData.form;
	try {
		const startMs = new Date().getTime();
		let res = await axios(config);
		const endMs = new Date().getTime();
        const durationMs = endMs-startMs;
		return {ok: true, data: res.data, durationMs};
	} catch(error) {
		console.log(caller + ': ERROR: Request failed: ' + requestData.method + ' ' + url + ': ' + error);
		//console.log(caller + ': ERROR: ' + JSON.stringify(error));
		if(config.data) console.log(caller + ': ERROR: data sent: ' + JSON.stringify(config.data));
		if (error.response) {
			// Request made and server responded
			console.log(caller + ': ERROR: ' + error.response.status + ' ' + error.response.statusText);
			console.log(caller + ': ERROR: Data: ' + JSON.stringify(error.response.data));
		} else if (error.request) {
			// The request was made but no response was received
			console.log(caller + ': ERROR: No response received from the server');
			console.log(caller + ': ERROR: Request sent: ' + requestData.method + ' ' + url);
		}
		return {ok: false};
	}
}