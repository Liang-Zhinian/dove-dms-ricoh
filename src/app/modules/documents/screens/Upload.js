//@flow

import React, { Component } from 'react';
import {
    Platform,
    View,
    ScrollView,
    Text,
    TextInput,
    StyleSheet,
    Alert,
    ActivityIndicator,
    ProgressViewIOS,
    TouchableOpacity,
} from 'react-native';
import RNFetchBlob from 'react-native-fetch-blob';
import moment from 'moment';
import { connect } from 'react-redux'
import { NAME } from '../constants'

import RNQuickLook from "../../../components/RNQuickLook";
import Base64 from '../lib/Base64';
import * as actions from '../actions';
import { translate as translateLocation, coordToAddress } from '../api/geolocation';
import { translate } from '../../../i18n/i18n';

class Upload extends Component {
    static navigationOptions = ({ navigation }) => {
        const { params = {} } = navigation.state;

        let headerTitle = translate('UploadMedia')
        let headerRight = (
            <View style={[
                CommonStyles.flexRow,
            ]}>
                <TouchableOpacity
                    style={{ marginRight: 14, justifyContent: 'center' }}
                    accessibilityLabel='upload'
                    onPress={params.upload ? params.upload : () => null}
                    disabled={params.uploadButtonDisabled}
                >
                    <Text style={{ color: params.uploadButtonDisabled ? 'gray' : '#ffffff', fontSize: 20 }}>{translate('Upload')}</Text>
                </TouchableOpacity>
            </View>
        );

        return { headerTitle, headerRight };
    };

    constructor(props) {
        super(props);
        this.state = {
            isLoading: true,
            error: false,
            errorInfo: "",
            animating: false,
            name: '',
            location: '',
            address: '',
            initialPosition: 'unknown',
            lastPosition: 'unknown',
        };
        this.props.navigation.setParams({ uploadButtonDisabled: true });

        this.watchID = null;
    }

    renderSpacer() {
        return (
            <View style={styles.spacer}></View>
        )
    }

    upload() {
        this.props.navigation.setParams({ uploadButtonDisabled: true });
        const { sid, username, password, navigation } = this.props;
        const params = navigation.state.params;
        const { uri, fileName, fileSize, data, path } = params.source;

        let name = this.state.name;
        let type = (path || uri).split('.').pop() || 'jpg';
        let folderId = params.folderId;
        let document = {
            "id": 0,
            "fileSize": fileSize,
            // "status": 0,
            // "exportStatus": 0,
            "title": this.state.name,
            // "version": "1",
            // "fileVersion": "1",
            // "date": moment().format('YYYY-MM-DD HH:mm:ss'),
            // "publisher": "string",
            // "publisherId": 0,
            // "creator": "string",
            // "creatorId": 0,
            "type": type,
            // "lockUserId": 0,
            // "creation":  moment().format('YYYY-MM-DD HH:mm:ss'),
            "fileName": name + (type === '' ? '' : `.${type}`),
            // "indexed": 0,
            // "signed": 0,
            // "stamped": 0,
            // "tags": [
            //     "string"
            // ],
            "folderId": folderId,
            // "templateId": 0,
            // "customId": "string",
            // "immutable": 0,
            // "digest": "string",
            // "exportName": "string",
            // "exportId": 0,
            // "docRef": 0,
            // "docRefType": "string",
            // "deleteUserId": 0,
            // "attributes": [
            //     {
            //         "name": "default",
            //         // "stringValue": "string",
            //         // "intValue": 0,
            //         // "doubleValue": 0,
            //         // "dateValue": "string",
            //         "type": 0,
            //         "mandatory": 0,
            //         // "position": 0,
            //         // "label": "string",
            //         "editor": 0,
            //         // "setId": 0,
            //         // "value": {}
            //     }
            // ],
            // "language": "en",
            // "summary": "string",
            // "score": 0,
            // "icon": "string",
            // "path": "string",
            // "comment": "string",
            // "lastModified": "string",
            // "rating": 0,
            // "workflowStatus": "string",
            // "published": 0,
            // "startPublishing": "string",
            // "stopPublishing": "string",
            // "pages": 0,
            // "nature": 0,
            // "formId": 0,
            // "passwordProtected": 0
        };

        const { upload } = this.props;
        upload(sid, document, data);
    }

    updateUploadButton() {
        this.props.navigation.setParams({ uploadButtonDisabled: '' === this.state.name });
    }

    componentDidMount() {
        const that = this;

        // We can only set the function after the component has been initialized
        that.props.navigation.setParams({ upload: that.upload.bind(that) });
    }

    getCurrentPosition() {
        // get geolocation
        navigator.geolocation.getCurrentPosition(pos => {
            var crd = pos.coords;
            that.setState({ location: `${crd.latitude},${crd.longitude}` });
            var initialPosition = JSON.stringify(pos);
            that.setState({ initialPosition });

            translateLocation(crd)
                .then(coord => {
                    coordToAddress(coord)
                        .then(address => {
                            that.setState({ address });
                        })
                        .catch(error => console.warn(error))
                })
                .catch(error => console.warn(error))
        }, err => {
            console.warn(`ERROR(${err.code}): ${err.message}`);
            Alert.alert('Location request', `ERROR(${err.code}): ${err.message}`, [{ text: 'OK', onPress: () => console.log('OK Pressed') },], { cancelable: false });
        }, {
                enableHighAccuracy: true,
                timeout: 20000,
                maximumAge: 0
            });

        try {
            that.watchID = navigator.geolocation.watchPosition(pos => {
                var lastPosition = JSON.stringify(pos);
                that.setState({ lastPosition });

                var crd = pos.coords;
                translateLocation(crd)
                    .then(coord => {
                        coordToAddress(coord)
                            .then(address => {
                                that.setState({ address });
                            })
                            .catch(error => console.warn(error))
                    })
                    .catch(error => console.warn(error))
            }, error => {
            })
        } catch (error) {
        }
    }

    // Start changing images with timer on first initial load
    componentWillReceiveProps(nextProps) {
        const { navigation } = this.props;
        const prevUploaded = this.props.uploaded;
        const { uploaded } = nextProps;
        //debugger;
        if (uploaded && prevUploaded != uploaded) {
            navigation.goBack();
        }
    }

    componentWillUnmount() {
        navigator.geolocation.clearWatch(this.watchID);
    }

    //加载等待的view
    renderLoadingView() {
        return (

            <View style={[styles.container, {
                flex: 1,
                backgroundColor: 'transparent',
                position: 'absolute',
                top: 0,
                bottom: 0,
                left: 0,
                right: 0
            }]}>
                <ActivityIndicator
                    animating={true}
                    style={[styles.gray, { flex: 1 }]}
                    color='red'
                    size="large"
                />
            </View>
        );
    }

    //加载失败view
    renderErrorView(error) {
        return (
            <View style={styles.container}>
                <Text>
                    Fail: {error}
                </Text>
            </View>
        );
    }

    renderProgressBar() {
        const { progress, isLoading } = this.props;
        return (
            <View style={[styles.container, {
                flex: 1,
                //backgroundColor: 'transparent',
                opacity: 0.9,
                position: 'absolute',
                top: 0,
                bottom: 0,
                left: 0,
                right: 0,
                //flexDirection: 'column',
                display: isLoading ? 'flex' : 'none'
            }]}>
                {Platform.OS === 'android' && <ActivityIndicator
                    animating={true}
                    style={[styles.gray, { flex: 1, marginTop: 20 }]}
                    color='red'
                    size="large"
                />}
                {Platform.OS === 'ios' &&
                    <View style={{
                        flex: 1,
                        flexDirection: 'column',
                        justifyContent: 'center',
                        //alignItems: 'center'
                        height: 40
                    }}>
                        <Text style={{
                            textAlign: 'center',
                            fontSize: 30,
                            color: 'orange'
                        }}>{Math.floor((progress / 100) * 10000)}%</Text>

                        <ProgressViewIOS style={styles.progressView} progress={progress} progressTintColor="orange" />
                    </View>
                }
            </View>);
    }

    render() {
        const { navigation } = this.props;
        const params = navigation.state.params;
        const { width, height, uri, fileName } = params.source;
        let viewHeight = 300, viewWidth = width / height * viewHeight;

        return (
            <ScrollView style={{
                flex: 1,
                top: 0,
                bottom: 0,
                left: 0,
                right: 0,
            }}>
                <View style={[styles.section]}>
                    <View style={[{ flex: 1 }, styles.row]}>
                        <Text style={[styles.title]}>{translate('FileName')}</Text>
                        <TextInput
                            style={{ flex: 1 }}
                            ref="txtName"
                            placeholder={translate('FileName')}
                            blurOnSubmit={true}
                            underlineColorAndroid={'transparent'}
                            onChangeText={(val) => { this.setState({ name: val }, this.updateUploadButton) }}
                        // setState is asynchronous and use the second argument to setState which is a callback
                        />
                    </View>
                    {this.renderSpacer()}
                    <View style={[{ flex: 5 }, styles.row]}>
                        <Text style={[styles.title]}>{translate('Media')}</Text>
                        {Platform.OS === 'ios' && <RNQuickLook
                            style={{ flex: 1, width: viewWidth, height: viewHeight }}
                            url={uri} />}
                        {Platform.OS === 'android' && <Text>{fileName || uri}</Text>}
                    </View>
                    {/*
                    {this.renderSpacer()}
                    <View style={[{ flex: 1 }, styles.row]}>
                        <Text style={[styles.title]}>{translate('Location')}</Text>
                        <TextInput
                            style={{ flex: 1 }}
                            ref="txtLocation"
                            placeholder={translate('Location')}
                            blurOnSubmit={true}
                            underlineColorAndroid={'transparent'}
                            onChangeText={(val) => { this.setState({ location: val }) }}
                            value={this.state.location}
                        // setState is asynchronous and use the second argument to setState which is a callback
                        />
                    </View>
                    {this.renderSpacer()}
                    <View style={[{ flex: 1 }, styles.row]}>
                        <Text style={[styles.title]}>{translate('Address')}</Text>
                        <TextInput
                            style={{ flex: 1 }}
                            ref="txtAddress"
                            placeholder={translate('Address')}
                            blurOnSubmit={true}
                            underlineColorAndroid={'transparent'}
                            onChangeText={(val) => { this.setState({ address: val }) }}
                            value={this.state.address}
                        // setState is asynchronous and use the second argument to setState which is a callback
                        />
                    </View>*/}
                </View>
                {this.renderProgressBar()}
            </ScrollView>
        );
    }
}

// export default UploadContainer;
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

export default connect(select, dispatch)(Upload);

const styles = StyleSheet.create({
    container: {
        flex: 1,
        flexDirection: 'row',
        justifyContent: 'center',
        alignItems: 'center',
        backgroundColor: '#F5FCFF',
    },
    section: {
        flex: 1,
        flexDirection: 'column',
        marginTop: 20,
        marginRight: 0,
        marginBottom: 20,
        marginLeft: 0,
        borderTopWidth: 1,
        borderTopColor: 'gray',
        borderBottomWidth: 1,
        borderBottomColor: 'gray',
    },
    row: {
        flexDirection: 'row',
        padding: 10,
        // marginRight: 10,
        // marginLeft: 10,
        backgroundColor: '#ffffff',
        // borderBottomWidth: 1,
        // borderBottomColor: 'gray',
        justifyContent: 'flex-start',
        alignItems: 'center',
    },
    title: {
        marginRight: 10,
        fontWeight: 'bold',
    },
    spacer: {
        height: 1,
        backgroundColor: 'gray',
        marginLeft: 10,
        marginRight: 10,
    },
    progressView: {
        flex: 1,
        marginLeft: 30,
        marginRight: 30,
        height: 10,
    },
});