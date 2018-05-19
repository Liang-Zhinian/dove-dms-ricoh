import React, { Component } from 'react';
import {
	ScrollView,
	Text,
	TextInput,
	View,
	Button,
	DeviceEventEmitter,
} from 'react-native';
import { connect } from 'react-redux';
import * as actions from '../actions';
import { NAME } from '../constants';
import RicohAuthAndroid from '../../../components/RCTRicohAuthAndroid';
import { default as Toast } from '../../../components/RCTToastModuleAndroid';

class Login extends Component {

	constructor(props) {
		super(props);
		this.state = {
            loginStatus: null,
			username: '',
			password: '',
			// user: null,
		};
	}

	componentWillMount() {
		var that = this;
		const { username, password, sid } = that.props;
		if (username && password)
			that.setState({ username, password })

		DeviceEventEmitter.addListener('onLoginStatusReceived', function (e) {
			that.setState({ loginStatus: e.loginStatus, screenDismissed: true });
			if (e.loginStatus == 'LOGOUT') {
				that.doLogout();
			}
		});

		DeviceEventEmitter.addListener('onEntryInfoReceived', function (e) {
			let entryInfo = JSON.parse(e.entryInfo);

			that.setState({ user: entryInfo });

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
							that.doLogin();
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
			<ScrollView style={{ padding: 20 }}>
				<Text
					style={{ fontSize: 27 }}>
					Login
                </Text>
				<TextInput placeholder='Username'
					onChangeText={(username) => this.setState({ username })}
					returnKeyType='next' />
				<TextInput placeholder='Password'
					onChangeText={(password) => this.setState({ password })}
					secureTextEntry={true} />
				<View style={{ margin: 7 }} />
				<Button
					onPress={this.onLoginPress.bind(this)}
					title="Submit"
				/>
			</ScrollView>
		)
	}

	onLoginPress() {
		this.props.navigation.navigate('Main');
	}

	doLogin() {
		const { login, navigation } = this.props;
		login(this.state.username, this.state.password);
		navigation.dispatch({ type: 'Login' });
		navigation.navigate('Main');
	}

	doLogout = () => {
		const { sid, logout, navigation } = this.props;
		logout(sid, navigation);
	}
}

function select(store) {
	return {
		username: store[NAME].account.username,
		password: store[NAME].account.password,
		sid: store[NAME].account.token.sid,
	};
}

function dispatch(dispatch) {
	return {
		// 发送行为
		login: (username, password) => dispatch(actions.login(username, password)),
		logout: (sid, navigation) => dispatch(actions.logout(sid, navigation)),
	}
};

export default connect(select, dispatch)(Login);