//
//  RNQuickLookView.h
//  dove
//
//  Created by Jack Leung on 10/11/2017.
//  Copyright © 2017 Facebook. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface RNQuickLookView : UIView {
  NSString* _url;
  NSString* _assetFileName;
}

- (instancetype)initWithPreviewItemUrl:(NSString*)url;

@end
