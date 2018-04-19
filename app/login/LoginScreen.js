import React, { Component } from 'react';
import {
    StyleSheet,
    ScrollView,
    Text,
    TextInput,
    View,
    Button,
    AsyncStorage,
    DeviceEventEmitter,
} from 'react-native';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { login, logout } from '../actions/auth';
import Toast from '../components/ToastModule';
import TextBox from '../components/TextBox';
import CommonStyles from '../styles/CommonStyles';
import ComponentStyles from '../styles/ComponentStyles';
import StyleConfig from '../styles/StyleConfig';
import DoveButton from '../components/DoveButton';
import RicohAuthAndroid from '../components/RCTRicohAuthAndroid';
// import { default as Toast } from '../components/RCTToastModuleAndroid';

class LoginScreen extends Component {
    static defaultProps = { _isMounted: PropTypes.boolean };

    static navigationOptions = {
        headerLeft: null
    };

    constructor(props) {
        super(props);
        this.state = {
            user: null,
            username: '',
            password: '',
            isLoading: false,
            loginStatus: null,
        };
    }

    componentDidMount() {
        this._isMounted = true;
    }

    componentWillUnmount() {
        this._isMounted = false;
    }

    componentWillMount() {
        var that = this;
        const { auth } = that.props;
        const { user = { username: null, password: null, token: { sid: null } } } = auth;

        const { username, password, token: { sid } } = user;
        if (username && password)
            that.setState({ username, password })

        DeviceEventEmitter.addListener('onLoginStatusReceived', function (e) {
            that.setState({ loginStatus: e.loginStatus, screenDismissed: true });
            if (e.loginStatus == 'LOGOUT') {
                that._signOutAsyc();
            }
        });

        DeviceEventEmitter.addListener('onEntryInfoReceived', function (e) {
            let entryInfo = JSON.parse(e.entryInfo);

            // that.setState({ user: entryInfo });

            AsyncStorage
                .getItem(entryInfo.loginUserName)
                .then(data => {
                    if (!!data) {
                        //alert('User data from AsyncStorage: ' + data);
                        return JSON.parse(data);
                    }

                    return null;
                })
                .then(user => {
                    if (user != null) {
                        that.setState({ username: user.username, password: user.password }, () => {
                            that._signInAsync();
                        });

                        // that.props.saveAccount(user.username, user.password);
                        //that.props.login(user.username, user.password);
                        // navigate to Explorer screen
                        //that.props.navigation.navigate('Main');

                        Toast.show(`Welcome, ${user.username}`, Toast.SHORT);



                    } else {
                        //alert('Please register your account!');
                        // navigate to Registration screen
                        that.props.navigation.navigate('Registration', { key: entryInfo.loginUserName });
                    }
                });

            // alert(entryInfo);
        });

        RicohAuthAndroid.getAuthState()
            .then((msg) => {
                console.log('success!!')
            }, (error) => {
                console.log('error!!')
            });

    }

    render() {
        return (
            <ScrollView
                contentContainerStyle={{
                    justifyContent: 'center',
                }}
                style={styles.container}
                keyboardShouldPersistTaps={'always'}
            >
                <View style={{
                    flexDirection: 'row', height: 50, marginTop: 1,
                    justifyContent: 'center',
                    alignItems: 'flex-start',
                }}>
                    <Text
                        style={{ fontSize: 27 }}>
                        Login
                    </Text>
                </View>
                <View style={{ marginTop: 10 }}>
                    <TextBox
                        placeholder={'User name'}
                        onChangeText={(username) => this.setState({ username })}
                        returnKeyType='next'
                        autoCapitalize='none'
                    />
                    <View style={{ margin: 7 }} />
                    <TextBox
                        placeholder={'Password'}
                        onChangeText={(password) => this.setState({ password })}
                        secureTextEntry={true}
                    />
                </View>
                <View style={{ margin: 7 }} />
                {this.state.isLoading ?
                    <DoveButton
                        style={[styles.button, this.props.style]}
                        caption="Please wait..."
                        onPress={() => { }}
                    />
                    : <DoveButton
                        caption="Sign in!"
                        onPress={this._signInAsync}
                    />}
            </ScrollView>
        )
    }

    _signInAsync = async () => {
        const { dispatch, login } = this.props;

        this.setState({ isLoading: true });
        try {
            await Promise.race([
                login(this.state.username, this.state.password),
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
            this._isMounted && this.setState({ isLoading: false });
        }
    };

    _signOutAsyc = async () => {
        const { logout, auth } = this.props;
        const { user } = auth;

        if (!user) {
            return;
        }

        const sid = auth.user.token.sid;
        await logout(sid);
    }
}

async function timeout(ms: number): Promise {
    return new Promise((resolve, reject) => {
        setTimeout(() => reject(new Error('Timed out')), ms);
    });
}

function select(state) {
    return {
        auth: state.auth
    };
}

function dispatch(dispatch) {
    return {
        login: (username, password) => dispatch(login(username, password)),
        logout: (sid) => dispatch(logout(sid)),
    }
};

export default connect(select, dispatch)(LoginScreen);

const styles = StyleSheet.create({
    container: {
        flex: 1,
        flexDirection: 'column',
        backgroundColor: 'gray',
        padding: 30,
    }
});