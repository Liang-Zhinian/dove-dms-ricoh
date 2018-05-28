import { combineReducers } from 'redux';

import { NAME } from '../constants'
// import nav from './nav';
import account from './account';
import document from './document';
import folder from './folder';
import login from './login';
import security from './security';

const reducers = {
    // login,
    account,
    document,
    folder,
    security
};
export default combineReducers(reducers);