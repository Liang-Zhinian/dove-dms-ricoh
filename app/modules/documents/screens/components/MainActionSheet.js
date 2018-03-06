import React, { Component } from 'react';
import {
    StyleSheet,
    View,
    TouchableOpacity,
    Text,
} from 'react-native';
import ImagePicker from 'react-native-image-picker';
import ActionSheet from './ActionSheet';

class MainActionSheet extends Component {

    constructor(props) {
        super(props);
        this.state = {
            modalVisible: true,
        };
    }

    upload = () => {
        const that = this;
        that.toggleActionSheet(that.showImagePicker)
    }

    showImagePicker = () => {
        const that = this;
        // More info on all the options is below in the README...just some common use cases shown here
        var options = {
            title: 'Select Avatar',
            customButtons: [
                //{ name: 'create-folder', title: 'Create Folder' },
            ],
            storageOptions: {
                skipBackup: true,
                path: 'images'
            },
            mediaType: 'mixed',
        };

        //
        // The first arg is the options object for customization (it can also be null or omitted for default options),
        // The second arg is the callback which sends object: response (more info below in README)
        //
        ImagePicker.showImagePicker(options, (response) => {
            // console.log('Response = ', response);

            that.toggleActionSheet();
            if (response.didCancel) {
                // console.log('User cancelled image picker');
            }
            else if (response.error) {
                // console.log('ImagePicker Error: ', response.error);
            }
            else if (response.customButton) {
                // console.log('User tapped custom button: ', response.customButton);
            }
            else {
                // debugger;
                let source = response;

                // You can also display the image using data:
                // let source = { uri: 'data:image/jpeg;base64,' + response.data };

            }
        });

    }

    toggleActionSheet(callback) {
        console.log('toggleActionSheet');
        const modalVisible = this.state.modalVisible;
        this.setState({
            modalVisible: !modalVisible,
        }, () => { callback && callback(); })
    }

    componentDidMount() {
    }

    componentWillReceiveProps(nextProps) {
    }

    render() {
        const { modalVisible } = this.props;
        return (
            <View>
                <ActionSheet modalVisible={this.state.modalVisible && modalVisible} onCancel={() => { this.toggleActionSheet() }}>
                    <View style={styles.actionSheet}>
                        <TouchableOpacity style={styles.button} onPress={this.props.onCreateFolderButtonPressed}>
                            <Text style={styles.buttonText}>
                                Create Folder
                                </Text>
                        </TouchableOpacity>
                        <TouchableOpacity style={styles.button} onPress={() => { this.upload(); }}>
                            <Text style={styles.buttonText}>
                                Upload
                                </Text>
                        </TouchableOpacity>
                        {/* <TouchableOpacity style={styles.button} onPress={() => { }}>
                        <Text style={styles.buttonText}>
                            Take Photo or Video
                                </Text>
                    </TouchableOpacity> */}
                        <TouchableOpacity style={[styles.button, styles.lastButton]} onPress={() => { }}>
                            <Text style={styles.buttonText}>
                                Record Audio
                                </Text>
                        </TouchableOpacity>
                    </View>
                </ActionSheet>
            </View>
        );
    }
}

export default MainActionSheet;

const styles = StyleSheet.create({
    actionSheet: {
        height: 230,
        backgroundColor: 'white',
        borderColor: 'white',
        borderWidth: 1,
        borderRadius: 20,
        marginBottom: 10,
        alignSelf: 'stretch',
        justifyContent: 'center'
    },
    buttonText: {
        color: '#0069d5',
        alignSelf: 'center',
        fontSize: 18
    },
    button: {
        height: 46,
        backgroundColor: 'white',
        borderColor: 'gray',
        borderWidth: 1,
        borderLeftWidth: 0,
        borderRightWidth: 0,
        borderBottomWidth: 0,
        //borderRadius: 6,
        marginBottom: 0,
        alignSelf: 'stretch',
        justifyContent: 'center'
    },
    lastButton: {
        borderBottomWidth: 1,
    }
});