import { createAction } from 'redux-actions';
import moment from 'moment';
import { SAVE_ACCOUNT, LOGIN, LOGOUT, RENEW, VALID, ERROR, NEED_RELOADING } from '../constants';
import { loginSOAP, logoutSOAP, validSOAP, renewSOAP } from '../api';
import { setItem } from '../../../services/storageService';
import { storageKey } from '../env';
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
          payload: { error }
        })
      })
  }
}

export const logout = (sid: string, navigation: any): ActionAsync => {
  return (dispatch, getState) => {

    logoutSOAP(sid)
      .then(result => {
        dispatch({ type: 'Logout' });

        dispatch({
          type: LOGOUT,
          payload: {
            token: { sid: null, expires_date: null },
            username: null,
            password: null,
          }
        });
      })
      .catch((error) => {
        handle(error);
        dispatch({
          type: ERROR,
          payload: { error }
        })
      })
  }
}

export const renew = (sid: string) => async (dispatch, getState) => {
  // Make API call and dispatch appropriate actions when done

  const response = await renewSOAP(sid)
    .then(response => {

      let expires_date = moment();
      expires_date.add(25, 'minutes');
      expires_date = expires_date.format('YYYY-MM-DD hh:mm:ss')
      dispatch({
        type: RENEW,
        payload: {
          token: {
            sid,
            expires_date,
          },
        }
      })
    })
    .catch((error) => {
      handle(error);
      dispatch({
        type: ERROR,
        payload: { error }
      })
    })
}

export const valid = (sid: string) => async (dispatch, getState) => {
  // Make API call and dispatch appropriate actions when done

  validSOAP(sid)
    .then(valid => {
      dispatch({
        type: VALID,
        payload: {
          valid: valid === 'true'
        }
      });
    })
    .catch((error) => {
      handle(error);
      dispatch({
        type: ERROR,
        payload: { error }
      });
    })

}

export const saveAccount = (username: string, password: string, server: string, https: boolean, port: int): Action => {

  setItem(storageKey.DOCUMENT_SERVER, { server, https, port });
  setNeedReloading(true);

  return {
    type: SAVE_ACCOUNT,
    payload: {
      username,
      password,
    }
  };
}

export const setNeedReloading = (needReloading: boolean): ActionAsync => {
  return (dispatch, getState) => {
    dispatch({
      type: NEED_RELOADING,
      payload: {
        needReloading,
      }
    });
  }
}

