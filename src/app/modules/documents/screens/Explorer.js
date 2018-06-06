

'use strict';
import React, { Component } from 'react'
import {
  Platform,
  View,
  FlatList,
  Text,
  ActivityIndicator,
  StyleSheet,
  TouchableOpacity,
  ScrollView,
  TextInput,
  RefreshControl,
  NetInfo,
} from 'react-native'
import { connect } from 'react-redux'
import ImagePicker from 'react-native-image-picker';
import OpenFile from 'react-native-doc-viewer';
import moment from 'moment';
import {
  listChildren,
  createDownloadTicketWithProgressSOAP,
  deleteDocuments,
  loginSOAP,
  searchDocuments,
  ensureLogin
} from '../api';
import { CommonStyles, colors } from '../styles';
import ListItem from './components/ListItem';
import * as actions from '../actions';
import { NAME } from '../constants';
import ProgressBar from './components/ProgressBar';
import MainActionSheet from './components/MainActionSheet';
import FolderCreationDialog from './components/FolderCreationDialog';
import DocumentService, { DownloadManager } from '../services/DocumentService';
import { HeaderButton } from './components/HeaderButtons';
import SearchBox from './components/SearchBox';
import DocumentList from './components/DocumentList';
import FileViewerAndroid from '../../../components/RCTFileViewerAndroid';
import Spinner from '../../../components/Spinner';
import { alert } from '../lib/alert';
import { convert } from '../api/converter';
import RNFS from 'react-native-fs';
import { translate } from '../../../i18n/i18n';

function isExpired(expires_date) {
  let currentTime = new Date();
  let d_expires_date = new Date(expires_date);
  return currentTime > d_expires_date;
}

class Explorer extends Component {

  static navigationOptions = ({ navigation }) => {
    const { params = {} } = navigation.state;

    let headerTitle = `${params.node ? params.node.name : translate('Documents')}`;
    let headerRight = (
      <View style={[
        CommonStyles.flexRow,
      ]}>
        <HeaderButton
          icon='add'
          visible={!params.isEditMode}
          onPress={params.onAddButtonPressed ? params.onAddButtonPressed : () => null}
        />
        <HeaderButton
          onPress={params.toggleEdit ? params.toggleEdit : () => null}
          text={translate(!params.isEditMode ? 'Edit' : 'Done')}
        />
      </View>
    );

    if (!params.node) headerRight = null;

    const withHeaderLeft = { headerTitle, headerRight };
    const withoutHeaderLeft = { headerTitle, headerRight, headerLeft: false };

    return !params.isEditMode ? withHeaderLeft : withoutHeaderLeft;
  };

  constructor(props) {
    super(props);
    this.state = {
      refreshing: false,
      folderCreationModalVisible: false,
      modalVisible: false,
      isEditMode: false,
      lastTick: Date.now(),
      isLoading: false,
      dataSource: [],
      mainListVisible: true,
      username: '',
      password: '',
      sid: '',
      folderId: null,
      docId: null,
      progressBarVisible: false,
      progress: 0,
      selectedList: [],
      folderName: '',
      isLoginLoading: false,
      searchListDataSource: [],
      searchListVisible: false,
      isConnected: null,
    }

    this.downloadTask = null;

    this.downloadManger = new DownloadManager();
  }

  componentWillMount() {
    const { username, password, sid, expires_date, login } = this.props;

    // check if sid is expired
    if (!username) {
      this.fetchData();
    }
    else {
      this.setState({
        username,
        password,
        sid,
      }, this.fetchData.bind(this));
    }
  }

  componentDidMount() {

    const that = this;

    const { navigation, isEditMode } = that.props;
    // We can only set the function after the component has been initialized
    navigation.setParams({
      onAddButtonPressed: that.showImagePicker.bind(that),
      toggleEdit: that.toggleEdit.bind(that),
      isEditMode
    });

    NetInfo.isConnected.addEventListener(
      'connectionChange',
      that._handleConnectivityChange.bind(this)
    );
    NetInfo.isConnected.fetch().done(
      (isConnected) => { that.setState({ isConnected }); }
    );
  }

  // Start changing images with timer on first initial load
  componentWillReceiveProps(nextProps) {
    const that = this;

    const { needReload } = nextProps;

    const { username, password, sid, expires_date, valid, login, renewSid } = nextProps;

    if (username && username !== that.state.username) {
      that.setState({
        username,
        password,
      }, () => {
        that.fetchData();
      });
    }

    if (username && sid && sid !== that.state.sid) {
      that.setState({
        sid,
      }, () => {
      });
    }

    needReload && setTimeout(that.fetchData, 1000);
  }

  componentWillUpdate(nextProps, nextState) {
  }

  componentDidUpdate(prevProps, prevState) {
  }

  componentWillUnmount() {
    const that = this;

    that.resetDownloadTask();

    NetInfo.isConnected.removeEventListener(
      'connectionChange',
      that._handleConnectivityChange.bind(that)
    )
  }

  _handleConnectivityChange(isConnected) {
    this.setState({ isConnected });
  }

  render() {

    const { username } = this.state;
    if (!username) {
      return this.renderWarningView('Please setup your account first!');
    }

    if (this.state.isLoading) {
      return this.renderLoadingView();
    }

    //加载数据
    return this.renderData();
  }

  toggleActionSheet() {
    const modalVisible = this.state.modalVisible;
    this.setState({
      modalVisible: !modalVisible,
    })
  }

  showImagePicker = () => {
    let _that = this;
    _that.setFetchingState(true);

    // More info on all the options is below in the README...just some common use cases shown here
    var options = {
      title: null,
      cancelButtonTitle: translate('Cancel'),
      takePhotoButtonTitle: translate('TakePhoto'),
      chooseFromLibraryButtonTitle: translate('ChooseFromLibrary'),
      customButtons: [
        { name: 'create-folder', title: translate('CreateFolder') },
        { name: 'scan', title: translate('Scan') },
      ],
      storageOptions: {
        skipBackup: true,
        path: 'images'
      },
      mediaType: 'mixed',
    };

    //
    // The first arg is the options object for customization (it can also be null or omitted for default options),
    // The second arg is the callback which sends object: response (more info below in README)
    //
    ImagePicker.showImagePicker(options, (response) => {

      if (response.didCancel) {
        _that.setFetchingState(false);
      }
      else if (response.error) {
        _that.setFetchingState(false);
      }
      else if (response.customButton) {
        const { customButton } = response;
        switch (customButton) {
          case 'create-folder':
            this.setState({ folderCreationModalVisible: true, isLoading: false })
            break;
          case 'scan':
            //this.setState({ folderCreationModalVisible: true, isLoading: false })
            this.props.navigation.navigate('Scan', { folderId: this.state.folderId })
            break;

          default:
            break;
        }

      }
      else {
        let source = response;

        // You can also display the image using data:
        // let source = { uri: 'data:image/jpeg;base64,' + response.data };

        // this.setState({
        //     avatarSource: source
        // });
        const { navigate, state } = this.props.navigation;

        _that.setFetchingState(false);
        navigate('Upload', { source, folderId: _that.state.folderId });

      }
    });

  }

  clearSeletedList = () => {
    this.setState({
      selectedList: []
    });

  }

  clearSeletedList = () => {
    this.setState({
      selectedList: []
    });
  }

  // edit
  toggleEdit = () => {
    const { navigation, toggleEditMode, isEditMode } = this.props;
    navigation.setParams({
      isEditMode: !isEditMode,
    });
    this.clearSeletedList();

    toggleEditMode(!isEditMode);
  }

  setFetchingState = (isLoading) => {
    this.setState({
      isLoading: isLoading,
    });
  }

  //网络请求
  fetchData = () => {
    var that = this;

    that.setState({
      //复制数据源
      isLoading: true,
    });

    const {
      username,
      password,
      sid,
    } = that.state;

    const {
      navigation: { state: { params } },
      fetchingList,
      doneFetchingList,
      chooseFolder,
      renewSid
    } = that.props;

    fetchingList();

    let folder = !!params ? params.node : null;
    let folderId = !!folder ? folder.id : null;
    that._fetchData()
      .then(([folderId, children]) => {
        that.setState({
          isLoading: false,
          dataSource: children,
          folderId
        });
        doneFetchingList();
      })

  }

  _fetchData = () => {
    const that = this;
    const {
      navigation: { state: { params } },
      renewSid
    } = that.props;
    const {
      username,
      password,
      sid,
    } = that.state;

    return new Promise((resolve, reject) => {
      let folder = !!params ? params.node : null;
      let folderId = !!folder ? folder.id : null;
      username && ensureLogin(username, password, sid)
        .then(sid => {
          that.setState({
            sid: sid,
          });
          renewSid(sid);

          listChildren(username, password, sid, folderId, (folderId, children) => {
            resolve([folderId, children]);
          })
            .catch(reason => {
              reject(reason);
            })
        })
        .catch(reason => console.log(reason));
    })
  }

  async filter(text) {
    const {
      username,
      password,
    } = this.state;

    const data = await searchDocuments(username, password, text);

    this.setState({
      searchListDataSource: data,
    })
  }

  //加载等待的view
  renderLoadingView() {
    return (
      <View style={styles.container}>
        <Spinner
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

  renderWarningView(msg) {
    return (
      <View style={styles.container}>
        <Text>
          {msg}
        </Text>
      </View>
    );
  }

  //返回itemView
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
        onPressCheckbox={this._onPressCheckbox.bind(this)}
        onPress={() => this._onPressItem(item)}
        onPressInfo={() => this._onPressItemInfo(item)}
        onPressCross={() => { this._onPressItemCross() }}
      />
    );
  }

  updateProgress = (received, total) => {

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
        }, (error) => {
        });

    _that.props.chooseDocument(null);
    _that.props.updateProgress(0);
  }

  resetDownloadTask = () => {
    const that = this;
    that.setState({
      progress: 0,
      lastTick: Date.now()
    });
    that.props.chooseDocument(null);
    that.props.updateProgress(0);
    that.props.updateDownloadStatus(false);
  }

  previewDocument = (item: any) => {
    const that = this;
    that.props.updateDownloadStatus(true);
    that.setState({
      progress: 0,
      docId: item.id,
    });
    const { sid } = that.state;
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
        that.openLocalUrl(path, fileName, type);
        that.resetDownloadTask();
      })
      .catch((err) => {
        that.resetDownloadTask();
        if (err.message === 'cancelled') return;
        alert('Preview', err.message);
      });

  }

  _onPressItem = (item: any) => {
    const { navigation: { navigate } } = this.props;
    if (item.type === 0 || item.type === 1) {
      if (!!this.downloadTask) {
        // cancel the HTTP request
        this.downloadTask.cancel((err, taskId) => {
          this.resetDownloadTask();
        });
      }
      // choose folder
      navigate('Explorer', { node: item });
    } else {
      // choose document
      this.props.chooseDocument(item);
      this.previewDocument(item);
    }
  }

  _onPressItemInfo = (item: any) => {
    const { navigate } = this.props.navigation;
    navigate('DocumentDetails', { node: item });
  }

  _onPressItemCross = () => {
    this.downloadManger.cancel()
  }

  _onPressCheckbox = (item: any, checked: boolean) => {

    let selectedList = this.state.selectedList;
    if (checked) {
      selectedList.push(item);
    } else {
      selectedList = selectedList.filter((o) => o.id !== item.id);
    }

    this.setState({
      selectedList
    })
  }

  selectionContainsFolders() {
    return this.state.selectedList.filter((o) => o.type == 0 || o.type == 1).length > 0;
  }

  _onDeleteButtonPressed = () => {

    const that = this;
    const { username, password } = that.props;
    const selectedList = that.state.selectedList;
    DocumentService.removeDocuments(username, password, selectedList)
      .then(resp => {
        that.fetchData();
        that.toggleEdit();
      })

  }

  _onDownloadButtonPressed = () => {
    const that = this;
    const { sid } = that.props;
    const selectedList = that.state.selectedList;
    DocumentService.downloadToDocumentDirectory(sid, selectedList)
      .then(resp => { that.toggleEdit(); })
    return;

  }

  _onPrintButtonPressed = () => {
    const that = this;
    const doc = that.state.selectedList[0];
    that.props.updateDownloadStatus(true);
    that.setState({
      //progressBarVisible: true,
      progress: 0,
      docId: doc.id,
    });
    const { fileName, type, fileSize } = doc;
    const { sid, navigation: { navigate } } = that.props;

    DocumentService.downloadToCacheDirectory(sid, doc, that.updateProgress, that.resetDownloadTask)
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
        console.log('The file saved to ', path);

        that.resetDownloadTask();
        that.toggleEdit();

        navigate('Print', { filePath: path, fileType: type, fileName });
      })
      .catch((err) => {
        that.resetDownloadTask();
        that.toggleEdit();
        if (err.message === 'cancelled') return;
        console.log(err);
        // Toast.show(err.message, Toast.SHORT);

        alert('Print', err.message);
      });
  }

  onFolderCreationOK = () => {
    const that = this,
      { folderName, folderId } = that.state,
      { sid } = that.props;


    DocumentService.createFolder(sid, folderId, folderName)
      .then(resp => {
        that.setState({ folderCreationModalVisible: false });
        that.fetchData();
      })
      .catch(reason => {
        alert('Create folder', reason.message);
      })
  }

  _onRefresh = () => {
    const that = this;
    that.setState({ refreshing: true });
    that._fetchData()
      .then(([folderId, children]) => {
        that.setState({
          dataSource: children,
          refreshing: false
        });
      })
      .catch(reason => {
        debugger;
        if (reason.message === 'token expired') {
          loginSOAP(username, password)
            .then(sid => {
              that.setState({ sid }, that._onRefresh())
            })
        }
      })
  }

  renderData() {
    return (
      <View style={{
        flex: 1,
        backgroundColor: 'white'
      }}>
        <SearchBox
          autoCapitalize='none'
          onFocus={() => {
            this.setState({
              mainListVisible: false,
              searchListVisible: true
            })
          }}
          onCancel={() => {
            this.setState({
              mainListVisible: true,
              searchListVisible: false,
              searchListDataSource: [],
            })
          }}
          onSearch={this.filter.bind(this)}
        />
        <DocumentList
          navigation={this.props.navigation}
          dataSource={this.state.dataSource}
          onRefresh={this._onRefresh}
          onPressCheckbox={this._onPressCheckbox}
          onPressItem={this._onPressItem}
          onPressItemInfo={this._onPressItemInfo}
          onPressCross={this._onPressItemCross}
          downloadManger={this.downloadManger}
          style={{
            flex: 1,
            display: this.state.mainListVisible ? 'flex' : 'none'
          }}
        />

        <ScrollView style={{
          flex: 1,
          display: this.state.searchListVisible ? 'flex' : 'none'
        }}
        >
          <FlatList
            data={this.state.searchListDataSource}
            renderItem={this.renderItem}
            keyExtractor={(item, index) => index}
          />
        </ScrollView>
        <ProgressBar
          visible={this.state.progressBarVisible}
          progress={this.state.progress}
        />
        <View
          style={[
            styles.overlay,
            { display: this.props.isEditMode ? 'flex' : 'none' },
          ]}
        >

          <TouchableOpacity
            style={{ marginRight: 20, justifyContent: 'center' }}
            accessibilityLabel='download'
            onPress={this.selectionContainsFolders() ? null : this._onDownloadButtonPressed}
          >
            <Text style={{ color: this.selectionContainsFolders() ? 'grey' : colors.primary, fontSize: 20 }}>{`${translate('Download')}${this.state.selectedList.length > 0 ? '(' + this.state.selectedList.length + ')' : ''}`}</Text>
          </TouchableOpacity>

          <TouchableOpacity
            style={{ marginRight: 20, justifyContent: 'center' }}
            accessibilityLabel='delete'
            onPress={() => { this._onDeleteButtonPressed(); }}
          >
            <Text style={{ color: 'red', fontSize: 20 }}>{`${translate('Delete')}${this.state.selectedList.length > 0 ? '(' + this.state.selectedList.length + ')' : ''}`}</Text>
          </TouchableOpacity>

          <TouchableOpacity
            style={{ marginRight: 20, justifyContent: 'center' }}
            accessibilityLabel='print'
            onPress={this.selectionContainsFolders() ? null : this._onPrintButtonPressed}
          >
            <Text style={{ color: this.selectionContainsFolders() ? 'grey' : colors.primary, fontSize: 20 }}>{`${translate('Print')}${this.state.selectedList.length > 0 ? '(' + this.state.selectedList.length + ')' : ''}`}</Text>
          </TouchableOpacity>
        
        </View>
        <FolderCreationDialog
          onCancel={() => {
            this.setState({ folderName: '', folderCreationModalVisible: false });
          }}
          onOK={this.onFolderCreationOK.bind(this)}
          modalVisible={this.state.folderCreationModalVisible}
          onChangeFolderName={(folderName) => this.setState({ folderName })}
          folderName={this.state.folderName}
        />
      </View>
    );
  }
} // end Explorer class



function select(store) {
  return {
    needReload: store[NAME].document.needReload,
    dataSource: store[NAME].document.dataSource,
    isLoading: store[NAME].document.isLoading,
    isEditMode: store[NAME].document.isEditMode,
    isDownloading: store[NAME].document.isDownloading,

    username: store[NAME].account.username,
    password: store[NAME].account.password,
    sid: store[NAME].account.token.sid,
    expires_date: store[NAME].account.token.expires_date,
    valid: store[NAME].account.valid,
  };
}

function dispatch(dispatch) {
  return {
    // 发送行为
    updateDownloadStatus: (isDownloading) => dispatch(actions.updateDownloadStatus(isDownloading)),
    toggleEditMode: (isEditMode) => dispatch(actions.toggleEditMode(isEditMode)),
    updateProgress: (percent) => dispatch(actions.updateProgress(percent)),
    fetchingList: () => dispatch(actions.fetchingList()),
    doneFetchingList: () => dispatch(actions.doneFetchingList()),
    chooseDocument: (document) => dispatch(actions.chooseDocument(document)),

    validSid: (sid) => dispatch(actions.valid(sid)),
    renewSid: (sid) => dispatch(actions.renew(sid)),
    login: (username, password) => dispatch(actions.login(username, password)),
  }
};

export default connect(select, dispatch)(Explorer);


const styles = StyleSheet.create({
  container: {
    flex: 1,
    flexDirection: 'row',
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
  thumb: {
    width: 50,
    height: 50,
    marginRight: 10
  },
  // title: {
  //   fontSize: 15,
  //   color: 'blue',
  // },
  content: {
    fontSize: 12,
    color: 'black',
  },
  textContainer: {
    flex: 1
  },
  separator: {
    height: 1,
    backgroundColor: '#dddddd'
  },
  price: {
    fontSize: 20,
    fontWeight: 'bold',
    color: '#48BBEC'
  },
  title: {
    fontSize: 15,
    color: '#656565'
  },
  rowContainer: {
    flexDirection: 'row',
    padding: 10
  },
  overlay: {
    bottom: 0,
    left: 0,
    right: 0,
    height: 60,
    width: window.width,
    position: 'absolute',
    backgroundColor: 'gray',
    flex: 1,
    flexDirection: 'row',
    paddingLeft: 10,
  },
  actionSheet: {
    height: 230,
    backgroundColor: 'white',
    borderColor: 'white',
    borderWidth: 1,
    borderRadius: 6,
    marginBottom: 10,
    alignSelf: 'stretch',
    justifyContent: 'center'
  },
  buttonText: {
    color: '#0069d5',
    alignSelf: 'center',
    fontSize: 18
  },
  button: {
    height: 46,
    backgroundColor: 'white',
    borderColor: 'gray',
    borderWidth: 1,
    borderLeftWidth: 0,
    borderRightWidth: 0,
    borderBottomWidth: 0,
    //borderRadius: 6,
    marginBottom: 0,
    alignSelf: 'stretch',
    justifyContent: 'center'
  },
  lastButton: {
    borderBottomWidth: 1,
  }
});
