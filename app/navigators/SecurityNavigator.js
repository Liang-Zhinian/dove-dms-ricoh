import React, { Component } from 'react';
// import { BackHandler, Platform, DeviceEventEmitter } from 'react-native';
// import PropTypes from 'prop-types';
// import { connect } from 'react-redux';
import {
    StackNavigator,
    // addNavigationHelpers,
} from 'react-navigation';

import SplashScreen from '../login/SplashScreen';
import LoginScreen from '../login/LoginScreen';
import RegistrationScreen from '../login/RegistrationScreen';

export default StackNavigator(
    {
        SplashScreen: { screen: SplashScreen },
        RegistrationScreen: { screen: RegistrationScreen },
        LoginScreen: { screen: LoginScreen },
    },
    {
        headerMode: 'none',
        // initialRouteName: 'SplashScreen',
    });