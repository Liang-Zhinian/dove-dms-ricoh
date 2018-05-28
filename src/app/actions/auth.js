import React from 'react';
import {
    AsyncStorage
} from 'react-native';
import { createAction } from 'redux-actions';
import moment from 'moment';
import { loginSOAP, logoutSOAP, validSOAP, renewSOAP } from '../modules/documents/api';
import { Documents } from '../modules';

export type ActionAsync = (dispatch: Function, getState: Function) => void

//each action should have the following signiture.
//  {
//     type: <type of action>,        type is required
//     payload: <the actual payload>  payload is optional. if you don't
//                                    have anything to send to reducer,
//                                    you don't need the payload.
//  }

/*
 *this action tell the reducer which account with specified username & password needs to be
 *verified.
 */
export const login = (username: string, password: string): ActionAsync => {
    return async (dispatch, getState) => {
        if (username && password) {
            await loginSOAP(username, password)
                .then(sid => {
                    let expires_date = moment();
                    expires_date.add(25, 'minutes');
                    expires_date = expires_date.format('YYYY-MM-DD HH:mm:ss')

                    const user = {
                        username,
                        password,
                        token: {
                            sid,
                            expires_date,
                        }
                    };

                    dispatch({
                        type: 'Login',
                        payload: user
                    })

                    dispatch({
                        type: `${Documents.NAME}/LOGIN`,
                        payload: user
                    })

                    dispatch({
                        type: `${Documents.NAME}/SAVE_ACCOUNT`,
                        payload: user
                    })

                })
                .catch((error) => {
                    dispatch({
                        type: 'ERROR',
                        payload: error
                    })
                })
        } else {

            dispatch({
                type: 'ERROR',
                payload: new Error('Invalid username or password! ' + username + '>>' + password)
            })
        }
    }
}

export const logout = (sid: string): ActionAsync => {
    return async (dispatch, getState) => {

        dispatch({ type: 'Logout' });
        debugger;

        sid && await logoutSOAP(sid)
            .then(result => {

                dispatch({
                    type: `${Documents.NAME}/LOGOUT`,
                    payload: {
                        token: { sid: null, expires_date: null },
                        username: null,
                        password: null,
                    }
                });

            })
            .catch((error) => {
                dispatch({
                    type: 'ERROR',
                    payload: error
                })
            })
    }
}

export const valid = (sid: string): ActionAsync => {
    return async (dispatch, getState) => {
        let valid = sid && await validSOAP(sid)
            .then(valid => {
                return valid == true;
            })
            .catch((error) => {
                dispatch({
                    type: 'ERROR',
                    payload: error
                });
            })
        return valid;
    }
}

export const initializeApp = (sid: string) => {
    return async (dispatch) => {
        dispatch({ type: 'INITIALIZE_APP' })

        const isValid = await validSOAP(sid)
            .then(valid => {
                return valid == true;
            })
            .catch((error) => {
                dispatch({
                    type: 'ERROR',
                    payload: error
                });
            })

        if (!isValid) return dispatch({ type: 'ERROR', payload: 'Sesion has expired!' })

    }
}