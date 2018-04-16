import React, { Component } from "react";
import { Text, View, TouchableOpacity, StyleSheet } from "react-native";
import { connect } from "react-redux";
import * as actions from '../actions';
import { NAME } from '../constants';

class Logout extends Component {
  static navigationOptions = {
    title: "LogoutScreen"
  };
  render() {
    return (
      <View
        style={{
          flex: 1,
          backgroundColor: "powderblue",
          justifyContent: "center",
          alignItems: "center"
        }}
      >
        <Text>{this.props.navigation.state.params.name}</Text>
        <TouchableOpacity
          onPress={this.props.logout}
          style={styles.touchableStyles}
        >
          <Text style={{ color: "white", fontSize: 22 }}>Logout</Text>
        </TouchableOpacity>
      </View>
    );
  }
}


function select(store) {
    return {
    };
  }
  
  function dispatch(dispatch) {
    return {
      // 发送行为
      logout: () => dispatch(actions.logout()),
    }
  };
  
  export default connect(select, dispatch)(Logout);

const styles = StyleSheet.create({
  touchableStyles: {
    marginTop: 15,
    backgroundColor: "black",
    paddingHorizontal: 50,
    paddingVertical: 10,
    borderRadius: 5
  }
});