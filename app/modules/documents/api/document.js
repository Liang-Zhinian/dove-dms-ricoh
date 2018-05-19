import {
    DocumentSoapAPI,
    DocumentRestAPI,
    SearchRestAPI,
    buildJsonHeaders,
    convertToJson,
    filterFault
} from './util';

// restful api
export const listChildrenDocuments = (username: string, password: string, folderId: int) => {

    var options = {
        method: 'GET',
        headers: buildJsonHeaders(username, password)
    };

    return fetch(`${DocumentRestAPI}/list?folderId=${folderId}`, options)
        .then(response => response.json());
}

export const deleteDocument = (username: string, password: string, docId: int) => {

    var options = {
        method: 'DELETE',
        headers: buildJsonHeaders(username, password)
    };

    return fetch(`${DocumentRestAPI}/delete?docId=${docId}`, options)
}

export const search = (username: string, password: string, expression: string) => {

    var options = {
        method: 'POST',
        headers: buildJsonHeaders(username, password),
        body: JSON.stringify({
            expression: expression
        })
    };

    return fetch(`${SearchRestAPI}/find`, options)
}

// soap api
export const getContentSOAP = (sid: string, docId: int, onProgress: (percent) => {}) => {
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
                    reject(reason)
                }
            } else {
                reject(Error(this.response))
            }

        });

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

export const createDownloadTicketWithProgressSOAP = (sid: string, docId: int, onProgress: (percent) => {}) => {
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
                reject(reason)
            }
        };
        xhr.onerror = function () {
            // Also deal with the case when the entire request fails to begin with
            // This is probably a network error, so reject the promise with an appropriate message
            reject(Error('There was a network error while creating download ticket.'));
        };

        xhr.onprogress = function (oEvent) {
            console.log(oEvent);
            if (oEvent.lengthComputable) {
                let loaded = oEvent.loaded;
                let total = oEvent.total;
                //   this.setState({
                //     progress: loaded / total,
                //   });
                console.log(`progress: ${loaded / total}`);

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

export const createDocumentWithProgressSOAP = (sid: string, document: string, content: string, onProgress: (percent) => {}) => {

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
        // xml += '<title>' + document.title + '</title> \n';
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
            // resolve(xhr.response);
            try {
                const responseJson = convertToJson(xhr.response);
                responseJson = filterFault(responseJson);


                resolve(responseJson.Body.createResponse.document)
            } catch (reason) {
                reject(reason)
            }
        };
        xhr.onerror = function () {
            // Also deal with the case when the entire request fails to begin with
            // This is probably a network error, so reject the promise with an appropriate message
            reject(Error('There was a network error.'));
        };

        xhr.upload.onprogress = function (oEvent) {
            console.log(oEvent);
            if (oEvent.lengthComputable) {
                let loaded = oEvent.loaded;
                let total = oEvent.total;
                //   this.setState({
                //     progress: loaded / total,
                //   });
                console.log(`progress: ${loaded / total}`);

                onProgress && onProgress(loaded / total);

                // if (loaded === total)
                //     resolve(xhr.response);
            }
        };

        // xhr.onload = function (oEvent) {
        //     debugger;
        //     console.log(oEvent);
        //     resolve(xhr.response);
        // };


        // xhr.upload.addEventListener("load", function (oEvent) {
        //     debugger;
        //     console.log(oEvent);
        //     resolve(xhr.response);
        // });

        xhr.upload.onerror = function (oEvent) {
            console.log(oEvent);
            console.log("An error occurred while transferring the file.");
            reject(Error('An error occurred while transferring the file.'));
        };

        xhr.upload.onabort = function (oEvent) {
            console.log(oEvent);
            console.log("The transfer has been canceled by the user.");
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

}