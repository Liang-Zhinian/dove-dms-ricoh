import React, { Component } from 'react';
import {
    Platform,
    View,
    Text,
    StyleSheet,
    ScrollView,
    FlatList,
    ActivityIndicator,
} from 'react-native';
import PropTypes from 'prop-types';
import Swipeout from 'react-native-swipeout';
import RNFS from 'react-native-fs';
import moment from 'moment';
import ListItem from './components/ListItem';
import { translate } from '../../../i18n/i18n';

class Downloads extends Component {

    static navigationOptions = {
        headerTitle: translate('Downloads'),
    }

    constructor(props) {
        super(props);

        this.state = {
            isFetching: true,
            error: false,
            errorInfo: "",
            files: [],
            activeRow: null
        };
    }

    componentWillMount() {
        let _that = this;
        // get a list of files and directories in the main bundle
        RNFS.readDir(RNFS.DocumentDirectoryPath) // On Android, use "RNFS.DocumentDirectoryPath" (MainBundlePath is not defined)
            .then((result) => {
                var files = [];
                result.map((item, index) => {
                    RNFS.stat(item.path)
                        .then((stat) => {
                            if (stat.isFile()) {
                                item.id = index;
                                files.push(item);
                            }
                        })
                        .catch((err) => {
                            _that.setState({
                                isFetching: false,
                                error: true,
                                errorInfo: err.message,
                                files: [],
                            })
                        });
                });
                _that.setState({
                    isFetching: false,
                    files: files,
                });

                return files;
            })
            .catch((err) => {
                _that.setState({
                    isFetching: false,
                    error: true,
                    errorInfo: err.message,
                    files: [],
                })
            });
    }

    renderItem(info, activeRow) {
        const swipeSettings = {
            autoClose: true,
            close: info.item.id !== this.state.activeRow,
            onClose: (secId, rowId, direction) => this.onSwipeClose(info.item, rowId, direction),
            onOpen: (secId, rowId, direction) => this.onSwipeOpen(info.item, rowId, direction),
            right: [
                {
                    onPress: () => this.onDeleteItem(info.item),
                    text: translate('Delete'),
                    type: 'delete'
                }
            ],
            rowId: info.index,
            sectionId: 1
        };

        const item = info.item;

        let filetype = item.name.split('.').pop();

        const lastModified = moment(item.ctime, 'YYYY-MM-DD HH:mm:ss.sss ZZ').format('YYYY-MM-DD HH:mm:ss')
        const fileSize = ` | ${item.size} Bytes`;
        const description = lastModified + fileSize;

        /**
         * ctime: Mon Nov 20 2017 23:16:58 GMT+0800 (CST) {}
         * isDirectory:ƒ isDirectory()
         * isFile:ƒ isFile()
         * mtime:Mon Nov 20 2017 23:17:11 GMT+0800 (CST) {}
         * name:"Flower.JPG"
         * path:"/Users/devel/Library/Developer/CoreSimulator/Devices/A8BE8632-3B53-467F-B995-7CBBADBB2C36/data/Containers/Data/Application/B72734C9-B583-4C5B-95BF-1596FF306FE2/Documents/Flower.JPG"
         * size:6209998
         */
        const data = {
            id: null,
            title: item.name,
            description: description,
            type: filetype,
        };
        return (
            <Swipeout {...swipeSettings}>
                <ListItem
                    data={data}
                    onPress={() => this.onSelectItem(item)}
                    onPressInfo={() => { }}
                    infoIconVisible={false}
                />
            </Swipeout>
        );
    }


    onDeleteItem = (item) => {
        RNFS.unlink(item.path);
        var files = this.state.files;
        files = files.filter(o => o.id !== item.id);

        this.setState({ files });
    }

    onRefreshItems = () => {  }

    onSelectItem = (item) => {
        
        if (this.state.activeRow !== null) return;

        this._onPressItem(item);
    }

    refreshing = false;


    //加载等待的view
    renderLoadingView() {
        return (
            <View style={styles.container}>
                <ActivityIndicator
                    animating={true}
                    style={[styles.gray, { height: 80 }]}
                    color='red'
                    size="large"
                />
            </View>
        );
    }

    //加载失败view
    renderErrorView(error) {
        return (
            <View style={styles.container}>
                <Text>
                    Fail: {error}
                </Text>
            </View>
        );
    }

    render() {

        const listSettings = {
            data: this.state.files,
            extraData: this.state.activeRow,
            keyExtractor: (item, index) => item.id,
            onRefreshItems: this.onRefreshItems,
            refreshing: this.refreshing,
            renderItem: (info) => this.renderItem(info, this.state.activeRow)
        };

        if (this.state.isFetching && !this.state.error) {
            return this.renderLoadingView();
        } else if (this.state.error) {
            //请求失败view
            return this.renderErrorView(this.state.errorInfo);
        }
        return (
            <FlatList
                {...listSettings}
            />
        )
    }

    _onPressItem = (item: any) => {
        const { navigation: { navigate } } = this.props;

        this.previewDocument(item);
    }

    previewDocument = (item: any) => {
        const that = this;
        const { path, name, size } = item;
        
        const type = name.split('.').pop();

        that.openLocalUrl(path, name, type);
    }

    openLocalUrl = (url, fileName, fileType) => {
        const _that = this;
        const { navigate } = _that.props.navigation;
        Platform.OS == 'ios' && navigate('FileViewer', { file: { uri: `file://${url}`, fileName, fileType }, readOnly: false });
        Platform.OS == 'android' &&
            FileViewerAndroid.open(url)
                .then((msg) => {
                }, (error) => {
                });
    }

    onSwipeOpen(item, rowId, direction) {
        this.setState({ activeRow: item.id });
    }

    onSwipeClose(item, rowId, direction) {
        if (item.id === this.state.activeRow && typeof direction !== 'undefined') {
            this.setState({ activeRow: null });
        }
    }
}


const styles = StyleSheet.create({
    container: {
        flex: 1,
        flexDirection: 'row',
        justifyContent: 'center',
        alignItems: 'center',
        backgroundColor: '#F5FCFF',
    },
});

export default Downloads;