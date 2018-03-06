
'use strict';

import React, { Component } from 'react';
import {
    StyleSheet,
    View,
} from 'react-native';

var PropTypes = React.PropTypes;

class DovePageControl extends Component {
    static propTypes = {
        style: View.propTypes.style,
        count: PropTypes.number.isRequired,
        selectedIndex: PropTypes.number.isRequired,
    },

    render() {
        var images = [];
        for (var i = 0; i < this.props.count; i++) {
            var isSelected = this.props.selectedIndex === i;
            images.push(<Circle key={i} isSelected={isSelected} />);
        }
        return (
            <View style={[styles.container, this.props.style]}>
                <View style={styles.innerContainer}>
                    {images}
                </View>
            </View>
        );
    }
};

class Circle extends Component {
    render() {
        var extraStyle = this.props.isSelected ? styles.full : styles.empty;
        return <View style={[styles.circle, extraStyle]} />;
    }
};

var CIRCLE_SIZE = 4;

var styles = StyleSheet.create({
    container: {
        alignItems: 'center',
        justifyContent: 'center',
    },
    innerContainer: {
        flexDirection: 'row',
    },
    circle: {
        margin: 2,
        width: CIRCLE_SIZE,
        height: CIRCLE_SIZE,
        borderRadius: CIRCLE_SIZE / 2,
    },
    full: {
        backgroundColor: '#fff',
    },
    empty: {
        backgroundColor: '#fff5',
    },
});

export default DovePageControl;
module.exports.__cards__ = (define) => {
    define('Simple 2', () => <DovePageControl count={2} selectedIndex={0} />);
    define('Simple 5', () => <DovePageControl count={5} selectedIndex={2} />);
};