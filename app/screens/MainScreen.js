import React from 'react';
import { StyleSheet, View } from 'react-native';
import Home from '../modules/documents/screens/Home';
import { BottomTabs } from '../navigators/navigation';

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
});

class MainScreen extends React.Component {
  static navigationOptions = {
    title: 'Home Screen',
  };


  componentDidMount() {
    console.log('componentDidMount');
  }

  render() {
    return <View></View>
  }

}

export default MainScreen;
