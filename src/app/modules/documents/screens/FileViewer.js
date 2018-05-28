/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

import React, { Component } from 'react';
import {
    ActionSheetIOS,
    Platform,
    StyleSheet,
    Text,
    View,
    Dimensions,
    TouchableOpacity,
} from 'react-native';
import ImagePicker from 'react-native-image-picker';
import RNFS from 'react-native-fs';
import Entypo from 'react-native-vector-icons/Entypo';
import RNPrint from 'react-native-print';

import FileViewerIOS from "../../../components/RNQuickLook";
import FileViewerAndroid from '../../../components/RCTFileViewerAndroid';
import { CommonStyles, colors } from '../styles';
import ActionSheet from './components/ActionSheet';
import { translate } from '../../../i18n/i18n';

const { height, width } = Dimensions.get('window');

var BUTTONS = [
    'Option 0',
    'Option 1',
    'Option 2',
    'Delete',
    'Cancel',
];
var DESTRUCTIVE_INDEX = 3;
var CANCEL_INDEX = 4;

export default class FileViewer extends Component<{}> {

    static navigationOptions = ({ navigation }) => {
        const { params = {} } = navigation.state;

        let headerTitle = `${params.file ? params.file.fileName : translate('Document')}`;
        let headerRight = (
            <View style={[
                CommonStyles.flexRow,
            ]}>
                <TouchableOpacity
                    style={{ marginRight: 14, justifyContent: 'center' }}
                    accessibilityLabel='share'
                    onPress={params.toggleActionSheet ? params.toggleActionSheet : () => null}
                >
                    <Entypo
                        name='share-alternative'
                        size={24}
                        style={{ color: colors.textOnPrimary }}
                    />
                </TouchableOpacity>
            </View>
        );

        return { headerTitle, headerRight };
    };

    constructor(props) {
        super(props);
        this.state = {
            modalVisible: false,
            clicked: 'none',
            text: '',
            selectedPrinter: null,
        }
    }

    render() {
        return (
            <View style={styles.container}>
                {this.renderPreview()}
            </View>
        );
    }

    download() {
        const that = this;
        const { navigation } = that.props;
        const file = navigation.state.params.file;

        // create a path you want to write to
        const destPath = `${RNFS.DocumentDirectoryPath}/${file.fileName}`;

        // write the file
        RNFS.copyFile(file.uri, destPath)
            .then((success) => {
                that.toggleActionSheet();
            })
            .catch((err) => {
            });
    }

    delete() {
        const { file } = this.props.navigation.state.params;
        
        RNFS.unlink(file.uri)
            .then(() => console.log('FILE DELETED'))
            .catch(err => console.log(err.message));
    }

    toggleActionSheet() {
        const modalVisible = this.state.modalVisible;
        this.setState({
            modalVisible: !modalVisible,
        })
    }

    showImagePicker = () => {
        let _that = this;
        var customButtons = [];

        const { readOnly = true } = _that.props.navigation.state.params;
        if (readOnly) {
            customButtons.push({
                name: 'download',
                title: translate('Download')
            })
        } else {

            customButtons.push({
                name: 'delete',
                title: translate('Delete')
            })
        }

        customButtons.push({
            name: 'print',
            title: translate('Print')
        })

        // More info on all the options is below in the README...just some common use cases shown here
        var options = {
            title: null,
            cancelButtonTitle: translate('Cancel'),
            takePhotoButtonTitle: null,
            chooseFromLibraryButtonTitle: null,
            customButtons: customButtons,
            storageOptions: {
                skipBackup: true,
                path: 'images'
            },
            mediaType: 'mixed',
        };

        ImagePicker.showImagePicker(options, (response) => {

            if (response.didCancel) {
            }
            else if (response.error) {
            }
            else if (response.customButton) {
                const { customButton } = response;
                switch (customButton) {
                    case 'download':
                        _that.download();
                        break;

                    case 'delete':
                        _that.delete();
                        break;

                    case 'print':
                        _that.props.navigation.navigate('Print', { file: this.props.navigation.state.params.file });
                        break;

                    default:
                        break;
                }

            }
            else {
                let source = response;

                // You can also display the image using data:
                // let source = { uri: 'data:image/jpeg;base64,' + response.data };
            }
        });

    }

    componentDidMount() {
        const { navigation } = this.props;
        // We can only set the function after the component has been initialized
        navigation.setParams({ toggleActionSheet: this.showImagePicker.bind(this) });

    }

    renderPreview() {
        const { file } = this.props.navigation.state.params;
        if (!file) return null;

        if (Platform.OS === 'ios')
            return (
                <FileViewerIOS
                    style={{
                        flex: 1,
                        width,
                        height
                    }}
                    url={file.uri}
                />
            )

        return (
            <FileViewerAndroid
                style={{
                    flex: 1,
                    width,
                    height
                }}
                url={file.uri}
            />
        );
    }

    showShareActionSheet() {
        const { file } = this.props.navigation.state.params;
        if (!file) return null;

        ActionSheetIOS.showShareActionSheetWithOptions(
            {
                url: file.uri,
                excludedActivityTypes: [
                    'com.apple.UIKit.activity.PostToTwitter'
                ]
            },
            (error) => alert(error),
            (success, method) => {
                var text;
                if (success) {
                    text = `Shared via ${method}`;
                } else {
                    text = 'You didn\'t share';
                }
                this.setState({ text });
            })
    }

    showActionSheet() {
        ActionSheetIOS.showActionSheetWithOptions({
            option: BUTTONS,
            cancelButtonIndex: CANCEL_INDEX,
            destructiveButtonIndex: DESTRUCTIVE_INDEX,
        },
            (buttonIndex) => {
                this.setState({ clicked: BUTTONS[buttonIndex] })
            })
    }


}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
        backgroundColor: '#F5FCFF',
    },
    title: {
        fontSize: 20,
        textAlign: 'center',
        margin: 10,
    },
    actionSheet: {
        height: 180,
        backgroundColor: 'white',
        borderColor: 'white',
        borderWidth: 1,
        borderRadius: 6,
        marginBottom: 10,
        alignSelf: 'stretch',
        justifyContent: 'center'
    },
    buttonText: {
        color: '#0069d5',
        alignSelf: 'center',
        fontSize: 18
    },
    button: {
        height: 55,
        backgroundColor: 'white',
        borderColor: 'gray',
        borderWidth: 1,
        borderLeftWidth: 0,
        borderRightWidth: 0,
        borderBottomWidth: 0,
        //borderRadius: 6,
        marginBottom: 0,
        alignSelf: 'stretch',
        justifyContent: 'center'
    },
    lastButton: {
        borderBottomWidth: 1,
    }
});
