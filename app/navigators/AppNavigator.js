import React, { Component } from 'react';
import { BackHandler, Platform, DeviceEventEmitter } from 'react-native';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import {
    StackNavigator,
    addNavigationHelpers,
} from 'react-navigation';

import AuthLoadingScreen from '../components/AuthLoadingScreen';
import LoginScreen from '../login/LoginScreen';
import MainScreen from '../components/MainScreen';
import ProfileScreen from '../components/ProfileScreen';
import { addListener } from '../utils/redux';

import { BottomTabs } from './navigation';
import Splash from '../components/Splash';

export const AppNavigator = StackNavigator(
    {
        // AuthLoading: { screen: AuthLoadingScreen },
        Login: { screen: LoginScreen },
        Main: { screen: BottomTabs },
        Profile: { screen: ProfileScreen },
    },
    {
        headerMode: 'none',
        initialRouteName: 'Main',
    });



class AppWithNavigationState extends Component {
    static propTypes = {
        dispatch: PropTypes.func.isRequired,
        nav: PropTypes.object.isRequired,
    };

    componentDidMount() {
        if (Platform.OS === 'android') {
            DeviceEventEmitter.addListener('appStateChange', this.handleAppStateChange);
            BackHandler.addEventListener("hardwareBackPress", this.onBackPress);
        }
    }

    componentWillUnmount() {
        if (Platform.OS === 'android') {
            DeviceEventEmitter.removeListener('appStateChange', this.handleAppStateChange);
            BackHandler.removeEventListener("hardwareBackPress", this.onBackPress);
        }
    }

    render() {
        if (!this.props.isLoggedIn) {
            return <Splash />;
        }

        const { dispatch, nav } = this.props;
        return (
            <AppNavigator navigation={addNavigationHelpers({
                dispatch,
                state: nav,
                addListener,
            })} />

        );
    }

    handleAppStateChange = (appState) => {
        console.log(`current state: ${appState.currentAppState}`);
        if (appState.currentAppState === 'active') {

        }
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
        isLoggedIn: state.auth.isLoggedIn,
        nav: state.nav,
    };
}

export default connect(mapStateToProps)(AppWithNavigationState);
