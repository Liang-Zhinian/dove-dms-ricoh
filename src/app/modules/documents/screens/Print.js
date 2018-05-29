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
import { connect } from 'react-redux';
import ModalSelector from 'react-native-modal-selector';
import RNFS from 'react-native-fs';

import { NAME } from '../constants';
import RicohPrinterAndroid from '../../../components/RCTRicohPrinterAndroid';
import { default as Toast } from '../../../components/RCTToastModuleAndroid';
import * as actions from '../actions';
import Base64 from '../lib/Base64';
import {
    ComponentStyles,
    CommonStyles,
    colors,
    StyleConfig,
} from '../styles';
import { convert } from '../api/converter';
import { Numeric } from '../../../components/TextInputs';
import { translate } from '../../../i18n/i18n';

function alert(title, msg) {
    Alert.alert(title, msg, [{ text: 'OK', onPress: () => console.log('OK Pressed') },], { cancelable: false });
}

const PrintColors = [
    { key: 0, section: true, label: 'PrintColor' },
    { key: 1, label: 'Monochrome' },
    { key: 2, label: 'Color' },
];

export default class Settings extends Component<{}> {
    static navigationOptions = {
        // headerStyle: { backgroundColor: StyleConfig.color_primary },
        // headerTintColor: StyleConfig.textOnPrimary,
        headerTitle: 'Print',
    };

    constructor(props) {
        super(props);
        this.state = {
            isLoading: true,
            printServiceAttributeStatus: null,
            copies: '1',
            printColor: 'Monochrome',
            filePath: null,
            printFilePath: null
        };
    }

    componentWillMount() {
        var that = this;

        const { navigate, state } = that.props.navigation;
        const { filePath, fileType, fileName } = state.params;
        var newPath = filePath + '.pdf';

        if (fileType != 'pdf') {
            RNFS.readFile(filePath, 'base64') // On Android, use "RNFS.DocumentDirectoryPath" (MainBundlePath is not defined)
                .then((content) => {
                    console.log('GOT CONTENT', content);

                    // convert to pdf
                    convert(fileName, fileType, content)
                        .then(pdfContent => {
                            // save to a new path
                            RNFS.writeFile(newPath, pdfContent, 'base64')
                                .then(() => {


                                    console.log('File save @', newPath);
                                    Toast.show(`File save @ ${newPath}`, Toast.SHORT);

                                    that.setState({ printFilePath: `file://${newPath}` });

                                    that.initPrinter(`file://${newPath}`);
                                })
                                .catch((err) => {
                                    console.log(err.message, err.code);
                                    // Toast.show(`WRITE FILE ERROR => ${err.code}: ${err.message}`, Toast.SHORT);
                                    throw err;
                                });;
                        })
                })
                .catch((err) => {
                    console.log(err.message, err.code);
                    // Toast.show(`READ FILE ERROR => ${err.code}: ${err.message}`, Toast.SHORT);
                    throw err;
                });

        } else {
            that.setState({ printFilePath: `file://${filePath}` });
            that.initPrinter(filePath);
        }

        DeviceEventEmitter.addListener('PrintServiceAttributeStatusUpdated', function (e) {
            that.setState({ printServiceAttributeStatus: e.status });
            console.log(e.status);

            that.setState({ isLoading: false });
        });
    }

    initPrinter(filePath) {
        var that = this;
        that.setState({ filePath });
        RicohPrinterAndroid.onCreate()
            .then((msg) => {
                //alert('Print document', msg);
                const { navigate, state } = that.props.navigation;
                // const {filePath, fileType} = state.params;
                that.setPrintFilePath(filePath);
                that.setPrintColor(that.state.printColor);
                that.setPrintCopies(that.state.copies);

                //that.setState({isLoading: false});
            }, (error) => {
                alert('Print document', error.message());
            });
    }

    setPrintFilePath(filePath) {
        RicohPrinterAndroid.setPrintFilePath(filePath)
            .then(msg => { }, // alert('Print document', msg),
                error => alert('Print document', error.message()));
    }

    setPrintColor(color) {
        RicohPrinterAndroid.setPrintColor(color)
            .then(msg => { }, // alert('Print document', msg),
                error => alert('Print document', error.message()));
    }

    setPrintCopies(copies) {
        if (!copies) return;

        RicohPrinterAndroid.setPrintCopies('' + copies + '')
            .then(msg => { }, //alert('Print document', msg),
                error => alert('Print document', error.message()));
    }

    render() {
        return (
            <ScrollView style={{ padding: 20 }}>
                {this.renderSpacer()}
                <View style={[{ flex: 1 }, styles.row]}>
                    <Text style={[styles.title]}>{translate('Copies')}</Text>
                    <Numeric
                        style={{ flex: 1 }}
                        ref="txtCopies"
                        underlineColorAndroid={'transparent'}
                        onChangeText={(val) => {
                            this.setState({ copies: val });

                            this.setPrintCopies(val);
                        }}
                        value={this.state.copies}
                    // setState is asynchronous and use the second argument to setState which is a callback
                    />
                </View>
                <View style={[{ flex: 1 }, styles.row]}>
                    <Text style={[styles.title]}>{translate('PrintColor')}</Text>
                    <ModalSelector
                        data={PrintColors}
                        initValue={PrintColors[1].label}
                        onChange={(option) => {
                            this.setState({ printColor: option.label });
                            this.setPrintColor(option.label);
                        }} />
                </View>
                <View style={[{ flex: 1 }, styles.row]}>
                    <Text style={[styles.title]}>{translate('PrintServiceStatus')}</Text>
                    <Text style={styles.title}>{this.state.printServiceAttributeStatus}</Text>
                </View>
                {this.renderSpacer()}

                <TouchableOpacity onPress={this.isPrintFileReady() ? this.print.bind(this) : null} style={styles.button}>
                    <Text style={styles.buttonFont}>Print</Text>
                </TouchableOpacity>
                {this.renderSpacer()}

{/*
                <TouchableOpacity onPress={this.isPrintFileReady() ? this.printStaticPdf.bind(this) : null} style={styles.button}>
                    <Text style={styles.buttonFont}>Print static pdf</Text>
                </TouchableOpacity>
                    {this.renderSpacer()}*/}
                <Text style={styles.title}>{translate('FilePath')}: {this.state.filePath}</Text>

                {this.renderSpinner()}
            </ScrollView>
        );
    }

    print() {
        if (!this.isPrintFileReady()) {
            alert('Print document', 'The document to be printed is not ready.');
            return false;
        }

        if (!this.state.copies) {
            alert('Print document', 'Please input print copies.');
            return false;
        }

        //that.setPrintFilePath(filePath);

        RicohPrinterAndroid.onStartPrint()
            .then((msg) => {
                //alert('Print document', msg);
            }, (error) => {
                alert('Print document', error.message());
            });
    }

    printStaticPdf() {
        if (!this.isPrintFileReady()) {
            alert('Print document', 'The document to be printed is not ready.');
            return false;
        }
        that.setPrintFilePath('test.pdf');
        RicohPrinterAndroid.onStartPrint()
            .then((msg) => {
                //alert('Print document', msg);
            }, (error) => {
                alert('Print document', error.message());
            });
    }

    open() {
        RicohPrinterAndroid.openActivity()
            .then((msg) => {
                //alert('Print document', msg);
            }, (error) => {
                alert('Print document', error.message());
            });
    }

    renderSpacer() {
        return (
            <View style={styles.spacer}></View>
        )
    }

    isPrintFileReady() {
        return this.props.navigation.state.params.filePath && this.props.navigation.state.params.filePath != '';
    }

    renderSpinner() {
        const { isLoading } = this.state;
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

const styles = StyleSheet.create({
    container: {
        flex: 1,
        // justifyContent: 'center',
        // alignItems: 'flex-start',
        backgroundColor: '#F5FCFF',
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
    gray: {

    }
});
