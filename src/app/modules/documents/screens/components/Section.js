import React, { Component } from 'react';
import {
    View,
    Text,
    StyleSheet
} from 'react-native';

export default class Section extends Component {

    constructor(props) {
        super(props);
    }


    render() {

        const title = this.props.title || 'Section';
        return (

            <View style={styles.container}>
                <Text style={styles.title}>{title}</Text>
                <View style={[styles.content]}>
                    {this.props.children}
                </View>
            </View>
        );
    }
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        marginTop: 20,
    },
    content: {
        flex: 1,
        flexDirection: 'column',
        marginTop: 10,
        marginRight: 0,
        marginBottom: 20,
        marginLeft: 0,
        borderTopWidth: StyleSheet.hairlineWidth,
        borderColor: 'gray',
        borderBottomWidth: StyleSheet.hairlineWidth,
    },
    title: { 
        marginLeft: 10, 
        fontSize: 14, 
        color: 'grey' 
    },
});
