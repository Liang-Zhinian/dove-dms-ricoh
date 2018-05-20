import React from 'react'
import {
    TouchableOpacity
} from 'react-native';
import {
    StackNavigator,
} from 'react-navigation';
import MaterialIcons from 'react-native-vector-icons/MaterialIcons';
import Routes from '../config/routes';
import { defaultHeader } from './styles';
import { stackNavigationOptions } from './util';

const {
    Search,
    FileViewer,
} = Routes; 

export default SearchStack = StackNavigator({
    Search,
    FileViewer,
}, {
        headerMode: 'screen',
        initialRouteName: 'Search',
        navigationOptions: {
            ...defaultHeader
        }
    })

