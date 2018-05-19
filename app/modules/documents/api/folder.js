import {
    FolderSoapAPI,
    FolderRestAPI,
    buildJsonHeaders,
    convertToJson,
    filterFault
} from './util';

// rest api
export const listChildrenFolders = (username: string, password: string, folderId: int) => {
    var options = {
        method: 'GET',
        headers: buildJsonHeaders(username, password)
    };
    return fetch(`${FolderRestAPI}/listChildren?folderId=${folderId}`, options)
        .then(response => response.json());

}

// soap api
export const getRootFolder = (sid: string) => {
    
    return new Promise((resolve, reject) => {
        let xml = '<?xml version="1.0" encoding="utf-8"?>'
        xml += '<soap:Envelope '
        xml += 'xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" '
        xml += 'xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" '
        xml += 'xmlns:tns="http://ws.logicaldoc.com" '
        xml += 'xmlns:ns1="http://ws.logicaldoc.com"> '
        xml += '<soap:Body> '
        xml += '<ns1:getRootFolder> '
        xml += '    <sid>' + sid + '</sid>'
        xml += '</ns1:getRootFolder>'
        xml += '</soap:Body></soap:Envelope>';

        var options = {
            method: 'POST',
            headers: {
                'Content-Length': xml.length,
                'Content-Type': 'application/soap+xml; charset=utf-8',
                'SOAPAction': '""'
            },
            body: xml
        };

        fetch(FolderSoapAPI, options)
            .then(response => response.text())
            .then(xml => convertToJson(xml))
            .then(filterFault)
            .then(responseJson => resolve(responseJson.Body.getRootFolderResponse.folder))
            .catch(reason => reject(reason))
    });
}

export const createFolderSOAP = (sid: string, parentId: int, name: string, onProgress: (percent) => {}) => {
    return new Promise((resolve, reject) => {
        let xml = '<?xml version="1.0" encoding="utf-8"?>'
        xml += '<soap:Envelope '
        xml += 'xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" '
        xml += 'xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" '
        xml += 'xmlns:tns="http://ws.logicaldoc.com" '
        xml += 'xmlns:ns1="http://ws.logicaldoc.com"> '
        xml += '<soap:Body> '
        xml += '<ns1:createFolder> '
        xml += '    <sid>' + sid + '</sid>'
        xml += '    <parentId>' + parentId + '</parentId>'
        xml += '    <name>' + name + '</name>'
        xml += '</ns1:createFolder>'
        xml += '</soap:Body></soap:Envelope>';

        var xhr = new XMLHttpRequest();
        xhr.withCredentials = true;

        xhr.onload = function () {
            

            try {
                const responseJson = convertToJson(xhr.response);
                responseJson = filterFault(responseJson);

                resolve(xhr.response)
            } catch (reason) {
                reject(reason)
            }
        };
        xhr.onerror = function () {
            // Also deal with the case when the entire request fails to begin with
            // This is probably a network error, so reject the promise with an appropriate message
            reject(Error('There was a network error.'));
        };


        xhr.addEventListener('progress', function (oEvent) {
            if (oEvent.lengthComputable) {
                let loaded = oEvent.loaded;
                let total = oEvent.total;
                //   this.setState({
                //     progress: loaded / total,
                //   });
                // console.log(`progress: ${loaded / total}`);
                onProgress && onProgress(loaded / total);
            }
        })

        xhr.open("POST", FolderSoapAPI);
        // xhr.responseType = "moz-blob";
        xhr.setRequestHeader('Content-Length', xml.length);
        xhr.setRequestHeader('Accept', 'application/soap+xml; charset=utf-8');
        xhr.setRequestHeader('Content-Type', 'application/soap+xml; charset=utf-8');
        xhr.setRequestHeader('Accept-Encoding', 'application/soap+xml; charset=utf-8');
        xhr.setRequestHeader('SOAPAction', '""');

        // Send the request
        xhr.send(xml);
    });
}
