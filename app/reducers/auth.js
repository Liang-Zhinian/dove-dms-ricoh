import Toast from '../components/ToastModule';

const initialAuthState = { isLoggedIn: false };

export default function auth(state = initialAuthState, action) {
  //const { payload } = action;
  switch (action.type) {
    case 'Login':
      return { ...state, isLoggedIn: true, user: action.payload };
    case 'Logout':
      return { ...state, isLoggedIn: false, user: null };
    case 'ERROR':
      console.log(action.error);
      debugger;
      Toast.show(action.error.message, Toast.SHORT);
      return { ...state, /*error: payload.message*/ };
    default:
      return state;
  }
}