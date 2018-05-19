
'use strict';

import React from 'react';
import {
  TouchableHighlight,
  TouchableNativeFeedback,
  Platform,
} from 'react-native';

function DoveTouchableIOS(props: Object): ReactElement {
  return (
    <TouchableHighlight
      accessibilityTraits="button"
      underlayColor="#3C5EAE"
      {...props}
    />
  );
}

const DoveTouchable = Platform.OS === 'android'
  ? TouchableNativeFeedback
  : DoveTouchableIOS;

export default DoveTouchable;