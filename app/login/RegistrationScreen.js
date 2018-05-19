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
    Image,
    Dimensions,
} from 'react-native';
import { connect } from 'react-redux';
import { login } from '../actions/auth';
import {getItem, setItem} from '../services/storageService';
// import { NAME } from '../constants';

function alert(msg) {
    Alert.alert('Registration Component', msg, [{ text: 'OK', onPress: () => console.log('OK Pressed') },], { cancelable: false });
}

class RegistrationScreen extends Component<{}> {
    static navigationOptions = {
        headerTitle: 'Registration',
    }

    constructor(props) {
        super(props);
        this.state = {
            key: null,
            username: 'admin',
            password: 'admin',
            layout: {
                width: Dimensions.get('window').width,
                height: Dimensions.get('window').height,
            }
        };
    }

    componentDidMount() {
        console.log('componentDidMount');
        const { params = {} } = this.props.navigation.state;
        const key = params.key;
        // alert(key);
        this.setState({ key: key });
    }

    render() {

        return (
            <View style={styles.container}>
            {this._renderBackground()}
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
            </View>
        );
    }

    submit() {
        const that = this;
        let user = {};
        user.username = that.state.username;
        user.password = that.state.password;


        var sid = that.props.login(user.username, user.password);


        if (sid){
            AsyncStorage
                .setItem(that.state.key, JSON.stringify(user), (error) => {

                    if (error == null) {
                        // that.props.saveAccount(user.username, user.password);
                        // that.props.login(user.username, user.password);
                        alert('Registration finished.');

                        // navigate to Explorer screen
                        // that.props.navigation.navigate('Main');
                    } else {
                        alert('Registration failed.');
                    }
                });
        }
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

// 获取 state 变化
const mapStateToProps = (state) => {
    return {
        // 获取 state 变化
        // isLoggedIn: state[NAME].account.isLoggedIn,
        // username: state[NAME].account.username,
        // password: state[NAME].account.password,
        // sid: state[NAME].account.token.sid
    }
};

// 发送行为
const mapDispatchToProps = (dispatch) => {
    return {
        // 发送行为
        login: (username, password) => dispatch(login(username, password)),
        // saveAccount: (username, password) => dispatch(actions.saveAccount(username, password)),
    }
};

export default connect(
    mapStateToProps,
    mapDispatchToProps,
    null,
    {
        withRef: true
    }
)(RegistrationScreen);

const styles = StyleSheet.create({
    container: {
        flex: 1,
        // justifyContent: 'center',
        // alignItems: 'center',
        // backgroundColor: '#F5FCFF',
    },
    title: {
        fontSize: 20,
        textAlign: 'center',
        margin: 10,
    },
});
