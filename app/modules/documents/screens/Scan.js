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
    ActivityIndicator,
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
            isLoading: true,
            connectState: '',
            scanJobState: '',
            scanJobAttributeState: '',
            scanServiceAttributeState: '',
            scanServiceAttributeListenerError: '',
            scanJobListenerError: '',
            scannedImage: '',
            folderId: null,
            fileName: '',
            isEditMode: false,
            uploadButtonText: 'Upload',

        };
    }

    componentWillMount() {
        var that = this;

        DeviceEventEmitter.addListener('onConnectStateUpdated', function (e) {
            that.setState({ connectState: e.label });
            if (that.isReady()) {
                that.setState({ isLoading: false });
            }
        });
        DeviceEventEmitter.addListener('onScanServiceAttributeUpdated', function (e) {
            that.setState({ scanServiceAttributeState: e.label });

            if (that.isReady()) {
                that.setState({ isLoading: false });
            }
        });
        DeviceEventEmitter.addListener('onScanJobStateUpdated', function (e) {
            that.setState({ scanJobState: e.label });
        });
        DeviceEventEmitter.addListener('onScanJobAttributeUpdated', function (e) {
            that.setState({ scanJobAttributeState: e.label });

            if (that.isReady()) {
                that.setState({ isLoading: false });
            }
        });
        DeviceEventEmitter.addListener('onScanServiceAttributeListenerErrorUpdated', function (e) {
            that.setState({ scanServiceAttributeListenerError: e.label });
        });
        DeviceEventEmitter.addListener('onScanJobListenerErrorUpdated', function (e) {
            that.setState({ scanJobListenerError: e.label });
        });
        DeviceEventEmitter.addListener('onScannedImageUpdated', function (e) {
            that.setState({ scannedImage: e.label });
            console.log(e.label);
            if (e.label != null && e.label !== '') {
                that.setState({ isEditMode: true/*, isLoading: false*/ });
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
                        {/* <TouchableOpacity onPress={this.restore.bind(this)} style={styles.button}>
                            <Text style={styles.buttonFont}>Restore</Text>
                        </TouchableOpacity> */}
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
                                <Text style={styles.buttonFont}>{this.props.isLoading ? 'Uploading ...' : 'Upload'}</Text>
                            </TouchableOpacity>
                            {/* <TouchableOpacity onPress={this.doUploadEncoded.bind(this)} style={[styles.button, { width: 200 }]}>
                                <Text style={styles.buttonFont}>Upload (encoded)</Text>
                            </TouchableOpacity>
                            <TouchableOpacity onPress={this.doUploadDecoded.bind(this)} style={[styles.button, { width: 200 }]}>
                                <Text style={styles.buttonFont}>Upload (decoded)</Text>
                            </TouchableOpacity> */}
                        </View>
                        {this.renderSpacer()}

                        <Text style={styles.title}>Scanner: {this.isReady()?'Ready':'Please wait ...'}</Text>
                        {/* <Text style={styles.title}>Connection State: {this.state.connectState}</Text> */}
                        {/* <Text style={styles.title}>Scan Job State: {this.state.scanJobState}</Text> */}
                        {/* <Text style={styles.title}>Scan Service Attribute State: {this.state.scanServiceAttributeState}</Text> */}
                        <Text style={styles.title}>Scanned Image: {this.state.scannedImage?'Ready':'Empty'}</Text>

                    </View>
                    {this.renderSpinner()}
                </ScrollView>
            </View>
        );
    }

    scan() {
        var that = this;

        if (!that.isReady()) {
            alert('Scan', 'Please wait for the scan service to be ready.');
            return;
        }

        // that.setState({isLoading: true});

        RicohScannerAndroid.start()
            .then((msg) => {
                console.log('Start scan success!!');
                // that.setState({isLoading: true});
            }, (error) => {
                console.log('Start scan error!!')
                // that.setState({isLoading: false});
            });
    }

    init() {

        RicohScannerAndroid.init()
            .then((msg) => {
                console.log('Init scanner success!!')
            }, (error) => {
                console.log('Init scanner error!!')
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
            // "title": name,
            "type": type,
            "fileName": name + (type === '' ? '' : `.${type}`),
            "folderId": folderId,
        };

        const { upload } = this.props;
        try {
            var data = this.state.scannedImage;
            //data = Base64.atob(data); // error
            // data = Base64.btoa(data); 

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

    doUploadEncoded() {
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
            var data = this.state.scannedImage;
            //data = Base64.atob(data); // error
            data = Base64.btoa(data);

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

    doUploadDecoded() {
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
            var data = this.state.scannedImage;
            data = Base64.atob(data); // error
            //data = Base64.btoa(data); 

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

    renderSpinner() {
        const { isLoading } = this.state;

        return (
            <View style={[styles.container, {
                flex: 1,
                //backgroundColor: 'transparent',
                opacity: 0.5,
                position: 'absolute',
                top: 0,
                bottom: 0,
                left: 0,
                right: 0,
                //flexDirection: 'column',
                display: isLoading ? 'flex' : 'none'
            }]}>
                <View style={{
                    flex: 1,
                    flexDirection: 'column',
                    justifyContent: 'center',
                    //alignItems: 'center'
                    height: 40
                }}>
                    <ActivityIndicator
                        animating={true}
                        style={[styles.gray, { height: 80 }]}
                        color='red'
                        size="large"
                    />
                </View>
            </View>);
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
