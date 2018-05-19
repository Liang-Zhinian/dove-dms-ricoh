/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

import React, { Component } from 'react';
import {
    Platform,
    StyleSheet,
    Text,
    View,
    Dimensions,
    TouchableOpacity,
} from 'react-native';
import RNFS from 'react-native-fs';
import Entypo from 'react-native-vector-icons/Entypo';
import FileViewerIOS from "../../../components/RNQuickLook";
import FileViewerAndroid from '../../../components/RCTFileViewerAndroid';
import { CommonStyles, colors } from '../styles';
import ActionSheet from './components/ActionSheet';

const { height, width } = Dimensions.get('window');

export default class FileViewer extends Component<{}> {

    static navigationOptions = ({ navigation }) => {
        const { params = {} } = navigation.state;

        let headerTitle = `${params.file ? params.file.fileName : 'Document'}`;
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
        }
    }

    download() {
        console.log('download');
        const that = this;
        const { navigation } = that.props;
        const file = navigation.state.params.file;

        // create a path you want to write to
        const destPath = `${RNFS.DocumentDirectoryPath}/${file.fileName}`;

        // write the file
        RNFS.copyFile(file.uri, destPath)
            .then((success) => {
                console.log(`FILE WRITTEN! ${destPath}`);
                that.toggleActionSheet();
            })
            .catch((err) => {
                console.log(err.message);
            });
    }

    delete() {
        const { file } = this.props.navigation.state.params;
        RNFS.unlink(file.url)
            .then(() => console.log('FILE DELETED'))
            .catch(err => console.log(err.message));
    }

    toggleActionSheet() {
        console.log('toggleActionSheet');
        const modalVisible = this.state.modalVisible;
        this.setState({
            modalVisible: !modalVisible,
        })
    }

    componentDidMount() {
        console.log('componentDidMount');
        const { navigation } = this.props;
        // We can only set the function after the component has been initialized
        navigation.setParams({ toggleActionSheet: this.toggleActionSheet.bind(this) });

    }

    renderActionSheet() {
        const { readOnly = true } = this.props.navigation.state.params;
        
        //if (typeof readOnly === undefined) readOnly = true;

        return (
            <ActionSheet modalVisible={this.state.modalVisible} onCancel={this.toggleActionSheet.bind(this)}>
                <View style={styles.actionSheet}>
                    <TouchableOpacity style={styles.button} onPress={() => { }}>
                        <Text style={styles.buttonText}>
                            Open in...
                            </Text>
                    </TouchableOpacity>
                    <TouchableOpacity style={styles.button} onPress={() => { }}>
                        <Text style={styles.buttonText}>
                            Email as Attachment
                            </Text>
                    </TouchableOpacity>
                    <TouchableOpacity style={[styles.button, { display: readOnly ? 'flex' : 'none' }]} onPress={this.download.bind(this)}>
                        <Text style={styles.buttonText}>
                            Download
                            </Text>
                    </TouchableOpacity>
                    <TouchableOpacity style={[styles.button, { display: readOnly ? 'none' : 'flex' }]} onPress={this.delete.bind(this)}>
                        <Text style={styles.buttonText}>
                            Delete
                            </Text>
                    </TouchableOpacity>
                    <TouchableOpacity style={[styles.button, styles.lastButton]} onPress={() => { }}>
                        <Text style={styles.buttonText}>
                            Print
                            </Text>
                    </TouchableOpacity>
                </View>
            </ActionSheet>
        );
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

    render() {

        // let fileWidth = file.width, fileHeight = file.height;
        // let viewWidth = 300, viewWidth = width / height * viewHeight;

        return (
            <View style={styles.container}>
                {this.renderPreview()}
                {this.renderActionSheet()}
            </View>
        );
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
        height: 230,
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
        height: 46,
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
