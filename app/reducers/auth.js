import Toast from '../components/ToastModule';

const initialAuthState = { isLoggedIn: false };

export default function auth(state = initialAuthState, action) {
  //const { payload } = action;
  switch (action.type) {
    case 'Login':
      return { ...state, isLoggedIn: true, user: action.payload };
    case 'Logout':
      return { ...state, isLoggedIn: false, user: null };
    case 'AUTH_ERROR':
      console.log(action.payload);
      Toast.show(action.payload.message, Toast.LONG);
      return { ...state, /*error: payload.message*/ };
    default:
      return state;
  }
}