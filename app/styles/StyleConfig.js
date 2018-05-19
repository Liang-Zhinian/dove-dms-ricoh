import {
    Image,
    StyleSheet,
    Dimensions
} from 'react-native';

const { height, width } = Dimensions.get('window');

// ===============================================
// style config
// ===============================================

export default StyleConfig = {
    
    color_primary: 'rgba(60, 177, 158, 1)',
    color_danger: 'rgba(199, 85, 74, 1)',
    color_warning: 'rgba(216, 196, 128, 1)',
    color_success: 'rgba(69, 190, 174, 1)',
    color_white: 'rgba(255, 255, 255, 1)',
    color_light: 'rgba(255, 255, 255, 0.6)',
    color_muted: 'rgba(0, 0, 0, 0.4)',
    color_gray: 'rgba(0, 0, 0, 0.6)',
    color_dark: 'rgba(0, 0, 0, 0.7)',
    color_black: 'rgba(0, 0, 0, 0.8)',
    color_transparent: 'transparent',

    font_eg: 24,
    font_lg: 20,
    font_md: 18,
    font_sm: 16,
    font_xs: 14,
    font_ms: 12,

    line_height_lg: 36,
    line_height_md: 26,
    line_height_sm: 24,

    space_0: 0,
    space_1: 5,
    space_2: 10,
    space_3: 15,
    space_4: 20,

    htmlRender_font: 16,
    htmlRender_color: 'rgba(48,59,71,1)',
    htmlRender_lineHeight: 28,
    htmlRender_spaceHeight: 15,

    header_height: 200,
    navbar_height: 70,
    bottomBar_height: 46,
    icon_size: 22,
    avatarSize_lg: 60,
    avatarSize_sm: 20,

    border_width: 0.5,
    border_radius: 2,
    border_color: 'rgba(0, 0, 0, 0.05)',
    panel_bg_color: 'rgba(0, 0, 0, 0.02)',
    touchable_press_color: 'rgba(0, 0, 0, 0.05)',
    touchable_press_opacity: 0.7,

    screen_width: width,
    screen_height: height,

    action_size: 50,
    action_offset_y: 20,
    action_offset_x: 20,
    action_color_primary: 'rgba(60, 177, 158, 0.5)',
    action_color_primary_active: 'rgba(199, 85, 74, 0.9)',
    action_color_danger: 'rgba(199, 85, 74, 0.5)',
    action_color_danger_active: 'rgba(199, 85, 74, 0.9)',
    action_background_color: 'rgba(255, 255, 255, 0.2)'
};