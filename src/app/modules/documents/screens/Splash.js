import React, { Component } from 'react';
import {
    Alert,
    StyleSheet,
    View,
    Image,
    Text,
    AsyncStorage,
    DeviceEventEmitter,
} from 'react-native';
import { connect } from 'react-redux';
import * as actions from '../actions';
import { NAME } from '../constants';
import RicohAuthAndroid from '../../../components/RCTRicohAuthAndroid';

function alert(msg) {
    Alert.alert('Splash Component', msg, [{ text: 'OK', onPress: () => console.log('OK Pressed') },], { cancelable: false });
}

class Splash extends Component {
    constructor(props) {
        super(props)
        this.state = {
            loginStatus: null,
            user: null,
        }
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
                    if (!!data){
                        //alert('User data from AsyncStorage: ' + data);
                        return JSON.parse(data);
                    }

                    return null;
                })
                .then(user => {
                    if (user != null) {
                        that.props.login(user.username, user.password);
                        // navigate to Explorer screen
                        that.props.navigation.navigate('Explorer');
                        
                    } else {
                        that.props.navigation.navigate('Registration', {key: entryInfo.loginUserName});
                    }
                });

        });

        RicohAuthAndroid.getAuthState()
            .then((msg) => {
                console.log('success!!')
            }, (error) => {
                console.log('error!!')
            });

    }

    componentWillReceiveProps(nextProps) {
        // if (!nextProps.authenticated) this.props.navigation.navigate('Login')
    }

    componentWillUpdate(nextProps, nextState) {
        if (nextState.loginStatus == 'LOGOUT') this.props.navigation.navigate('Account')
    }

    render() {
        const { container, image, text } = styles
        return (
            <View style={container}>
                <Text>Splash</Text>

                <Text>{this.state.loginStatus}</Text>
                <Text>{!!this.state.user && this.state.user.loginUserName}</Text>
            </View>
        )
    }
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
    }
})

// export default Splash

// 获取 state 变化
const mapStateToProps = (state) => {
    return {
        // 获取 state 变化
        isLoggedIn: state.auth.user.isLoggedIn,
        username: state.auth.user.username,
        password: state.auth.user.password,
        sid: state.auth.user.token.sid
    }
};

// 发送行为
const mapDispatchToProps = (dispatch) => {
    return {
        // 发送行为
        login: (username, password) => dispatch(actions.login(username, password)),
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