import {
    Image,
    StyleSheet,
    Dimensions
} from 'react-native';
const { height, width } = Dimensions.get('window');

import StyleConfig from './StyleConfig';

// ===============================================
// component styles
// ===============================================

export default ComponentStyles = StyleSheet.create({
    
    
        // container
    
        container: {
            flex: 1,
            backgroundColor: StyleConfig.color_white,
            flexDirection: 'column'
        },
    
        message_container: {
            marginVertical: 60
        },
    
        pending_container: {
            position: 'absolute',
            left: 0,
            right: 0,
            top: 0,
            bottom: 0,
            margin: 0,
            width: StyleConfig.screen_width,
            height: StyleConfig.screen_height,
            backgroundColor: 'rgba(255,255,255, 0.1)',
            justifyContent: 'center',
            alignItems: 'center'
        },
    
        // button
    
        btn: {
            paddingVertical: StyleConfig.space_2,
            borderRadius: 2,
            width: 120
        },
    
        btn_sm: {
            paddingVertical: StyleConfig.space_1,
            paddingHorizontal: StyleConfig.space_1,
            width: 60
        },
    
        btn_icon: {
            width: StyleConfig.icon_size * 2
        },
    
        btn_block: {
            width: width - (StyleConfig.space_3 * 2)
        },
    
        btn_white: {
            backgroundColor: StyleConfig.color_white
        },
    
        btn_white_outline: {
            borderWidth: .5,
            borderColor: StyleConfig.color_white
        },
    
        btn_primary: {
            backgroundColor: StyleConfig.color_primary
        },
    
        btn_primary_outline: {
            borderWidth: .5,
            borderColor: StyleConfig.color_primary
        },
    
        btn_danger: {
            backgroundColor: StyleConfig.color_danger
        },
    
        btn_danger_outline: {
            borderWidth: .5,
            borderColor: StyleConfig.color_danger
        },
    
        btn_dark: {
            backgroundColor: StyleConfig.color_dark
        },
    
        btn_dark_outline: {
            borderWidth: .5,
            borderColor: StyleConfig.color_dark
        },
    
        btn_warning: {
            backgroundColor: StyleConfig.color_warning
        },
    
        btn_warning_outline: {
            borderWidth: .5,
            borderColor: StyleConfig.color_warning
        },
    
        btn_success: {
            backgroundColor: StyleConfig.color_success
        },
    
        btn_success_outline: {
            borderWidth: .5,
            borderColor: StyleConfig.color_success
        },
    
        btn_text: {
            color: StyleConfig.color_white,
            fontSize: StyleConfig.font_sm,
            textAlign: "center"
        },
    
        // imgage
    
        avatar: {
            width: StyleConfig.avatarSize_lg,
            height: StyleConfig.avatarSize_lg,
            borderRadius: StyleConfig.avatarSize_lg / 2
        },
    
        avatar_mini: {
            width: StyleConfig.avatarSize_sm,
            height: StyleConfig.avatarSize_sm,
            borderRadius: StyleConfig.avatarSize_sm / 2
        },
    
        header_img: {
            width: StyleConfig.screen_width,
            height: StyleConfig.header_height
        },
    
        header_backdrop: {
            position: 'absolute',
            left: 0,
            right: 0,
            bottom: 0,
            top: 0,
            backgroundColor: 'rgba( 0, 0, 0, 0.3)',
            height: StyleConfig.header_height
        },
    
        // position
    
        pos_absolute: {
            position: 'absolute',
            left: 0,
            right: 0
        },
    
        // list
    
        list: {
            padding: StyleConfig.space_3,
            backgroundColor: StyleConfig.color_white,
            borderBottomWidth: StyleConfig.border_width,
            borderBottomColor: StyleConfig.border_color
        },
    
        // modal
    
        modal_backdrop: {
            position: 'absolute',
            top: 0,
            left: 0,
            bottom: 0,
            right: 0,
            backgroundColor: 'rgba(0, 0, 0, 0.8)'
        },
    
        modal_container: {
            flex: 1,
            alignItems: 'center',
            flexDirection: 'column',
            justifyContent: 'center',
            alignSelf: "center",
            width: width - 60,
            overflow: 'hidden',
            backgroundColor: 'transparent',
        },
    
        modal_header: {
            backgroundColor: 'transparent'
        },
    
        modal_header_img: {
            width: width - 60,
            height: 100,
            borderTopLeftRadius: StyleConfig.border_radius,
            borderTopRightRadius: StyleConfig.border_radius,
        },
    
        modal_body: {
            width: width - 60,
            padding: StyleConfig.space_4,
            backgroundColor: StyleConfig.color_white,
            borderBottomWidth: .5,
            borderBottomColor: StyleConfig.border_color
        },
    
        modal_footer: {
            padding: StyleConfig.space_4,
            alignItems: 'center',
            backgroundColor: StyleConfig.color_white,
            borderBottomLeftRadius: StyleConfig.border_radius,
            borderBottomRightRadius: StyleConfig.border_radius,
        },
    
        modal_button: {
            width: width - 60 - (StyleConfig.space_4 * 2)
        },
    
        input_control: {
            paddingVertical: StyleConfig.space_3,
            borderBottomWidth: .5,
            borderBottomColor: StyleConfig.color_muted
        },
    
        input: {
            fontSize: StyleConfig.font_sm,
            padding: StyleConfig.space_0,
            width: width - 50,
            height: 30,
            lineHeight: 26,
            color: StyleConfig.color_dark
        },
    
        textarea: {
            padding: StyleConfig.space_0,
            fontSize: StyleConfig.font_sm,
            color: StyleConfig.color_dark,
            width: StyleConfig.screen_width - (StyleConfig.space_3 * 2),
            textAlign: "left",
            textAlignVertical: "top"
        },
    
        bar_container: {
            position: 'absolute',
            bottom: 0,
            left: 0,
            right: 0,
            width: StyleConfig.screen_width,
            height: StyleConfig.bottomBar_height,
            flexDirection: 'row',
            alignItems: 'center',
            borderTopWidth: .5,
            borderTopColor: StyleConfig.border_color,
            justifyContent: 'space-around',
            backgroundColor: 'rgba(255, 255, 255, 0.95)'
        },
    
        bar_item: {
            flex: 1,
            height: StyleConfig.bottomBar_height,
            flexDirection: 'row',
            alignItems: 'center',
            justifyContent: 'center'
        },
    
        bar_item_badge: {
            justifyContent: 'center',
            alignItems: 'center',
            top: 0,
            position: 'absolute'
        },
    
        bar_item_badge_text: {
            color: StyleConfig.color_danger,
            fontSize: StyleConfig.font_sm,
            fontStyle: 'italic',
            fontWeight: 'bold'
        },
    
        panel_bg: {
            backgroundColor: StyleConfig.panel_bg_color
        },
    
        button_icon: {
            color: StyleConfig.color_white,
            backgroundColor: StyleConfig.color_transparent,
            fontSize: StyleConfig.icon_size
        },
    
        action_button_container: {
            borderWidth: 0,
            paddingVertical: 4,
            paddingHorizontal: 10,
            backgroundColor: StyleConfig.color_primary
        },
        action_button_text: {
            color: StyleConfig.color_white,
            fontSize: 14,
        },
        action_button_icon: {
            fontSize: StyleConfig.icon_size + 6
        }
    });
    