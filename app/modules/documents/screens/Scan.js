/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

import React, { Component } from 'react';
import {
    Image,
    Alert,
    Platform,
    StyleSheet,
    Text,
    TextInput,
    View,
    TouchableOpacity,
    DeviceEventEmitter,
    ScrollView,
    Button,
} from 'react-native';
import { connect } from 'react-redux'
import { NAME } from '../constants'
import RicohScannerAndroid from '../../../components/RCTRicohScannerAndroid';
import * as actions from '../actions';
import Base64 from '../lib/Base64';
import {
    ComponentStyles,
    CommonStyles,
    colors,
    StyleConfig,
} from '../styles';

function alert(title, msg) {
    Alert.alert(title, msg, [{ text: 'OK', onPress: () => console.log('OK Pressed') },], { cancelable: false });
}

class Scan extends Component<{}> {
    static navigationOptions = {
        // headerStyle: { backgroundColor: StyleConfig.color_primary },
        // headerTintColor: StyleConfig.textOnPrimary,
        headerTitle: 'Scan',
    };

    constructor(props) {
        super(props);
        this.state = {
            connectState: '',
            scanJobState: '',
            scanServiceAttributeState: '',
            scanServiceAttributeListenerError: '',
            scanJobListenerError: '',
            scannedImage: '',
            folderId: null,
            fileName: '',
            isEditMode: false,
            uploadButtonText: 'Upload'
        };
    }

    componentWillMount() {
        var that = this;

        DeviceEventEmitter.addListener('ConnectStateUpdated', function (e) {
            that.setState({ connectState: e.stateLabel });
        });
        DeviceEventEmitter.addListener('ScanServiceAttributeUpdated', function (e) {
            that.setState({ scanServiceAttributeState: e.stateLabel });
        });
        DeviceEventEmitter.addListener('ScanJobStateUpdated', function (e) {
            that.setState({ scanJobState: e.stateLabel });
        });
        DeviceEventEmitter.addListener('ScanServiceAttributeListenerErrorUpdated', function (e) {
            that.setState({ scanServiceAttributeListenerError: e.stateLabel });
        });
        DeviceEventEmitter.addListener('ScanJobListenerErrorUpdated', function (e) {
            that.setState({ scanJobListenerError: e.stateLabel });
        });
        DeviceEventEmitter.addListener('ScannedImageUpdated', function (e) {
            that.setState({ scannedImage: e.stateLabel });
            console.log(e.stateLabel);
            if (e.stateLabel != null && e.stateLabel !== '') {
                that.setState({ isEditMode: true });
            } else {
                that.setState({ isEditMode: false });
            }
        });



        that.init();

        const { navigate, state } = this.props.navigation;
        that.setState({ 'folderId': state.params.folderId })
    }

    componentWillReceiveProps(nextProps) {
        // if (this.props.progress < 1 && nextProps.progress == 1) {
        //     this.setState({ uploadButtonText: 'Upload' });
        //     alert('Upload document', 'Upload done.');
        // }
    }

    render() {
        let { progress } = this.props;
        return (
            <View>
                <ScrollView style={{ padding: 20 }}>
                    <View>
                        <TouchableOpacity onPress={this.restore.bind(this)} style={styles.button}>
                            <Text style={styles.buttonFont}>Restore</Text>
                        </TouchableOpacity>
                        {this.renderSpacer()}

                        <TouchableOpacity onPress={this.scan.bind(this)} style={styles.button}>
                            <Text style={styles.buttonFont}>Scan</Text>
                        </TouchableOpacity>
                        {this.renderSpacer()}

                        <View style={{ flex: 1, flexDirection: 'row' }}>
                            <TextInput
                                ref="txtFileName"
                                blurOnSubmit={true}
                                onChangeText={(fileName) => this.setState({ fileName })}
                                value={this.state.fileName}
                                autoCapitalize='none'
                                editable={this.state.isEditMode}
                                underlineColorAndroid={'transparent'}
                                style={styles.textInput}
                            />
                            <TouchableOpacity onPress={this.doUpload.bind(this)} style={[styles.button, { width: 200 }]}>
                                <Text style={styles.buttonFont}>Upload</Text>
                            </TouchableOpacity>
                        </View>
                        {this.renderSpacer()}

                        <Text style={styles.title}>Connection State: {this.state.connectState}</Text>
                        <Text style={styles.title}>Scan Job State: {this.state.scanJobState}</Text>
                        <Text style={styles.title}>Scan Service Attribute State: {this.state.scanServiceAttributeState}</Text>
                        <Text style={styles.title}>Scanned Image: {this.state.scannedImage}</Text>

                    </View>
                </ScrollView>
            </View>
        );
    }

    scan() {
        if (!this.isReady()) {
            alert('Scan', 'Please wait for the scan service to be ready.');
            return;
        }

        RicohScannerAndroid.start()
            .then((msg) => {
                console.log('success!!')
            }, (error) => {
                console.log('error!!')
            });
    }

    init() {

        RicohScannerAndroid.init()
            .then((msg) => {
                console.log('success!!')
            }, (error) => {
                console.log('error!!')
            });
    }

    restore() {

        RicohScannerAndroid.restore()
            .then((msg) => {
                console.log('success!!')
            }, (error) => {
                console.log('error!!')
            });
    }

    doUpload() {
        if (!this.state.isEditMode) {
            alert('Upload document', 'Please wait for the scan operation to be done.');
            return;
        }

        if (!this.state.fileName) {
            alert('Upload document', 'Please input a file name.');
            return;
        }

        // this.setState({ uploadButtonText: 'Uploading' });

        const { sid, username, password, navigation } = this.props;
        let name = this.state.fileName;
        let type = 'pdf';
        let folderId = this.state.folderId;
        let document = {
            "id": 0,
            "fileSize": this.state.scannedImage.length,
            "title": name,
            "type": type,
            "fileName": name + (type === '' ? '' : `.${type}`),
            "folderId": folderId,
        };

        const { upload } = this.props;
        try {
            const data = this.state.scannedImage;
            upload(sid, document, data);

            this.setState({
                scanJobState: '',
                scanServiceAttributeListenerError: '',
                scanJobListenerError: '',
                scannedImage: '',
                fileName: '',
                isEditMode: false,
            });
        } catch (error) {
            alert('Upload document', error.message);
            this.setState({
                scanJobState: '',
                scanServiceAttributeListenerError: '',
                scanJobListenerError: '',
                scannedImage: '',
                fileName: '',
                isEditMode: false,
            });
        }
    }

    renderSpacer() {
        return (
            <View style={styles.spacer}></View>
        )
    }

    isReady() {
        return this.state.connectState == 'CONNECTED' && this.state.scanServiceAttributeState == 'Ready';
    }
}

function select(store) {
    return {
        sid: store[NAME].account.token.sid,
        username: store[NAME].account.username,
        password: store[NAME].account.password,
        uploaded: store[NAME].document.uploaded,
        progress: store[NAME].document.progress,
        isLoading: store[NAME].document.isLoading,
    };
}

function dispatch(dispatch) {
    return {
        // 发送行为
        startUpload: () => dispatch(actions.startUpload()),
        upload: (sid, document, content) => { dispatch(actions.upload(sid, document, content)) }
    }
};

export default connect(select, dispatch)(Scan);

const styles = StyleSheet.create({
    container: {
        flex: 1,
        // justifyContent: 'center',
        // alignItems: 'flex-start',
        backgroundColor: '#F5FCFF',
    },
    title: {
        flex: 1,
        fontSize: 20,
        textAlign: 'left',
        margin: 10,
    },
    buttonFont: {
        color: "white",
        fontSize: 17
    },
    spacer: {
        height: 10,
    },
    button: {
        height: 50,
        // marginLeft: 30,
        // marginRight: 30,
        backgroundColor: '#18B4FF',
        justifyContent: 'center',
        alignItems: 'center',
        borderRadius: 10,
    },
    textInput: {
        flex: 1,
        borderWidth: 1,
        marginLeft: 5,
        paddingLeft: 5,
        borderColor: '#ccc',
        borderRadius: 4
    },
});
