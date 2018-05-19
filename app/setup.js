/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

import React, { Component } from 'react';
import { View, Platform, StatusBar } from 'react-native';
import { Provider } from 'react-redux';

import configureStore from './store';

import App from './App';


export default setup = () => {

  

  class Root extends Component<{}> {
    constructor() {
      super();
      this.state = {
        isLoading: true,
        store: configureStore(() => this.setState({ isLoading: false })),
      };
    }

    render() {
      if (this.state.isLoading) {
        return null;
      }

      return (
        <Provider store={this.state.store}>
          {/*<View style={{ flex: 1 }}>
          {Platform.OS === 'ios' && <StatusBar barStyle="light-content" />}
    {Platform.OS === 'android' && <View style={{ backgroundColor: 'rgba(0,0,0,0.2)' }} />}*/}
          <App />
          {/*</View>*/}
        </Provider>
      );
    }
  }

  return Root;
}

