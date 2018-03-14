import React, { Component } from 'react';
import {
	View,
	Text,
	StyleSheet,
	TouchableHighlight,
	Platform,
	Dimensions,
	Image,
	TouchableOpacity,
    AsyncStorage,
    DeviceEventEmitter,
} from 'react-native';
// import { DrawerNavigator } from 'react-navigation'
import { default as Icon } from 'react-native-vector-icons/MaterialIcons';
import MaterialCommunityIcons from 'react-native-vector-icons/MaterialCommunityIcons';
import Octicons from 'react-native-vector-icons/Octicons';
import FontAwesome from 'react-native-vector-icons/FontAwesome';
import { default as Ionicons } from 'react-native-vector-icons/Ionicons';
import { connect } from 'react-redux';
import * as actions from '../actions';
import { NAME } from '../constants';
import RicohAuthAndroid from '../../../components/RCTRicohAuthAndroid';

import {
	ComponentStyles,
	CommonStyles,
	colors,
	StyleConfig,
} from '../styles';

const firstLineItems = [{
	title: 'My Documents',
	color: StyleConfig.color_white,
	// icon: 'cloud',
	icon: (<Octicons name='file-submodule'
		size={36}
		color={StyleConfig.color_white}
		style={[CommonStyles.m_b_1, CommonStyles.background_transparent]} />),
	route: 'ExplorerTab',
},
{
	title: 'Search',
	color: StyleConfig.color_white,
	icon: 'search',
	route: 'SearchTab',
}];

const secondLineItems = [{
	title: 'Downloads',
	color: StyleConfig.color_white,
	icon: 'cloud-download',
	route: 'Downloads',
}, {
	title: 'My Storage',
	color: StyleConfig.color_white,
	icon: (<FontAwesome name='pie-chart'
		size={36}
		color={StyleConfig.color_white}
		style={[CommonStyles.m_b_1, CommonStyles.background_transparent]} />),
	route: 'RepositoryUsage',
},];

const thirdLineItems = [
	{
		title: 'Documents Checked Out',
		color: StyleConfig.color_white,
		// icon: 'ios-cash',
		icon: (<FontAwesome name='briefcase'
			size={36}
			color={StyleConfig.color_white}
			style={[CommonStyles.m_b_1, CommonStyles.background_transparent]} />),
		route: 'CheckedoutReport',
	},
	{
		title: 'Documents Locked',
		color: StyleConfig.color_white,
		// icon: 'delete',
		icon: (<MaterialCommunityIcons name='file-lock'
			size={36}
			color={StyleConfig.color_white}
			style={[CommonStyles.m_b_1, CommonStyles.background_transparent]} />),
		route: 'LockedReport',
	}];

const fourthLineItems = [
	{
		title: 'Scan',
		color: StyleConfig.color_white,
		icon: 'account-circle',
		route: 'Scan',
	}, {
		title: 'Settings',
		color: StyleConfig.color_white,
		icon: 'settings',
		route: 'Settings',
	},];

class Home extends Component {
	static navigationOptions = {
		// headerStyle: { backgroundColor: colors.primary },
		// headerTintColor: colors.textOnPrimary,
		headerTitle: 'Home',
		headerLeft: (
			<TouchableOpacity
				style={{ marginLeft: 14 }}
				accessibilityLabel='bell'
			>
				<MaterialCommunityIcons
					name='bell-outline'
					size={24}
					style={{ color: colors.textOnPrimary }}
				/>
			</TouchableOpacity>
		),
		headerRight: (
			<View style={[
				// CommonStyles.headerRight,
				CommonStyles.flexRow,
				// CommonStyles.flexItemsMiddle, 
				// CommonStyles.flexItemsBetween,
			]}>
				<TouchableOpacity
					style={{ marginRight: 14 }}
					accessibilityLabel='info'
				>
					<Icon
						name='info-outline'
						size={24}
						style={{ color: colors.textOnPrimary }}
					/>
				</TouchableOpacity>
				<TouchableOpacity
					style={{ marginRight: 14 }}
					accessibilityLabel='help'
				>
					<Icon
						name='help-outline'
						size={24}
						style={{ color: colors.textOnPrimary }}
					/>
				</TouchableOpacity>
			</View>
		),
	}

	constructor(props) {
		console.log('constructor');
		super(props);
		this.state = {
            loginStatus: null,
            user: null,
			hasFocus: false,
			layout: {
				width: Dimensions.get('window').width,
				height: Dimensions.get('window').height,
			}
		};

		this.onLayout = this.onLayout.bind(this);
		// this.shouldComponentUpdate = PureRenderMixin.shouldComponentUpdate.bind(this);
	}


    componentWillMount() {
        var that = this;

        DeviceEventEmitter.addListener('onLoginStatusReceived', function (e) {
            that.setState({ loginStatus: e.loginStatus });
        });

        DeviceEventEmitter.addListener('onEntryInfoReceived', function (e) {
            let entryInfo = JSON.parse(e.entryInfo);

            that.setState({ user: entryInfo });

            AsyncStorage
                .getItem(entryInfo.loginUserName)
                .then(data => {
                    if (!!data){
                        //alert('User data from AsyncStorage: ' + data);
                        return JSON.parse(data);
                    }

                    return null;
                })
                .then(user => {
                    if (user != null) {
                        that.props.saveAccount(user.username, user.password);
                        that.props.login(user.username, user.password);
                        // navigate to Explorer screen
                        that.props.navigation.navigate('Explorer');
                        
                    } else {
                        //alert('Please register your account!');
                        // navigate to Registration screen
                        that.props.navigation.navigate('Registration', {key: entryInfo.loginUserName});
                    }
                });

            // alert(entryInfo);
        });

        RicohAuthAndroid.getAuthState()
            .then((msg) => {
                console.log('success!!')
            }, (error) => {
                console.log('error!!')
            });

    }

    componentWillReceiveProps(nextProps) {
        // if (!nextProps.authenticated) this.props.navigation.navigate('Login')
    }

    componentWillUpdate(nextProps, nextState) {
        if (nextState.loginStatus == 'LOGOUT') this.props.navigation.navigate('Account')
    }

	componentDidFocus() {
		this.setState({
			hasFocus: true
		});
	}

	render() {
		const { router, user } = this.props;
		return (
			<View
				onLayout={this.onLayout}
				style={[styles.container, { flexDirection: 'column' }]}
			>
				{/*this.renderContent()*/}
				{this.renderLogo()}
				{this.renderNavContent()}
			</View>
		)
	}

	onNavItemPress(item) {
		if (item && item.route) {
			const { navigate } = this.props.navigation;
			navigate(item.route);

		}
	}

	onLayout(e) {
		this.setState({
			layout: {
				width: Dimensions.get('window').width,
				height: Dimensions.get('window').height,
			}
		})
	}

	renderSpacer() {
		return (
			<View style={styles.spacer}></View>
		)
	}

	renderNavItem(item, index) {
		let icon;
		if (typeof item.icon === 'string')
			icon = <Icon name={item.icon}
				size={36}
				color={item.color}
				style={[CommonStyles.m_b_1, CommonStyles.background_transparent]} />;
		else
			icon = item.icon;
		return (
			<TouchableHighlight
				key={index}
				onPress={() => this.onNavItemPress(item)}
				style={[CommonStyles.flex_1,
				CommonStyles.p_a_2,
				CommonStyles.border_t,
				CommonStyles.border_r,
				CommonStyles.border_b,
				CommonStyles.border_l,
				CommonStyles.flexItemsCenter,
				styles.cell]}
				underlayColor={StyleConfig.touchable_press_color}>
				<View style={[CommonStyles.flexColumn,
				CommonStyles.flexItemsMiddle,
				CommonStyles.flexItemsCenter,]}>
					{icon}
					<Text style={[CommonStyles.font_xs, CommonStyles.text_white]}>
						{item.title}
					</Text>
				</View>
			</TouchableHighlight>
		)
	}

	renderLogo() {
		return (
			<View style={[
				CommonStyles.flex_1,
				CommonStyles.flexItemsCenter,
				CommonStyles.flexItemsMiddle,
			]}>
				<Image
					style={[styles.image, { width: this.state.layout.width - 50, }]}
					resizeMode={Image.resizeMode.contain}
					source={require("../assets/logo2.png")}
				/>

			</View>
		)
	}

	renderNavContent() {
		return (
			<View style={[CommonStyles.flex_4, CommonStyles.flexColumn]}>
				<View style={[CommonStyles.flex_1, CommonStyles.flexRow, styles.row]}>
					{
						firstLineItems && firstLineItems.map((nav, index) => {
							return this.renderNavItem(nav, index)
						})
					}
				</View>
				{/*this.renderSpacer()*/}
				<View style={[CommonStyles.flex_1, CommonStyles.flexRow, styles.row]}>
					{
						secondLineItems && secondLineItems.map((nav, index) => {
							return this.renderNavItem(nav, index)
						})
					}
				</View>
				{/*this.renderSpacer()*/}
				<View style={[CommonStyles.flex_1, CommonStyles.flexRow, styles.row, styles.lastRow]}>
					{
						thirdLineItems && thirdLineItems.map((nav, index) => {
							return this.renderNavItem(nav, index)
						})
					}
				</View>
				{/*this.renderSpacer()*/}
				<View style={[CommonStyles.flex_1, CommonStyles.flexRow, styles.row, styles.lastRow]}>
					{
						fourthLineItems && fourthLineItems.map((nav, index) => {
							return this.renderNavItem(nav, index)
						})
					}
				</View>

				{this.renderSpacer()}
			</View>
		)
	}

	renderContent() {
		return (
			<View style={[/*CommonStyles.flex_1, CommonStyles.flexSelfTop,*/ { borderColor: 'grey', borderWidth: 2 }]}>
				{/*this.renderLogo()*/}
				{this.renderNavContent()}
				{/*<Text>Hello World</Text>*/}
			</View>
		)
	}
}

// 获取 state 变化
const mapStateToProps = (state) => {
    return {
        // 获取 state 变化
        isLoggedIn: state[NAME].account.isLoggedIn,
        username: state[NAME].account.username,
        password: state[NAME].account.password,
        sid: state[NAME].account.sid
    }
};

// 发送行为
const mapDispatchToProps = (dispatch) => {
    return {
        // 发送行为
        login: (username, password) => dispatch(actions.login(username, password)),
        saveAccount: (username, password) => dispatch(actions.saveAccount(username, password)),
    }
};

export default connect(
    mapStateToProps,
    mapDispatchToProps,
    null,
    {
        withRef: true
    }
)(Home);

const styles = StyleSheet.create({
	container: {
		flex: 1,
		//alignItems: 'center',
		//justifyContent: 'center',
		//width: null,
		//height: null,
		backgroundColor: colors.primary,
	},
	row: {
		//flex: 1,
		//width: StyleConfig.screen_width,

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
		// height: 100,
		"borderTopColor": colors.borderOnPrimary,
		"borderTopWidth": 0,
		"borderRightColor": colors.borderOnPrimary,
		"borderRightWidth": 0,
		"borderBottomColor": colors.borderOnPrimary,
		"borderBottomWidth": 0,
		"borderLeftColor": colors.borderOnPrimary,
		"borderLeftWidth": 1,
		backgroundColor: '#0d7cd1'
	},
	list_icon: {
		width: StyleConfig.icon_size
	},
	spacer: {
		height: 10,
		backgroundColor: StyleConfig.background_transparent
	},
	hamburgerButton: {
		marginLeft: 14
	},
	image: {
		//flex: 1,
		//marginLeft: 30,
		//marginRight: 30,
		// marginTop: 0,
		// marginBottom: 0,
		// width: Dimensions.get("window").width - 50,
		// height: 365 * (Dimensions.get("window").width - 150) / 651,
	},
	welcome: {
		fontSize: 20,
		textAlign: 'center',
		color: '#fff',
		margin: 5,
	},
	instructions: {
		fontSize: 12,
		textAlign: 'center',
		color: '#fff',
		marginBottom: 5,
	},
});
