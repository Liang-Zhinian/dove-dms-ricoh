
import Base64 from '../lib/Base64'
import XMLParser from '../lib/XMLParser'

const PROTOCOL = 'http:'
const HOST = 'dms.isd4u.com'
const PORT = '8080'

// API URL
const API = `${PROTOCOL}//${HOST}${PORT ? ':' + PORT : ''}/services`

export const AuthSoapAPI = API + '/Auth?wsdl'
export const FolderSoapAPI = API + '/Folder?wsdl'
export const FolderRestAPI = API + '/rest/folder'
export const DocumentSoapAPI = API + '/Document?wsdl'
export const DocumentRestAPI = API + '/rest/document'
export const SearchRestAPI = API + '/rest/search'


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

export function filterFault(responseJson){
    if (responseJson.Fault) {
        throw new Error(responseJson.Fault.faultstring);
    } else {
        return responseJson;
    }
}

