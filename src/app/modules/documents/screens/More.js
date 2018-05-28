import React, { Component } from 'react';
import {
    View,
    Text,
    StyleSheet,
    TouchableHighlight,
    Platform,
    Dimensions,
    ScrollView
} from 'react-native';
import Icon from 'react-native-vector-icons/Ionicons';
import {
    ComponentStyles,
    CommonStyles,
    colors,
    StyleConfig,
} from '../styles';
import { translate } from '../../../i18n/i18n';

const MainRoutes = [
    {
        title: translate('Downloads'),
        color: StyleConfig.color_white,
        icon: 'ios-cloud-download-outline',
        id: 'Downloads',
    }, {
        title: translate('Account'),
        color: StyleConfig.color_white,
        icon: 'ios-contact-outline',
        id: 'Account',
    }, /*{
        title: translate('Settings'),
        color: StyleConfig.color_white,
        icon: 'ios-settings-outline',
        id: 'Settings',
    }, {
        title: translate('About'),
        color: StyleConfig.color_white,
        icon: 'ios-information-circle-outline',
        id: 'About',
    },*/
    {
        title: translate('Profile'),
        color: StyleConfig.color_white,
        icon: 'ios-person-outline',
        id: 'Profile',
    },/* {
        title: translate('CheckForUpdate'),
        color: StyleConfig.color_white,
        icon: 'ios-settings-outline',
        id: 'Update',
    },*/
];

class More extends Component {
    static navigationOptions = {
        headerTitle: translate('More'),
    };

    constructor(props) {
        super(props);
        this.state = {
            screen: {
                width: StyleConfig.screen_width,
                height: StyleConfig.screen_height
            }
        };

        Dimensions.addEventListener('change', (dimensions) => {
            // you get:
            //  dimensions.window.width
            //  dimensions.window.height
            //  dimensions.screen.width
            //  dimensions.screen.height
            this.setState({
                screen: {
                    width: dimensions.window.width,
                    height: dimensions.window.height
                }
            })
        });
    }

    render() {
        const { router, user } = this.props;
        return (
            <ScrollView
                style={styles.root}
                contentContainerStyle={styles.rootContainer}>
                {this.renderGrid()}
            </ScrollView>
        )
    }

    isLandscape() {
        return this.state.screen.width > this.state.screen.height;
    }

    renderSpacer() {
        return (
            <View style={styles.spacer}></View>
        )
    }

    getEmptyCount(size) {
        let rowCount = Math.ceil((this.state.screen.height - 20) / size);
        return rowCount * 2 - MainRoutes.length;
    }

    renderRoute(route, index) {

        const size = this.state.screen.width / 2;
        // const height = width;
        return (
            <TouchableHighlight
                key={index}
				onPress={() => {
					this.props.navigation.navigate(route.id);
				}}
                style={[{
                    width: size,
                    height: size,
                },
                CommonStyles.flexItemsMiddle,
                CommonStyles.flexItemsCenter,
                styles.cell
                ]}
                underlayColor={StyleConfig.touchable_press_color}>
                <View style={[
                    CommonStyles.flexColumn,
                    CommonStyles.flexItemsMiddle,
                    CommonStyles.flexItemsCenter,
                ]}>
                    <Icon name={route.icon}
                        size={40}
                        color={route.color}
                        style={[CommonStyles.m_b_2, CommonStyles.background_transparent]} />
                    <Text style={[CommonStyles.font_xs, CommonStyles.text_white]}>
                        {route.title}
                    </Text>
                </View>
            </TouchableHighlight>
        )
    }

    renderGrid() {
        let items = <View />;
        let size = this.state.screen.width / 2;
        let emptyCount = this.getEmptyCount(size);

        items = MainRoutes.map((route, index) => {
            return this.renderRoute(route, index)
        });


        for (let i = 0; i < emptyCount; i++) {
            items.push(<View key={'empty' + i} style={[{ height: size, width: size }, styles.empty]} />)
        }

        return items;
    }
}

const styles = StyleSheet.create({
    root: {
        backgroundColor: colors.primary,
    },
    rootContainer: {
        flexDirection: 'row',
        flexWrap: 'wrap',
    },
    cell: {
        borderWidth: StyleSheet.hairlineWidth,
        borderColor: colors.borderOnPrimary
    },
    spacer: {
        height: 10,
        backgroundColor: StyleConfig.panel_bg_color
    },
    empty: {
        borderWidth: StyleSheet.hairlineWidth,
        borderColor: colors.borderOnPrimary
    },
});

export default More;