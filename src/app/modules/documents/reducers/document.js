
import { handleActions } from 'redux-actions'
import {
  CHANGING_DOCUMENT,
  DONE_CHANGING_DOCUMENT,

  FETCHING_LIST,
  DONE_FETCHING_LIST,

  PROGRESS,
  UPLOADING_DOCUMENT,
  DONE_UPLOADING_DOCUMENT,

  IS_EDIT_MODE,

  IS_DOWNLOADING,

  UPDATE_DOCUMENT,
  DONE_UPDATING_DOCUMENT
} from '../constants'

type State = {}

const initialState: State = {}

//you can do better here, I was just showing that you need to make a new copy
//of state. It is ok to deep copy of state. It will prevent unseen bugs in the future
//for better performance you can use immutableJS

//handleActions is a helper function to instead of using a switch case statement,
//we just use the regular map with function state attach to it.

export default handleActions(
  {

    [UPDATE_DOCUMENT]: (state: State = initialState, action) => {
      const { payload: { isLoading } } = action

      //because payload contains the id and we already know that we are about
      //to increment the value of that id, we modify only that value by one

      return {
        ...state,
        isLoading,
      }
    },

    [DONE_UPDATING_DOCUMENT]: (state: State = initialState, action) => {
      const { payload: { isLoading } } = action

      //because payload contains the id and we already know that we are about
      //to increment the value of that id, we modify only that value by one

      return {
        ...state,
        isLoading,
      }
    },

    [FETCHING_LIST]: (state: State = initialState, action) => {
      const { payload: { isLoading, needReload } } = action

      //because payload contains the id and we already know that we are about
      //to increment the value of that id, we modify only that value by one

      return {
        ...state,
        isLoading,
        needReload
      }
    },

    [DONE_FETCHING_LIST]: (state: State = initialState, action) => {
      const { payload: { isLoading, needReload } } = action

      //because payload contains the id and we already know that we are about
      //to increment the value of that id, we modify only that value by one

      return {
        ...state,
        isLoading,
        needReload
      }
    },

    [CHANGING_DOCUMENT]: (state: State = initialState, action) => {
      const { payload: { isLoading } } = action

      //because payload contains the id and we already know that we are about
      //to increment the value of that id, we modify only that value by one

      return {
        ...state,
        isLoading,
      }
    },

    [DONE_CHANGING_DOCUMENT]: (state: State = initialState, action) => {
      const { payload: { isLoading, document } } = action

      //because payload contains the id and we already know that we are about
      //to increment the value of that id, we modify only that value by one

      return {
        ...state,
        isLoading,
        document,
      }
    },

    [PROGRESS]: (state: State = initialState, action) => {
      const { payload: { progress } } = action
      return {
        ...state,
        progress,
      }
    },

    [UPLOADING_DOCUMENT]: (state: State = initialState, action) => {
      const { payload: { isLoading, uploaded, needReload } } = action;
      return {
        ...state,
        isLoading,
        progress: 0,
        uploaded,
        needReload,
      }
    },

    [DONE_UPLOADING_DOCUMENT]: (state: State = initialState, action) => {
      const { payload: { isLoading, uploaded, needReload } } = action;
      return {
        ...state,
        isLoading,
        progress: 0,
        uploaded,
        needReload,
      }
    },

    [IS_EDIT_MODE]: (state: State = initialState, action) => {
      const { payload: { isEditMode } } = action;
      return {
        ...state,
        isEditMode,
      }
    },

    [IS_DOWNLOADING]: (state: State = initialState, action) => {
      const { payload: { isDownloading } } = action;
      return {
        ...state,
        isDownloading,
      }
    },

    // Handle API request errors
    ['ERROR']: (state: State = initialState, action) => { return initialState; },

  },
  initialState
)