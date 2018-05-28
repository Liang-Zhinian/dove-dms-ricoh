'use strict';

import React, { Component } from 'react';
import {
    View,
    Text,
    TextInput,
    DatePickerIOS,
    StyleSheet,
} from 'react-native';
import ActionSheet from './Dialog';

export default class CustomActionSheet extends Component {
    constructor(props) {
        super(props);
        this.state = {
            dateForPicker: Date.now(),
        };
    }

    render() {
        return (
            <View>
                <ActionSheet modalVisible={this.props.modalVisible} onCancel={this.props.toggleModal}>
                    <View style={styles.datePickerContainer}>
                        <Text>CustomActionSheet</Text>
                    </View>
                </ActionSheet>
            </View>
        );
    }
};

const styles = StyleSheet.create({
    datePickerContainer: {},
})