import React, { Component } from 'react';
import { BackHandler, Platform, DeviceEventEmitter } from 'react-native';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import {
    StackNavigator,
    addNavigationHelpers,
} from 'react-navigation';
import { addListener } from '../utils/redux';


import SecurityNavigator from './SecurityNavigator';
import { BottomTabs } from './navigation';
import MainScreen from '../screens/MainScreen';
import SplashScreen from '../login/SplashScreen';
import LoginScreen from '../login/LoginScreen';
import RegistrationScreen from '../login/RegistrationScreen';

var routesConfig = {
    // Login: { screen: SecurityNavigator },
    Splash: { screen: SplashScreen },
    Signup: { screen: RegistrationScreen },
    Login: { screen: LoginScreen },
    Main: {
        screen: BottomTabs
    },
};

export const AppNavigator = StackNavigator(
    routesConfig, {
        headerMode: 'none',
        initialRouteName: 'Main'
    }
); //naviagtor for after login

export const AppBeforeLogin = StackNavigator(routesConfig); //naviagtor for before login

class AppWithNavigationState extends Component {
    static propTypes = {
        dispatch: PropTypes.func.isRequired,
        nav: PropTypes.object.isRequired,
    };

    componentDidMount() {
        if (Platform.OS === 'android') {
            // DeviceEventEmitter.addListener('appStateChange', this.handleAppStateChange);
            BackHandler.addEventListener("hardwareBackPress", this.onBackPress);
        }
    }

    componentWillUnmount() {
        if (Platform.OS === 'android') {
            // DeviceEventEmitter.removeListener('appStateChange', this.handleAppStateChange);
            BackHandler.removeEventListener("hardwareBackPress", this.onBackPress);
        }
    }

    render() {
        const { dispatch, nav } = this.props;
        return (
            <AppNavigator navigation={addNavigationHelpers({
                dispatch,
                state: nav,
                addListener,
            })} />

        );
    }

    onBackPress = () => {
        const { dispatch, nav } = this.props;
        if (this.isRootScreen(nav)) {
            BackHandler.exitApp();
            return;
        }

        // dispatch(ReactNavigation.NavigationActions.back());
        dispatch({ type: 'Navigation/BACK' })

        return true;
    }

    isRootScreen(navigator) {
        if (typeof navigator.index == 'undefined') return true;

        let isCurrentRoot = navigator.index == 0;

        if (navigator.routes && navigator.routes.length > 0) {
            let allChildAreRoots = true;

            navigator.routes.map((r) => {
                if (allChildAreRoots) {
                    if (!this.isRootScreen(r)) {
                        allChildAreRoots = false;
                    }
                }
            });

            return allChildAreRoots && isCurrentRoot;
        }

        return isCurrentRoot;
    }
}

function mapStateToProps(state) {
    return {
        nav: state.nav,
    };
}

export default connect(mapStateToProps)(AppWithNavigationState);
