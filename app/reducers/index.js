import { combineReducers } from 'redux';
import { NavigationActions } from 'react-navigation';

import auth from './auth';
import auth_ext from './auth_ext';
import nav from './nav';
import security from './security';
import storage from './storage';
import { Documents } from '../modules'

const AppReducer = combineReducers({
  nav,
  auth,
  auth_ext,
  security,
  storage,
  [Documents.NAME]: Documents.reducer
});

export default AppReducer;
