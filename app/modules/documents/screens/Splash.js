import React, { Component } from 'react';
import {
    StyleSheet,
    View,
    Image,
    Text,
    AsyncStorage,
    DeviceEventEmitter,
} from 'react-native';
import { connect } from 'react-redux';
import RicohAuthAndroid from '../../../components/RCTRicohAuthAndroid';

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
            // AsyncStorage.getItem('firebaseUser').then(data => data ? JSON.parse(data) : null);
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
        if (nextState.loginStatus == 'LOGOUT') this.props.navigation.navigate('Registration')
    }

    render() {
        const { container, image, text } = styles
        return (
            <View style={container}>
                <Text>Splash</Text>

                <Text>{this.state.loginStatus}</Text>
                <Text>{this.state.user.entryId}</Text>
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

export default Splash