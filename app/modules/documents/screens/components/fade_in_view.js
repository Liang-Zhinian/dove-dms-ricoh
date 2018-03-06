'use strict';

import React, { Component } from 'react';
import {
    Animated,
    StyleSheet,
    Dimensions,
    View
} from 'react-native';

const window = Dimensions.get('window');

class FadeInView extends Component {
    constructor(props) {
        super(props);
        this.state = {
            fadeAnim: new Animated.Value(0)
        }
    }

    componentDidMount() {
        this._animate(this.props);
    }

    componentWillReceiveProps(newProps) {
        this._animate(newProps);
    }

    _animate(newProps) {
        return Animated.timing(this.state.fadeAnim, {
            toValue: newProps.visible ? 0.7 : 0,
            duration: 300
        }).start();
    }

    render() {
        return (
            <Animated.View style={[styles.overlay,
            { opacity: this.state.fadeAnim },
            { backgroundColor: this.props.backgroundColor || 'black' }
            ]}>
                {this.props.children}
            </Animated.View>
        );
    }
};

const styles = StyleSheet.create({
    overlay: {
        top: 0,
        bottom: 0,
        left: 0,
        right: 0,
        height: window.height,
        width: window.width,
        position: 'absolute',
    }
});

export default FadeInView;