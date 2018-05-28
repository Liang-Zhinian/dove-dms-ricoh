import {  
    UPDATE_FOLDER,
    DONE_UPDATING_FOLDER
  } from '../constants'
  import { updateFolder } from '../api'
  import handle from '../../../ExceptionHandler';
  
  export type Action = {
    type: string,
    payload?: {}
  }
  
  export type ActionAsync = (dispatch: Function, getState: Function) => void
  
  export const update = (username, password, folder): ActionAsync => {
    return (dispatch, getState) => {
  
      dispatch({
        type: UPDATE_FOLDER,
        payload: {
          isLoading: true,
        }
      });
  
      updateFolder(username, password, folder)
        .then(res => {
          dispatch({
            type: DONE_UPDATING_FOLDER,
            payload: {
              isLoading: false,
            }
          });
        })
        .catch(error => {
          handle(error);
        })
    }
  }