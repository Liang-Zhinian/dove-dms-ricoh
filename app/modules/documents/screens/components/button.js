'use strict'

import React, { Component } from 'react';
import {
    TouchableOpacity,
    StyleSheet,
    Text,
    View
} from 'react-native';


class Button extends Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <TouchableOpacity style={styles.button} onPress={this.props.onPress}>
                <Text style={styles.buttonText}>
                    {this.props.text}
                </Text>
            </TouchableOpacity>
        );
    }
};

const styles = StyleSheet.create({
    buttonText: {
        color: '#0069d5',
        alignSelf: 'center',
        fontSize: 18
    },
    button: {
        height: 36,
        backgroundColor: 'white',
        borderColor: 'white',
        borderWidth: 1,
        borderRadius: 6,
        marginBottom: 10,
        alignSelf: 'stretch',
        justifyContent: 'center'
    }
});

export default Button;