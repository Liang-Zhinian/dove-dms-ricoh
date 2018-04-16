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
    Login,
    Logout,
    Registration,
    Home,
    Search,
    Downloads,
    Account,
    Settings,
    RepositoryUsage,
    CheckedoutReport,
    LockedReport,
    // Scan
} = DocumentRoutes; 

const HomeStack = StackNavigator(
    {
        // Login,
        // Logout,
        // Registration,
        Home,
        Search,
        Downloads,
        Account,
        Settings,
        CheckedoutReport,
        LockedReport,
        RepositoryUsage,
        // Scan
    },
    {
        headerMode: 'screen',
        initialRouteName: 'Home',
        navigationOptions: {
            ...defaultHeader
        }
    },
)

export const BottomTabs = TabNavigator(
    {
        HomeTab: {
            screen: HomeStack,
            navigationOptions: tabNavigationOptions('Home', 'home')
        },
        ExplorerTab: {
            screen: Documents.Navigation.ExplorerStack,//DocumentRoutes.Explorer.screen,//
            navigationOptions: tabNavigationOptions('My Documents', 'file-submodule')
        },
        SearchTab: {
            screen: Documents.Navigation.SearchStack,
            navigationOptions: tabNavigationOptions('Search', 'search')
        },
        MoreTab: {
            screen: Documents.Navigation.MoreStack,
            navigationOptions: tabNavigationOptions('More', 'kebab-horizontal')
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

const MainStack = StackNavigator({
    Login,
    Main: {
        screen: BottomTabs
    },
    Logout,
    Registration
}, {
    headerMode: 'none'
})

const styles = StyleSheet.create({
    drawer: {
        flex: 1,
        justifyContent: 'space-between',
        backgroundColor: 'darkgray',
    },
    drawerItem: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        alignItems: 'center',
        paddingHorizontal: 18,
        borderBottomColor: 'rgba(0,0,0,.1)',
        borderBottomWidth: StyleSheet.hairlineWidth,
    },
    drawerText: {
        color: '#fff',
        fontSize: 18,
        // fontFamily: 'nemoy-medium',
        padding: 14
    },
    header: {
        paddingTop: 20,
        paddingBottom: 5,
        backgroundColor: colors.primary,
        justifyContent: 'center',
        shadowColor: '#21292b',
        shadowOffset: { width: -2, height: 2 },
        shadowRadius: 2,
        shadowOpacity: .7,
        marginBottom: 8,
        elevation: 10
    },
    logo: {
        height: 60,
        width: 200,
        alignSelf: 'center',
        marginVertical: 10,
    },
    footer: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        alignItems: 'center',
        paddingHorizontal: 20,
    }
})

export default MainStack
