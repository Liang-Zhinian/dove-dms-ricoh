import {
    convertToJson,
    filterFault
} from './util';

const BASE = 'http://isd4u.com:8080/conversionservice';
export const convert = (fileName, contentType, content) => {

    let xml = '<?xml version="1.0" encoding="utf-8"?>'
    xml += '<soap:Envelope '
    xml += 'xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" '
    xml += 'xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" '
    xml += 'xmlns:tns="http://ws.atpath.com" '
    xml += 'xmlns:ns1="http://ws.atpath.com"> '
    xml += '<soap:Body> '
    xml += '<ns1:convert> 	'
    xml += '<Dfile>'
    xml += '<name>'
    xml += fileName
    xml += '</name> 	'
    xml += '<fileType>'
    xml += contentType
    xml += '</fileType> 	'
    xml += '<dfile>'
    xml += content //Base64.atob(content)
    xml += '</dfile> 	'
    xml += '</Dfile> 	'
    xml += '</ns1:convert>'
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
        fetch(`${BASE}/services/Convert?wsdl`, options)
            .then(response => response.text())
            .then(xml => convertToJson(xml))
            .then(filterFault)
            .then(responseJson => resolve(responseJson.Body.convertResponse.return))
            .catch(reason => reject(reason))
    });
} 