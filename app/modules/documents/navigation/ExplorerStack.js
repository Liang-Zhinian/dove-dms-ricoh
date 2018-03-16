import React from 'react'
import {
    TouchableOpacity
} from 'react-native';
import {
    StackNavigator,
} from 'react-navigation';
import MaterialIcons from 'react-native-vector-icons/MaterialIcons';
// import BottomTabs from './BottomTabs';
import Routes from '../config/routes';
import { defaultHeader } from './styles';
import { stackNavigationOptions } from './util';

const {
    Explorer,
    DocumentDetails,
    Upload,
    FileViewer,
    Scan,
} = Routes; 

export default ExplorerStack = StackNavigator({
    Explorer,
    DocumentDetails,
    Upload,
    FileViewer,
    Scan,
}, {
        headerMode: 'screen',
        // initialRouteName: 'Explorer',
        navigationOptions: {
            ...defaultHeader
        }
    })
