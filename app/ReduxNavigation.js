import React, { Component } from 'react';
import { BackHandler, Platform } from 'react-native';
import * as ReactNavigation from 'react-navigation';
import { connect } from 'react-redux';
import MainNavigation from './navigation';
import { addListener } from './utils/redux';
//import { NAME } from './constants';



class ReduxNavigation extends Component {
    componentDidMount() {
        BackHandler.addEventListener("hardwareBackPress", this.onBackPress);
    }

    componentWillUnmount() {
        BackHandler.removeEventListener("hardwareBackPress", this.onBackPress);
    }

    onBackPress = () => {
        const { dispatch, nav } = this.props;
        if (this.isRootScreen(nav)) {
            BackHandler.exitApp();
            return;
        }

        dispatch(ReactNavigation.NavigationActions.back());

        return true;
    }

    isRootScreen(navigator) {
        if (typeof navigator.index == 'undefined') return true;

        let isCurrentRoot = navigator.index == 0;

        if (navigator.routes && navigator.routes.length > 0) {
            let allChildAreRoots = true;

            navigator.routes.map((r) => {
                if (allChildAreRoots) {
                    if (!this.isRootScreen(r)) {
                        allChildAreRoots = false;
                    }
                }
            });

            return allChildAreRoots && isCurrentRoot;
        }

        return isCurrentRoot;
    }

    render() {
        const { dispatch, nav, } = this.props;

        const navigation = ReactNavigation.addNavigationHelpers({
            dispatch,
            state: nav,
            addListener,
        })
        return <MainNavigation navigation={navigation} />
    }
}
const mapStateToProps = state => {
    return {
        nav: state.nav,
    }
};
export default connect(mapStateToProps)(ReduxNavigation)