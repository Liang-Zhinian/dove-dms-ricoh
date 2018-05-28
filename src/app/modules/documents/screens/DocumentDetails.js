import React, { Component } from 'react';
import {
  View,
  Text,
  StyleSheet,
  Image,
  ActivityIndicator,
  ScrollView,
  Button,
  TextInput
} from 'react-native';
import { connect } from 'react-redux'
import moment from 'moment'
import { StyleConfig, ComponentStyles, CommonStyles } from '../styles'
import { NAME } from '../constants'
import * as actions from '../actions';
import Form from './components/Form';
import Section from './components/Section';
import KeyValueRow from './components/KeyValueRow';
import { translate } from '../../../i18n/i18n';
import { HeaderButton } from './components/HeaderButtons';
import configureNavigationOptions from './components/EditableNavigationOptions';

function textInput({ props, state }) {
  return <TextInput
    style={[ComponentStyles.input]}
    placeholderTextColor={StyleConfig.color_gray}
    blurOnSubmit={true}
    underlineColorAndroid={'transparent'}
    onChangeText={props.onChangeText}
    autoCapitalize='none'
    editable={state.isEditMode}
    returnKeyType='next'
  />
}

class DocumentDetails extends Component {

  static defaultProps = {
    data: {},
  };


  static navigationOptions = configureNavigationOptions();

  constructor(props) {
    super(props);
    this.state = {
      workflowMessage: '',
      isEditMode: false,
    }

    this.bootstrap();
  }

  bootstrap() {
    var that = this;
    const { navigation } = that.props;
    // We can only set the function after the component has been initialized

    let headerTitle = 'Document';

    if (typeof navigation.state.params !== 'undefined') {
      this.data = Object.assign({}, navigation.state.params.node);
      headerTitle = this.data.name || this.data.fileName;
    }

    navigation.setParams({
      toggleEdit: that.toggleEdit.bind(that),
      isEditMode: that.state.isEditMode,
      cancel: that.cancel.bind(that),
      title: headerTitle,
      isEditable: this.data.type !== 1
    });

  }

  componentDidMount() {

    this.setState({ ...this.data }, () => {
    });
  }

  render() {
    return (
      <Form>
        {this.renderGeneralSection()}
        {this.renderDetailsSection()}
        {!this.isFolder() && this.renderWorkflowSection()}
      </Form >
    )
  }

  isFolder = () => {
    return [0, 1].indexOf(this.data.type) >= 0;
  }

  // edit
  toggleEdit = () => {
    const { navigation } = this.props;
    const isEditMode = this.state.isEditMode;

    if (isEditMode) {
      this.handleSave();
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

  handleSave = () => {
    const { username, password, updateDocument, updateFolder } = this.props;

    if (!this.isFolder()) {

      updateDocument(username, password, this.data);
    } else {
      updateFolder(username, password, this.data);
    }
  }

  renderNameInput = () => {
    return (
      <TextInput
        ref="txtName"
        style={[ComponentStyles.input]}
        placeholderTextColor={StyleConfig.color_gray}
        placeholder={translate('Name')}
        blurOnSubmit={true}
        // underlineColorAndroid={'transparent'}
        onChangeText={(name) => {
          if (this.isFolder()) {
            this.setState({ name })
            this.data.name = name;
          } else {
            this.setState({ fileName: name })
            this.data.fileName = name;
          }
        }}
        value={this.isFolder() ? this.state.name : this.state.fileName}
        autoCapitalize='none'
        editable={this.state.isEditMode}
        returnKeyType='next'
      />
    )
  }

  renderLanguageInput = () => {
    return (
      <TextInput
        ref="txtLanguage"
        style={[ComponentStyles.input]}
        placeholderTextColor={StyleConfig.color_gray}
        placeholder={translate('Language')}
        blurOnSubmit={true}
        underlineColorAndroid={'transparent'}
        onChangeText={(language) => this.setState({ language })}
        value={this.state.language}
        autoCapitalize='none'
        editable={this.state.isEditMode}
        returnKeyType='next'
      />
    )
  }

  renderTagsInput = () => {
    return (
      <TextInput
        ref="txtTags"
        style={[ComponentStyles.input]}
        placeholderTextColor={StyleConfig.color_gray}
        placeholder={translate('Tags')}
        blurOnSubmit={true}
        underlineColorAndroid={'transparent'}
        onChangeText={(tags) => {
          this.setState({ tags: tags.split(',') })
          this.data.tags = tags.split(',');
        }}
        value={this.state.tags && this.state.tags.join(',')}
        autoCapitalize='none'
        editable={this.state.isEditMode}
        returnKeyType='next'
      />
    )
  }

  renderRatingInput = () => {
    return (
      <TextInput
        ref="txtRating"
        style={[ComponentStyles.input]}
        placeholderTextColor={StyleConfig.color_gray}
        placeholder={translate('Rating')}
        blurOnSubmit={true}
        underlineColorAndroid={'transparent'}
        onChangeText={(rating) => this.setState({ rating })}
        value={this.state.rating}
        autoCapitalize='none'
        editable={this.state.isEditMode}
        returnKeyType='next'
      />
    )
  }

  renderCommentInput = () => {
    return (
      <TextInput
        ref="txtComment"
        style={[ComponentStyles.input]}
        placeholderTextColor={StyleConfig.color_gray}
        placeholder={translate('VersionComment')}
        blurOnSubmit={true}
        underlineColorAndroid={'transparent'}
        onChangeText={(comment) => {
          this.setState({ comment })
          this.data.comment = comment;
        }}
        value={this.state.comment}
        autoCapitalize='none'
        editable={this.state.isEditMode}
        returnKeyType='next'
      />
    )
  }

  renderDescriptionInput = () => {
    return (
      <TextInput
        ref="txtDescription"
        style={[ComponentStyles.input]}
        placeholderTextColor={StyleConfig.color_gray}
        placeholder={translate('Description')}
        blurOnSubmit={true}
        underlineColorAndroid={'transparent'}
        onChangeText={(description) => {
          this.setState({ description })
          this.data.description = description;
        }}
        value={this.state.description}
        autoCapitalize='none'
        editable={this.state.isEditMode}
        returnKeyType='next'
      />
    )
  }

  renderGeneralSection() {
    return (
      <Section title='General'>
        <KeyValueRow title={translate('Name')}>
          {
            this.state.isEditMode ? this.renderNameInput() :
              <Text style={styles.content}>
                {this.state.name || this.state.fileName}
              </Text>
          }
        </KeyValueRow>
        <KeyValueRow title={translate('Creator')}>
          <Text style={styles.content}>
            {this.state.creator}
          </Text>
        </KeyValueRow>
        <KeyValueRow title={translate('CreationDate')}>
          <Text style={styles.content}>
            {moment(this.state.creation, 'YYYY-MM-DD HH:mm:ss.sss ZZ').format('YYYY-MM-DD')}
          </Text>
        </KeyValueRow>
        <KeyValueRow title={translate('Modifier')}>
          <Text style={styles.content}>
            {this.state.creator}
          </Text>
        </KeyValueRow>
        <KeyValueRow title={translate('ModificationDate')}>
          <Text style={styles.content}>
            {moment(this.state.lastModified, 'YYYY-MM-DD HH:mm:ss.sss ZZ').format('YYYY-MM-DD HH:mm:ss')}
          </Text>
        </KeyValueRow>
        {
          !this.isFolder() &&
          <KeyValueRow title={translate('Version')}>
            <Text style={styles.content}>
              {this.state.version}
            </Text>
          </KeyValueRow>
        }
      </Section>
    );
  }

  renderDetailsSection() {
    return (
      <Section title='Details'>
        <KeyValueRow title={translate('Tags')}>
          {
            this.state.isEditMode ? this.renderTagsInput() :
              <Text style={styles.content}>
                {this.state.tags && this.state.tags.join(',')}
              </Text>
          }
        </KeyValueRow>
        {/*
        <KeyValueRow title={translate('Rating')}>
          {
            this.state.isEditMode ? this.renderRatingInput() :
              <Text style={styles.content}>
                {item.rating}
              </Text>
          }
        </KeyValueRow>
        <KeyValueRow title={translate('Language')}>
          {
            this.state.isEditMode ? this.renderLanguageInput() :
              <Text style={styles.content}>
                {item.language}
              </Text>
          }
        </KeyValueRow>*/}
        {
          !this.isFolder() &&
          <KeyValueRow title={translate('VersionComment')}>
            {
              this.state.isEditMode ? this.renderCommentInput() :
                <Text style={styles.content}>
                  {this.state.comment}
                </Text>
            }
          </KeyValueRow>
        }

        {
          this.isFolder() &&
          <KeyValueRow title={translate('Description')}>
            {
              this.state.isEditMode ? this.renderDescriptionInput() :
                <Text style={styles.content}>
                  {this.state.description}
                </Text>
            }
          </KeyValueRow>
        }
      </Section>
    );
  }

  renderWorkflowSection() {
    return (

      <Section title='Workflow'>
        <View style={[{ flex: 1 }, styles.row]}>
          <Button
            onPress={this.startWorkflow.bind(this)}
            title={translate("Submit")} />

          <Text style={{ fontSize: 14 }}>{this.state.workflowMessage}</Text>
        </View>
      </Section >
    );
  }

  startWorkflow() {
    const that = this;
    const { navigation, username, password } = this.props;
    const item = navigation.state.params.node;

    let apiUrl = 'http://isd4u.com/dmsadmin/index.php';
    var options = {
      method: 'POST',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
      }
    };


    fetch(apiUrl + '?user/loginSubmit&name=' + username + '&password=' + password + '&language=en&action=api', options)
      .then(response => response.json())
      .then(responseJson => {
        return responseJson.return;
      })
      .then(sid => {
        fetch(apiUrl + '?workflow/getworkflowlist&tenantid=1', options)
          .then(response => response.json())
          .then(appList => {
            return appList[0].id;
          })
          .then(appId => {
            fetch(apiUrl + '?workflow/startworkfolw&startuser=' + username + '&workflowid=' + appId + '&docid=' + item.id, options)
              .then(response => response.json())
              .then(responseJson => {
                that.setState({ workflowMessage: responseJson.code + ' - ' + responseJson.msg });
              })
          })
      })

  }

}

function select(store) {
  return {
    username: store[NAME].account.username,
    password: store[NAME].account.password,
  };
}

function dispatch(dispatch) {
  return {
    // 发送行为
    updateDocument: (username, password, document) => dispatch(actions.updateDocument(username, password, document)),
    updateFolder:(username, password, folder) => dispatch(actions.updateFolder(username, password, folder)),
  }
};

export default connect(select, dispatch)(DocumentDetails);

const styles = StyleSheet.create({
  container: {
    flex: 1,
    flexDirection: 'column',
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
    backgroundColor: '#ffffff',
    justifyContent: 'space-between',
    alignItems: 'center',
  },
  title: {
    flex: 1,
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
});
