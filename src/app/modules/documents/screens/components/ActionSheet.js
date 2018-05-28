'use strict';

import React, { Component } from 'react';
import {
    Modal,
    StyleSheet,
    TouchableOpacity,
    View
} from 'react-native';
import Button from './button';
import FadeInView from './fade_in_view';
import {translate} from '../../../../i18n/i18n';

class ActionModal extends Component {
    render() {
        return (
            <FadeInView visible={this.props.modalVisible} backgroundColor={this.props.backgroundColor} >
                <Modal
                    animationType="slide"
                    transparent={true}
                    visible={this.props.modalVisible}
                    onRequestClose={this.props.onCancel}>
                    <View style={styles.modalContainer}>
                        <TouchableOpacity style={styles.container} onPress={this.props.onCancel}></TouchableOpacity>
                        {this.props.children}
                        <Button style={{height: 40}} onPress={this.props.onCancel} text={this.props.buttonText || translate('Cancel')} />
                    </View>
                </Modal>
            </FadeInView>
        );
    }
};

const styles = StyleSheet.create({
    container: {
        flex: 1
    },
    modalContainer: {
        flex: 1,
        padding: 8,
        paddingBottom: 0,
        justifyContent: "flex-end"
    }
});

export default ActionModal;