
'use strict';

import React, {Component} from 'react';
import ReactNative, {StyleSheet, Dimensions} from 'react-native';
import DoveColors from './DoveColors';

export function Text({style, ...props}: Object): ReactElement {
  return <ReactNative.Text style={[styles.font, style]} {...props} />;
}

export function Heading1({style, ...props}: Object): ReactElement {
  return <ReactNative.Text style={[styles.font, styles.h1, style]} {...props} />;
}

export function Paragraph({style, ...props}: Object): ReactElement {
  return <ReactNative.Text style={[styles.font, styles.p, style]} {...props} />;
}

const scale = Dimensions.get('window').width / 375;

function normalize(size: number): number {
  return Math.round(scale * size);
}

const styles = StyleSheet.create({
  font: {
    fontFamily: 'Arial',
  },
  h1: {
    fontSize: normalize(24),
    lineHeight: normalize(27),
    color: DoveColors.darkText,
    fontWeight: 'bold',
    letterSpacing: -1,
  },
  p: {
    fontSize: normalize(15),
    lineHeight: normalize(23),
    color: DoveColors.lightText,
  },
});