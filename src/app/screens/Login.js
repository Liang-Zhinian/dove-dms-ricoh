import React, { Component } from 'react';
import {
    StyleSheet,
    ScrollView,
    Text,
    TextInput,
    View,
    Button,
    AsyncStorage,
    Dimensions,
    Image
} from 'react-native';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';

import { login } from '../actions/auth';
import Toast from '../components/ToastModule';
import TextBox from '../components/TextBox';
import CommonStyles from '../styles/CommonStyles';
import ComponentStyles from '../styles/ComponentStyles';
import StyleConfig from '../styles/StyleConfig';
import DoveButton from '../components/DoveButton';
import {translate} from '../i18n/i18n';

class LoginScreen extends Component {
    static defaultProps = { _isMounted: PropTypes.boolean };

    static navigationOptions = {
        headerLeft: null
    };

    constructor(props) {
        super(props);
        this.state = {
            username: '',
            password: '',
            isLoading: false,
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
        // const {username, password} = this.props;
        // this.setState({username, password});
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
                        value={this.state.username}
                    />
                    <View style={{ margin: 7 }} />
                    <TextBox
                        placeholder={'Password'}
                        onChangeText={(password) => this.setState({ password })}
                        secureTextEntry={true}
                        value={this.state.password}
                    />
                </View>
                <View style={{ margin: 7 }} />
                {this.state.isLoading ?
                    <DoveButton
                        style={[styles.button, this.props.style]}
                        caption={translate('PleaseWait')}
                        onPress={() => { }}
                    />
                    : <DoveButton
                        caption={translate('SignIn')}
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

    _renderLogo() {
        return (
            <View style={[
                CommonStyles.flex_1,
                CommonStyles.flexItemsCenter,
                CommonStyles.flexItemsMiddle,
            ]}>
                <Image
                    style={[styles.image, { width: this.state.layout.width - 10, }]}
                    resizeMode={Image.resizeMode.contain}
                    source={require("../assets/logo.png")}
                />

            </View>
        )
    }
}

async function timeout(ms: number): Promise {
    return new Promise((resolve, reject) => {
        setTimeout(() => reject(new Error('Timed out')), ms);
    });
}

function select(store) {
    return {
        // username: store.auth.user.username,
        // password: store.auth.user.password,
        // sid: store[NAME].account.token.sid,
    };
}

function dispatch(dispatch) {
    return {
        login: (username, password) => dispatch(login(username, password)),
    }
};

export default connect(select, dispatch)(LoginScreen);

const styles = StyleSheet.create({
    container: {
        flex: 1,
        flexDirection: 'column',
        backgroundColor: '#D9D6CF', //'#008573',
        padding: 30,
    }
});