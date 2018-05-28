import React from 'react'
import {
    Platform,
    StyleSheet,
} from 'react-native';
import styles from '../styles';

const colors = styles.colors;

export const defaultTabs = {
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

export const defaultHeader = {
    headerStyle: {
        backgroundColor: colors.primary,
        shadowOpacity: 0,
        elevation: 0,
    },
    headerTitleStyle: {
        alignSelf: 'center',
        fontSize: 20,
        marginLeft: Platform.OS === 'ios' ? -10 : 10
    },
    headerTintColor: 'white',
    headerBackTitle: null
}

export default StyleSheet.create({
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


