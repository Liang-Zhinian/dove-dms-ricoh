
import { handleActions } from 'redux-actions'
import {
  LOGIN_INITIALIZE,
  LOGIN_SUCCESS,
  LOGIN_FAIL,
  LOGOUT
} from '../constants'

type State = {}

const initialState: State = { isAuthenticated: false }

//you can do better here, I was just showing that you need to make a new copy
//of state. It is ok to deep copy of state. It will prevent unseen bugs in the future
//for better performance you can use immutableJS

//handleActions is a helper function to instead of using a switch case statement,
//we just use the regular map with function state attach to it.

export default handleActions(
  {
    [LOGIN_INITIALIZE]: (state: State = initialState, action) => {
      const { payload: { } } = action

      //because payload contains the id and we already know that we are about
      //to increment the value of that id, we modify only that value by one

      return {
        ...state,
        isAuthenticated: false,
      }
    },

    [LOGIN_SUCCESS]: (state: State = initialState, action) => {
      const { payload: { user } } = action

      //because payload contains the id and we already know that we are about
      //to increment the value of that id, we modify only that value by one

      return {
        ...state,
        user,
        isAuthenticated: true,
      }
    },

    [LOGIN_FAIL]: (state: State = initialState, action) => {
      const { payload: { error } } = action

      //because payload contains the id and we already know that we are about
      //to increment the value of that id, we modify only that value by one

      return {
        ...state,
        error,
        isAuthenticated: false,
      }
    },

    // [LOGOUT]: (state: State = initialState, action) => {
    //   const { payload: { } } = action

    //   //because payload contains the id and we already know that we are about
    //   //to increment the value of that id, we modify only that value by one

    //   return {
    //     ...state,
    //     isAuthenticated: false,
    //   }
    // },

    // Handle API request errors
    ['ERROR']: (state: State = initialState, action) => { return initialState; },

  },
  initialState
)