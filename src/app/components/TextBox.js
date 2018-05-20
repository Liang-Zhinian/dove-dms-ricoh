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