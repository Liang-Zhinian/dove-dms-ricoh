import React, { Component } from 'react';
import {
    Alert,
    StyleSheet,
    View,
    ScrollView,
    Image,
    Text,
    TextInput,
    AsyncStorage,
    DeviceEventEmitter,
} from 'react-native';
import { connect } from 'react-redux';
import RicohAuthAndroid from '../components/RCTRicohAuthAndroid';
import { login, logout } from '../actions/auth';
import DoveButton from '../components/DoveButton';
import Toast from '../components/ToastModule';

function alert(msg) {
    Alert.alert('Splash Component', msg, [{ text: 'OK', onPress: () => console.log('OK Pressed') },], { cancelable: false });
}

class Splash extends Component {
    constructor(props) {
        super(props)
        this.state = {
            key: null,
            isLoading: false,
            loginStatus: null,
            user: null,
            username: 'admin',
            password: 'admin',
            newUser: false,
        }
        this._isMounted = false;


    }


    componentDidMount() {
        this._isMounted = true;

        DeviceEventEmitter.addListener('appStateChange', this.handleAppStateChange);
    }

    componentWillUnmount() {
        this._isMounted = false;
    }

    componentWillMount() {
        var that = this;

        DeviceEventEmitter.addListener('onLoginStatusReceived', function (e) {
            that.setState({ loginStatus: e.loginStatus });
        });

        DeviceEventEmitter.addListener('onEntryInfoReceived', function (e) {
            let entryInfo = JSON.parse(e.entryInfo);

            that.setState({ user: entryInfo });

            AsyncStorage
                .getItem(entryInfo.loginUserName)
                .then(data => {
                    if (data) {
                        alert('User data from AsyncStorage: ' + data);
                        let user = JSON.parse(data);
                        that._signInAsync(user.username, user.password);
                    } else {
                        that.setState({ newUser: true });
                    }
                    that.props.doneCheckingUser();
                });

        });

        that._getAuthStateAsync();

    }

    componentWillReceiveProps(nextProps) {
        // if (!nextProps.authenticated) this.props.navigation.navigate('Login')
        Toast.show(`auth.isLoggedIn: ${auth.isLoggedIn}`, Toast.SHORT);
    }

    componentWillUpdate(nextProps, nextState) {
        if (nextState.loginStatus == 'LOGOUT') this._signOutAsync();
    }

    render() {
        const { container, image, text, title } = styles;

        if (this.props.userChecking) {
            return (
                <View style={container}>
                    <Text style={title}>Please wait ...</Text>
                </View>
            );
        } else {

            return (
                <View style={container}>
                    <Text style={title}>Ready</Text>
                </View>
            );
        }
    }

    handleAppStateChange = (appState) => {
        console.log(`current state: ${appState.currentAppState}`);
        if (appState.currentAppState === 'active') {
            this._getAuthStateAsync();
        }
    }

    _signInAsync = async (username, password) => {
        const { dispatch, login } = this.props;

        this.setState({ isLoading: true });
        try {
            await Promise.race([
                login(username, password),
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

    _signOutAsync = async () => {
        const { logout, auth } = this.props;
        const { user } = auth;

        if (!user) {
            return;
        }

        const sid = auth.user.token.sid;
        await logout(sid);
    };

    _registerAsync = async () => {
        this.setState({ isLoading: true });
        const that = this;
        const { login } = that.props;
        const { username, password } = that.state;
        let user = { username, password };

        return await AsyncStorage
            .setItem(user.username, JSON.stringify(user), (error) => {

                if (error == null) {
                    that._signInAsync(user.username, user.password);

                    Toast.show('Registration finished.', Toast.SHORT);
                } else {
                    Toast.show('Registration failed.', Toast.SHORT);
                }
            });
    };

    _getAuthStateAsync = async () => {
        this.props.checkingUser();

        if (RicohAuthAndroid) {
            RicohAuthAndroid.getAuthState()
                .then((msg) => {
                    console.log('success!!')
                }, (error) => {
                    console.log('error!!')
                });
        } else {

            AsyncStorage
                .getItem('admin')
                .then(data => {
                    if (data) {
                        alert('User data from AsyncStorage: ' + data);
                        let user = JSON.parse(data);
                        that._signInAsync(user.username, user.password);
                    } else {
                        that.setState({ newUser: true });
                    }
                    that.props.doneCheckingUser();
                });
        }
    }
}

async function timeout(ms: number): Promise {
    return new Promise((resolve, reject) => {
        setTimeout(() => reject(new Error('Timed out')), ms);
    });
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
        backgroundColor: '#F0F0F0'
    },
    image: {
        height: 110,
        resizeMode: 'contain'
    },
    text: {
        marginTop: 50,
        fontSize: 15,
        color: '#1A1A1A'
    },
    title: {
        fontSize: 20,
        textAlign: 'center',
        margin: 10,
    },
})

// export default Splash

// 获取 state 变化
const mapStateToProps = (state) => {
    return {
        // 获取 state 变化
        // isLoggedIn: state[NAME].account.isLoggedIn,
        // username: state[NAME].account.username,
        // password: state[NAME].account.password,
        // sid: state[NAME].account.sid,
        auth: state.auth,
        userChecking: state.auth_ext.userChecking,
    }
};

// 发送行为
const mapDispatchToProps = (dispatch) => {
    return {
        // 发送行为
        login: (username, password) => dispatch(login(username, password)),
        logout: (sid) => dispatch(logout(sid)),
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
)(Splash);