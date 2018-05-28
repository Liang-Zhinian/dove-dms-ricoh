
import Base64 from '../lib/Base64'
import XMLParser from '../lib/XMLParser'
import { getItem } from '../../../services/storageService';
import { storageKey } from '../env';
import handle from '../../../ExceptionHandler';

const getServerUrl = async () => {
    const Document_Server = await getItem(storageKey.DOCUMENT_SERVER);
    const PROTOCOL = Document_Server.https ? 'https:' : 'http:';
    const HOST = Document_Server.server;
    const PORT = Document_Server.port;

    // API URL
    const API = `${PROTOCOL}//${HOST}${PORT ? ':' + PORT : ''}/services`;
    return API;
};

export const getAuthSoapAPI = async () => {
    const url = await getServerUrl();
    return url + '/Auth?wsdl';
}

export const getFolderSoapAPI = async () => {
    const url = await getServerUrl();
    return url + '/Folder?wsdl';
}

export const getFolderRestAPI = async () => {
    const url = await getServerUrl();
    return url + '/rest/folder';
}

export const getDocumentSoapAPI = async () => {
    const url = await getServerUrl();
    return url + '/Document?wsdl';
}

export const getDocumentRestAPI = async () => {
    const url = await getServerUrl();
    return url + '/rest/document';
}

export const getSearchRestAPI = async () => {
    const url = await getServerUrl();
    return url + '/rest/search';
}

export const getSecuritySoapAPI = async () => {
    const url = await getServerUrl();
    return url + '/Security?wsdl';
}

export function buildJsonHeaders(username: string, password: string) {
    return {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
        'authorization': createBasicAuthHeader(username, password),
    };
}
export function buildPlainHeaders(username: string, password: string) {
    return {
        'Accept': 'application/octet-stream;charset=UTF-8',
        'Content-Type': 'application/octet-stream;charset=UTF-8',
        'authorization': createBasicAuthHeader(username, password),
    };
}
export function createBasicAuthHeader(username: string, password: string) {
    return 'Basic ' + Base64.btoa(username + ':' + password)
}
export function convertToJson(xmlString) {
    var xmlParser = new XMLParser();
    var xmlDoc = xmlParser.parseFromString(xmlString);
    let json = xmlParser.toJson(xmlDoc);
    return json;
}

export function filterFault(responseJson) {
    if (responseJson.Body.Fault) {
        throw new Error(responseJson.Body.Fault.faultstring);
    } else {
        return responseJson;
    }
}

export function postREST(url, username, password, data){

    var options = {
        method: 'POST',
        headers: buildJsonHeaders(username, password),
        body: JSON.stringify(data),
    };

    return fetch(url, options)
}

export function putREST(url, username, password, data){

    var options = {
        method: 'PUT',
        headers: buildJsonHeaders(username, password),
        body: JSON.stringify(data),
    };

    return fetch(url, options)
}

export function getREST(url, username, password){

    var options = {
        method: 'GET',
        headers: buildJsonHeaders(username, password),
    };

    return fetch(url, options)
}

// SOAP
export const postSOAP = async (url: string, sid: string, data: string, onPress:()=>null) => {
    
    return new Promise((resolve, reject) => {

        var xhr = new XMLHttpRequest();
        xhr.withCredentials = true;

        xhr.onload = function () {

            try {
                const responseJson = convertToJson(xhr.response);
                responseJson = filterFault(responseJson);

                resolve(responseJson)
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
                if (typeof onProgress === 'undefined') return;

                onProgress && onProgress(loaded / total);
            }
        })

        xhr.open("POST", url);
        // xhr.responseType = "moz-blob";
        xhr.setRequestHeader('Content-Length', data.length);
        xhr.setRequestHeader('Accept', 'application/soap+xml; charset=utf-8');
        xhr.setRequestHeader('Content-Type', 'application/soap+xml; charset=utf-8');
        xhr.setRequestHeader('Accept-Encoding', 'application/soap+xml; charset=utf-8');
        xhr.setRequestHeader('SOAPAction', '""');

        // Send the request
        xhr.send(data);
    });
}

