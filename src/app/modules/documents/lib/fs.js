import React from 'react';
import RNFS from 'react-native-fs';
import PropTypes from 'prop-types';

export const DocumentDirectoryPath = RNFS.DocumentDirectoryPath;
export const saveToDocumentDirectory = (contents: PropTypes.string.isRequired, encoding?: RNFS.EncodingOrOptions) => {
    RNFS.writeFile(DocumentDirectoryPath, contents, encoding);
}