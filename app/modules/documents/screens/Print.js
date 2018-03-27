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
import { connect } from 'react-redux';
import ModalSelector from 'react-native-modal-selector';
import { NAME } from '../constants';
import RicohPrinterAndroid from '../../../components/RCTRicohPrinterAndroid';
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
            printServiceAttributeStatus: null,
            copies: 1,
            printColor: 'Monochrome',
        };
    }

    componentWillMount() {
        var that = this;

        // DeviceEventEmitter.addListener('ConnectStateUpdated', function (e) {
        //     that.setState({ connectState: e.stateLabel });
        // });
        // DeviceEventEmitter.addListener('ScanServiceAttributeUpdated', function (e) {
        //     that.setState({ scanServiceAttributeState: e.stateLabel });
        // });
        // DeviceEventEmitter.addListener('ScanJobStateUpdated', function (e) {
        //     that.setState({ scanJobState: e.stateLabel });
        // });
        // DeviceEventEmitter.addListener('ScanServiceAttributeListenerErrorUpdated', function (e) {
        //     that.setState({ scanServiceAttributeListenerError: e.stateLabel });
        // });
        // DeviceEventEmitter.addListener('ScanJobListenerErrorUpdated', function (e) {
        //     that.setState({ scanJobListenerError: e.stateLabel });
        // });
        DeviceEventEmitter.addListener('PrintServiceAttributeStatusUpdated', function (e) {
            that.setState({ printServiceAttributeStatus: e.status });
            console.log(e.status);

        });

        RicohPrinterAndroid.onCreate()
            .then((msg) => {
                alert('Print document', msg);
                const { navigate, state } = that.props.navigation;
                const filePath = state.params.filePath;
                RicohPrinterAndroid.setPrintFilePath(filePath)
                    .then(msg => alert('Print document', msg),
                        error => alert('Print document', error.message()))

            }, (error) => {
                alert('Print document', error.message());
            });
    }

    render() {
        return (
            <ScrollView style={{ padding: 20 }}>
                {this.renderSpacer()}
                <View style={[{ flex: 1 }, styles.row]}>
                    <Text style={[styles.title]}>Copies</Text>
                    <TextInput
                        style={{ flex: 1 }}
                        ref="txtCopies"
                        blurOnSubmit={true}
                        underlineColorAndroid={'transparent'}
                        onChangeText={(val) => { this.setState({ copies: val }) }}
                    // setState is asynchronous and use the second argument to setState which is a callback
                    />
                </View>
                <View style={[{ flex: 1 }, styles.row]}>
                    <Text style={[styles.title]}>PrintColor</Text>
                    <ModalSelector
                        data={PrintColors}
                        initValue={PrintColors[1].label}
                        onChange={(option) => { this.setState({ printColor: option.label }) }} />
                </View>
                <View style={[{ flex: 1 }, styles.row]}>
                    <Text style={[styles.title]}>Print Service Status</Text>
                    <Text style={styles.title}>{this.state.printServiceAttributeStatus}</Text>
                </View>
                {this.renderSpacer()}

                <TouchableOpacity onPress={this.isPrintFileReady() ? this.print.bind(this) : null} style={styles.button}>
                    <Text style={styles.buttonFont}>Print</Text>
                </TouchableOpacity>
                {this.renderSpacer()}
                <Text style={styles.title}>Print File Path: {this.props.navigation.state.params.filePath}</Text>

            </ScrollView>
        );
    }

    print() {
        if (!this.isPrintFileReady()){
            alert('Print document', 'The document to be printed is not ready.');
            return false;
        }

        RicohPrinterAndroid.onStartPrint()
            .then((msg) => {
                alert('Print document', msg);
            }, (error) => {
                alert('Print document', error.message());
            });
    }

    open() {
        RicohPrinterAndroid.openActivity()
            .then((msg) => {
                alert('Print document', msg);
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
});
