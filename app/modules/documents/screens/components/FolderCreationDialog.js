'use strict';

import React, { Component } from 'react';
import {
    View,
    Text,
    TextInput,
    Dimensions,
} from 'react-native';
import Dialog from './Dialog';

const { width, height } = Dimensions.get('window');

export default class FolderCreationDialog extends Component {
    render() {
        return (
            <View>
                <Dialog
                    onCancel={this.props.onCancel}
                    onOK={this.props.onOK}
                    modalVisible={this.props.modalVisible}
                    height={height * 0.3}
                >
                    <View style={{ flex: 1, }}>
                        <View style={{ flex: 1 }}>
                            <Text style={{
                                fontSize: 20,
                                fontWeight: 'bold',
                                alignSelf: 'center',
                                marginBottom: 20,
                                // height: 50,
                            }}>Folder Name:</Text>
                        </View>
                        <View style={{ flex: 1, marginTop: 10, marginBottom: 10 }}>
                            <TextInput
                                ref="txtFolderName"
                                style={{
                                    borderWidth: 1,
                                    // height: 50,
                                    // fontSize: 16,
                                    padding: 3
                                }}
                                placeholder={'Folder Name'}
                                blurOnSubmit={true}
                                underlineColorAndroid={'transparent'}
                                onChangeText={this.props.onChangeFolderName}
                                value={this.props.folderName}
                            />
                        </View>
                    </View>
                </Dialog>
            </View>
        );
    }
}