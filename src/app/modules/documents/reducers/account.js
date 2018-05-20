
import { handleActions } from 'redux-actions'
import { SAVE_ACCOUNT, LOGIN, LOGOUT, RENEW, VALID, ERROR } from '../constants'
import * as types from '../middlewares/authenticationTypes';

type State = {
  isAuthenticated: boolean,
  username: ?string,
  password: ?string,
  token: ?{
    sid: ?string,
    expires_date: ?string
  },
}

const initialState: State = {
  isAuthenticated: false,
  username: null,
  password: null,
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
    [SAVE_ACCOUNT]: (state: State = initialState, action) => {
      const { payload: { username, password } } = action

      //because payload contains the id and we already know that we are about
      //to increment the value of that id, we modify only that value by one

      return {
        ...state,
        username,
        password,
      }
    },

    [LOGIN]: (state: State = initialState, action) => {
      const { payload: { username, password, token, isAuthenticated } } = action

      //because payload contains the id and we already know that we are about
      //to increment the value of that id, we modify only that value by one

      return {
        ...state,
        // username,
        // password,
        isAuthenticated,
        token,
      }
    },

    [LOGOUT]: (state: State = initialState, action) => {
      const { payload: { username, password, token } } = action
      return {
        ...state,
        username,
        password,
        isAuthenticated: false,
        token,
      }
    },


    [RENEW]: (state: State = initialState, action) => {
      const { payload: { token } } = action;
      return {
        ...state,
        token,
      }
    },

    [VALID]: (state: State = initialState, action) => {
      const { payload: { valid } } = action
      return {
        ...state,
        isAuthenticated: valid,
        valid,
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
    [ERROR]: (state: State = initialState, action) => { return { ...state, }; },

  },
  initialState
)