'use strict';

import React, { Component } from 'react';
import {
    Modal,
    StyleSheet,
    TouchableOpacity,
    View,
    Text,
    KeyboardAvoidingView,
    Dimensions,
} from 'react-native';
import FadeInView from './fade_in_view';

const { width, height } = Dimensions.get('window');

class Dialog extends Component {

    renderCancelButton() {
        return (
            <TouchableOpacity
                style={[styles.button, {
                    borderRightWidth: 1,
                    borderRightColor: 'grey',
                    borderBottomLeftRadius: 10,
                }]}
                onPress={this.props.onCancel}>
                <Text style={styles.buttonText}>
                    Cancel
                </Text>
            </TouchableOpacity>
        )
    }

    renderOkButton() {
        return (
            <TouchableOpacity
                style={[styles.button, {
                    borderBottomRightRadius: 10
                }]}
                onPress={this.props.onOK}>
                <Text style={styles.buttonText}>
                    OK
                </Text>
            </TouchableOpacity>
        )
    }

    render() {
        const contentHeight = this.props.height || height * 0.5;
        return (
            // <FadeInView
            //     visible={this.props.modalVisible}
            //     backgroundColor={this.props.backgroundColor}
            // >
            <Modal
                animationType={"slide"}
                transparent={true}
                visible={this.props.modalVisible}
                onRequestClose={this.props.onCancel}
            >
                {/*<View
                    style={[styles.container, {
                        backgroundColor: 'rgba(0, 0, 0, 0.5)',
                    }]}>*/}

                <KeyboardAvoidingView
                    keyboardVerticalOffset={-50}
                    behavior='padding'
                    style={[styles.container, {
                        backgroundColor: 'rgba(0, 0, 0, 0.5)',
                    }]}
                >
                    <View style={[styles.innerContainer, { height: contentHeight }]}>
                        <View style={{
                            flex: 1,
                            paddingTop: 10,
                            paddingLeft: 10,
                            paddingRight: 10,
                            flexDirection: 'column',
                        }}>
                            {this.props.children}
                        </View>
                        <View style={{
                            // flex: 1,
                            flexDirection: 'row',
                            borderTopWidth: 1,
                            borderTopColor: 'grey',
                            // paddingBottom: 10,
                        }}>
                            {this.renderCancelButton()}
                            {this.renderOkButton()}
                        </View>
                    </View>
                </KeyboardAvoidingView>
                {/*</View>*/}
            </Modal >

            //</FadeInView>
        );
    }
}

export default Dialog;

const styles = StyleSheet.create({
    container: {
        flex: 1,
        justifyContent: 'center',
        // padding: 20,
    },
    innerContainer: {
        // flex: 1,
        // flexDirection: 'column',
        borderRadius: 10,
        backgroundColor: 'white',
        marginLeft: '5%',
        marginRight: '5%',
        // width: width * 0.8,
        // height: height * 0.4,
    },
    buttonText: {
        color: '#0069d5',
        alignSelf: 'center',
        fontSize: 18
    },
    button: {
        flex: 1,
        height: 36,
        backgroundColor: 'white',
        justifyContent: 'center',
        alignSelf: 'center'
    }
});
