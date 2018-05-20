import React, { Component } from 'react';
import {
    Alert,
} from 'react-native';

export function alert(title, msg) {
    Alert.alert(title, msg, [{ text: 'OK', onPress: () => console.log('OK Pressed') },], { cancelable: false });
}