import React, { Component } from 'react';
import {
    ScrollView,
    StyleSheet
} from 'react-native';

export default class Form extends Component {

    constructor(props) {
        super(props);
    }


    render() {

        return (
            <ScrollView
                style={styles.container}
                keyboardShouldPersistTaps={'always'}>
                {this.props.children}
            </ScrollView>
        );
    }
}

const styles = StyleSheet.create({
    container: {
      flex: 1,
      flexDirection: 'column',
      backgroundColor: '#F5FCFF',
    },
});
