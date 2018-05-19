/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

import React, { Component } from 'react';
import {
    DeviceEventEmitter,
    AsyncStorage,
    View,
} from 'react-native';
import { connect } from 'react-redux';

import RicohAuthAndroid from './components/RCTRicohAuthAndroid';
import Toast from './components/ToastModule';

class SOP extends Component {
    constructor(props) {
        super(props)
        this.state = {
            isLoading: false,
            loginStatus: null,
        }
        this._isMounted = false;
        this.sopEnabled = true;

    }

    componentWillMount() {
        var that = this;

        DeviceEventEmitter.addListener('onLoginStatusReceived', function (e) {
            Toast.show(e.loginStatus, Toast.SHORT);
            that.props.onLoginStatusReceived && that.props.onLoginStatusReceived(e.loginStatus);
        });

        DeviceEventEmitter.addListener('onEntryInfoReceived', function (e) {
            console.log('onEntryInfoReceived');
            console.log(e.entryInfo);
            that.props.onEntryInfoReceived && that.props.onEntryInfoReceived(JSON.parse(e.entryInfo));
        });

        that.getAuthStateAsync();

    }

    componentDidMount() {
        console.log('componentDidMount');
        this._isMounted = true;
    }

    componentWillUnmount() {
        DeviceEventEmitter.removeListener('onLoginStatusReceived');
        DeviceEventEmitter.removeListener('onEntryInfoReceived');

        this._isMounted = false;
    }

    render() {
        return null;
    }

    getAuthStateAsync = async () => {
        var that = this;

        that.props.checkingUser();
        that.setState({ isLoading: true });

        async function getAuthState() {
            if (that.sopEnabled && RicohAuthAndroid) {
                await RicohAuthAndroid.getAuthState()
                    .then((msg) => {
                        console.log('success!!')
                    }, (error) => {
                        console.log('error!!')
                    });
            } else {
                // for testing
                await AsyncStorage
                    .getItem('jackl')
                    .then(data => {
                        if (data) {
                            let user = JSON.parse(data);
                            console.log('user found in storage: ' + user.username);
                        } else {
                            console.log('user not found in storage');
                        }

                    });
            }
            setTimeout(() => { that.props.doneCheckingUser(); }, 3000); // 模拟

        }

        try {
            await Promise.race([
                getAuthState(),
                timeout(15000),
            ]);
        } catch (e) {
            const message = e.message || e;
            if (message !== 'Timed out' && message !== 'Canceled by user') {
                alert(message);
                console.warn(e);
            }
            return;
        } finally {
            that._isMounted && that.setState({ isLoading: false });
        }

    }
};

async function timeout(ms: number): Promise {
    return new Promise((resolve, reject) => {
        setTimeout(() => reject(new Error('Timed out')), ms);
    });
}

const mapStateToProps = (state) => {
    return {
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        checkingUser: () => { dispatch({ type: 'CHECKING_USER' }); },
        doneCheckingUser: () => { dispatch({ type: 'DONE_CHECKING_USER' }); }
    }
};

export default connect(
    mapStateToProps,
    mapDispatchToProps,
    null,
    {
        withRef: true
    }
)(SOP);
