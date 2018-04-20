/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

import React, { Component } from 'react';
import {
  StyleSheet,
  AsyncStorage
} from 'react-native';
import { connect } from 'react-redux';

import SplashScreen from './login/Splash';
import LoginScreen from './login/LoginScreen';
import AppWithNavigationState from './navigators/AppNavigator';
import SecurityNavigator from './navigators/SecurityNavigator';
import configureAppWithNavigationState from './navigators/AppWithNavigationState';

class App extends Component<{}> {
  constructor(props) {
    super(props);
    this.state = {
      // userChecked: false,
      isLoading: false,
    };

    AsyncStorage
      .setItem('admin', JSON.stringify({ username: 'admin', password: 'admin' }), (error) => {

        if (error == null) {
          this.props.checkUser();

        }
      });


  }

  render() {
    if (!!this.props.userChecking) {
      return <SplashScreen />;
    }

    // var secNav = configureAppWithNavigationState(SecurityNavigator)
    // return <secNav />;

    // if (!this.props.isLoggedIn) {
    //   return <secNav />;
    // }

    return <AppWithNavigationState />;
  }
}

// 获取 state 变化
const mapStateToProps = (state) => {
  return {
    userChecking: state.auth_ext.userChecking,
    isLoggedIn: state.auth.isLoggedIn,
  }
};

// 发送行为
const mapDispatchToProps = (dispatch) => {
  return {
    checkUser: () => { dispatch({ type: 'CHECKING_USER' }); }
  }
};

export default connect(
  mapStateToProps,
  mapDispatchToProps,
  null,
  {
    withRef: true
  }
)(App);

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
});
