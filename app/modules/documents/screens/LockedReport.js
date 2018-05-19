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
    ScrollView,
    FlatList,
    TouchableOpacity
} from 'react-native';

import DocumentList from './components/DocumentList';
import ListItem from './components/ListItem';
import getIcon from '../lib/icon';
import { CommonStyles } from '../styles';

const dataSource = [{
    id: '1',
    title: '发票.jpg',
    description: '09/11/2017 14:12:05',
    type: 'jpg',
}, {
    id: '2',
    title: 'master-seo.pdf',
    description: '22/08/2017 17:19:33',
    type: 'pdf',
}, {
    id: '3',
    title: '考勤表201707.xlsx',
    description: '17/08/2017 13:23:06',
    type: 'xlsx',
}, {
    id: '4',
    title: '绩效考核方法.pptx',
    description: '17/08/2017 13:23:06',
    type: 'pptx',
}, {
    id: '5',
    title: '商铺租赁合同范本.png',
    description: '17/08/2017 11:15:16',
    type: 'png',
}, {
    id: '6',
    title: '注册文件.png',
    description: '09/11/2017 14:12:05',
    type: 'png',
}, {
    id: '7',
    title: '销售协议.docx',
    description: '09/11/2017 14:03:36',
    type: 'docx',
}, {
    id: '8',
    title: '钢琴曲 - 眼泪 - tears 轻音乐.mp3',
    description: '09/11/2017 14:24:39',
    type: 'mp3',
}]

export default class LockedReport extends Component {
    static navigationOptions = {
        headerTitle: 'Locked Documents',
    };

    render() {
        return (
            <ScrollView
                style={styles.container}>

                <FlatList
                    data={dataSource}
                    renderItem={this.renderItem}
                    keyExtractor={(item, index) => index}
                />
            </ScrollView>
        );
    }

    renderItem = ({ item }) => {
        const {
            id,
            title,
            description,
            type,
        } = item;

        return (
            <View style={{
                flex: 1,
                flexDirection: 'row',
                justifyContent: 'center',
                alignContent: 'center',
                alignItems: 'center',
            }}>
                <TouchableOpacity
                    underlayColor={'grey'}
                    testID='docuCell'
                    accessibilityLabel='docuCell'
                    style={{
                        flex: 1,
                        justifyContent: 'center',
                        alignContent: 'center',
                    }}
                >
                    <View>
                        <View style={styles.rowContainer}>
                            <View style={CommonStyles.m_r_1}>
                                {getIcon(type)}
                            </View>
                            <View style={styles.textContainer}>
                                <Text style={styles.title}>{title}</Text>
                                <Text style={styles.content}>{description}</Text>
                            </View>
                        </View>
                        <View style={styles.separator} />
                    </View>
                </TouchableOpacity>
            </View>
        );
    }
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: 'white',
    },
    thumb: {
        width: 50,
        height: 50,
        marginRight: 10
    },
    content: {
        fontSize: 12,
        color: 'black',
    },
    textContainer: {
        flex: 1,
    },
    separator: {
        height: 1,
        backgroundColor: '#dddddd'
    },
    title: {
        fontSize: 15,
        color: '#656565'
    },
    rowContainer: {
        flexDirection: 'row',
        padding: 10
    },
    progress: {
        marginTop: 5,
    }
});
