import { NAME } from './constants';
// import App from './App';
import reducer from './reducers';
import * as actions from './actions';
import * as Navigation from './navigation';
import Screens from './screens';
import Routes from './config/routes';

//to reduce the number of bugs, make sure not to export action types.
//action types are internal only and only actions and reducer should access them

export default {
  NAME,
  // App,
  reducer,
  actions,
  Navigation,
  // Screens,
  Routes
}
