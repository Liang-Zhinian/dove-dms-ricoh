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
	DeviceEventEmitter,
	AsyncStorage,
	ScrollView
} from 'react-native';
import { connect } from 'react-redux';
import { DrawerNavigator, NavigationActions } from 'react-navigation'
import { default as Icon } from 'react-native-vector-icons/MaterialIcons';
import MaterialCommunityIcons from 'react-native-vector-icons/MaterialCommunityIcons';
import Octicons from 'react-native-vector-icons/Octicons';
import FontAwesome from 'react-native-vector-icons/FontAwesome';
import { default as Ionicons } from 'react-native-vector-icons/Ionicons';
import { HeaderButton } from './components/HeaderButtons';
import { login, logout, valid } from '../../../actions/auth';
import requireAuth from '../../../HOC/require_auth';
import { translate } from '../../../i18n/i18n';

import {
	ComponentStyles,
	CommonStyles,
	colors,
	StyleConfig,
} from '../styles';

var MainRoutes = [
	{
		title: translate('Documents'),
		color: StyleConfig.color_white,
		// icon: 'cloud',
		icon: (<Octicons name='file-submodule'
			size={36}
			color={StyleConfig.color_white}
			style={[CommonStyles.m_b_1, CommonStyles.background_transparent]} />),
		id: 'ExplorerTab',
	}, {
		title: translate('Search'),
		color: StyleConfig.color_white,
		icon: 'search',
		id: 'SearchTab',
	}, {
		title: translate('Downloads'),
		color: StyleConfig.color_white,
		icon: 'cloud-download',
		id: 'Downloads',
	}, {
		title: translate('Settings'),
		color: StyleConfig.color_white,
		icon: 'settings',
		id: 'MoreTab',
	},/* {
	title: translate('Storage'),
	color: StyleConfig.color_white,
	icon: (<FontAwesome name='pie-chart'
		size={36}
		color={StyleConfig.color_white}
		style={[CommonStyles.m_b_1, CommonStyles.background_transparent]} />),
	id: 'RepositoryUsage',
}, {
		title: translate('CheckedOutDocuments'),
		color: StyleConfig.color_white,
		// icon: 'ios-cash',
		icon: (<FontAwesome name='briefcase'
			size={36}
			color={StyleConfig.color_white}
			style={[CommonStyles.m_b_1, CommonStyles.background_transparent]} />),
		id: 'CheckedoutReport',
	}, {
		title: translate('LockedDocuments'),
		color: StyleConfig.color_white,
		// icon: 'delete',
		icon: (<MaterialCommunityIcons name='file-lock'
			size={36}
			color={StyleConfig.color_white}
			style={[CommonStyles.m_b_1, CommonStyles.background_transparent]} />),
		id: 'LockedReport',
	}, {
		title: translate('Profile'),
		color: StyleConfig.color_white,
		icon: 'account-circle',
		id: 'Profile',
	}, {
		title: translate('Settings'),
		color: StyleConfig.color_white,
		icon: 'settings',
		id: 'Settings',
	},*/
];

class Home extends Component {
	static navigationOptions = ({ navigation }) => {
		const { params = {} } = navigation.state;

		return {
			headerTitle: translate('Home'),
			/*
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
			),*/
			headerRight: (
				<View style={[
					CommonStyles.flexRow,
				]}>
					{/*
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
					</TouchableOpacity>*/}
					{params.isAuthenticated &&
						<HeaderButton
							onPress={params.onLogoutButtonPressed}
							text={translate('SignOut')}
						/>}
				</View>
			),
		}
	};

	constructor(props) {
		super(props);
		this.state = {
			layout: {
				width: Dimensions.get('window').width,
				height: Dimensions.get('window').height,
			}
		};

		this._bootstrapAsync();
	}

	// Fetch the token from storage then navigate to our appropriate place
	_bootstrapAsync = async () => {
		const { login, valid, isLoggedIn, token, username, password } = this.props;

		// if (username == 'admin') {
		// 	MainRoutes.push({
		// 		title: translate('Log'),
		// 		color: StyleConfig.color_white,
		// 		icon: (<FontAwesome name='exclamation-triangle'
		// 			size={36}
		// 			color={StyleConfig.color_white}
		// 			style={[CommonStyles.m_b_1, CommonStyles.background_transparent]} />),

		// 		id: 'Log',
		// 	})
		// }

		let isValid = await valid(token.sid);
		if (!isValid) {
			await login(username, password);
		}
	};

	_signOutAsync = async () => {
		debugger;
		const { logout, isLoggedIn, token, username, password } = this.props;

		await logout(token.sid);

	}

	componentWillMount() {
	}

	componentDidMount() {
		const { navigation } = this.props;
		// We can only set the function after the component has been initialized
		navigation.setParams({
			onLogoutButtonPressed: this._signOutAsync.bind(this),
			isAuthenticated: this.props.isLoggedIn,
		});
	}

	componentWillReceiveProps(nextProps) {
		const { navigation } = nextProps;
		if (nextProps.isLoggedIn != this.props.isLoggedIn)
			navigation.setParams({
				isAuthenticated: nextProps.isLoggedIn,
			});
	}

	componentWillUnmount() {
	}

	render() {
		return (
			<View
				style={[
					CommonStyles.flex_1,
					styles.root
				]}>
				{this.renderLogo()}
				<ScrollView
					style={[
						styles.root,
						CommonStyles.flex_4,
					]}
					contentContainerStyle={styles.rootContainer}
					onLayout={this.onLayout.bind(this)}
				>
					{this.renderGrid()}
				</ScrollView>
			</View>
		)
	}

	onLayout(e) {
		this.setState({
			layout: {
				width: Dimensions.get('window').width,
				height: Dimensions.get('window').height,
			}
		})
	}

	renderLogo() {
		return (
			<View style={[
				// CommonStyles.flex_1,
				CommonStyles.flexItemsCenter,
				CommonStyles.flexItemsMiddle,
				{ height: 100 }
			]}>
				<Image
					style={[styles.image, { width: this.state.layout.width - 50, }]}
					resizeMode={Image.resizeMode.contain}
					source={require("../assets/logo2.png")}
				/>

			</View>
		)
	}

	isLandscape() {
		return this.state.layout.width > this.state.layout.height;
	}

	renderSpacer() {
		return (
			<View style={styles.spacer}></View>
		)
	}

	getEmptyCount(size) {
		let rowCount = Math.ceil((this.state.layout.height - 20) / size);
		return rowCount * 3 - MainRoutes.length;
	}

	renderRoute(route, index) {
		const size = this.state.layout.width / 3;
		let { icon, title, color } = route;
		let ic = icon;
		if (typeof icon === 'string')
			ic = <Icon name={icon}
				size={36}
				color={color}
				style={[
					CommonStyles.m_b_1,
					CommonStyles.background_transparent
				]} />;


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
					{ic}
					<Text style={[CommonStyles.font_xs, CommonStyles.text_white]}>
						{title}
					</Text>
				</View>
			</TouchableHighlight>
		)
	}

	renderGrid() {
		let items = <View />;
		let size = this.state.layout.width / 3;
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

const mapStateToProps = (state) => {
	return {
		username: state.auth.username,
		password: state.auth.password,
		token: state.auth.token || {},
		isLoggedIn: state.auth.isLoggedIn,
	};
}
const mapDispatchToProps = (dispatch) => {
	return {
		valid: (sid) => dispatch(valid(sid)),
		logout: (user) => dispatch(logout(user)),
		login: (username, password) => dispatch(login(username, password)),
	}
}
export default connect(mapStateToProps, mapDispatchToProps)(Home);