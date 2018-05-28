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
        return (
            // <FadeInView
            //     visible={this.props.modalVisible}
            //     backgroundColor={this.props.backgroundColor}
            // >
            <View>
                <Modal
                    animationType={"slide"}
                    transparent={true}
                    visible={this.props.modalVisible}
                    onRequestClose={this.props.onCancel}
                >
                    <View style={[styles.container, { backgroundColor: 'rgba(0, 0, 0, 0.5)', }]}>
                        <KeyboardAvoidingView
                            // keyboardVerticalOffset={-50}
                            behavior='padding'
                            // style={[styles.container,]}
                        >
                            <View>
                                <TouchableOpacity style={styles.container} onPress={this.props.onCancel}></TouchableOpacity>
                                <View style={[styles.innerContainer,]}>
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
                                <TouchableOpacity style={styles.container} onPress={this.props.onCancel}></TouchableOpacity>
                            </View>
                            <View style={{ height: 60 }} />
                        </KeyboardAvoidingView>
                    </View>
                </Modal>
            </View>
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
    modalContainer: {
        flex: 1,
        flexDirection: 'column',
        // alignSelf: 'center',
        // justifyContent: 'center',
        // paddingHorizontal: 20,
        // paddingTop: 20,
        // backgroundColor: 'rgba(0, 0, 0, 0.5)',
    },
    innerContainer: {
        flex: 1,
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
