package com.funcage.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;


//To-do list
//TODO: Landscape Orientation buttons arent showing
//TODO: Fixed width on the webview
//TODO: ActionSheet for sharing functionality
		//email
		//twitter
		//facebook
//TODO: Admob integration
//TODO: shake for random picture
//TODO: loading indicator?


public class FunCageActivity extends Activity {
	WebView randomPic;
	Button generateRandomPic;
	Button shareRandomPic;
	
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
		
		//AlertView Builder
		final AlertDialog.Builder builder;
		builder = new AlertDialog.Builder(this);
	    builder.setTitle(" ");
	    builder.setIcon(R.drawable.banner);
	    builder.setMessage("Share this picture from FunCage.com using any of the below!");
	    builder.setPositiveButton("Email",
	            new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int id) {
	                  System.out.print("Emailing...");
	                }
	            });

	    builder.setNeutralButton("Facebook",
	            new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int id) {
	                	System.out.print("Facebooking...");
	                }
	            });

	    builder.setNegativeButton("Twitter",
	            new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int id) {
	                    System.out.print("Tweeting...");
	                }
	            });
	    
	    shareRandomPic = (Button) findViewById(R.id.shareButton);
		shareRandomPic.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				builder.show();
			}
		});

    
    }
    
    public void generateNewRandomPic() {
    	randomPic = (WebView) findViewById(R.id.randomPicWebView);
        randomPic.loadUrl("http://www.funcage.com/app-image.php");
        randomPic.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        randomPic.setScrollbarFadingEnabled(false);
    }
    
    public void pullUpActionSheet() {
    	//pull up the actionsheet with the sharing buttons
    }
    
    public void shareOnFacebook() {
    	//share on facebook
    }
    
    public void shareOnTwitter() {
    	//share on twitter
    }
    
    public void shareOnEmail() {
    	//write a prewritten email to share the picture
    }
}