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
import UpdateScreen from '../../../components/UpdateScreen';

const {
    Downloads,
    Account,
    Settings,
    More
} = Routes; 

export default AuthStack = StackNavigator({
    More,
    Downloads,
    Account,
    Settings,
    Update:{
        screen: UpdateScreen,
    },
}, {
        headerMode: 'screen',
        initialRouteName: 'More',
        navigationOptions: {
            ...defaultHeader
        }
    })

