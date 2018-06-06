import Toast from '../components/ToastModule';

const initialAuthState = {
    userChecking: false
};

export default function auth_ext(state = initialAuthState, action) {
    switch (action.type) {
        case 'CHECKING_USER':
            return { ...state, userChecking: true, };
        case 'DONE_CHECKING_USER':
            return { ...state, userChecking: false, };
        default:
            return state;
    }
}