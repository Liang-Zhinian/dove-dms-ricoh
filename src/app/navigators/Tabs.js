import React from 'react'
import {
    Platform,
    Text,
    StyleSheet,
    Image,
    ScrollView,
    View,
    TouchableOpacity
} from 'react-native';
import {
    DrawerNavigator,
    StackNavigator,
    TabNavigator,
    TabBarTop,
    TabBarBottom,
    NavigationActions
} from 'react-navigation';
import Octicons from 'react-native-vector-icons/Octicons';

import I18n, { translate } from '../i18n/i18n';
import { Documents } from '../modules';
import Styles from '../styles';

const colors = Styles.colors;
const DocumentRoutes = Documents.Routes;

const tabNavigationOptions = (label: string, iconName: string) => {
    return {
        tabBarLabel: label,
        tabBarIcon: ({ tintColor }) => (
            <Octicons
                name={iconName}
                size={26}
                style={{ color: tintColor }}
                accessibilityLabel={label}
            />
        )
    }
}


const defaultTabs = {
    labelStyle: {
        fontSize: 12
    },
    indicatorStyle: {
        borderColor: 'lightgray',
        borderWidth: 2,
    },
    style: {
        //backgroundColor: colors.primary,
    },
    tabStyle: {
        padding: 0,
    }
}

const defaultHeader = {
    headerStyle: {
        backgroundColor: colors.primary,
        shadowOpacity: 0,
        elevation: 0,
    },
    headerTitleStyle: {
        // alignSelf: 'flex-start',
        fontSize: 20,
        marginLeft: Platform.OS === 'ios' ? -10 : 10
    },
    headerTintColor: 'white',
    headerBackTitle: null
}

const {
    Home,
    Search,
    Downloads,
    Account,
    Settings,
    RepositoryUsage,
    CheckedoutReport,
    LockedReport,
    FileViewer,
} = DocumentRoutes;

const HomeStack = StackNavigator(
    {
        Home,
        Search,
        Downloads,
        Account,
        Settings,
        CheckedoutReport,
        LockedReport,
        RepositoryUsage,
        FileViewer,
    },
    {
        headerMode: 'screen',
        initialRouteName: 'Home',
        navigationOptions: {
            ...defaultHeader
        }
    },
)

export default TabNavigator(
    {
        HomeTab: {
            screen: HomeStack,
            navigationOptions: tabNavigationOptions(translate('Home'), 'home')
        },
        ExplorerTab: {
            screen: Documents.Navigation.ExplorerStack,
            navigationOptions: tabNavigationOptions(translate('Documents'), 'file-submodule')
        },
        SearchTab: {
            screen: Documents.Navigation.SearchStack,
            navigationOptions: tabNavigationOptions(translate('Search'), 'search')
        },
        MoreTab: {
            screen: Documents.Navigation.MoreStack,
            navigationOptions: tabNavigationOptions(translate('More'), 'kebab-horizontal')
        }
    },
    {
        initialRouteName: 'HomeTab',
        tabBarComponent: TabBarBottom,
        tabBarPosition: 'bottom',
        tabBarOptions: {
            ...defaultTabs,
            activeTintColor: colors.primary
        }
    }
)
