import { createAction } from 'redux-actions';
import moment from 'moment';
import { USER_PROFILE } from '../constants'
import { getUserByUsernameSOAP } from '../api'
import handle from '../../../ExceptionHandler';


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
                    payload: user,
                });
            })
            .catch((error) => {
                handle(error);
                dispatch({
                    type: 'ERROR',
                    payload: error
                });
            })
    }
}