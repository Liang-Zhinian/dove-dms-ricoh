import React, { Component } from 'react';
import {
    TouchableOpacity
} from 'react-native';
import Ionicons from 'react-native-vector-icons/Ionicons';
import { colors } from '../../styles';

export default class InfoIcon extends Component {

    render() {
        return (
            <TouchableOpacity style={[this.props.style]}
                onPress={this.props.onPress}>
                <Ionicons name='ios-information-circle-outline'
                    style={{ color: colors.primary }}
                    size={30} /></TouchableOpacity>
        );
    }
}