import {
    AuthSoapAPI,
    convertToJson,
    filterFault
} from './util';


export const loginSOAP = (username: string, password: string): Promise<string> => {
    let xml = '<?xml version="1.0" encoding="utf-8"?>'
    xml += '<soap:Envelope '
    xml += 'xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" '
    xml += 'xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" '
    xml += 'xmlns:tns="http://ws.logicaldoc.com" '
    xml += 'xmlns:ns1="http://ws.logicaldoc.com"> '
    xml += '<soap:Body> '
    xml += '<ns1:login xmlns:ns1="http://ws.logicaldoc.com"> 	'
    xml += '<username>' + username + '</username> 	'
    xml += '<password>' + password + '</password>  '
    xml += '</ns1:login>'
    xml += '</soap:Body></soap:Envelope>';

    var options = {
        method: 'POST',
        headers: {
            'Accept': 'text/html,application/xhtml+xml,application/xml,text/xml;q=0.9,*/*;q=0.8',
            'Content-Length': xml.length,
            'Content-Type': 'text/xml; charset=utf-8',
            'SOAPAction': '""'
        },
        body: xml
    };

    return new Promise((resolve, reject) => {
        fetch(AuthSoapAPI, options)
            .then(response => response.text())
            .then(xml => convertToJson(xml))
            .then(filterFault)
            .then(responseJson => resolve(responseJson.Body.loginResponse.return))
            .catch(reason => reject(reason))
    });
}

export const logoutSOAP = (sid: string): Promise<string> => {
    let xml = '<?xml version="1.0" encoding="utf-8"?>'
    xml += '<soap:Envelope '
    xml += 'xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" '
    xml += 'xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" '
    xml += 'xmlns:tns="http://ws.logicaldoc.com" '
    xml += 'xmlns:ns1="http://ws.logicaldoc.com"> '
    xml += '<soap:Body> '
    xml += '<ns1:logout xmlns:ns1="http://ws.logicaldoc.com"> 	'
    xml += '<sid>' + sid + '</sid> 	'
    xml += '</ns1:logout>'
    xml += '</soap:Body></soap:Envelope>';

    var options = {
        method: 'POST',
        headers: {
            'Accept': 'text/html,application/xhtml+xml,application/xml,text/xml;q=0.9,*/*;q=0.8',
            'Content-Length': xml.length,
            'Content-Type': 'text/xml; charset=utf-8',
            'SOAPAction': '""'
        },
        body: xml
    };

    return new Promise((resolve, reject) => {
        fetch(AuthSoapAPI, options)
            .then(response => response.text())
            .then(xml => convertToJson(xml))
            .then(filterFault)
            .then(responseJson => resolve(responseJson.Body.logoutResponse.return))
            .catch(reason => reject(reason))
    });
}

export const renewSOAP = (sid: string): Promise => {
    let xml = '<?xml version="1.0" encoding="utf-8"?>'
    xml += '<soap:Envelope '
    xml += 'xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" '
    xml += 'xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" '
    xml += 'xmlns:tns="http://ws.logicaldoc.com" '
    xml += 'xmlns:ns1="http://ws.logicaldoc.com"> '
    xml += '<soap:Body> '
    xml += '<ns1:renew xmlns:ns1="http://ws.logicaldoc.com"> 	'
    xml += '<sid>' + sid + '</sid> 	'
    xml += '</ns1:renew>'
    xml += '</soap:Body></soap:Envelope>';

    var options = {
        method: 'POST',
        headers: {
            'Accept': 'text/html,application/xhtml+xml,application/xml,text/xml;q=0.9,*/*;q=0.8',
            'Content-Length': xml.length,
            'Content-Type': 'text/xml; charset=utf-8',
            'SOAPAction': '""'
        },
        body: xml
    };

    return fetch(AuthSoapAPI, options);

}

export const validSOAP = (sid: string): Promise<boolean> => {
    let xml = '<?xml version="1.0" encoding="utf-8"?>'
    xml += '<soap:Envelope '
    xml += 'xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" '
    xml += 'xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" '
    xml += 'xmlns:tns="http://ws.logicaldoc.com" '
    xml += 'xmlns:ns1="http://ws.logicaldoc.com"> '
    xml += '<soap:Body> '
    xml += '<ns1:valid xmlns:ns1="http://ws.logicaldoc.com"> 	'
    xml += '<sid>' + sid + '</sid> 	'
    xml += '</ns1:valid>'
    xml += '</soap:Body></soap:Envelope>';

    var options = {
        method: 'POST',
        headers: {
            'Accept': 'text/html,application/xhtml+xml,application/xml,text/xml;q=0.9,*/*;q=0.8',
            'Content-Length': xml.length,
            'Content-Type': 'text/xml; charset=utf-8',
            'SOAPAction': '""'
        },
        body: xml
    };

    return new Promise((resolve, reject) => {
        fetch(AuthSoapAPI, options)
            .then(response => response.text())
            .then(xml => convertToJson(xml))
            .then(responseJson => {
                const body = responseJson.Body;
                if (body.Fault) {
                    reject(Error(body.Fault.faultstring))
                } else {
                    resolve(body.validResponse.return === 'true')
                }
            })
            .catch(reason => reject(reason))
    });
}

export const ensureLogin = (username: string, password: string, sid: string) => {
    return new Promise((resolve, reject) => {
        if (!sid) {
            loginSOAP(username, password)
                .then(sid => {
                    console.log('login and return new sid');
                    resolve(sid)
                })
                .catch(reason => reject(reason))
        }
        else {
            validSOAP(sid)
                .then(valid => {
                    if (!valid) {
                        loginSOAP(username, password)
                            .then(sid => {
                                console.log('login and return new sid');
                                resolve(sid)
                            })
                            .catch(reason => reject(reason))
                    } else {
                        resolve(sid)
                    }
                })
                .catch(reason => reject(reason))
        }
    })
}
