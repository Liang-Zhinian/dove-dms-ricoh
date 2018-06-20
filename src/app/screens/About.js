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
    View
} from 'react-native';

export default class About extends Component<{}> {
    render() {
        return (
            <View style={styles.container}>
                <Text style={styles.title}>
                    ATPATH Technologies Limited (络基数码科技广州有限公司) 成立于1998年，在中国香港和广州分别建立了软件研发和销售地点。络基科技追随云端理念，致力于专业优质的软件开发，APP开发服务，保护企业营销和管理生命力企业软件开发，是企业管理信息化建设的忠实合作伙伴。
                </Text>
            </View>
        );
    }
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
        backgroundColor: '#F5FCFF',
    },
    title: {
        fontSize: 20,
        textAlign: 'center',
        margin: 10,
    },
});
