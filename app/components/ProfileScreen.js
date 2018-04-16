import React from 'react';
import { StyleSheet, Text, View, ScrollView } from 'react-native';
import { connect } from 'react-redux';
import { getUserByUsername } from '../actions/security';
import DoveButton from './DoveButton';

class ProfileScreen extends React.Component {
  static navigationOptions = {
    title: 'Profile',
  };

  constructor(props) {
    super(props);
    this.state = {
      isLoading: true,
      user: null
    }
  }

  componentDidMount() {
    const { sid, username, getUserByUsername } = this.props;
    getUserByUsername(sid, username);
  }

  componentWillReceiveProps(nextProps) {
    const { userProfile, error } = nextProps;
    if (typeof userProfile != 'undefined' && userProfile) {
      console.log('userProfile returns');
      this.setState({ isLoading: false });
    }

    if (error) {
      console.log('error returns');
      this.setState({ isLoading: false });
    }
  }

  render = () => {
    const { userProfile, error } = this.props;

    if (error) {
      return this.renderMessage(error);
    }

    if (this.state.isLoading) {
      return this.renderMessage('Please wait ...');
    }

    return (<ScrollView style={{
      flex: 1,
      top: 0,
      bottom: 0,
      left: 0,
      right: 0,
    }}>
      <View style={{ flex: 1, marginTop: 50, alignItems: 'center' }}>
        <Text style={[styles.title, { fontSize: 20 }]}>User Profile</Text>
      </View>
      <View style={[styles.section]}>
        <View style={[{ flex: 1 }, styles.row]}>
          <Text style={[styles.title]}>User name</Text>
          <Text style={[styles.content]}>
            {`${userProfile.firstName} ${userProfile.name}`}
          </Text>
        </View>
        {this.renderSpacer()}
        <View style={[{ flex: 1 }, styles.row]}>
          <Text style={[styles.title]}>Email</Text>
          <Text style={[styles.content]}>
            {userProfile.email}
          </Text>
        </View>
      </View>
    </ScrollView>);
  }

  renderSpacer() {
    return (
      <View style={styles.spacer}></View>
    )
  }

  renderMessage(message) {

    return (
      <View style={{
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
        backgroundColor: '#F5FCFF',
      }}>
        <Text style={{
          fontSize: 20,
          textAlign: 'center',
          margin: 10,
        }}>
          {message}
        </Text>
        {this.props.error && <DoveButton
            caption="Click to reload"
            onPress={() => {
              this.setState({ isLoading: true });
              const { sid, username, getUserByUsername } = this.props;
              getUserByUsername(sid, username);
            }}
          />
        }
      </View>
    )
  }
}

function select(store) {
  return {
    username: store.auth.user.username,
    sid: store.auth.user.token.sid,
    userProfile: store.security.user,
    error: store.security.error,
  };
}

function dispatch(dispatch) {
  return {
    getUserByUsername: (sid, username) => { dispatch(getUserByUsername(sid, username)) }
  }
};

export default connect(select, dispatch)(ProfileScreen);


const styles = StyleSheet.create({
  container: {
    flex: 1,
    flexDirection: 'row',
    // justifyContent: 'center',
    // alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
  section: {
    flex: 1,
    flexDirection: 'column',
    marginTop: 20,
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
    // justifyContent: 'flex-start',
    // alignItems: 'center',
  },
  title: {
    flex: 1,
    marginRight: 10,
    fontWeight: 'bold',
  },
  content: {
    flex: 3,
    marginRight: 10,
    // textAlign: 'left',
  },
  spacer: {
    height: 1,
    backgroundColor: 'gray',
    marginLeft: 10,
    marginRight: 10,
  },
});
