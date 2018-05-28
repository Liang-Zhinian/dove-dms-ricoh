import Toast from '../components/ToastModule';

export default function security(state = {}, action) {
  const { payload } = action;
  switch (action.type) {
    case 'USER_PROFILE':
      return { ...state, user: payload };
    case 'ERROR':
      Toast.show(payload.message, Toast.SHORT);
      return { ...state };
    default:
      return state;
  }
}