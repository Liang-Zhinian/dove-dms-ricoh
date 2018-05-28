/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

import React, { Component } from 'react';
import { View, Platform, StatusBar } from 'react-native';
import { Provider } from 'react-redux';

import configureStore from './store/index';

import App from './App';
import { getItem, setItem } from './services/storageService';
import env, { storageKey } from './modules/documents/env';


export default setup = () => {

  

  class Root extends Component<{}> {
    constructor() {
      super();
      this.state = {
        isLoading: true,
        store: configureStore(() => this.setState({ isLoading: false })),
        EnvChecked: false,
      };
    }

    componentDidMount() {
      this.checkEnv();
    }

    render() {
      if (this.state.isLoading || !this.state.EnvChecked) {
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

    checkEnv() {
      getItem(storageKey.DOCUMENT_SERVER)
        .then(server => {
          if (!server) {
            setItem(storageKey.DOCUMENT_SERVER, { server: env.host, https: env.https, port: env.port })
              .then(() => {
                this.setState({ EnvChecked: true });
              })
          } else {
            this.setState({ EnvChecked: true });
          }
        });
    }
  }

  return Root;
}

