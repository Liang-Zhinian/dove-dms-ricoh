import React, { Component } from 'react';
import {
    View,
} from 'react-native';
import { CommonStyles } from '../../styles'
import { HeaderButton } from './HeaderButtons';
import { translate } from '../../../../i18n/i18n';

export default configureNavigationOptions = () => {
    return ({ navigation }) => {
        const {
            params = {
                onCancel: () => null,
                onToggleEdit: () => null,
                isEditMode: false,
                title: 'Title'
            }
        } = navigation.state;

        let headerTitle = params.title;
        let headerLeft = (
            <View style={[
                CommonStyles.flexRow,
                CommonStyles.m_l_2,
            ]}>
                <HeaderButton
                    onPress={params.cancel ? params.cancel : () => null}
                    text={translate('Cancel')}
                />
            </View>
        );
        let headerRight = (
            <View style={[
                CommonStyles.flexRow,
            ]}>
                <HeaderButton
                    onPress={params.toggleEdit ? params.toggleEdit : () => null}
                    text={translate(!params.isEditMode ? 'Edit' : 'Save')}
                />
            </View>
        );

        if  (!params.isEditable) return { headerTitle };
        if (params.isEditMode) return { headerLeft, headerTitle, headerRight };
        return { headerTitle, headerRight };
    }
};