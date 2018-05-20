'use strict';
import React from 'react';
import {TouchableOpacity} from 'react-native';
import MaterialIcons from 'react-native-vector-icons/MaterialIcons';

export function stackNavigationOptions() {
    return ({ navigation }: { navigation: any }) => ({
        headerLeft: (
            <TouchableOpacity onPress={() => navigation.navigate('DrawerOpen')} >
                <MaterialIcons name='menu' size={28} color={'white'} style={{ paddingLeft: 12 }} />
            </TouchableOpacity>
        )
    })
}

