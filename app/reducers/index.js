import { combineReducers } from 'redux';

import nav from './nav';
import { Documents } from '../modules'


export default combineReducers({
    //every modules reducer should be define here
    nav,
    [Documents.NAME]: Documents.reducer
})
