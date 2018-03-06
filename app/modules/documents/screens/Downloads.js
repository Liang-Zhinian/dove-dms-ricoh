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
import RNFS from 'react-native-fs';
import moment from 'moment';
import ListItem from './components/ListItem';

class Downloads extends Component {
    static navigationOptions = {
        headerTitle: 'Downloads',
    }

    constructor(props) {
        super(props);

        this.state = {
            isFetching: true,
            error: false,
            errorInfo: "",
            files: [],
        };
    }

    componentWillMount() {
        let _that = this;
        // get a list of files and directories in the main bundle
        RNFS.readDir(RNFS.DocumentDirectoryPath) // On Android, use "RNFS.DocumentDirectoryPath" (MainBundlePath is not defined)
            .then((result) => {
                console.log('GOT RESULT', result);
                var files = [];
                result.map((item) => {
                    RNFS.stat(item.path)
                        .then((stat) => {
                            if (stat.isFile()) {
                                files.push(item);
                            }
                        })
                        .catch((err) => {
                            console.log(err.message, err.code);

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
                console.log(err.message, err.code);

                _that.setState({
                    isFetching: false,
                    error: true,
                    errorInfo: err.message,
                    files: [],
                })
            });
    }

    //返回itemView
    renderItem(item) {
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
            <ListItem
                data={data}
                onPress={() => this._onPressItem(item)}
                onPressInfo={() => { }}
                infoIconVisible={false}
            />
        );
    }

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
        if (this.state.isFetching && !this.state.error) {
            return this.renderLoadingView();
        } else if (this.state.error) {
            //请求失败view
            return this.renderErrorView(this.state.errorInfo);
        }
        return (
            <FlatList
                data={this.state.files}
                renderItem={({ item }) => this.renderItem(item) /*<Text>{item.name}</Text>*/}
                keyExtractor={(item, index) => index}
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
        debugger;
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
                    console.log('success!!')
                }, (error) => {
                    console.log('error!!')
                });
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