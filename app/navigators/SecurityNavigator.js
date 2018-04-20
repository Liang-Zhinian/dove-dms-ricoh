import React, { Component } from 'react';
// import { BackHandler, Platform, DeviceEventEmitter } from 'react-native';
// import PropTypes from 'prop-types';
// import { connect } from 'react-redux';
import {
    StackNavigator,
    // addNavigationHelpers,
} from 'react-navigation';

import SplashScreen from '../security/SplashScreen';
import LoginScreen from '../security/LoginScreen';
import RegistrationScreen from '../security/RegistrationScreen';

export default StackNavigator(
    {
        SplashScreen: { screen: SplashScreen },
        LoginScreen: { screen: LoginScreen },
        RegistrationScreen: { screen: RegistrationScreen },
    },
    {
        headerMode: 'none',
        // initialRouteName: 'SplashScreen',
    });