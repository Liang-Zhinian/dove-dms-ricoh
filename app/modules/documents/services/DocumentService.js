'use strict';

import React, { Component } from 'react';
import {
    Alert,
} from 'react-native';
import RNFS from 'react-native-fs';
import RNFetchBlob from 'react-native-fetch-blob';
import {
    listChildren,
    createDownloadTicketWithProgressSOAP,
    deleteDocuments,
    getContentSOAP,
    createFolderSOAP,
} from '../api';
import XMLParser from '../lib/XMLParser';
import Base64 from '../lib/Base64';
import { default as Toast } from '../../../components/RCTToastModuleAndroid';


const toJson = (xmlString) => {
    var xmlParser = new XMLParser();
    var xmlDoc = xmlParser.parseFromString(xmlString);
    let json = xmlParser.toJson(xmlDoc);
    return json;
}

const download = (url, options, onProgress) => {
    return RNFetchBlob
        .config(options)
        .fetch('GET', url, {
            //... some headers, 
            'Content-Type': 'octet-stream'
        })
        // listen to download progress event
        .progress((received, total) => {
            onProgress && onProgress(received, total);
        });
}

const downloadToDocumentDirectory = (sid, docs) => {
    var promises = [];
    for (var index = 0; index < docs.length; index++) {
        var element = docs[index];
        promises.push(getContentSOAP(sid, element.id))
    }

    return Promise.all(promises)
        .then(responses => {
            console.log(responses);
            return responses.map(resp => {
                let { id, content } = resp;
                let item = docs.filter(o => o.id === id)[0];
                item.content = content;
                return item;
            });
        }, reason => {
            console.log(reason)
        })
        .then(items => {
            console.log('will save:')
            console.log(items);
            return items.map(item => {
                let { title, content } = item;
                let fileName = title;
                console.log('saving ' + fileName);
                let path = `${RNFS.DocumentDirectoryPath}/${fileName}`;
                // write the file
                RNFS.writeFile(path, Base64.atob(content), 'utf8')
                    .then((success) => {
                        console.log(success)
                        console.log('FILE WRITTEN!');
                    })
                    .catch((err) => {
                        console.log(err.message);
                    });

                return item;
            });

        })
        .catch(reason => {
            console.log(reason)
        });
}

/*
const downloadToCacheDirectory = (sid, doc, onProgress) => {
    const { fileName } = doc;

    getContentSOAP(sid, doc.id, onProgress)
        .then(({ id, response }) => {
            return toJson(response).Body.getContentResponse.return;
        })
        .then(content => {
            console.log('saving ' + fileName);
            const SHA1 = require('crypto-js/sha1');
            const path = RNFetchBlob.fs.dirs.CacheDir + '_immutable_images/' + fileName;
            // write the file
            RNFS.writeFile(path, Base64.atob(content), 'utf8')
                .then((success) => {
                    console.log(success)
                    console.log('FILE WRITTEN!');
                })
                .catch((err) => {
                    console.log(err.message);
                });
        })
        .catch(reason => {
            console.log(reason)
        });

}*/

const downloadToCacheDirectory = (sid, doc, onProgress, onCanceled) => {
    const that = this;
    var downloadManager = new DownloadManager();
    // downloadManager.onProgress = (received, total) => {
    //     onProgress && onProgress(received, doc.fileSize);
    // };
    // downloadManager.onCanceled = onCanceled;

    return createDownloadTicketWithProgressSOAP(sid, doc.id)
        .then(ticket => {
            // Alert.alert('Download Ticket', ticket, [{ text: 'OK', onPress: () => console.log('OK Pressed') },], { cancelable: false });

            const SHA1 = require('crypto-js/sha1');
            const path = RNFetchBlob.fs.dirs.CacheDir + '_immutable_images/' + SHA1(ticket) + '.' + doc.type;
            // Alert.alert('Temp file path', path, [{ text: 'OK', onPress: () => console.log('OK Pressed') },], { cancelable: false });

            return downloadManager.start(ticket, {
                // add this option that makes response data to be stored as a file,
                // this is much more performant.
                fileCache: true,
                path,
            });
        })
        .catch(error=>{
            Toast.show(error, Toast.SHORT);
            throw new Error(error);
        })
    //.then(man => { return man.task })
    //.then(task => { return task.path() });
}

const upload = () => { }

const createFolder = (sid, parentId, name) => {
    return createFolderSOAP(sid, parentId, name);
}

const removeDocuments = (username, password, docs) => {
    let docIds = docs.map(function (o) {
        return o.id;
    })
    return deleteDocuments(username, password, docIds)
        .then(values => {
            console.log(values);
            if (values.length === docs.length)
                console.log('All selected items were deleted.');
            return values.length === docs.length;
        })
}

const list = (username, password, sid, folderId) => { }

export class DownloadManager {

    task: any;
    onProgress: () => any;
    onCanceled: () => any;

    constructor(singleTask = true) {
        this.task = null;
        this.onProgress = (received, total) => {
            console.log(`progress: ${received} / ${total}`);
        };
        this.onCanceled = () => { };
    }

    start(url, options) {
        const that = this;
        that.task = RNFetchBlob
            .config(options)
            .fetch('GET', url, {
                //... some headers, 
                'Content-Type': 'octet-stream'
            });

        // listen to download progress event
        that.task.progress((received, total) => {
            that.onProgress(received, total);
            // Toast.show('progress: ' + received + '/' + total, Toast.SHORT);
        });

        return that;
    }

    cancel() {
        const that = this;
        debugger;
        if (that.task)
            that.task.cancel((err, taskId) => {
                // task successfully canceled
                console.log('user canceled the previous download task');

                that.onCanceled();
            });
        else
            that.onCanceled();

        return that;
    }

}

export default {
    download,
    downloadToCacheDirectory,
    removeDocuments,
    downloadToDocumentDirectory,
    createFolder
};

