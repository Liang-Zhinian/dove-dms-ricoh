
import { handleActions } from 'redux-actions'
import { SAVE_ACCOUNT, LOGIN, LOGOUT, RENEW, VALID, ERROR, NEED_RELOADING } from '../constants'
import * as types from '../middlewares/authenticationTypes';

type State = {
  username: ?string,
  password: ?string,
  isAuthenticated: ?boolean,
  token: ?{
    sid: ?string,
    expires_date: ?string
  },
}

const initialState: State = {
  username: null,
  password: null,
  isAuthenticated: false,
  token: {
    sid: null,
    expires_date: null
  },
}

//you can do better here, I was just showing that you need to make a new copy
//of state. It is ok to deep copy of state. It will prevent unseen bugs in the future
//for better performance you can use immutableJS

//handleActions is a helper function to instead of using a switch case statement,
//we just use the regular map with function state attach to it.

export default handleActions(
  {
    [NEED_RELOADING]: (state: State = initialState, action) => {
      const {
        payload: {
          needReloading,
        }
      } = action

      return {
        ...state,
        needReloading,
      }
    },

    [SAVE_ACCOUNT]: (state: State = initialState, action) => {
      const {
        payload: {
          username,
          password,
        }
      } = action

      //because payload contains the id and we already know that we are about
      //to increment the value of that id, we modify only that value by one

      return {
        ...state,
        username,
        password,
      }
    },

    [LOGIN]: (state: State = initialState, action) => {
      const { payload: { username, password, token } } = action

      //because payload contains the id and we already know that we are about
      //to increment the value of that id, we modify only that value by one

      return {
        ...state,
        // username,
        // password,
        isAuthenticated: true,
        token,
      }
    },

    [LOGOUT]: (state: State = initialState, action) => {
      return {
        ...state,
        username: null,
        password: null,
        isAuthenticated: false,
        token: {
          sid: null,
          expires_date: null
        },
      }
    },

    [RENEW]: (state: State = initialState, action) => {
      const { payload: { token } } = action;
      return {
        ...state,
        isAuthenticated: true,
        token,
      }
    },

    [VALID]: (state: State = initialState, action) => {
      const { payload: { valid } } = action
      return {
        ...state,
        valid,
        isAuthenticated: valid,
      }
    },

    [types.LOGIN_SUCCESS]: (state: State = initialState, action) => {
      const { data } = action
      return {
        ...state,
        data
      }
    },

    [types.REFRESHING_TOKEN]: (state: State = initialState, action) => {
      const { refreshTokenPromise } = action
      return {
        ...state,
        refreshTokenPromise
      }
    },

    // Handle API request errors
    [ERROR]: (state: State = initialState, action) => { return { ...state }; },

  },
  initialState
)