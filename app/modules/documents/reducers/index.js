import { combineReducers } from 'redux';

import { NAME } from '../constants'
// import nav from './nav';
import account from './account';
import document from './document';

const reducers = {
    // nav,
    account,
    document,
};
export default combineReducers(reducers);