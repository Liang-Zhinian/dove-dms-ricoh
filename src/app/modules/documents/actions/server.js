import { createAction } from 'redux-actions';
import moment from 'moment';
import { SAVE_ACCOUNT, LOGIN, LOGOUT, RENEW, VALID, ERROR } from '../constants'
import { loginSOAP, logoutSOAP, validSOAP, renewSOAP } from '../api'
import handle from '../../../ExceptionHandler';


export type Action = {
  type: string,
  payload?: {
    username: string,
    password: string,
    token: any
  }
}

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
  return (dispatch, getState) => {


    loginSOAP(username, password)
      .then(sid => {
        let expires_date = moment();
        expires_date.add(25, 'minutes');
        expires_date = expires_date.format('YYYY-MM-DD HH:mm:ss')
        dispatch({
          type: LOGIN,
          payload: {
            // username,
            // password,
            token: {
              sid,
              expires_date,
            },
          }
        });

        dispatch(saveAccount(username, password));
      })
      .catch((error) => {
        handle(error);
        dispatch({
          type: ERROR,
          payload: {error}
        })
      })
  }
}