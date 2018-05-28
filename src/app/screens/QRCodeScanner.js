'use strict';

import React, { Component } from 'react';

import {
    AppRegistry,
    StyleSheet,
    Text,
    TouchableOpacity,
    Linking,
} from 'react-native';

import QRCodeScanner from 'react-native-qrcode-scanner';

import {translate} from '../i18n/i18n';

export default class ScanScreen extends Component {
    onSuccess(e) {
        // e has data attribute
        this.props.onSuccess && this.props.onSuccess(e);
    }

    render() {
        return (
            <QRCodeScanner
                onRead={this.onSuccess.bind(this)}
                topContent={this.renderTopContent()}
                bottomContent={
                    <TouchableOpacity style={styles.buttonTouchable}
                    onPress={()=>{this.props.onCancel && this.props.onCancel()}}
                    >
                        <Text style={styles.buttonText}>{translate('Cancel')}</Text>
                    </TouchableOpacity>
                }
            />
        );
    }

    renderTopContent() {

        var topConent = (
            <Text style={styles.centerText}>
                {translate('ScanQRCode')}
            </Text>
        );

        if (this.props.renderTopContent) {
            return this.props.renderTopContent();
        }
        return topConent;
    }
}

const styles = StyleSheet.create({
    centerText: {
        flex: 1,
        fontSize: 18,
        padding: 32,
        color: '#777',
    },
    textBold: {
        fontWeight: '500',
        color: '#000',
    },
    buttonText: {
        fontSize: 21,
        color: 'rgb(0,122,255)',
    },
    buttonTouchable: {
        padding: 16,
    },
});
