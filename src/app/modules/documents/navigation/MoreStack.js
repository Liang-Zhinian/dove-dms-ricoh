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
import AboutScreen from '../../../screens/About';

const {
    Downloads,
    Account,
    Settings,
    More,
    Profile
} = Routes; 

export default MoreStack = StackNavigator({
    More,
    Downloads,
    Account,
    Settings,
    Update:{
        screen: UpdateScreen,
    },
    About: {
        screen: AboutScreen,
    },
    Profile
}, {
        headerMode: 'screen',
        initialRouteName: 'More',
        navigationOptions: {
            ...defaultHeader
        }
    })

