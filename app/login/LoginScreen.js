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
    TouchableOpacity,
    Dimensions,
    Image
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
        header: null
    };

    constructor(props) {
        super(props);
        this.state = {
            user: null,
            username: '',
            password: '',
            isLoading: false,
            loginStatus: null,
            layout: {
                width: Dimensions.get('window').width,
                height: Dimensions.get('window').height,
            }
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

        if (!auth.user) return;

        const { user = { username: null, password: null, token: { sid: null } } } = auth;

        const { username, password, token: { sid } } = user;
        if (username && password)
            that.setState({ username, password })

    }

    render() {
        return (
            <View style={styles.container}>
                {this._renderBackground()}
                <ScrollView
                    contentContainerStyle={{
                        justifyContent: 'center',
                    }}
                    style={styles.container}
                    keyboardShouldPersistTaps={'always'}
                >
                    <View style={{
                        flexDirection: 'row', height: 80, marginTop: 10,
                        justifyContent: 'center',
                        alignItems: 'flex-start',
                    }}>
                        {this._renderLogo()}
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
                    <View style={{ margin: 7 }} />
                    <View style={{
                        flexDirection: 'row',
                        alignItems: 'flex-start',
                        justifyContent: 'space-between',
                    }}>
                        <TouchableOpacity onPress={() => { this.props.navigation.navigate('RegistrationScreen') }}>
                            <Text style={{backgroundColor: 'transparent'}}>Register</Text>
                        </TouchableOpacity>
                        <TouchableOpacity onPress={() => { this.props.navigation.navigate('ForgetPasswordScreen') }}>
                            <Text style={{backgroundColor: 'transparent'}}>Forget password?</Text>
                        </TouchableOpacity>
                    </View>
                </ScrollView>
            </View>
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

    _renderLogo() {
        return (
            <View style={[
                CommonStyles.flex_1,
                CommonStyles.flexItemsCenter,
                CommonStyles.flexItemsMiddle,
            ]}>
                <Image
                    style={[styles.image, { width: this.state.layout.width - 20, }]}
                    resizeMode={Image.resizeMode.contain}
                    source={require("../assets/logo.png")}
                />

            </View>
        )
    }

    _renderBackground() {
        return (
            <Image
                style={[{
                    width: this.state.layout.width,
                    height: this.state.layout.height,
                    position: 'absolute',
                    flex: 1,
                    resizeMode: 'cover',
                }]}
                resizeMode={Image.resizeMode.cover}
                source={require("../assets/wallpaper.png")}
            />
        )
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
        // backgroundColor: 'gray',
        padding: 30,
    }
});