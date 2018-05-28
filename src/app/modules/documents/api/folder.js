import {
    getFolderSoapAPI,
    getFolderRestAPI,
    buildJsonHeaders,
    convertToJson,
    filterFault,
    getREST,
    postREST,
    postSOAP
} from './util';
import handle from '../../../ExceptionHandler';

// rest api
export const listChildrenFolders = async (username: string, password: string, folderId: int) => {
    const FolderRestAPI = await getFolderRestAPI();
    return getREST(`${FolderRestAPI}/listChildren?folderId=${folderId}`, username, password)
        .then(response => response.json());

}

export const updateFolder = async (username: string, password: string, folder: {}) => {

    const FolderRestAPI = await getFolderRestAPI();

    return postREST(`${FolderRestAPI}/update`, username, password, folder);
}

// soap api
export const getRootFolder = async (sid: string) => {

    const FolderSoapAPI = await getFolderSoapAPI();
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

    return postSOAP(FolderSoapAPI, sid, xml)
        .then(json => json.Body.getRootFolderResponse.folder)
        .catch(error => { throw error });
}

export const createFolderSOAP = async (sid: string, parentId: int, name: string, onProgress: (percent) => {}) => {

    const FolderSoapAPI = await getFolderSoapAPI();
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

    return postSOAP(FolderSoapAPI, sid, xml, onProgress)
        .then(json => json.Body)
        .catch(error => {
            handle(error);
            throw error
        });

}
