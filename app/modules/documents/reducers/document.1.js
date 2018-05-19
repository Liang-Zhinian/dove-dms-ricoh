
import { handleActions } from 'redux-actions'
import { 
  CHANGING_FOLDER,
  DONE_CHANGING_FOLDER,
  CHANGING_DOCUMENT,
  DONE_CHANGING_DOCUMENT,
  
  PROGRESS, 
  UPLOAD_DOCUMENT, 
  UPLOADED_DOCUMENT, 
  RELOADED 
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
    [FETCHING_ROOT_FOLDER]: (state: State = initialState, action) => {
      const { payload: { isFetching } } = action

      //because payload contains the id and we already know that we are about
      //to increment the value of that id, we modify only that value by one

      return {
        ...state,
        isFetching,
      }
    },

    [DONE_FETCHING_ROOT_FOLDER]: (state: State = initialState, action) => {
      const { payload: { isFetching } } = action

      //because payload contains the id and we already know that we are about
      //to increment the value of that id, we modify only that value by one

      return {
        ...state,
        isFetching,
      }
    },

    [FOLDER_CHANGED]: (state: State = initialState, action) => {
      const { payload: { folderId } } = action

      //because payload contains the id and we already know that we are about
      //to increment the value of that id, we modify only that value by one

      return {
        ...state,
        folderId,
      }
    },

    [FETCHING_LIST]: (state: State = initialState, action) => {
      const { payload: { isFetching } } = action

      //because payload contains the id and we already know that we are about
      //to increment the value of that id, we modify only that value by one

      return {
        ...state,
        isFetching,
      }
    },

    [DONE_FETCHING_LIST]: (state: State = initialState, action) => {
      const { payload: { isFetching, dataList } } = action

      //because payload contains the id and we already know that we are about
      //to increment the value of that id, we modify only that value by one

      return {
        ...state,
        isFetching,
        dataList,
      }
    },

    [CHOOSE_DOCUMENT]: (state: State = initialState, action) => {
      const { payload: { docId, fileName } } = action

      //because payload contains the id and we already know that we are about
      //to increment the value of that id, we modify only that value by one

      return {
        ...state,
        docId,
        fileName,
      }
    },

    [CHOOSE_FILE]: (state: State = initialState, action) => {
      const { payload: { fileName, content } } = action

      //because payload contains the id and we already know that we are about
      //to increment the value of that id, we modify only that value by one

      return {
        ...state,
        fileName,
        content,
      }
    },

    [GET_CONTENT]: (state: State = initialState, action) => {
      const { payload: { content, docId, fileName } } = action
      return {
        ...state,
        content,
        docId,
        fileName,
      }
    },

    [PROGRESS]: (state: State = initialState, action) => {
      const { payload: { progress, docId, fileName } } = action
      return {
        ...state,
        progress,
        docId,
        fileName,
      }
    },

    [UPLOAD_DOCUMENT]: (state: State = initialState, action) => {
      const { payload: { needReload, uploaded } } = action;
      return {
        ...state,
        needReload,
        uploaded,
        progress: 0,
      }
    },

    [UPLOADED_DOCUMENT]: (state: State = initialState, action) => {
      const { payload: { needReload, uploaded } } = action;
      return {
        ...state,
        needReload,
        uploaded,
        progress: 0,
      }
    },

    [RELOADED]: (state: State = initialState, action) => {
      const { payload: { needReload } } = action;
      return {
        ...state,
        needReload,
      }
    },

    // Handle API request errors
    ['ERROR']: (state: State = initialState, action) => { return initialState; },

  },
  initialState
)