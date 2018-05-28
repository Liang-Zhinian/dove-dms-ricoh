import React, { Component } from 'react';
import {
    ScrollView,
    Text,
    TextInput,
    View,
    Button,
    AsyncStorage,
} from 'react-native';
import { connect } from 'react-redux';
import * as actions from '../actions';
import { NAME } from '../constants';


const AsyncStorageKey = "AS_";

class Login extends Component {

    constructor(props) {
        super(props);
        this.state = {
            username: '',
            password: '',
        };
    }

    componentWillMount() {
        const { username, password, sid } = this.props;
        // if (username && password && sid)
        //     this.props.navigation.navigate('Main');
    }

    componentDidMount() {
        const { username, password, sid } = this.props;
        if (username && password)
            this.setState({ username, password })
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
                    onPress={this.submit.bind(this)}
                    title="Submit"
                />
            </ScrollView>
        )
    }

    submit() {
        const { login, navigation } = this.props;
        login(this.state.username, this.state.password);
        navigation.dispatch({ type: 'Login' });
        navigation.navigate('Main');
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
    }
};

export default connect(select, dispatch)(Login);