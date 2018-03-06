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
import { connect } from 'react-redux'

import Spinner from '../../../components/Spinner';
import * as actions from '../actions';
import { NAME } from '../constants';
import {
    //listChildren,
    //createDownloadTicketWithProgressSOAP,
    //deleteDocuments,
    //loginSOAP,
    searchDocuments,
    //ensureLogin
} from '../api';
import DocumentService, { DownloadManager } from '../services/DocumentService';
import SearchBox from './components/SearchBox';
import DocumentList from './components/DocumentList';
import FileViewerAndroid from '../../../components/RCTFileViewerAndroid';

class Search extends Component {
    static defaultProps = {
        downloadTask: null,
        downloadManger: new DownloadManager()
    };

    static navigationOptions = ({ navigation }) => {
        const { params = {} } = navigation.state;

        let headerTitle = 'Search Results';
        return { headerTitle };
    };

    constructor(props) {
        super(props);
        this.state = {
            isLoading: false,
            dataSource: [],
            progress: 0,
            lastTick: Date.now(),
            docId: 0,
        };
    }

    render() {
        return (
            <View style={{
                flex: 1,
                backgroundColor: 'white'
            }}>
                <View style={[styles.container, {
                    flex: 1,
                    //backgroundColor: 'transparent',
                    opacity: 0.9,
                    position: 'absolute',
                    top: 0,
                    bottom: 0,
                    left: 0,
                    right: 0,
                    //flexDirection: 'column',
                    display: this.state.isLoading ? 'flex' : 'none'
                }]}>
                    <View style={{
                        flex: 1,
                        flexDirection: 'column',
                        justifyContent: 'center',
                    }}>
                        <Spinner style={{
                        }} />
                    </View>
                </View>
                <SearchBox
                    onSearch={this.filter.bind(this)}
                />
                <DocumentList
                    navigation={this.props.navigation}
                    dataSource={this.state.dataSource}
                    onRefresh={this._onRefresh}
                    //onPressCheckbox={this._onPressCheckbox}
                    onPressItem={this._onPressItem}
                    //onPressInfo={this._onPressItemInfo}
                    //onPressCross={this._onPressItemCross}
                    downloadManger={this.downloadManger}
                    infoIconVisible={false}
                />
            </View>
        );
    }

    _onPressItem = (item: any) => {
        const { navigation: { navigate } } = this.props;

        this.props.chooseDocument(item);
        this.previewDocument(item);
    }

    async filter(text) {
        this.setState({ isLoading: true });
        const {
          username,
            password,
        } = this.props;

        const data = await searchDocuments(username, password, text);

        this.setState({
            dataSource: data,
            isLoading: false,
        })
    }

    previewDocument = (item: any) => {
        const that = this;
        that.props.updateDownloadStatus(true);
        that.setState({
            progress: 0,
            docId: item.id,
        });
        const { sid } = that.props;
        const { fileName, type, fileSize } = item;

        DocumentService.downloadToCacheDirectory(sid, item, that.updateProgress, that.resetDownloadTask)
            .then(man => {
                that.downloadManger = man;
                that.downloadManger.onCanceled = that.resetDownloadTask;
                that.downloadManger.onProgress = (received, total) => { that.updateProgress(received, fileSize) };

                return man.task;
            })
            .then(task => { return task.path() })
            .then((path) => {
                if (!path) return;
                // the temp file path
                console.log('The file saved to ', path)
                that.openLocalUrl(path, fileName, type);
                that.resetDownloadTask();
            })
            .catch((err) => {
                if (err.message === 'cancelled') return;
                console.log(err);
            });
    }


    updateProgress = (received, total) => {

        // if (total > 3 * 1000 * 1000 && Date.now() - this.state.lastTick < 1000)
        //   return
        // console.log(`progress: ${received} / ${total}`)

        this.setState({
            progress: received / total,
            lastTick: Date.now()
        });
        this.props.updateProgress(received / total);
    }

    openLocalUrl = (url, fileName, fileType) => {
        const _that = this;
        const { navigate } = _that.props.navigation;
        Platform.OS == 'ios' && navigate('FileViewer', { file: { uri: `file://${url}`, fileName, fileType } });
        Platform.OS == 'android' &&
            FileViewerAndroid.open(url)
                .then((msg) => {
                    console.log('success!!')
                }, (error) => {
                    console.log('error!!')
                });

        _that.props.chooseDocument(null);
        _that.props.updateProgress(0);
    }

    resetDownloadTask = () => {
        console.log('resetDownloadTask');
        const that = this;
        that.setState({
            progress: 0, //total,
            lastTick: Date.now()
        });
        that.props.chooseDocument(null);
        that.props.updateProgress(0);
        that.props.updateDownloadStatus(false);
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

function select(store) {
    return {
        username: store[NAME].account.username,
        password: store[NAME].account.password,
        sid: store[NAME].account.token.sid,
    };
}

function dispatch(dispatch) {
    return {
        updateDownloadStatus: (isDownloading) => dispatch(actions.updateDownloadStatus(isDownloading)),
        updateProgress: (percent) => dispatch(actions.updateProgress(percent)),
        chooseDocument: (document) => dispatch(actions.chooseDocument(document)),
    }
};

export default connect(select, dispatch)(Search);
