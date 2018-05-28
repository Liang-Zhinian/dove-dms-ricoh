
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

type Props = {
    id: number,
    title: string,
    description: string,
    type: string,
    onPress: () => void,
    onPressInfo: () => void,
    // progress: number
}

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
            infoIconVisible = true
        } = this.props;

        const {
            id,
            title,
            description,
            type,
        } = data;

        const touched = document && document.id == id;

        return (
            <View style={{
                flex: 1,
                flexDirection: 'row',
                justifyContent: 'center',
                alignContent: 'center',
                alignItems: 'center',
            }}>
                <TouchableOpacity
                    style={[
                        CommonStyles.m_l_1,
                        { display: isEditMode ? 'flex' : 'none' }
                    ]}
                    onPress={this.toggleCheckbox.bind(this)}
                >
                    <MaterialCommunityIcons
                        name={this.state.checked ? 'checkbox-marked-circle' : 'checkbox-blank-circle-outline'}
                        size={30} />
                </TouchableOpacity>
                <TouchableOpacity
                    // onPress={isDownloading ? null : onPress}
                    underlayColor={isDownloading ? '' : 'grey'}
                    //backgroundColor={isDownloading ? 'grey' : 'white'}
                    // testID='docuCell'
                    // accessibilityLabel='docuCell'
                    style={{
                        flex: 1,
                        justifyContent: 'center',
                        alignContent: 'center',
                    }}
                >
                    <View>
                        <View style={styles.rowContainer}>
                            <View style={CommonStyles.m_r_1}>
                                {getIcon(type)}
                            </View>
                            <View style={styles.textContainer}>
                                <Text style={styles.title}>{title}</Text>
                                <Text style={styles.content}>{description}</Text>
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
                            <InfoIcon style={{ display: touched && isDownloading || !infoIconVisible ? 'none' : 'flex' }}
                                onPress={isDownloading ? null : onPressInfo} />
                            <CrossIcon style={{ display: touched && (false || isDownloading) ? 'flex' : 'none' }}
                                onPress={onPressCross} />
                        </View>
                        <View style={styles.separator} />
                    </View>
                </TouchableOpacity>
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
        // 发送行为
    }
};

export default connect(select, dispatch)(ListItem);

const styles = StyleSheet.create({
    thumb: {
        width: 50,
        height: 50,
        marginRight: 10
    },
    content: {
        fontSize: 12,
        color: 'black',
    },
    textContainer: {
        flex: 1,
    },
    separator: {
        height: 1,
        backgroundColor: '#dddddd'
    },
    title: {
        fontSize: 15,
        color: '#656565'
    },
    rowContainer: {
        flexDirection: 'row',
        padding: 10
    },
    progress: {
        marginTop: 5,
    }
});
