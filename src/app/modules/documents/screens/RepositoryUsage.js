/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

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
import { VictoryPie, VictoryLabel } from "victory-native";
const { width, height } = Dimensions.get('window');
const PickerItemIOS = PickerIOS.Item;

import fromBytes from '../lib/filesize';
import CustomLabel from './components/CustomLabel';

function getSum(array) {
    return array
        .map(item => {
            return item.y;
        })
        .reduce((prev, curr, index) => {
            return prev += curr;
        });
}

export default class RepositoryUsage extends Component {
    static navigationOptions = {
        headerTitle: 'My Storage',
    };

    constructor(props) {
        super(props);
        this.state = {
            type: 'MyStorage',
            chartData: null,
            total: null,
            tooltip: ''
        }
    }

    componentWillMount() {
        const data = [
            { x: "Used", y: 50000000 },
            { x: "Remaining", y: 450000000 },
        ];
        this.setState({
            chartData: data,
            total: getSum(data)
        })
    }

    render() {
        const chartWidth = Math.min(width, height);
        return (
            <ScrollView contentContainerStyle={{ justifyContent: 'center', alignItems: 'center' }}
                style={styles.container}>
                {Platform.OS === 'android' && <Picker
                    selectedValue={this.state.type}
                    onValueChange={this._onValueChanged.bind(this)}
                    style={{ width: 300 }}
                    mode={Picker.MODE_DIALOG}
                    // mode="dialog"
                    prompt='请选择类别'
                >
                    <Picker.Item label="My Storage" value="MyStorage" />
                    <Picker.Item label="Overall Storage (for Admin)" value="OverallStorage" />
                </Picker>}

                {Platform.OS === 'ios' && <PickerIOS
                    selectedValue={this.state.type}
                    onValueChange={this._onValueChanged.bind(this)}
                    style={{ marginBottom: 60, height: 100, width: 300 }}
                    itemStyle={{
                        fontSize: 22,
                        color: 'blue',
                        textAlign: 'center',
                        fontWeight: 'bold',
                        fontFamily: 'arial'
                    }}
                >
                    <PickerItemIOS key='MyStorage' label="My Storage" value="MyStorage" />
                    <PickerItemIOS key='OverallStorage' label="Overall Storage (for Admin)" value="OverallStorage" />
                </PickerIOS>}

                <View style={styles.chart}>
                    <Svg
                        width={chartWidth}
                        height={chartWidth * 1.3}
                        viewBox={`0 0 ${chartWidth} ${chartWidth}`}
                        style={{
                            flex: 1,
                            flexDirection: 'row',
                            alignItems: 'center',
                            justifyContent: 'center',
                            // paddingLeft: 100
                        }}>
                        <VictoryPie
                            standalone={false}
                            // domainPadding={50}
                            height={chartWidth}
                            width={chartWidth}
                            padding={{ top: 70, bottom: 70 }}
                            margin={{ top: 70, bottom: 70 }}
                            data={this.state.chartData}
                            colorScale='blue'
                            innerRadius={chartWidth * 0.15}
                            labels={(d) => `${d.x} (${d.y / this.state.total * 100}%)`}
                            labelRadius={chartWidth * 0.45}
                            labelComponent={<VictoryLabel
                                angle={90}
                                verticalAnchor="start"
                                textAnchor="middle" />}
                            // labelComponent={<CustomLabel text='asdfd' style={{ fill: "black", fontSize: 12 }} angle={90} />}
                            style={{
                                labels: {
                                    fill: "black",
                                    fontSize: 12,
                                    fontFamily: 'arial'
                                }
                            }}
                            animate={{ duration: 1500 }}
                            events={[
                                {
                                    target: "data",
                                    eventHandlers: {
                                        onPressIn: (proxy, data, index, sender) => {
                                            this.setState({ tooltip: `${data.datum.x}\n${fromBytes(data.datum.y)}` })
                                        }
                                    }
                                }
                            ]}
                        />
                        <VictoryLabel
                            textAnchor="middle"
                            style={{ fontSize: 12 }}
                            x={chartWidth * 0.5}
                            y={chartWidth * 0.5}
                            text={this.state.tooltip}
                        />
                    </Svg>
                </View>
                <View style={{
                    flex: 1,
                    flexDirection: 'column',
                    alignItems: 'flex-start',
                    // height: 200,
                    width: width,
                    // backgroundColor: 'grey',
                    margin: 10,

                }}>
                    {this.state.chartData.map((item) => {
                        return (<View
                            key={item.x}
                            style={{
                                flex: 1,
                                flexDirection: 'row',
                                marginTop: 10,
                                marginLeft: 10,
                                // backgroundColor: 'grey',
                            }}>
                            <Text style={{ fontWeight: 'bold' }}>{item.x}: </Text><Text>{fromBytes(item.y)}</Text>
                        </View>)
                    })}
                    <View
                        style={{
                            flex: 1,
                            flexDirection: 'row',
                            marginTop: 10,
                            marginLeft: 10,
                            // backgroundColor: 'grey',
                        }}>
                        <Text style={{ fontWeight: 'bold' }}>Total: </Text><Text>{fromBytes(this.state.total)}</Text>
                    </View>
                </View>
            </ScrollView>
        );
    }

    _onValueChanged(value) {
        let data = [];
        switch (value) {
            case 'MyStorage':
                data = [
                    { x: "Used", y: 50000000 },
                    { x: "Remaining", y: 450000000 },
                ];
                break;
            case 'OverallStorage':
                data = [
                    { x: "Jack Leung", y: 150000000 },
                    { x: "Esmond Lo", y: 300000000 },
                    { x: "Henry Qiu", y: 50000000 },
                    { x: "Tony Ye", y: 100000000 },
                    { x: "Eva Wu", y: 1500000000 },
                    { x: "Robert Ho", y: 500000000 },
                    { x: "Chris Wang", y: 1550000000 },
                    { x: "Remaining", y: 850000000 },
                ];
                break;
            default:
                break;
        }

        this.setState({
            type: value,
            chartData: data,
            total: getSum(data),
            tooltip: ''
        });
    }
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        flexDirection: 'column',
        backgroundColor: 'white',
    },
    chart: {
        flex: 1,
        // height: width * 0.9,
        // width: width * 0.9,
        // width: 600,
        // height: 200,
        // margin: 50
        alignItems: 'center',
        justifyContent: 'center',
        flexDirection: 'row'
    },
    title: {
        fontSize: 20,
        textAlign: 'center',
        margin: 10,
    },
});
