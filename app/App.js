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
    NetInfo
} from 'react-native';
import { connect } from 'react-redux';

import SOPApp from './SOPApp';
import AppWithNavigationState, { AppBeforeLogin } from './navigators';

class App extends Component<{}> {

    constructor(props) {
        super(props);
        this.state = {
            isConnected: false,
            isLoading: true,
        };

        console.log('App did constructed');
    }

    componentDidMount() {
        console.log('App did mount');
        var that = this;
        DeviceEventEmitter.addListener('appStateChange', that.handleAppStateChange);

        NetInfo.isConnected.addEventListener(
            'connectionChange',
            that.handleConnectivityChange
        );
        that.fetchNetStatus();
    }

    componentWillUnmount() {
        console.log('App did unmount');
        DeviceEventEmitter.removeListener('appStateChange', this.handleAppStateChange);
        DeviceEventEmitter.removeListener('connectionChange', this.handleConnectivityChange);
    }

    render = () => {

        if (!this.state.isConnected) {
            return (
                <View style={{ flex: 1 }}>
                    <Text>Please check your network settings.</Text>
                </View>
            );
        }

        return (
            <SOPApp>
                {
                    this.props.isLoggedIn ?
                        <AppWithNavigationState />
                        : <AppBeforeLogin />
                }
            </SOPApp>
        )
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

    handleConnectivityChange(isConnected) {
        this.setState({ isConnected });
    }

    fetchNetStatus = () => {
        this.setState({ isLoading: true });
        NetInfo.isConnected.fetch().done(
            (isConnected) => { this.setState({ isConnected }); }
        );
    }
}

// 获取 state 变化
const mapStateToProps = (state) => {
    return {
        isLoggedIn: state.auth.isLoggedIn,
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
