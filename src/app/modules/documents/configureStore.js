// @flow



/* eslint-disable global-require */
/* eslint-disable no-undef */
import { 
    createStore, 
    applyMiddleware, 
    compose, 
    combineReducers 
} from 'redux';
import thunk from 'redux-thunk';
import logger from 'redux-logger';
import { persistStore, autoRehydrate } from 'redux-persist';
import { AsyncStorage } from 'react-native';
import { NAME } from './constants'
import reducers from './reducers';
import {authMiddleware} from './middlewares';

const reducer = combineReducers({
    [NAME]:reducers
});

var isDebuggingInChrome = __DEV__ && !!window.navigator.userAgent;
let middleware = [authMiddleware, thunk];
// debugger;
if (__DEV__) {
    const reduxImmutableStateInvariant = require('redux-immutable-state-invariant').default();
    middleware = [...middleware, reduxImmutableStateInvariant, logger];
} else {
    middleware = [...middleware];
}

var enhancer = compose(
    applyMiddleware(...middleware),
    autoRehydrate()
);

export default function configureStore(onComplete: ?() => void) {
    const store = createStore(
        reducer,
        undefined,
        enhancer
    );
    persistStore(store, { storage: AsyncStorage }, onComplete);
    if (isDebuggingInChrome) {
        window.store = store;
    }
    return store;
}
