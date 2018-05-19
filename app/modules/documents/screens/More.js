import React, { Component } from 'react';
import {
    View,
    Text,
    StyleSheet,
    TouchableHighlight,
    Platform
} from 'react-native';
import Icon from 'react-native-vector-icons/Ionicons';
import { 
    ComponentStyles, 
    CommonStyles, 
    colors, 
    StyleConfig, 
} from '../styles';

const firstLineItems = [
    {
        title: 'Downloads',
        color: StyleConfig.color_white,
        icon: 'ios-cloud-download-outline',
        route: 'Downloads',
    }, {
        title: 'Account',
        color: StyleConfig.color_white,
        icon: 'ios-contact-outline',
        route: 'Account',
    }];

const secondLineItems = [{
    title: 'Settings',
    color: StyleConfig.color_white,
    icon: 'ios-settings-outline',
    route: 'Settings',
}, {
    title: 'About',
    color: StyleConfig.color_white,
    icon: 'ios-information-circle-outline',
    route: 'About',
}];

const thirdLineItems = [{
    title: 'Check for Update',
    color: StyleConfig.color_white,
    icon: 'ios-settings-outline',
    route: 'Update',
},];

class More extends Component {
    static navigationOptions = {
        // headerStyle: { backgroundColor: StyleConfig.color_primary },
        // headerTintColor: StyleConfig.textOnPrimary,
        headerTitle: 'More',
    };

    constructor(props) {
        console.log('constructor');
        super(props);
        this.state = {
            hasFocus: false
        };
        // this.shouldComponentUpdate = PureRenderMixin.shouldComponentUpdate.bind(this);
    }

    componentDidFocus() {
        this.setState({
            hasFocus: true
        });
    }

    onNavItemPress(item) {
        if (item && item.route) {
            const { navigate } = this.props.navigation;
            navigate(item.route);

        }
    }

    renderSpacer() {
        return (
            <View style={styles.spacer}></View>
        )
    }

    renderNavItem(item, index) {
        return (
            <TouchableHighlight
                key={index}
                onPress={() => this.onNavItemPress(item)}
                style={[CommonStyles.flex_1/*, CommonStyles.p_a_3*/]}
                underlayColor={StyleConfig.touchable_press_color}>
                <View style={[CommonStyles.flexColumn, CommonStyles.flexItemsMiddle, CommonStyles.flexItemsCenter, CommonStyles.border_t, CommonStyles.border_r, CommonStyles.border_b, CommonStyles.border_l, styles.cell]}>
                    <Icon name={item.icon}
                        size={40}
                        color={item.color}
                        style={[CommonStyles.m_b_2, CommonStyles.background_transparent]} />
                    <Text style={[CommonStyles.font_xs, CommonStyles.text_white]}>
                        {item.title}
                    </Text>
                </View>
            </TouchableHighlight>
        )
    }

    renderNavContent() {
        return (
            <View style={[styles.container]}>
                <View style={[CommonStyles.flexRow, styles.row]}>
                    {
                        firstLineItems && firstLineItems.map((nav, index) => {
                            return this.renderNavItem(nav, index)
                        })
                    }
                </View>
                {/*this.renderSpacer()*/}
                <View style={[CommonStyles.flexRow, styles.row]}>
                    {
                        secondLineItems && secondLineItems.map((nav, index) => {
                            return this.renderNavItem(nav, index)
                        })
                    }
                </View>
                {/*this.renderSpacer()*/}
                <View style={[CommonStyles.flexRow, styles.row]}>
                    {
                        thirdLineItems && thirdLineItems.map((nav, index) => {
                            return this.renderNavItem(nav, index)
                        })
                    }
                </View>
                {/*this.renderSpacer()*/}
            </View>
        )
    }

    renderContent() {
        return (
            <View>
                {this.renderNavContent()}
            </View>
        )
    }

    render() {
        const { router, user } = this.props;
        return (
            <View style={[ComponentStyles.container, styles.container]}>
                {this.renderContent()}
            </View >
        )
    }
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
        width: null,
        height: null,
        backgroundColor: colors.primary,
        // color: 'rgba(60, 177, 158, 1)'
    },
    row: {
        width: StyleConfig.screen_width,

        "borderTopColor": colors.borderOnPrimary,
        "borderTopWidth": 1,
        "borderRightColor": colors.borderOnPrimary,
        "borderRightWidth": 1,
        "borderBottomColor": colors.borderOnPrimary,
        "borderBottomWidth": 0,
        "borderLeftColor": colors.borderOnPrimary,
        "borderLeftWidth": 0,
    },
    lastRow: {
        "borderTopColor": colors.borderOnPrimary,
        "borderTopWidth": 1,
        "borderRightWidth": 1,
        "borderRightColor": colors.borderOnPrimary,
        "borderBottomWidth": 1,
        "borderBottomColor": colors.borderOnPrimary,
        "borderLeftColor": colors.borderOnPrimary,
        "borderLeftWidth": 0,
    },
    cell: {
        height: 100,
        "borderTopColor": colors.borderOnPrimary,
        "borderTopWidth": 0,
        "borderRightColor": colors.borderOnPrimary,
        "borderRightWidth": 0,
        "borderBottomColor": colors.borderOnPrimary,
        "borderBottomWidth": 0,
        "borderLeftColor": colors.borderOnPrimary,
        "borderLeftWidth": 1,
    },
    list_icon: {
        width: StyleConfig.icon_size
    },
    spacer: {
        height: 10,
        backgroundColor: StyleConfig.panel_bg_color
    }
});

export default More;