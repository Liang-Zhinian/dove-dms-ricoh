import React, { Component } from 'react';
import {
  View,
  Text,
  StyleSheet,
  TextInput,
  TouchableOpacity,
  ScrollView,
  Alert
} from 'react-native';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import { CommonStyles } from '../styles';
import * as actions from '../actions';
import { NAME } from '../constants';
import { HeaderButton } from './components/HeaderButtons';

class Account extends Component {
  static navigationOptions = ({ navigation }) => {
    const { params = {} } = navigation.state;

    let headerTitle = 'Manage Account';
    let headerLeft = (
      <View style={[
        CommonStyles.flexRow,
        CommonStyles.m_l_2,
      ]}>
        <HeaderButton
          onPress={params.cancel ? params.cancel : () => null}
          text='Cancel'
        />
      </View>
    );
    let headerRight = (
      <View style={[
        CommonStyles.flexRow,
      ]}>
        <HeaderButton
          onPress={params.toggleEdit ? params.toggleEdit : () => null}
          text={!params.isEditMode ? 'Edit' : 'Save'}
        />
      </View>
    );

    const withCancelButton = { headerLeft, headerTitle, headerRight };
    const withoutCancelButton = { headerTitle, headerRight };

    return params.isEditMode ? withCancelButton : withoutCancelButton;
    // return { headerLeft, headerTitle, headerRight }
  };


  constructor(props) {
    super(props);
    this.state = {
      username: '',
      password: '',
      pending: false,
      isEditMode: false
    };
  }

  componentDidMount() {
    console.log('componentDidMount');
    var that = this;
    const { navigation } = that.props;
    // We can only set the function after the component has been initialized
    navigation.setParams({
      toggleEdit: that.toggleEdit.bind(that),
      isEditMode: that.state.isEditMode,
      cancel: that.cancel.bind(that),
    });

    const { username, password, sid, valid } = that.props;
    that.setState({
      username,
      password
    })
  }

  render() {
    return (
      //<View style={styles.container}>
      <ScrollView
        contentContainerStyle={{
          justifyContent: 'center',
          //alignItems: 'flex-start',
        }}
        style={styles.container}
        keyboardShouldPersistTaps={'always'}>
        <View style={{
          flex: 1,
          marginTop: 20,
          marginRight: 0,
          marginBottom: 0,
          //marginLeft: 10,
        }}>
          <Text style={{ marginLeft: 10, fontSize: 14, color: 'grey' }}>ACCOUNT AUTHENTICATION</Text>
          {this.renderFormPanel()}
        </View>
        {this.renderPending()}
      </ScrollView>
      //</View>

    );
  }

  // edit
  toggleEdit = () => {
    const { navigation } = this.props;
    const isEditMode = this.state.isEditMode;

    if (isEditMode) {
      this.handleSave();
    } else {
      this.refs.txtUserName.focus();
      this.refs.txtUserName.selection = { start: 3, end: 3 };
      //Alert.alert('UserName focused', 'Focused!', [{ text: 'OK', onPress: () => console.log('OK Pressed') },], { cancelable: false });
      
    }

    this.setState({
      isEditMode: !isEditMode
    });
    navigation.setParams({
      isEditMode: !isEditMode
    });
  }

  cancel = () => {
    const { navigation } = this.props;

    this.setState({
      isEditMode: false
    });
    navigation.setParams({
      isEditMode: false
    });
  }

  encryptData = (data) => {
    let encrypt = new JSEncrypt({
      default_key_size: 1024,
      default_public_exponent: '010001'
    });
    encrypt.setPublicKey(authData.pubKey);
    return encrypt.encrypt(data);
  }

  renderUserName = () => {
    return (
      <TextInput
        ref="txtUserName"
        style={[ComponentStyles.input]}
        placeholderTextColor={StyleConfig.color_gray}
        placeholder={'User name'}
        blurOnSubmit={true}
        underlineColorAndroid={'transparent'}
        onChangeText={(username) => this.setState({ username })}
        value={this.state.username}
        autoCapitalize='none'
        editable={this.state.isEditMode}
        returnKeyType='next'
      />
    )
  }

  renderPassword = () => {
    return (
      <TextInput
        ref="txtPassword"
        style={[ComponentStyles.input]}
        placeholderTextColor={StyleConfig.color_gray}
        placeholder={'Password'}
        blurOnSubmit={true}
        underlineColorAndroid={'transparent'}
        onChangeText={(password) => this.setState({ password })}
        value={this.state.password}
        autoCapitalize='none'
        editable={this.state.isEditMode}
      /* secureTextEntry={true} */
      />
    )
  }

  renderSessionId = () => {
    return (
      <View style={[ComponentStyles.input_control]}>
        <TextInput
          ref="txtSid"
          style={[ComponentStyles.input]}
          placeholderTextColor={StyleConfig.color_gray}
          placeholder={'Session Id'}
          blurOnSubmit={true}
          underlineColorAndroid={'transparent'}
          value={this.props.sid} />
      </View>
    )
  }

  renderSaveButton = () => {
    return (
      <TouchableOpacity
        activeOpacity={StyleConfig.touchable_press_opacity}
        style={[ComponentStyles.btn, { backgroundColor: '#8a8482' }]}
        onPress={() => this.handleSave()}>
        <Text style={ComponentStyles.btn_text}>
          Save
        </Text>
      </TouchableOpacity>
    )
  }
  renderFormPanel = () => {
    return (
      <View style={[styles.section]}>
        <View style={[{ flex: 1 }, styles.row]}>
          <Text style={[styles.title]}>Username</Text>
          <View style={[styles.content]}>{this.renderUserName()}</View>
        </View>
        {this.renderSpacer()}
        <View style={[{ flex: 1 }, styles.row]}>
          <Text style={[styles.title]}>Password</Text>
          <View style={[styles.content]}>{this.renderPassword()}</View>
        </View>
        {/*this.renderSpacer()*/}
      </View>
    );
  }

  renderSpacer() {
    return (
      <View style={styles.spacer}></View>
    )
  }

  renderButtons = () => {
    return (
      <View style={[CommonStyles.flexRow, CommonStyles.flexItemsMiddle, CommonStyles.flexItemsBetween, CommonStyles.m_t_4]}>
        {this.renderSaveButton()}
      </View>
    )
  }

  accountValidator = () => {
    let username = this.state.username;
    let password = this.state.password;
    //username = this.encryptData(username);
    //password = this.encryptData(password);
    return {
      username,
      password
    };
  }

  handleSave = () => {
    let _that = this;
    const accountData = _that.accountValidator();
    if (accountData) {
      _that.refs.txtUserName.blur();
      _that.refs.txtPassword.blur();
      // _that.setState({ pending: true });

      const { saveAccount, login } = _that.props;
      login(
        accountData.username,
        accountData.password
      );

      this.setState({ pending: false });
      Alert.alert('Account setting', 'Saved!', [{ text: 'OK', onPress: () => console.log('OK Pressed') },], { cancelable: false });
      // _that.setState({ sid: sid });

      // _that.props.login({
      //   username: accountData.username,
      //   password: accountData.password,
      //   resolved: (data)=>{
      //       // this.handleLoginResolved(data);
      //       _that.setState({ sid: data });
      //   },
      //   rejected: (data)=>{
      //     _that.handleLoginRejected(data);
      //   }
      // });


      // let data = {};
      // data.username = accountData.username;
      // data.password = accountData.password;
      // _that.handleAccountResolved(data);
    }
  }

  handleAccountResolved = (data) => {
    let backup = ConfigAction.getConfig(storageKey.USER_TOKEN);
    if (!!backup) {
      ConfigAction.updateConfig(storageKey.USER_TOKEN, data);
    } else {
      ConfigAction.setConfig(storageKey.USER_TOKEN, data);
    }

    this.setState({ pending: false });
    Alert.alert('Account setting', 'Saved!', [{ text: 'OK', onPress: () => console.log('OK Pressed') },], { cancelable: false });
  }

  handleAccountRejected = (data) => {
    this.setState({ pending: false });
    Alert.alert('Account setting', 'Failed!', [{ text: 'OK', onPress: () => console.log('OK Pressed') },], { cancelable: false });
  }

  renderPending = (data) => {
    if (this.state.pending === true) {
      return (
        <Spinner style={ComponentStyles.pending_container} />
      )
    }
  }
}

// 获取 state 变化
const mapStateToProps = (state) => {
  return {
    // 获取 state 变化
    isLoggedIn: state.auth.isLoggedIn,
    username: state.auth.user.username,
    password: state.auth.user.password,
    sid: state.auth.user.token.sid
  }
};

// 发送行为
const mapDispatchToProps = (dispatch) => {
  return {
    // 发送行为
    login: (username, password) => dispatch(actions.login(username, password)),
    saveAccount: (username, password) => dispatch(actions.saveAccount(username, password)),
  }
};

export default connect(
  mapStateToProps,
  mapDispatchToProps,
  null,
  {
    withRef: true
  }
)(Account);

const styles = StyleSheet.create({
  container: {
    flex: 1,
    flexDirection: 'column',
    // justifyContent: 'center',
    // alignItems: 'flex-start',
    // width: null,
    // height: null,
    backgroundColor: '#F5FCFF',
  },
  section: {
    flex: 1,
    flexDirection: 'column',
    marginTop: 10,
    marginRight: 0,
    marginBottom: 20,
    marginLeft: 0,
    borderTopWidth: 1,
    borderTopColor: 'gray',
    borderBottomWidth: 1,
    borderBottomColor: 'gray',
  },
  row: {
    flexDirection: 'row',
    padding: 10,
    // marginRight: 10,
    // marginLeft: 10,
    backgroundColor: '#ffffff',
    // borderBottomWidth: 1,
    // borderBottomColor: 'gray',
    justifyContent: 'space-between',
    alignItems: 'center',
  },
  title: {
    flex: 1,
    // marginRight: 10,
    fontWeight: 'bold',
    textAlign: 'right',
    fontSize: 17,
  },
  content: {
    flex: 1,
    marginLeft: 10,
  },
  spacer: {
    height: 1,
    backgroundColor: 'gray',
    marginLeft: 10,
    marginRight: 10,
  },
})
