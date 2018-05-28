
import {
    createDownloadTicketWithProgressSOAP,
    listChildrenDocuments,
    deleteDocument,
    getContentSOAP,
    createDocumentWithProgressSOAP,
    search,
    updateDocument
} from './document';
import {
    getRootFolder,
    listChildrenFolders,
    createFolderSOAP,
    updateFolder
} from './folder';
import {
    loginSOAP,
    logoutSOAP,
    renewSOAP,
    validSOAP,
    ensureLogin
} from './auth';

import {
    getUserByUsernameSOAP
} from './security';


const _listChildren = async (username: string, password: string, folderId: int, callback: (folderId, children) => {}) => {

    let dataArray = [];
    let folders = await listChildrenFolders(username, password, folderId);
    let documents = await listChildrenDocuments(username, password, folderId);
    dataArray = [...folders, ...documents];
    // bug: call three times
    callback && callback(folderId, dataArray);

}

const listChildren = async (username: string, password: string, sid: string, folderId: int, callback: (folderId, children) => {}) => {
    return new Promise(async (resolve, reject) => {

        // let valid = await validSOAP(sid);
        // if (!valid) reject(Error('token expired'));
        // ensureLogin(username, password, sid)
        //     .then(sid => {
        if (folderId) {
            await _listChildren(username, password, folderId, callback)
            return
        }

        getRootFolder(sid)
            .then(async (folder) => {
                folderId = folder.id
                const children = await _listChildren(username, password, folderId, callback);
                resolve(children);
            })
            .catch((error) => {
                reject(error);
            });
        // })
        // .catch(reason => reject(reason))
    });
};

const deleteDocuments = (username: string, password: string, docIds: number[]) => {
    var promises = [];
    for (var index = 0; index < docIds.length; index++) {
        var element = docIds[index];
        promises.push(deleteDocument(username, password, element))
    }

    return Promise.all(promises)
        .then(values => {
            return values;
        }, reason => {
        })
        .catch(reason => {
        });
}

const searchDocuments = (username: string, password: string, expression: string) => {
    return search(username, password, expression)
        .then(response => response.json())
        .then(responseJson => responseJson.hits)
        .catch(reason => console.error(reason))
}

export {
    listChildren,
    createDownloadTicketWithProgressSOAP,
    deleteDocuments,
    getContentSOAP,
    createDocumentWithProgressSOAP,
    createFolderSOAP,
    searchDocuments,
    updateDocument,

    loginSOAP,
    logoutSOAP,
    renewSOAP,
    validSOAP,
    ensureLogin,

    getUserByUsernameSOAP,

    updateFolder
};