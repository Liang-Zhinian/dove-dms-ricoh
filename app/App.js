/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

import React, { Component } from 'react';
import {
    DeviceEventEmitter,
    StyleSheet,
    View,
    Text,
    AsyncStorage,
} from 'react-native';
import { connect } from 'react-redux';

import SOP from './SOP';
import AppWithNavigationState, { AppBeforeLogin } from './navigators';

class App extends Component<{}> {

    constructor(props) {
        super(props);
        this.state = {
        };

        console.log('App did constructed');
    }

    componentDidMount() {
        console.log('App did mount');
        DeviceEventEmitter.addListener('appStateChange', this.handleAppStateChange);
    }

    componentWillUnmount() {
        console.log('App did unmount');
        DeviceEventEmitter.removeListener('appStateChange', this.handleAppStateChange);
    }

    render = () => {
        if (this.props.checkingSOPAuthState) {
            return (
                <View>
                    <Text>Checking SOP Auth State ...</Text>
                    <SOP
                        onLoginStatusReceived={onLoginStatusReceived.bind(this)}
                        onEntryInfoReceived={onEntryInfoReceived.bind(this)}
                    />
                </View>
            );
        }

        if (this.props.isLoggedIn)
            return <AppWithNavigationState />;
        else
            return <AppBeforeLogin />;

        // return <AppWithNavigationState />
    }

    handleAppStateChange = (appState) => {
        console.log(`current state: ${appState.currentAppState}`);

        switch (appState.currentAppState) {
            case 'active':
                break;
            case 'destroyed':

                break;
        }
    }

    onLoginStatusReceived = (loginStatus) => { }

    onEntryInfoReceived = (loggedInUser) => { }
}

// 获取 state 变化
const mapStateToProps = (state) => {
    return {
        isLoggedIn: state.auth.isLoggedIn,
        checkingSOPAuthState: state.auth_ext.userChecking
    }
};

// 发送行为
const mapDispatchToProps = (dispatch) => {
    return {
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
