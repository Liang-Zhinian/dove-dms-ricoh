'use strict';
import React, { Component } from 'react';
import {
    FlatList,
    StyleSheet,
    ScrollView,
    RefreshControl
} from 'react-native';
import moment from 'moment';

import ListItem from './ListItem';
import DocumentService, { DownloadManager } from '../../services/DocumentService';


export class DocumentList extends Component {
    static defaultProps = {
        downloadTask: null,
        downloadManger: new DownloadManager()
    };

    constructor(props) {
        super(props);

        this.state = {
            refreshing: false,
        };
    }

    render() {
        return (
            <ScrollView style={{ flex: 1, ...this.props.style }}
                refreshControl={
                    <RefreshControl
                        refreshing={this.state.refreshing}
                        onRefresh={this.props.onRefresh}
                    />
                }
            >
                <FlatList
                    data={this.props.dataSource}
                    renderItem={this.renderItem}
                    keyExtractor={(item, index) => index}
                />
            </ScrollView>
        );
    }

    renderItem = ({ item }: { item: any }) => {

        const lastModified = moment(item.lastModified, 'YYYY-MM-DD HH:mm:ss.sss ZZ').format('YYYY-MM-DD HH:mm:ss')
        const fileSize = !(item.type == 0 || item.type == 1) ? ` | ${item.fileSize} Bytes` : '';
        const description = lastModified + fileSize;
        const data = {
            id: item.id,
            title: item.name || item.fileName,
            description: description,
            type: item.type,
        };
        return (
            <ListItem
                {...this.props}
                data={data}
                downloadManger={this.downloadManger}
                onPressCheckbox={this.props.onPressCheckbox}
                onPress={() => this.props.onPressItem(item)}
                onPressInfo={() => this.props.onPressItemInfo(item)}
                onPressCross={this.props.onPressItemCross}
            />
        );
    }
}

export default DocumentList;