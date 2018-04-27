/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

import React, { Component } from 'react';
import {
    StyleSheet,
    DeviceEventEmitter,
    AsyncStorage,
    View,
    ActivityIndicator,
} from 'react-native';
import { connect } from 'react-redux';

import RicohAuthAndroid from './components/RCTRicohAuthAndroid';
import Spinner from './components/Spinner';

const styles = StyleSheet.create({
    container: {
        flex: 1,
        flexDirection: 'row',
        justifyContent: 'center',
        alignItems: 'center',
        backgroundColor: '#F5FCFF',
    },
});

class SOPApp extends Component {
    constructor(props) {
        super(props)
        this.state = {
            isLoading: true,
            sopLoginStatus: 'LOGOUT',
            sopLoggedInUser: null,
            savedUser: null
        }
        this._isMounted = false;
        this.sopEnabled = false;
        this.setSavedUser();
    }

    componentWillMount() {
        var that = this;

        DeviceEventEmitter.addListener('onLoginStatusReceived', function (e) {
            that.onLoginStatusReceived(e.loginStatus);
        });

        DeviceEventEmitter.addListener('onEntryInfoReceived', function (e) {
            console.log('onEntryInfoReceived');
            console.log(e.entryInfo);
            that.onEntryInfoReceived(JSON.parse(e.entryInfo));
        });


    }

    componentDidMount() {
        console.log('componentDidMount');
        this._isMounted = true;
        this.getAuthStateAsync();
    }

    componentWillUnmount() {
        DeviceEventEmitter.removeListener('onLoginStatusReceived');
        DeviceEventEmitter.removeListener('onEntryInfoReceived');

        this._isMounted = false;
    }

    render() {
        if (this.state.isLoading) {
            return (
                <View style={styles.container}>
                    <Spinner
                        style={[styles.gray, { height: 80 }]}
                        color='red'
                        size="large"
                    />
                </View>
            );
        }

        //if (this.isSOPLoggedIn()) { }

        return (
            <View style={{ flex: 1 }}>
                {this.props.children}
            </View>
        );
    }

    getAuthStateAsync = async () => {
        var that = this;

        that.setState({ isLoading: true });
        // that.props.checkingUser();

        if (that.sopEnabled && RicohAuthAndroid) {
            await RicohAuthAndroid.getAuthState()
                .then((msg) => {
                    console.log('success!!')
                }, (error) => {
                    console.log('error!!')
                });
        } else {
            // for testing

            var entryInfo = {};
            entryInfo.entryId = '00001';
            entryInfo.registrationNumber = '00001';
            entryInfo.name = 'admin';
            entryInfo.mailAddress = 'admin@atpath.com';
            entryInfo.loginUserName = 'admin';

            that.setState({ sopLoggedInUser: entryInfo });
            setTimeout(() => {
                that.props.setQueryUserName(entryInfo.loginUserName);
                that.setState({ isLoading: false });
                // that.props.doneCheckingUser();
            }, 3000); // 模拟
        }
    }

    onLoginStatusReceived = (status) => {
        this.setState({ sopLoginStatus: status });
        console.log('SOP login status: ' + status);
    }

    onEntryInfoReceived = (entryInfo) => {
        var that = this;

        that.setState({ sopLoggedInUser: entryInfo });

        // var userId = entryInfo.loginUserName;
        // if (!userId)
        //     userId = entryInfo.userId;

        // // for testing
        // let user = await that.getSavedUser(userId);
        // that.setState({ savedUser: user });

        that.setQueryUserName(entryInfo.loginUserName);
        that._isMounted && that.setState({ isLoading: false });
    }

    isSOPLoggedIn = () => {
        return this.state.sopLoginStatus == 'LOGIN';
    }

    getSavedUser = async (userId) => {
        return await AsyncStorage
            .getItem(userId)
            .then(data => {
                if (data) {
                    let user = JSON.parse(data);
                    return user;
                }
                return null;
            });
    }

    setSavedUser = async () => {
        let username = 'admin';
        let password = 'admin';
        await AsyncStorage
            .setItem(username, JSON.stringify({ username, password }));
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
        doneCheckingUser: () => { dispatch({ type: 'DONE_CHECKING_USER' }); },
        setQueryUserName: (username) => { dispatch({ type: 'QUERY_USER', payload: username }); },
    }
};

export default connect(
    mapStateToProps,
    mapDispatchToProps,
    null,
    {
        withRef: true
    }
)(SOPApp);
