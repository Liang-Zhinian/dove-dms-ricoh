import Toast from '../components/ToastModule';

export default function storage(state = {}, action) {
  const { payload } = action;
  switch (action.type) {
    case 'QUERY_USER':
      return { ...state, queryUserName: payload };
    case 'STORAGE_ERROR':
      console.log(payload);
      
      Toast.show(payload.message, Toast.SHORT);
      return { ...state, /*error: payload.message*/ };
    default:
      return state;
  }
}