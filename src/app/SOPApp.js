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
    Text,
    ActivityIndicator,
} from 'react-native';
import { connect } from 'react-redux';

import { login, logout } from './actions/auth';
import RicohAuthAndroid from './components/RCTRicohAuthAndroid';
import Spinner from './components/Spinner';
import DoveButton from './components/DoveButton';
import { translate } from './i18n/i18n';

const styles = StyleSheet.create({
    container: {
        flex: 1,
        flexDirection: 'column',
        justifyContent: 'center',
        alignItems: 'center',
        backgroundColor: '#F5FCFF',
    },
    p: {fontSize: 20, marginBottom: 10},
    button: {}
});

class SOPApp extends Component {
    constructor(props) {
        super(props)
        this.state = {
            isLoading: true,
            sopAuthState: null,
            sopLoggedInUser: null,
            hasSkippedLogin: false,
        }
        this._isMounted = false;
        this.sopEnabled = true;
        // this.setSavedUser();
    }

    componentWillMount() {
        var that = this;

        DeviceEventEmitter.addListener('onAuthStateReceived', function (e) {
            console.log(e.authState);
            that.onAuthStateReceived(JSON.parse(e.authState));
        });
        DeviceEventEmitter.addListener('onAuthStateReceivedError', function (e) {
            alert(e.message);
        });

        // DeviceEventEmitter.addListener('onEntryInfoReceived', function (e) {
        //     console.log('onEntryInfoReceived');
        //     console.log(e.entryInfo);
        //     that.onEntryInfoReceived(JSON.parse(e.entryInfo));
        // });


    }

    componentDidMount() {
        console.log('componentDidMount');
        this._isMounted = true;
        this.getAuthStateAsync();
    }

    componentWillUnmount() {
        DeviceEventEmitter.removeListener('onAuthStateReceived');
        DeviceEventEmitter.removeListener('onAuthStateReceivedError');
        // DeviceEventEmitter.removeListener('onEntryInfoReceived');

        this._isMounted = false;
    }

    render() {
        if (this.state.isLoading) {
            return (
                <View style={styles.container}>
                <Text>SOPApp</Text>
                    <Spinner
                        style={[styles.gray, { height: 80 }]}
                        color='red'
                        size="large"
                    />
                </View>
            );
        }

        if (!this.isSOPLoggedIn() && !this.state.hasSkippedLogin) {
            return (
                <View style={styles.container}>
                    <Text style={styles.p}>{translate('PleaseLoginTheSopOr')}</Text>

                    <DoveButton style={[styles.button]}
                        onPress={() => this.setState({ hasSkippedLogin: true })}
                        caption={translate("Skip")}
                    />
                </View>
            );
        }

        return (
            <View style={{ flex: 1 }}>
                {this.props.children}
            </View>
        );
    }

    getAuthStateAsync = async () => {
        var that = this;

        that.setState({ isLoading: true });
        that.props.checkingUser();

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

            var authState = {};
            authState.userId = 'admin';
            authState.loginStatus = 'LOGIN';

            that.setState({ sopLoggedInUser: entryInfo, sopAuthState: authState });
            setTimeout(() => {
                that.props.setQueryUserName(entryInfo.loginUserName);
                that.setState({ isLoading: false });
                // that.props.doneCheckingUser();
            }, 3000); // 模拟
        }
    }

    onAuthStateReceived = (authState) => {
        this.setState({ sopAuthState: authState }, () => {
            // alert('authState.userId: '+authState.userId);
            if (this.isSOPLoggedIn()) {
                this.props.doneCheckingUser();
                this.props.setQueryUserName(authState.userId);
            } else {
                this.props.doneCheckingUser();
                // SOP logged out
                this.props.isLoggedIn && this.props.logout();
            }
            // this.props.doneCheckingUser();
            this.setState({ isLoading: false });
        });
        console.log('SOP login status: ' + authState.loginStatus);

    }

    onEntryInfoReceived = (entryInfo) => {
        var that = this;

        that.setState({ sopLoggedInUser: entryInfo });

        that.props.setQueryUserName(entryInfo.loginUserName);
        that._isMounted && that.setState({ isLoading: false });
    }

    isSOPLoggedIn = () => {
        return this.state.sopAuthState.loginStatus == 'LOGIN';
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
        isLoggedIn: state.auth.isLoggedIn
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        checkingUser: () => { alert('checkingUser'); dispatch({ type: 'CHECKING_USER' }); },
        doneCheckingUser: () => { alert('doneCheckingUser'); dispatch({ type: 'DONE_CHECKING_USER' }); },
        setQueryUserName: (username) => { dispatch({ type: 'QUERY_USER', payload: username }); },
        logout: () => dispatch(logout()),
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
