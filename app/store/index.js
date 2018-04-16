// @flow



/* eslint-disable global-require */
/* eslint-disable no-undef */
import { createStore, applyMiddleware, compose } from 'redux';
import thunk from 'redux-thunk';
import logger from 'redux-logger';
import { persistStore, autoRehydrate } from 'redux-persist';
import { AsyncStorage } from 'react-native';
import AppReducer from '../reducers';

import { middleware } from '../utils/redux';


var isDebuggingInChrome = __DEV__ && !!window.navigator.userAgent;
let middlewares = [thunk, middleware];
// debugger;
if (__DEV__) {
	const reduxImmutableStateInvariant = require('redux-immutable-state-invariant').default();
	middlewares = [...middlewares, reduxImmutableStateInvariant, logger];
} else {
	middlewares = [...middlewares];
}

var enhancer = compose(
	applyMiddleware(...middlewares),
	autoRehydrate()
);

export default function configureStore(onComplete: ?() => void) {
	const store = createStore(
		AppReducer,
		{},
		enhancer
	);
	persistStore(store, { storage: AsyncStorage }, onComplete);
	if (isDebuggingInChrome) {
		window.store = store;
	}
	return store;
}
