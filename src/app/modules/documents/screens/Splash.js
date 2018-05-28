import React, { Component } from 'react'
import { StyleSheet, View, Image, Text, AsyncStorage } from 'react-native'
import { connect } from 'react-redux'

class Splash extends Component {
    constructor(props) {
        super(props)
        this.state = {}
    }

    componentWillMount() {
        
    }

    componentWillReceiveProps(nextProps) {
        // if (!nextProps.authenticated) this.props.navigation.navigate('Login')
        if (nextProps.authenticated) this.props.navigation.navigate('WeLoggedIn')
    }

    render() {
        const { container, image, text } = styles
        return (
            <View style={container}>
                    <Text>Splash</Text>
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