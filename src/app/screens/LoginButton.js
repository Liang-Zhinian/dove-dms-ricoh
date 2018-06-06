import React, { Component } from 'react';
import {
    StyleSheet,
} from 'react-native';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { login, logout } from '../actions/auth';
import Toast from '../components/ToastModule';
import DoveButton from '../components/DoveButton';
import RicohAuthAndroid from '../components/RCTRicohAuthAndroid';

class LoginButton extends Component {
    static defaultProps = { _isMounted: PropTypes.boolean };

    static navigationOptions = {
        header: null
    };

    constructor(props) {
        super(props);
        this.state = {
            isLoading: false,
        };
    }

    componentDidMount() {
        this._isMounted = true;
    }

    componentWillUnmount() {
        this._isMounted = false;
    }

    componentWillMount() {
    }

    render() {
        if (this.state.isLoading)
            return (
                <DoveButton
                    style={[styles.button, this.props.style]}
                    caption="Please wait..."
                    onPress={() => { }}
                />
            )
        return (
            <DoveButton
                style={[styles.button, this.props.style]}
                caption="Sign in!"
                onPress={this._signInAsync}
            />
        )

    }

    _signInAsync = async () => {
        const { dispatch, login } = this.props;

        this.setState({ isLoading: true });
        try {
            await Promise.race([
                login(this.props.username, this.props.password),
                timeout(15000),
            ]);
        } catch (e) {
            const message = e.message || e;
            if (message !== 'Timed out' && message !== 'Canceled by user') {
                alert(message);
                console.warn(e);
            }
            return;
        } finally {
            this._isMounted && this.setState({ isLoading: false });
        }
    };
}

async function timeout(ms: number): Promise {
    return new Promise((resolve, reject) => {
        setTimeout(() => reject(new Error('Timed out')), ms);
    });
}

function select(state) {
    return {
    };
}

function dispatch(dispatch) {
    return {
        login: (username, password) => dispatch(login(username, password)),
        logout: (sid) => dispatch(logout(sid)),
    }
};

export default connect(select, dispatch)(LoginButton);

const styles = StyleSheet.create({
    button: {}
});