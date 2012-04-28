package com.funcage.android;

import android.util.Log;

public class FuncageJavaScriptInterface {
	
	String imageLocation;
	
    public void appimageCallback(String jsResult) {
    	imageLocation = jsResult;
    	System.out.println(imageLocation+"origval");
    }
    
    public String getAppImage(){
    	return imageLocation;
    }
}
