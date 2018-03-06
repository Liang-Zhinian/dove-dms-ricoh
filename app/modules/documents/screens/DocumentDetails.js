import React, { Component } from 'react';
import {
  View,
  Text,
  StyleSheet,
  Image,
  ActivityIndicator,
  ScrollView,
} from 'react-native';
import { connect } from 'react-redux'
import moment from 'moment'
import { StyleConfig, ComponentStyles, CommonStyles } from '../styles'

export default class DocumentDetails extends Component {
  static navigationOptions = ({ navigation }) => ({
    headerTitle: `${typeof navigation.state.params !== 'undefined' ? navigation.state.params.node.name || navigation.state.params.node.fileName : 'Documents'}`,
    // headerRight: (
    //   <Button title={isInfo ? 'Done' : `${user}'s info`}
    //   onPress={() => setParams({ mode: isInfo ? 'none' : 'info'})} />
    // ),
  });

  render() {
    const { navigation } = this.props;
    const item = navigation.state.params.node;
    
    return (
      <ScrollView
        contentContainerStyle={{
          justifyContent: 'center',
          //alignItems: 'flex-start',
        }}
        style={styles.container}>
        <View style={{
          flex: 1,
          marginTop: 20,
          marginRight: 0,
          marginBottom: 0,
          marginLeft: 10,
        }}><Text style={{ fontSize: 20 }}>GENERAL</Text></View>
        <View style={[styles.section]}>
          <View style={[{ flex: 1 }, styles.row]}>
            <Text style={styles.title}>
              Name
              </Text>
            <Text style={styles.content}>
              {item.name || item.fileName}
            </Text>
          </View>
          <View style={[{ flex: 1 }, styles.row]}>
            <Text style={styles.title}>
              Created By
            </Text>
            <Text style={styles.content}>
              {item.creator}
            </Text>
          </View>
          <View style={[{ flex: 1 }, styles.row]}>
            <Text style={styles.title}>
              Creation Date
              </Text>
            <Text style={styles.content}>
              {moment(item.creation, 'YYYY-MM-DD HH:mm:ss.sss ZZ').format('YYYY-MM-DD')}
            </Text>
          </View>
          <View style={[{ flex: 1 }, styles.row]}>
            <Text style={styles.title}>
              Last Modified By
              </Text>
            <Text style={styles.content}>
              {item.creator}
            </Text>
          </View>
          <View style={[{ flex: 1 }, styles.row]}>
            <Text style={styles.title}>
              Last Modification Date
              </Text>
            <Text style={styles.content}>
              {moment(item.lastModified, 'YYYY-MM-DD HH:mm:ss.sss ZZ').format('YYYY-MM-DD HH:mm:ss')}
            </Text>
          </View>
          <View style={[{ flex: 1 }, styles.row]}>
            <Text style={styles.title}>
              Version Label
              </Text>
            <Text style={styles.content}>
              {item.version}
            </Text>
          </View>
        </View>
      </ScrollView>
    )
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    flexDirection: 'column',
    // justifyContent: 'center',
    // alignItems: 'flex-start',
    // width: null,
    // height: null,
    backgroundColor: '#F5FCFF',
  },
  section: {
    flex: 1,
    flexDirection: 'column',
    marginTop: 20,
    marginRight: 0,
    marginBottom: 20,
    marginLeft: 0,
    borderTopWidth: 1,
    borderTopColor: 'gray',
    borderBottomWidth: 1,
    borderBottomColor: 'gray',
  },
  row: {
    flexDirection: 'row',
    padding: 10,
    // marginRight: 10,
    // marginLeft: 10,
    backgroundColor: '#ffffff',
    // borderBottomWidth: 1,
    // borderBottomColor: 'gray',
    justifyContent: 'space-between',
    alignItems: 'center',
  },
  title: {
    flex: 1,
    // marginRight: 10,
    fontWeight: 'bold',
    textAlign: 'right',
    fontSize: 17,
  },
  content: {
    flex: 1,
    marginLeft: 10,
    fontSize: 17,
  },
});
