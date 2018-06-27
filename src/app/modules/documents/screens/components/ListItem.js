
import React from 'react';
import {
    View,
    TouchableHighlight,
    TouchableOpacity,
    StyleSheet,
    Image,
    Text,
    ProgressViewIOS,
    ProgressBarAndroid,
    Platform,

} from 'react-native';
import MaterialIcons from 'react-native-vector-icons/MaterialIcons';
import MaterialCommunityIcons from 'react-native-vector-icons/MaterialCommunityIcons';

import { connect } from 'react-redux';
import moment from 'moment';
import DoveTouchable from '../../../../components/DoveTouchable';
import InfoIcon from './InfoIcon';
import CrossIcon from './CrossIcon';
import getIcon from '../../lib/icon';
import { CommonStyles } from '../../styles';
import * as actions from '../../actions';
import { NAME } from '../../constants';

// type Props = {
//     id: number,
//     title: string,
//     description: string,
//     type: string,
//     onPress: () => void,
//     onPressInfo: () => void,
//     onPressAction: () => void
// }

class ListItem extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            checked: false,
        };
    }

    toggleCheckbox() {
        const { data, onPressCheckbox } = this.props;
        const checked = this.state.checked;
        this.setState({
            checked: !checked,
        }, () => {
            onPressCheckbox(data, !checked);
        });
    }

    componentWillReceiveProps(nextProps){
        // if (typeof nextProps.props.isEditMode != 'undefined' && nextProps.props.isEditMode != this.props.isEditMode && !nextProps.props.isEditMode){
            this.setState({
                checked: false
            })
        // }
    }

    render() {
        const {
            data,
            onPress,
            onPressInfo,
            progress,
            document,
            isEditMode,
            onPressCheckbox,
            onPressCross,
            isDownloading,
            infoIconVisible = true,
            onPressAction,
        } = this.props;

        const {
            id,
            title,
            description,
            type,
        } = data;

        const touched = document && document.id == id;

        return (

            <View
                style={{
                    flex: 1,
                    flexDirection: 'row',
                }}
            >
                <TouchableOpacity
                    onPress={isDownloading ? null : (isEditMode ? this.toggleCheckbox.bind(this) : onPress)}
                    style={{
                        flex: 1,
                        flexDirection: 'row',
                        paddingTop: 10,
                    }}
                >
                    <View
                        style={[
                            CommonStyles.m_l_2, {
                                display: isEditMode ? 'flex' : 'none',
                            }, styles.center]}>
                        <MaterialCommunityIcons
                            name={this.state.checked ? 'checkbox-marked-circle' : 'checkbox-blank-circle-outline'}
                            size={30} />
                    </View>
                    <View style={[CommonStyles.m_l_2, styles.center, {
                    }]}>
                        {getIcon(type)}
                    </View>

                    <View style={[{
                        flex: 1,
                    }]}>
                        <View style={{
                            flexDirection: 'row',
                        }}>
                            <View
                                style={[CommonStyles.m_l_2, {
                                    flex: 1,
                                    marginBottom: 15
                                }]}
                            >
                                <Text style={styles.title}>{title}</Text>
                                <Text style={styles.details}>{description}</Text>
                                {Platform.OS === 'ios' && <ProgressViewIOS
                                    progress={touched ? progress : 0}
                                    style={[{ display: touched ? 'flex' : 'none' }, styles.progress]}
                                />}
                                {Platform.OS === 'android' && <ProgressBarAndroid
                                    progress={touched ? progress : 0}
                                    style={[{ display: touched ? 'flex' : 'none' }, styles.progress]}
                                    styleAttr='Horizontal'
                                />}
                            </View>
                        </View>
                        <View style={styles.separator} />
                    </View>
                </TouchableOpacity>
                <View
                    style={[
                        CommonStyles.m_l_2,
                        CommonStyles.m_r_2,
                        styles.center, {
                        }]}>
                    <InfoIcon onPress={onPressAction} />
                    <InfoIcon style={{ display: touched && isDownloading || !infoIconVisible ? 'none' : 'flex' }}
                        onPress={isDownloading ? null : onPressInfo} />
                    <CrossIcon style={{ display: touched && (false || isDownloading) ? 'flex' : 'none' }}
                        onPress={onPressCross} />
                </View>
            </View>
        )
    }
}

// export default ListItem;

function select(store) {
    return {
        document: store[NAME].document.document,
        progress: store[NAME].document.progress,
        isEditMode: store[NAME].document.isEditMode,
        isDownloading: store[NAME].document.isDownloading,
    };
}

function dispatch(dispatch) {
    return {
    }
};

export default connect(select, dispatch)(ListItem);

const styles = StyleSheet.create({
    separator: {
        height: 1,
        backgroundColor: '#dddddd'
    },
    title: {
        fontWeight: 'bold',
        fontSize: 15,
        color: '#000',
        marginBottom: 8
    },
    details: {
        fontSize: 12,
        color: 'gray',
    },
    progress: {
        marginTop: 5,
    },
    center: {
        justifyContent: 'center',
        alignContent: 'center',
    }
});
