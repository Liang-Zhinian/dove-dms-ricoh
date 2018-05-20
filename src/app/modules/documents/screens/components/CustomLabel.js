import React, { Component } from 'react';
import {
    Platform,
    StyleSheet,
    Text,
    View,
    Dimensions,
    Picker,
    PickerIOS,
    ScrollView
} from 'react-native';
import Svg, { G } from 'react-native-svg';
import { VictoryPie, VictoryLabel, VictoryTooltip } from "victory-native";
const { width, height } = Dimensions.get('window');

export default class CustomLabel extends Component {
    render() {
        return (
            <G>
                <VictoryLabel {...this.props} /><VictoryLabel {...this.props} />
                <VictoryTooltip
                    {...this.props}
                    x={200} y={250}
                    text={`Qualquer coisa`}
                    orientation="top"
                    pointerLength={0}
                    cornerRadius={50}
                    width={100}
                    height={100}
                    flyoutStyle={{ fill: "black" }}
                />
            </G>
        );
    }
}
CustomLabel.defaultEvents = VictoryTooltip.defaultEvents;