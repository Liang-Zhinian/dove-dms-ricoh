import React, { Component } from 'react';
import {
    ScrollView,
    FlatList,
    TouchableOpacity,
    View,
    Text,
} from 'react-native';
import { getItem } from '../services/storageService'

class MyListItem extends React.PureComponent {
    static navigationOptions = ({ navigation }) => {
        const { params = {} } = navigation.state;

        return {
            headerTitle: 'Log',

        }
    };

    _onPress = () => {
        this.props.onPressItem(this.props.id);
    };

    render() {
        const textColor = this.props.selected ? "red" : "black";
        return (
            <TouchableOpacity
                style={{
                    padding: 10,
                }}
                onPress={this._onPress}>
                <View>
                    <Text style={{ color: textColor }}>
                        {this.props.title}
                    </Text>
                </View>
                <View style={{
                    margin: 5,
                    height: 1,
                    backgroundColor: '#dddddd'
                }} />
            </TouchableOpacity>
        );
    }
}


export default class MultiSelectList extends React.PureComponent {
    state = {
        selected: (new Map(): Map<string, boolean>),
        data: []
    };

    _keyExtractor = (item, index) => item.id;

    _onPressItem = (id: string) => {
        // updater functions are preferred for transactional updates
        this.setState((state) => {
            // copy the map rather than modifying state.
            const selected = new Map(state.selected);
            selected.set(id, !selected.get(id)); // toggle
            return { selected };
        });
    };

    _renderItem = ({ item }) => (
        <MyListItem
            id={item.id}
            onPressItem={this._onPressItem}
            selected={!!this.state.selected.get(item.id)}
            title={item.title}
        />
    );

    componentWillMount() {
        const key = 'APP_EXCEPTION';
        getItem(key)
            .then(value => {
                var array = value.split('%**********%');
                var list = [];
                for (i = 0; i < array.length; i++) {
                    var item = {};
                    item.id = i;
                    item.title = array[i];

                    list.push(item);
                }
                if (value)
                    this.setState({ data: list });
            })
    }

    render() {
        return (
            <ScrollView style={{
                marginTop: 50
            }}>
                <FlatList
                    data={this.state.data}
                    extraData={this.state}
                    keyExtractor={this._keyExtractor}
                    renderItem={this._renderItem}
                />
            </ScrollView>
        );
    }
}