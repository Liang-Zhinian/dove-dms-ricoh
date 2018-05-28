import Toast from '../components/ToastModule';

const initialAuthState = { isLoggedIn: false };

export default function auth(state = initialAuthState, action) {
  const { payload } = action;
  switch (action.type) {
    case 'Login':
      return { ...state, isLoggedIn: true, ...payload };
    case 'Logout':
      return { ...state, isLoggedIn: false, };
    case 'ERROR':
      Toast.show(payload.message, Toast.SHORT);
      return { ...state };
    default:
      return state;
  }
}