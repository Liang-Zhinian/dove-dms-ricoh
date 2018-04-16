import Toast from '../components/ToastModule';

const initialAuthState = { isLoggedIn: false };

export default function auth(state = initialAuthState, action) {
  const { payload } = action;
  switch (action.type) {
    case 'Login':
      return { ...state, isLoggedIn: true, user: payload };
    case 'Logout':
      return { ...state, isLoggedIn: false, user: null };
    case 'ERROR':
      console.log(payload);
      
      Toast.show(payload.message, Toast.SHORT);
      return { ...state, /*error: payload.message*/ };
    default:
      return state;
  }
}