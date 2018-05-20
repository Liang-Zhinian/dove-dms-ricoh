//
//  RNQuickLookManager.m
//  dove
//
//  Created by Jack Leung on 10/11/2017.
//  Copyright © 2017 Facebook. All rights reserved.
//

#import "RNQuickLookManager.h"
#import "RNQuickLookView.h"

#import <QuickLook/QuickLook.h>

@implementation RNQuickLookManager

RCT_EXPORT_MODULE()

- (UIView *) view  {
  return [[RNQuickLookView alloc] init];
}

RCT_EXPORT_VIEW_PROPERTY(url, NSString)
RCT_EXPORT_VIEW_PROPERTY(assetFileName, NSString)


@end
