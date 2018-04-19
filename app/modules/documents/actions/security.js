import { createAction } from 'redux-actions';
import moment from 'moment';
import { USER_PROFILE, ERROR } from '../constants'
import { getUserByUsernameSOAP } from '../api'


export type Action = {
    type: string,
    payload?: {
        user: any
    }
}

export type ActionAsync = (dispatch: Function, getState: Function) => void

export const getUserByUsername = (sid: string, username: string): ActionAsync => {
    return async (dispatch, getState) => {
        return await getUserByUsernameSOAP(sid, username)
            .then(user => {
                return dispatch({
                    type: USER_PROFILE,
                    payload: {user},
                });
            })
            .catch((error) => {
                dispatch({
                    type: ERROR,
                    payload: { error }
                });
            })
    }
}