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
    Button
} from 'react-native';
import { connect } from 'react-redux';

import RicohAuthAndroid from '../components/RCTRicohAuthAndroid';
import { login, logout } from '../actions/auth';
import Toast from '../components/ToastModule';
import Spinner from '../components/Spinner';
import LoginButton from './LoginButton';
import DoveButton from '../components/DoveButton';

function alert(msg) {
    Alert.alert('Splash Component', msg, [{ text: 'OK', onPress: () => console.log('OK Pressed') },], { cancelable: false });
}

class Splash extends Component {

    static navigationOptions = {
        header: null
    };

    constructor(props) {
        super(props)
        this.state = {
            isLoading: true,
            savedUser: null,
        }
        this._isMounted = false;

    }

    componentWillMount() {
    }

    componentDidMount() {
        console.log('componentDidMount');
        var that = this;
        const { isLoggedIn, queryUserName } = that.props;
        var username = "";
        if (typeof queryUserName !== 'undefined') username = queryUserName;
        if (!isLoggedIn) {
            that.getSavedUser(username)
                .then(savedUser => {
                    that.setState({ savedUser, isLoading: false }, () => {
                        !that.state.savedUser && that.props.navigation.navigate('Login');
                    });
                })
        } else {
            that.setState({ isLoading: false });
        }

        this._isMounted = true;
    }

    componentWillUnmount() {
        this._isMounted = false;
    }

    render() {
        const { container, image, text, title } = styles;

        if (this.state.isLoading) {
            return (
                <View style={container}>
                    <Spinner
                        style={[styles.gray, { height: 80 }]}
                        color='red'
                        size="large"
                    />
                </View>
            );
        }

        if (this.state.savedUser) {
            const { username, password } = this.state.savedUser;
            return (
                <View style={container}>
                    <Text style={title}>Welcome, {username}</Text>

                    {/* <Button
                        onPress={() => this._signInAsync(username, password)}
                        title="Log In!"
                    /> */}
                    <LoginButton style={[styles.button]} username={username} password={password} />
                    <Text style={title}>or</Text>

                    <DoveButton style={[styles.button]}
                        onPress={() => this.props.navigation.navigate('Login')}
                        caption="Use another account!"
                    />
                </View>
            );
        }
        // this.props.navigation.navigate('Login');
        return (
            <View style={container}>
                <DoveButton style={[styles.button]}
                    onPress={() => this.props.navigation.navigate('Login')}
                    caption="Go to Log In!"
                />
            </View>
        );
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

    getSavedUser = (userId) => {
        // if (typeof userId == 'undefined' || !userId) Promise.reject(null);

        return AsyncStorage
            .getItem(userId)
            .then(data => {
                if (data) {
                    let user = JSON.parse(data);
                    return user;
                }
                return null;
            });
    };
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
    button: {
        width: 300
    }
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
        isLoggedIn: state.auth.isLoggedIn,
        queryUserName: state.storage.queryUserName,
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