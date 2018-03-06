import React, { Component } from 'react';
import {
    StyleSheet,
    View,
    Text,
    ProgressViewIOS,
    ProgressBarAndroid,
    Platform,
    Dimensions,
} from 'react-native';


const { height, width } = Dimensions.get('window');

export default class ProgressBar extends Component<{}>{

    render() {
        return (
            <View style={[styles.container, {
                flex: 1,
                //backgroundColor: 'transparent',
                opacity: 0.9,
                position: 'absolute',
                top: 0,
                bottom: 0,
                left: 0,
                right: 0,
                //flexDirection: 'column',
                display: this.props.visible ? 'flex' : 'none',
                //justifyContent: 'flex-start',
            }]}>
                <View style={{
                    position: 'absolute',
                    flex: 1,
                    flexDirection: 'column',
                    justifyContent: 'center',
                    //alignItems: 'center'
                    height: 40,
                    width: width,
                    // borderWidth: 2,
                    // borderColor: '#000000',
                    top: height / 2 - 20
                }}>
                    {/*
    <ActivityIndicator
        animating={true}
        style={{ backgroundColor: 'gray', height: 80, width: 80, borderRadius: 6, }}
        color='red'
        size="large"
        title='abc'
/>*/}
                    <Text style={{
                        textAlign: 'center',
                        fontSize: 30,
                        color: 'orange'
                    }}>{Math.floor((this.props.progress / 100) * 10000)}%</Text>
                    {Platform.OS === 'ios' && <ProgressViewIOS
                        style={styles.progressView}
                        progress={this.props.progress}
                        ßprogressTintColor="orange" />}

                    {Platform.OS === 'android' && <ProgressBarAndroid
                        style={styles.progressView}
                        progress={this.props.progress}
                        progressTintColor="orange"
                        styleAttr='Horizontal' />}
                </View>
            </View>
        );
    }
}


const styles = StyleSheet.create({
    container: {
        flex: 1,
        flexDirection: 'row',
        justifyContent: 'center',
        alignItems: 'center',
        backgroundColor: '#F5FCFF',
    },
    progressView: {
        flex: 1,
        marginLeft: 30,
        marginRight: 30,
        height: 10,
    },
});