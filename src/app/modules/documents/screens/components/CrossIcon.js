import React, { Component } from 'react';
import {
    TouchableOpacity
} from 'react-native';
import Entypo from 'react-native-vector-icons/Entypo';
import { colors } from '../../styles';

export default class CrossIcon extends Component {

    render() {
        return (
            <TouchableOpacity style={[this.props.style]}
                onPress={this.props.onPress}>
                <Entypo name='circle-with-cross'
                    style={{ color: colors.primary }}
                    size={28} /></TouchableOpacity>
        );
    }
}