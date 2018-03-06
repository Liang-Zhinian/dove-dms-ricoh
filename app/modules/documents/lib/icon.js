import React, { Component } from 'react';
import FontAwesome from 'react-native-vector-icons/FontAwesome';
import { colors } from '../styles';

function _getIcon(name) {
    return <FontAwesome
        style={{ color: colors.primary }}
        name={name}
        size={36}
    />;
}

export default (type) => {
    let icon = _getIcon('file');
    if ('number' === typeof type) type=''+type;
    switch (type.toLowerCase()) {
        case '0':
        case '1':
            icon = _getIcon('folder-o');
            break;

        case 'pdf':
            icon = _getIcon('file-pdf-o');
            break;

        case 'png':
        case 'jpg':
        case 'jpeg':
        case 'gif':
            icon = _getIcon('file-image-o');
            break;

        case 'txt':
            icon = _getIcon('file-text-o');
            break;

        case 'mp3':
            icon = _getIcon('file-audio-o');
            break;

        case 'mp4':
        case 'avi':
        case 'wmv':
            icon = _getIcon('file-video-o');
            break;

        case 'doc':
        case 'docx':
            icon = _getIcon('file-word-o');
            break;

        case 'ppt':
        case 'pptx':
            icon = _getIcon('file-powerpoint-o');
            break;

        case 'xls':
        case 'xlsx':
            icon = _getIcon('file-excel-o');
            break;

        case 'zip':
        case 'rar':
        case '7zip':
            icon = _getIcon('file-zip-o');
            break;

        default:
            break;

    }
    return icon;
}