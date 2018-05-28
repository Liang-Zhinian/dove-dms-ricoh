'use strict';

import React, { Component } from 'react';
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
            return responses.map(resp => {
                let { id, content } = resp;
                let item = docs.filter(o => o.id === id)[0];
                item.content = content;
                return item;
            });
        }, reason => {
        })
        .then(items => {
            return items.map(item => {
                let { title, content } = item;
                let fileName = title;
                let path = `${RNFS.DocumentDirectoryPath}/${fileName}`;
                // write the file
                RNFS.writeFile(path, content/*Base64.atob(content)*/, 'base64')
                    .then((success) => {
                    })
                    .catch((err) => {
                    });

                return item;
            });

        })
        .catch(reason => {
        });
}

const downloadToCacheDirectory = (sid, doc, onProgress, onCanceled) => {
    const that = this;
    var downloadManager = new DownloadManager();
    return createDownloadTicketWithProgressSOAP(sid, doc.id)
        .then(ticket => {
            const SHA1 = require('crypto-js/sha1');
            const path = RNFetchBlob.fs.dirs.CacheDir + '_immutable_images/' + SHA1(ticket) + '.' + doc.type;

            return downloadManager.start(ticket, {
                // add this option that makes response data to be stored as a file,
                // this is much more performant.
                fileCache: true,
                //path,
                appendExt: doc.type,
            });
        })
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
            if (values.length === docs.length)
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
        });

        return that;
    }

    cancel() {
        const that = this;
        debugger;
        if (that.task)
            that.task.cancel((err, taskId) => {

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

