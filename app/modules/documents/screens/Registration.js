/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

import React, { Component } from 'react';
import {
    Platform,
    StyleSheet,
    TextInput,
    View,
    Button,
    ScrollView,
    AsyncStorage,
    Text,
    Alert,
} from 'react-native';
import { connect } from 'react-redux';
import * as actions from '../actions';
import { NAME } from '../constants';

function alert(msg) {
    Alert.alert('Registration Component', msg, [{ text: 'OK', onPress: () => console.log('OK Pressed') },], { cancelable: false });
}

class Registration extends Component<{}> {
    static navigationOptions = {
        headerTitle: 'Registration',
    }

    constructor(props) {
        super(props);
        this.state = {
            key: null,
            username: 'admin',
            password: 'admin',

        };
    }

    componentDidMount() {

        const { params = {} } = this.props.navigation.state;
        const key = params.key;
        // alert(key);
        this.setState({ key: key });
    }

    render() {

        return (
            <ScrollView style={{ padding: 20 }}>
                <Text style={{ fontSize: 27 }}>
                    Please register your account!
                </Text>
                <Text style={styles.title}>
                    {this.state.key}
                </Text>
                <TextInput placeholder='Username'
                    onChangeText={(username) => this.setState({ username })}
                    returnKeyType='next'
                    value={this.state.username}
                />
                <TextInput placeholder='Password'
                    onChangeText={(password) => this.setState({ password })}
                    secureTextEntry={true}
                    value={this.state.password}
                />
                <View style={{ margin: 7 }} />
                <Button
                    onPress={this.submit.bind(this)}
                    title="Submit"
                />
            </ScrollView>
        );
    }

    submit() {
        const that = this;
        let user = {};
        user.username = that.state.username;
        user.password = that.state.password;

        AsyncStorage
            .setItem(that.state.key, JSON.stringify(user), (error) => {

                if (error == null) {
                    that.props.saveAccount(user.username, user.password);
                    that.props.login(user.username, user.password);
                    alert('Registration finished.');

                    // navigate to Explorer screen
                    that.props.navigation.navigate('Explorer');
                } else {
                    alert('Registration failed.');
                }
            });
    }
}

// 获取 state 变化
const mapStateToProps = (state) => {
    return {
        // 获取 state 变化
        isLoggedIn: state[NAME].account.isLoggedIn,
        username: state[NAME].account.username,
        password: state[NAME].account.password,
        sid: state[NAME].account.token.sid
    }
};

// 发送行为
const mapDispatchToProps = (dispatch) => {
    return {
        // 发送行为
        login: (username, password) => dispatch(actions.login(username, password)),
        saveAccount: (username, password) => dispatch(actions.saveAccount(username, password)),
    }
};

export default connect(
    mapStateToProps,
    mapDispatchToProps,
    null,
    {
        withRef: true
    }
)(Registration);

const styles = StyleSheet.create({
    container: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
        backgroundColor: '#F5FCFF',
    },
    title: {
        fontSize: 20,
        textAlign: 'center',
        margin: 10,
    },
});
