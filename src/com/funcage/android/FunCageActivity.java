package com.funcage.android;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

public class FunCageActivity extends Activity {
	WebView randomPic;
	Button generateRandomPic;
	Button share;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        generateNewRandomPic();
        
    generateRandomPic = (Button) findViewById(R.id.getRandomButton);
    generateRandomPic.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
        	generateNewRandomPic();
        }
    });
    }
    
    public void generateNewRandomPic() {
    	randomPic = (WebView) findViewById(R.id.randomPicWebView);
        randomPic.loadUrl("http://www.funcage.com/app-image.php");
    }
}