import {
    getDocumentSoapAPI,
    getDocumentRestAPI,
    getSearchRestAPI,
    buildJsonHeaders,
    convertToJson,
    filterFault,
    createBasicAuthHeader,
    postSOAP
} from './util';

import handle from '../../../ExceptionHandler';

// restful api
export const listChildrenDocuments = async (username: string, password: string, folderId: int) => {

    var options = {
        method: 'GET',
        headers: buildJsonHeaders(username, password)
    };
    const DocumentRestAPI = await getDocumentRestAPI();

    return fetch(`${DocumentRestAPI}/list?folderId=${folderId}`, options)
        .then(response => response.json());
}

export const deleteDocument = async (username: string, password: string, docId: int) => {

    var options = {
        method: 'DELETE',
        headers: buildJsonHeaders(username, password)
    };

    const DocumentRestAPI = await getDocumentRestAPI();
    return fetch(`${DocumentRestAPI}/delete?docId=${docId}`, options)
}

export const search = async (username: string, password: string, expression: string) => {

    var options = {
        method: 'POST',
        headers: buildJsonHeaders(username, password),
        body: JSON.stringify({
            expression: expression
        })
    };

    const SearchRestAPI = await getSearchRestAPI();
    return fetch(`${SearchRestAPI}/find`, options)
}

export const getDocument = async (username: string, password: string, docId: int) => {

    var options = {
        method: 'GET',
        headers: buildJsonHeaders(username, password)
    };

    const DocumentRestAPI = await getDocumentRestAPI();
    return fetch(`${DocumentRestAPI}/getDocument?docId=${docId}`, options)
}

export const updateDocument = async (username: string, password: string, document: {}) => {

    var options = {
        method: 'PUT',
        headers: buildJsonHeaders(username, password),
        body: JSON.stringify(document),
    };

    const DocumentRestAPI = await getDocumentRestAPI();
    return fetch(`${DocumentRestAPI}/update`, options)
}

export const createDocument = async (username: string, password: string, folderId: int, filename: string, filedata: string) => {
    if (!folderId) {
        Promise.reject(new Error('invalid folderId'));
    }
    if (!filename) {
        Promise.reject(new Error('invalid filename'));
    }
    if (!filedata) {
        Promise.reject(new Error('invalid filedata'));
    }

    var data = new FormData();
    data.append('folderId', folderId);
    data.append('filename', folderId);
    data.append('filedata', filedata);

    var options = {
        method: 'POST',
        headers: buildJsonHeaders(username, password),
        body: data,
    };

    const DocumentRestAPI = await getDocumentRestAPI();
    return fetch(`${DocumentRestAPI}/update`, options);
}


// soap api
export const getContentSOAP = async (sid: string, docId: int, onProgress: (percent) => {}) => {
    const DocumentSoapAPI = await getDocumentSoapAPI();
    return new Promise((resolve, reject) => {
        let xml = '<?xml version="1.0" encoding="utf-8"?>'
        xml += '<soap:Envelope '
        xml += 'xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" '
        xml += 'xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" '
        xml += 'xmlns:tns="http://ws.logicaldoc.com" '
        xml += 'xmlns:ns1="http://ws.logicaldoc.com"> '
        xml += '<soap:Body> '
        xml += '<ns1:getContent> '
        xml += '    <sid>' + sid + '</sid>'
        xml += '    <docId>' + docId + '</docId>'
        xml += '</ns1:getContent>'
        xml += '</soap:Body></soap:Envelope>';

        var xhr = new XMLHttpRequest();
        xhr.withCredentials = true;

        xhr.addEventListener("readystatechange", function () {
            if (this.readyState !== 4) {

                return;
            }
            if (this.status === 200) {
                try {
                    const responseJson = convertToJson(this.response);
                    responseJson = filterFault(responseJson);
                    resolve({ id: docId, content: responseJson.Body.getContentResponse.return })
                } catch (reason) {

                    handle(reason);
                    reject(reason);
                }
            } else {
                handle(this.response);
                reject(Error(this.response));
            }

        });

        xhr.addEventListener('progress', function (oEvent) {
            if (oEvent.lengthComputable) {
                let loaded = oEvent.loaded;
                let total = oEvent.total;
                onProgress && onProgress(loaded / total);
            }
        })

        xhr.open("POST", DocumentSoapAPI);
        // xhr.responseType = "moz-blob";
        xhr.setRequestHeader('Content-Length', xml.length);
        xhr.setRequestHeader('Accept', 'application/soap+xml; charset=utf-8');
        xhr.setRequestHeader('Content-Type', 'application/soap+xml; charset=utf-8');
        xhr.setRequestHeader('Accept-Encoding', 'application/soap+xml; charset=utf-8');
        xhr.setRequestHeader('SOAPAction', '""');


        xhr.send(xml);
    });
}

export const createDownloadTicketWithProgressSOAP = async (sid: string, docId: int, onProgress: (percent) => {}) => {

    const DocumentSoapAPI = await getDocumentSoapAPI();
    return new Promise((resolve, reject) => {
        let xml = '<?xml version="1.0" encoding="utf-8"?>'
        xml += '<soap:Envelope '
        xml += 'xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" '
        xml += 'xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" '
        xml += 'xmlns:tns="http://ws.logicaldoc.com" '
        xml += 'xmlns:ns1="http://ws.logicaldoc.com"> '
        xml += '<soap:Body> '
        xml += '<ns1:createDownloadTicket> '
        xml += '    <sid>' + sid + '</sid>'
        xml += '    <docId>' + docId + '</docId>'
        xml += '</ns1:createDownloadTicket>'
        xml += '</soap:Body></soap:Envelope>';


        var xhr = new XMLHttpRequest();
        xhr.withCredentials = true;

        xhr.onload = function () {
            try {
                const responseJson = convertToJson(xhr.response);
                responseJson = filterFault(responseJson);

                let ticket = responseJson.Body.createDownloadTicketResponse.ticket;

                resolve(ticket)
            } catch (reason) {
                handle(reason);
                reject(reason)
            }
        };
        xhr.onerror = function () {
            handle(Error('There was a network error while creating download ticket.'));
            // Also deal with the case when the entire request fails to begin with
            // This is probably a network error, so reject the promise with an appropriate message
            reject(Error('There was a network error while creating download ticket.'));
        };

        xhr.onprogress = function (oEvent) {
            if (oEvent.lengthComputable) {
                let loaded = oEvent.loaded;
                let total = oEvent.total;
                onProgress && onProgress(loaded / total);
            }
        };

        xhr.open("POST", DocumentSoapAPI);

        xhr.setRequestHeader('Content-Length', xml.length);
        xhr.setRequestHeader('Content-Type', 'application/soap+xml; charset=utf-8');
        xhr.setRequestHeader('cache-control', 'no-cache');
        xhr.setRequestHeader('SOAPAction', '""');

        // Send the request
        xhr.send(xml);
    });

}

export const createDocumentWithProgressSOAP = async (sid: string, document: string, content: string, onProgress: (percent) => {}) => {

    const DocumentSoapAPI = await getDocumentSoapAPI();
    return new Promise((resolve, reject) => {
        let xml = '<?xml version="1.0" encoding="utf-8"?> \n';
        xml += '<soap:Envelope \n';
        xml += 'xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" \n';
        xml += 'xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" \n';
        xml += 'xmlns:tns="http://ws.logicaldoc.com" \n';
        xml += 'xmlns:ns1="http://ws.logicaldoc.com"> \n';
        xml += '<soap:Body> \n';
        xml += '<ns1:create xmlns:ns1="http://ws.logicaldoc.com"> \n';

        xml += '<sid>' + sid + '</sid> \n';

        xml += '<document> \n';
        xml += '<id>0</id> \n'; // 0: automaticly generate document id
        xml += '<fileSize>' + document.fileSize + '</fileSize> \n';
        xml += '<title>' + document.title + '</title> \n';
        xml += '<date>' + document.date + '</date> \n';
        xml += '<type>' + document.type + '</type> \n';
        xml += '<fileName>' + document.fileName + '</fileName> \n';
        xml += '<folderId>' + document.folderId + '</folderId> \n';
        xml += '</document> \n';

        xml += '<content>' + content + '</content> \n'; // base64 maybe?
        xml += '</ns1:create> \n';
        xml += '</soap:Body></soap:Envelope>';

        var xhr = new XMLHttpRequest();
        xhr.withCredentials = true;

        xhr.onload = function () {
            // upload completed
            // If successful, resolve the promise by passing back the request response
            try {
                const responseJson = convertToJson(xhr.response);
                responseJson = filterFault(responseJson);


                resolve(responseJson.Body.createResponse)
            } catch (reason) {
                handle(reason);
                reject(reason)
            }
        };
        xhr.onerror = function () {
            // Also deal with the case when the entire request fails to begin with
            // This is probably a network error, so reject the promise with an appropriate message
            reject(Error('There was a network error.'));
        };

        xhr.upload.onprogress = function (oEvent) {
            if (oEvent.lengthComputable) {
                let loaded = oEvent.loaded;
                let total = oEvent.total;

                onProgress && onProgress(loaded / total);

            }
        };

        xhr.upload.onerror = function (oEvent) {
            handle(oEvent);
            reject(Error('An error occurred while transferring the file.'));
        };

        xhr.upload.onabort = function (oEvent) {
            reject(Error('The transfer has been canceled by the user.'));
        };

        xhr.open("POST", DocumentSoapAPI);

        xhr.setRequestHeader('Content-Length', xml.length);
        xhr.setRequestHeader('Content-Type', 'application/soap+xml; charset=utf-8');
        xhr.setRequestHeader('cache-control', 'no-cache');
        xhr.setRequestHeader('SOAPAction', '""');

        // Send the request
        xhr.send(xml);
    });

    /**
     * 
    const DocumentSoapAPI = await getDocumentSoapAPI();
    let xml = '<?xml version="1.0" encoding="utf-8"?> \n';
        xml += '<soap:Envelope \n';
        xml += 'xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" \n';
        xml += 'xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" \n';
        xml += 'xmlns:tns="http://ws.logicaldoc.com" \n';
        xml += 'xmlns:ns1="http://ws.logicaldoc.com"> \n';
        xml += '<soap:Body> \n';
        xml += '<ns1:create xmlns:ns1="http://ws.logicaldoc.com"> \n';

        xml += '<sid>' + sid + '</sid> \n';

        xml += '<document> \n';
        xml += '<id>0</id> \n'; // 0: automaticly generate document id
        xml += '<fileSize>' + document.fileSize + '</fileSize> \n';
        xml += '<title>' + document.title + '</title> \n';
        xml += '<date>' + document.date + '</date> \n';
        xml += '<type>' + document.type + '</type> \n';
        xml += '<fileName>' + document.fileName + '</fileName> \n';
        xml += '<folderId>' + document.folderId + '</folderId> \n';
        xml += '</document> \n';

        xml += '<content>' + content + '</content> \n'; // base64 maybe?
        xml += '</ns1:create> \n';
        xml += '</soap:Body></soap:Envelope>';

    return postSOAP(DocumentSoapAPI, sid, xml, onProgress)
        .then(json=>json.Body)
        .catch(error=>{throw error});
     */
}