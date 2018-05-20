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
import MaterialIcons from 'react-native-vector-icons/MaterialIcons';
import Octicons from 'react-native-vector-icons/Octicons';

import { Documents } from './modules';
import Styles from './styles';

const colors = Styles.colors;
const DocumentScreens = Documents.Screens;

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
        alignSelf: 'flex-start',
        fontSize: 20,
        marginLeft: Platform.OS === 'ios' ? -10 : 10
    },
    headerTintColor: 'white',
    headerBackTitle: null
}

const BottomTabs = TabNavigator(
    {
        HomeTab: {
            screen: DocumentScreens.Home,
            navigationOptions: tabNavigationOptions('Home', 'home')
        },
        ExplorerTab: {
            screen: DocumentScreens.Explorer,
            navigationOptions: tabNavigationOptions('My Documents', 'file-submodule')
        },
        SearchTab: {
            screen: DocumentScreens.Search,
            navigationOptions: tabNavigationOptions('Search', 'search')
        },
        // DownloadsTab: {
        //     screen: DocumentScreens.Downloads,
        //     navigationOptions: tabNavigationOptions('Downloads', 'cloud-download')
        // },
        MoreTab: {
            screen: DocumentScreens.More,
            navigationOptions: tabNavigationOptions('More', 'kebab-horizontal')
        }
    },
    {
        tabBarComponent: TabBarBottom,
        tabBarPosition: 'bottom',
        tabBarOptions: {
            ...defaultTabs,
            activeTintColor: colors.primary
        }
    }
)


// const ExplorerStackSummary = StackNavigator({
//     Explorer: {
//         screen: DocumentScreens.Explorer,
//         navigationOptions: ({ navigation }) => ({
//             title: 'Explorer'
//         })
//     }
// }, {
//         headerMode: 'none',
//         navigationOptions: {
//             ...defaultHeader
//         }
//     })


const HomeStackSummary = StackNavigator(
    {
        SummaryStack: {
            screen: BottomTabs,
            navigationOptions: ({ navigation }) => ({
                title: 'ATPATH ISD',
                headerLeft: (
                    <TouchableOpacity onPress={() => navigation.navigate('DrawerOpen')} >
                        <MaterialIcons name='menu' size={28} color={'white'} style={{ paddingLeft: 12 }} />
                    </TouchableOpacity>
                )
            })
        },
        Explorer: {
            screen: DocumentScreens.Explorer,
            navigationOptions: ({ navigation }) => ({
                title: 'Explorer'
            })
        },
        DocumentDetails: {
            screen: DocumentScreens.DocumentDetails,
            navigationOptions: ({ navigation }) => ({
                title: 'DocumentDetails'
            })
        },
        Upload: {
            screen: DocumentScreens.Upload,
            navigationOptions: ({ navigation }) => ({
                title: 'Upload'
            })
        },
        Search: {
            screen: DocumentScreens.Search,
            navigationOptions: ({ navigation }) => ({
                title: 'Search'
            })
        },
        Downloads: {
            screen: DocumentScreens.Downloads,
            navigationOptions: ({ navigation }) => ({
                title: 'Downloads'
            })
        },
        Account: {
            screen: DocumentScreens.Account,
            navigationOptions: ({ navigation }) => ({
                title: 'Account'
            })
        },
        FileViewer: {
            screen: DocumentScreens.FileViewer,
            navigationOptions: ({ navigation }) => ({
                title: 'FileViewer'
            })
        },
    },
    {
        headerMode: 'screen',
        initialRouteName: 'SummaryStack',
        navigationOptions: {
            ...defaultHeader
        }
    },
)
/*
const BottomTabsStack = StackNavigator(
    {
        BottomTabsLanding: {
            screen: BottomTabs,
            navigationOptions: ({ navigation }) => ({
                title: 'ATPATH ISD',
                headerLeft: (
                    <TouchableOpacity onPress={() => navigation.navigate('DrawerOpen')} >
                        <MaterialIcons name='menu' size={28} color={'white'} style={{ paddingLeft: 12 }} />
                    </TouchableOpacity>
                )
            })
        }
    }, {
        headerMode: 'screen',
        navigationOptions: {
            ...defaultHeader
        }
    })*/

const DrawerNavigation = DrawerNavigator(
    {
        Home: {
            screen: HomeStackSummary,
        },
    },
    {
        initialRouteName: 'Home',
        contentComponent: ({ navigation }) =>
            <View style={styles.drawer}>
                <View style={{ flex: 1 }}>
                    <View style={styles.header}>
                        <Image style={styles.logo} resizeMode='contain' source={require('./modules/documents/assets/icons/logo.png')} />
                    </View>

                    <ScrollView>

                        <TouchableOpacity
                            onPress={() => navigation.state.index === 1 ? navigation.navigate('DrawerClose') : navigation.navigate('BottomTabs')}
                            style={[styles.drawerItem, navigation.state.index === 1 ? { backgroundColor: 'black' } : null]}>
                            <Text style={styles.drawerText}>Home</Text>
                        </TouchableOpacity>

                        <TouchableOpacity
                            onPress={() => navigation.state.index === 2 ? navigation.navigate('DrawerClose') : navigation.navigate('Account')}
                            style={[styles.drawerItem, navigation.state.index === 2 ? { backgroundColor: 'black' } : null]}>
                            <Text style={styles.drawerText}>Account</Text>
                        </TouchableOpacity>

                    </ScrollView>
                </View>

            </View>
    })

const MainNavigation = StackNavigator(
    {
        App: {
            screen: DrawerNavigation
        }
    }, {
        initialRouteName: 'App',
        mode: 'card',
        headerMode: 'none',
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

export default MainNavigation
