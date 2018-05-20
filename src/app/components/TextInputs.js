import React, { Component } from 'react';
import {
    ToolbarAndroid,
    AppRegistry,
    StyleSheet,
    Text,
    View,
    Image,
    TextInput,
    TouchableOpacity
} from 'react-native';

import StyleConfig from '../styles/StyleConfig';

export default class TextBox extends Component {
    constructor(props) {
        super(props);
        this.state = { text: '' };
    }

    render() {
        return (
            <View style={Styles.TextInputView}>
                <TextInput
                    {...this.props}
                    blurOnSubmit={true}
                    style={Styles.TextInput}
                    placeholder={this.props.placeholder}
                    placeholderTextColor={StyleConfig.color_gray}
                    onChangeText={
                        (text) => {
                            this.setState({ text });
                            this.props.onChangeText(text);
                        }
                    }
                />
            </View>
        );
    }
}


const Styles = StyleSheet.create({
    TextInputView: {
        marginTop: 10,
        height: 50,
        backgroundColor: '#ffffff',
        borderRadius: 5,
        borderWidth: 0.3,
        borderColor: '#000000',
        flexDirection: 'column',
        justifyContent: 'center',
    },

    TextInput: {
        backgroundColor: '#ffffff',
        height: 45,
        margin: 18,
    },
});

export class Password extends Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <TextBox
                {...this.props}
                placeholder={'Password'}
                secureTextEntry={true}
                autoCapitalize='none'
            />
        );
    }
};

export class Numeric extends Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <TextBox
                {...this.props}
                placeholder={'Password'}
                keyboardType={'numeric'}
                autoCapitalize='none'
                onChangeText={
                    (text) => {
                        let that = this;

                        filterNumbers(text)
                            .then(t => that.props.onChangeText(t))
                            .catch(e => alert(e.messageF));
                    }
                }
            />
        );
    }
};

async function filterNumbers(text) {
    return new Promise((resolve, reject) => {

        let newText = '';
        let numbers = '0123456789';

        for (var i = 0; i < text.length; i++) {
            if (numbers.indexOf(text[i]) > -1) {
                newText = newText + text[i];
            }
            else {
                // your call back function
                reject(new Error("Please enter numbers only"));
                return;
            }
        }
        resolve(newText);
    })
}