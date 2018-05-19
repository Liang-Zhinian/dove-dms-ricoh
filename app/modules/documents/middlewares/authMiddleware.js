import * as types from './authenticationTypes';
import {renewSOAP} from '../api';
import { NAME } from '../constants';

export default function authMiddleware({ dispatch, getState }) {
    // debugger;
    return (next) => (action) => {
        // debugger;
        if (typeof action === 'function') {
            let state = getState();
            if (!state) {
                // debugger;
                if (state[NAME].account.token.sid && isExpired(state[NAME].account.token)) {
                    // make sure we are not already refreshing the token
                    if (!state[NAME].account.refreshTokenPromise) {
                        return refreshToken(dispatch, state).then(() => next(action));
                    } else {
                        return state[NAME].account.refreshTokenPromise.then(() => next(action));
                    }

                }
            }
        }

        return next(action);
    }
}

function isExpired(token) {
    let currentTime = new Date();
    let expires_date = new Date(token.expires_date);
    return currentTime > expires_date;
}

function refreshToken(dispatch, state) {
    let refreshTokenPromise = renewSOAP(state[NAME].account.token.sid)
        .then(([response, responseText]) => {
            dispatch({
                type: types.DONE_REFRESHING_TOKEN
            });

            dispatch({
                type: types.LOGIN_SUCCESS,
                data: response
            });

            dispatch({
                type: types.SET_HEADER,
                header: {
                }
            });

            return response ? refreshTokenPromise.resolve(response) : refreshTokenPromise.reject({
                message: 'could not refresh token'
            })
        })
        .catch(ex => {
            console.log('exception refresh_token', ex);

            dispatch({
                type: types.DONE_REFRESHING_TOKEN,
            });
            dispatch({
                type: types.LOGIN_FAILED,
                exception: ex
            });
        });

        dispatch({
            type: types.REFRESHING_TOKEN,
            // we want to keep track of token promise in the state so that we don't try to refresh the token again while refreshing is in progress
            refreshTokenPromise
        });

        return refreshTokenPromise;
}