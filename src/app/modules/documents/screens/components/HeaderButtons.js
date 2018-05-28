'use strict'

import React, { Component } from 'react';
import {
    TouchableOpacity,
    StyleSheet,
    Text,
    View
} from 'react-native';
import MaterialIcons from 'react-native-vector-icons/MaterialIcons';
import DoveTouchable from '../../../../components/DoveTouchable';
import { CommonStyles, colors } from '../../styles';

export const HeaderButton = (props: Object): ReactElement => {
    const { visible = true, onPress = () => null } = props;

    let icon;
    if (props.icon) {
        icon = <MaterialIcons
            name={props.icon}
            size={28}
            style={styles.icon}
        />;
    }

    let text;
    if (props.text) {
        text = <Text style={[styles.text]}>
            {props.text}
        </Text>;
    }

    return (
        <TouchableOpacity
            style={[styles.button, {
                display: visible ? 'flex' : 'none',
            }]}
            //accessibilityLabel={text || icon || ''}
            onPress={onPress}
        >
            {icon}
            {text}
        </TouchableOpacity>
    )
}

const styles = StyleSheet.create({
    button: {
        marginRight: 14,
        justifyContent: 'center',
    },
    icon: {
        color: colors.textOnPrimary,
    },
    text: {
        color: colors.textOnPrimary,
        fontSize: 20,
    },
});