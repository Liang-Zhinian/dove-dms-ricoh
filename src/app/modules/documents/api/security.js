import {
    getSecuritySoapAPI,
    convertToJson,
    filterFault
} from './util';

import handle from '../../../ExceptionHandler';

export const getUserByUsernameSOAP = async (sid: string, username: string): Promise<string> => {
    let xml = '<?xml version="1.0" encoding="utf-8"?>'
    xml += '<soap:Envelope '
    xml += 'xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" '
    xml += 'xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" '
    xml += 'xmlns:tns="http://ws.logicaldoc.com" '
    xml += 'xmlns:ns1="http://ws.logicaldoc.com"> '
    xml += '<soap:Body> '
    xml += '<ns1:getUserByUsername xmlns:ns1="http://ws.logicaldoc.com"> 	'
    xml += '<sid>' + sid + '</sid> 	'
    xml += '<username>' + username + '</username>  '
    xml += '</ns1:getUserByUsername>'
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

    const SecuritySoapAPI = await getSecuritySoapAPI();

    return new Promise((resolve, reject) => {
        fetch(SecuritySoapAPI, options)
            .then(response => response.text())
            .then(xml => convertToJson(xml))
            .then(filterFault)
            .then(responseJson => resolve(responseJson.Body.getUserByUsernameResponse.user))
            .catch(reason => {
                handle(reason);
                reject(reason)
            })
    });
}