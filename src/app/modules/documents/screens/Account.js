import React, { Component } from 'react';
import {
  View,
  Text,
  StyleSheet,
  TextInput,
  TouchableOpacity,
  ScrollView,
  Alert,
  Switch,
} from 'react-native';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import { CommonStyles } from '../styles';
import * as actions from '../actions';
import { NAME } from '../constants';
import { HeaderButton } from './components/HeaderButtons';
import { translate } from '../../../i18n/i18n';
import { getItem } from '../../../services/storageService';
import { storageKey } from '../env';
import Form from './components/Form';
import Section from './components/Section';
import KeyValueRow from './components/KeyValueRow';

class Account extends Component {
  static navigationOptions = ({ navigation }) => {
    const { params = {} } = navigation.state;

    let headerTitle = translate('ManageAccount');
    let headerLeft = (
      <View style={[
        CommonStyles.flexRow,
        CommonStyles.m_l_2,
      ]}>
        <HeaderButton
          onPress={params.cancel ? params.cancel : () => null}
          text={translate('Cancel')}
        />
      </View>
    );
    let headerRight = (
      <View style={[
        CommonStyles.flexRow,
      ]}>
        <HeaderButton
          onPress={params.toggleEdit ? params.toggleEdit : () => null}
          text={translate(!params.isEditMode ? 'Edit' : 'Save')}
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
      username: null,
      password: null,
      server: null,
      port: null,
      https: false,
      pending: false,
      isEditMode: false,
    };
  }

  async componentDidMount() {
    var that = this;
    const { navigation } = that.props;
    // We can only set the function after the component has been initialized
    navigation.setParams({
      toggleEdit: that.toggleEdit.bind(that),
      isEditMode: that.state.isEditMode,
      cancel: that.cancel.bind(that),
    });

    const { username, password, sid, valid } = that.props;

    const doc_server = await getItem(storageKey.DOCUMENT_SERVER);

    that.setState({
      username,
      password,
      ...doc_server,
    })
  }

  render() {
    return (
      <Form>
        {this.renderAccountAuthenticationPanel()}
        {this.renderAdvancedPanel()}
      </Form>

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
        placeholder={translate('UserName')}
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
        placeholder={translate('Password')}
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

  renderLogoutButton = () => {
    return (
      <TouchableOpacity
        activeOpacity={StyleConfig.touchable_press_opacity}
        style={[ComponentStyles.btn, { backgroundColor: '#8a8482' }]}
        onPress={() => this.handleLogout()}>
        <Text style={ComponentStyles.btn_text}>
          {translate('SignOut')}
        </Text>
      </TouchableOpacity>
    )
  }

  renderServerAddress = () => {
    return (
      <TextInput
        ref="txtServerAddress"
        style={[ComponentStyles.input]}
        placeholderTextColor={StyleConfig.color_gray}
        placeholder={translate('ServerAddress')}
        blurOnSubmit={true}
        underlineColorAndroid={'transparent'}
        onChangeText={(server) => this.setState({ server })}
        value={this.state.server}
        autoCapitalize='none'
        editable={this.state.isEditMode}
        returnKeyType='next'
      />
    )
  }

  renderHTTPS = () => {
    if (this.state.isEditMode)
      return (
        <Switch
          ref="txtHTTPS"
          value={this.state.https}
          onValueChange={(value) => {
            this.setState({
              https: value
            });
          }} />
      )
    return (
      <TextInput
        ref="txtHTTPS"
        style={[ComponentStyles.input]}
        placeholderTextColor={StyleConfig.color_gray}
        placeholder={translate('HTTPS')}
        blurOnSubmit={true}
        underlineColorAndroid={'transparent'}
        onChangeText={(https) => this.setState({ https })}
        value={translate(this.state.https ? 'On' : 'Off')}
        autoCapitalize='none'
        editable={false}
        returnKeyType='next'
      />

    )
  }

  renderPort = () => {
    return (
      <TextInput
        ref="txtPort"
        style={[ComponentStyles.input]}
        placeholderTextColor={StyleConfig.color_gray}
        placeholder={translate('Port')}
        blurOnSubmit={true}
        underlineColorAndroid={'transparent'}
        onChangeText={(port) => this.setState({ port })}
        value={this.state.port}
        autoCapitalize='none'
        editable={this.state.isEditMode}
        returnKeyType='next'
      />
    )
  }

  renderAccountAuthenticationPanel = () => {
    return (
      <Section title='ACCOUNT AUTHENTICATION'>
        <KeyValueRow title={translate('UserName')}>
          {this.renderUserName()}
        </KeyValueRow>
        {this.renderSpacer()}
        <KeyValueRow title={translate('Password')}>
          {this.renderPassword()}
        </KeyValueRow>
      </Section>
    );
  }

  renderAdvancedPanel = () => {
    return (
      <Section title='ADVANCED'>
        <KeyValueRow title={translate('ServerAddress')}>
          {this.renderServerAddress()}
        </KeyValueRow>
        {this.renderSpacer()}
        <KeyValueRow title={translate('HTTPS')}>
          {this.renderHTTPS()}
        </KeyValueRow>
        {this.renderSpacer()}
        <KeyValueRow title={translate('Port')}>
          {this.renderPort()}
        </KeyValueRow>
      </Section>
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
    let server = this.state.server;
    let https = this.state.https;
    let port = this.state.port;
    return {
      username,
      password,
      server,
      https,
      port
    };
  }

  handleSave = () => {
    let _that = this;
    const accountData = _that.accountValidator();
    if (accountData) {
      _that.refs.txtUserName.blur();
      _that.refs.txtPassword.blur();
      _that.refs.txtServerAddress.blur();
      _that.refs.txtHTTPS.blur();
      _that.refs.txtPort.blur();

      const { saveAccount, login } = _that.props;
      saveAccount(
        accountData.username,
        accountData.password,
        accountData.server,
        accountData.https,
        accountData.port,
      );

      this.setState({ pending: false });
      Alert.alert('Account setting', 'Saved!', [{ text: 'OK', onPress: () => console.log('OK Pressed') },], { cancelable: false });
    }
  }

  handleLogout = () => {
    const { sid, logout, navigation } = this.props;
    logout(sid, navigation);
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
    isLoggedIn: state[NAME].account.isLoggedIn,
    username: state[NAME].account.username,
    password: state[NAME].account.password,
    sid: state[NAME].account.token.sid

    // isLoggedIn: state.auth.isLoggedIn,
    // username: state.auth.username,
    // password: state.auth.password,
    // sid: state.auth.token.sid
  }
};

// 发送行为
const mapDispatchToProps = (dispatch) => {
  return {
    // 发送行为
    login: (username, password) => dispatch(actions.login(username, password)),
    logout: (sid, navigation) => dispatch(actions.logout(sid, navigation)),
    saveAccount: (username, password, server, https, port) => dispatch(actions.saveAccount(username, password, server, https, port)),
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
  spacer: {
    height: StyleSheet.hairlineWidth,
    backgroundColor: 'gray',
    marginLeft: 10,
    marginRight: 10,
  },
})
