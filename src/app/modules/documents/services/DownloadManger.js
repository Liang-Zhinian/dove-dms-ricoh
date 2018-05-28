'use strict';

class DownloadManager {
    task: any;

    constructor() {
        this.task = null;
    }

    get task() { return this.task; }
    set task(t) { this.task = t; }

    start(url, options, onProgress) {
        this.task = download(url, options, (received, total) => {
            onProgress && onProgress(received, doc.fileSize);
        });
    }

    cancel(onCanceled) {
        this.task && this.task.cancel((err, taskId) => {
            // task successfully canceled
            onCanceled && onCanceled();
        });
        this.task = null;
    }

    downloadToCacheDirectory(sid, doc, onProgress) {
        const that = this;
        if (that.task) that.cancel();

        return createDownloadTicketWithProgressSOAP(sid, doc.id)
            .then((response) => {
                let json = toJson(response.response);
                let ticket = json.Body.createDownloadTicketResponse.ticket;

                const SHA1 = require('crypto-js/sha1');
                const path = RNFetchBlob.fs.dirs.CacheDir + '_immutable_images/' + SHA1(ticket) + '.' + doc.type;

                return that.start(ticket, {
                    // add this option that makes response data to be stored as a file,
                    // this is much more performant.
                    fileCache: true,
                    path,
                }, (received, total) => {
                    onProgress && onProgress(received, doc.fileSize);
                });
            })
            .then(res => {
                return res.path();
            })
    }
}
