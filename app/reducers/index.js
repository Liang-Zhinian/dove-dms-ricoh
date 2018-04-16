import { combineReducers } from 'redux';
import { NavigationActions } from 'react-navigation';

import auth from './auth';
import nav from './nav';
import security from './security';
import { Documents } from '../modules'

const AppReducer = combineReducers({
  nav,
  auth,
  security,
  [Documents.NAME]: Documents.reducer
});

export default AppReducer;
