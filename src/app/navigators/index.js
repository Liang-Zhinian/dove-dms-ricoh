import React, { Component } from 'react';
import { BackHandler, Platform, DeviceEventEmitter } from 'react-native';
import PropTypes from 'prop-types';
import {
  StackNavigator,
  addNavigationHelpers
} from 'react-navigation';

import Splash from '../screens/Splash';
import Login from '../screens/Login';
import Profile from '../screens/Profile';
import Signup from '../screens/Signup';
import Main from '../screens/Main';
import Log from '../screens/Log';


export const SignedOut = StackNavigator({
  Splash: {
    screen: Splash,
  },
  Login: {
    screen: Login,
  },
  Signup: {
    screen: Signup,
    navigationOptions: {
      title: 'Sign Up'
    }
  }
}, {
    headerMode: 'none',
  });

export const SignedIn = StackNavigator(
  {
    Main: {
      screen: Main,
    },
    Log: {
      screen: Log,
    }
  }, {
    mode: 'card',
    headerMode: 'none',
  }
);

export const createRootNavigator = (signedIn = false) => {
  return StackNavigator(
    {
      Splash: {
        screen: Splash,
      },
      SignedIn: {
        screen: SignedIn,
        navigationOptions: {
          gesturesEnabled: false
        }
      },
      SignedOut: {
        screen: SignedOut,
        navigationOptions: {
          gesturesEnabled: false
        }
      },
    },
    {
      mode: 'modal',
      headerMode: 'none',
      initialRouteName: signedIn ? 'SignedIn' : 'SignedOut'
    }
  );
};

