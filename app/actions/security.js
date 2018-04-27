import React from 'react';
import {
    AsyncStorage
} from 'react-native';
import { createAction } from 'redux-actions';
import moment from 'moment';
import { getUserByUsernameSOAP } from '../modules/documents/api';
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
export const getUserByUsername = (sid: string, username: string): ActionAsync => {
    return async (dispatch, getState) => {

        await getUserByUsernameSOAP(sid, username)
            .then(user => {
                console.log(user);
                dispatch({
                    type: `USER_PROFILE`,
                    payload: user
                })

                dispatch({
                    type: `${Documents.NAME}/USER_PROFILE`,
                    payload: user
                })

            })
            .catch((error) => {
                dispatch({
                    type: 'SECURITY_ERROR',
                    payload: error
                })
            })

    }
}