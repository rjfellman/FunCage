package com.funcage.android;

import android.util.Log;

class FuncageJavaScriptInterface {
    public String appimageCallback(String jsResult) {
		
    	String imageLocation;
    	
    	imageLocation = jsResult;
    	Log.d(getClass().getName(),imageLocation);
    	
    	return jsResult;
    }
}
