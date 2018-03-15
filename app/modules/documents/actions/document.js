import {
  NAME,

  IS_EDIT_MODE,

  CHANGING_DOCUMENT,
  DONE_CHANGING_DOCUMENT,

  FETCHING_LIST,
  DONE_FETCHING_LIST,

  PROGRESS,
  UPLOADING_DOCUMENT,
  DONE_UPLOADING_DOCUMENT,

  IS_DOWNLOADING,
} from '../constants'
import OpenFile from 'react-native-doc-viewer';
import { createDocumentWithProgressSOAP } from '../api'
import Base64 from '../lib/Base64'
import { alert } from '../lib/alert';

export type Action = {
  type: string,
  payload?: {}
}

export type ActionAsync = (dispatch: Function, getState: Function) => void

export const chooseDocument = (document): ActionAsync => {
  return (dispatch, getState) => {

    dispatch({
      type: CHANGING_DOCUMENT,
      payload: {
        isLoading: true,
      }
    });

    dispatch({
      type: DONE_CHANGING_DOCUMENT,
      payload: {
        document,
        isLoading: false,
      }
    });
  }
}

export const upload = (sid: string, document: any, content: string): ActionAsync => {
  return (dispatch, getState) => {
    dispatch(uploading());

    createDocumentWithProgressSOAP(
      sid,
      document,
      content,
      (percent) => {
        dispatch({
          type: PROGRESS,
          payload: {
            progress: percent,
          }
        })
        // if (percent === 1)
        //   dispatch(doneUploading());
      })
      .then(([response, responseText]) => {
        // debugger;
        console.log(response);

        dispatch(doneUploading());
      })
      .catch(error => {
        // console.error(error);
        alert('Upload document', error.message);
        dispatch({
          type: DONE_UPLOADING_DOCUMENT,
          payload: {
            isLoading: false,
            progress: 0,
            uploaded: false,
            needReload: false,
          }
        })
      });
  }
};

export const uploading = (): Action => {
  return {
    type: UPLOADING_DOCUMENT,
    payload: {
      isLoading: true,
      progress: 0,
      uploaded: false,
      needReload: false,
    }
  };
};

export const doneUploading = (): Action => {
  return {
    type: DONE_UPLOADING_DOCUMENT,
    payload: {
      isLoading: false,
      progress: 0,
      uploaded: true,
      needReload: true,
    }
  };
};

export const fetchingList = (): Action => {
  return {
    type: FETCHING_LIST,
    payload: {
      isLoading: true,
      needReload: false,
    }
  };
};

export const doneFetchingList = (): Action => {
  return {
    type: DONE_FETCHING_LIST,
    payload: {
      isLoading: false,
      needReload: false,
    }
  };
};

export const updateProgress = (percent): Action => {
  return {
    type: PROGRESS,
    payload: {
      progress: percent,
    }
  }
}

export const toggleEditMode = (isEditMode): Action => {
  return {
    type: IS_EDIT_MODE,
    payload: {
      isEditMode,
    }
  }
}

export const updateDownloadStatus = (isDownloading): Action => {
  return {
    type: IS_DOWNLOADING,
    payload: {
      isDownloading,
    }
  }
}