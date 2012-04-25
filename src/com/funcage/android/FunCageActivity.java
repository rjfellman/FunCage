package com.funcage.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.google.ads.*;


//To-do list
//Landscape Orientation buttons arent showing
//Fixed width on the webview
//ActionSheet for sharing functionality
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
		randomPic = (WebView) findViewById(R.id.randomPicWebView);
		randomPic.getSettings().setUseWideViewPort(true);
		randomPic.getSettings().setLoadWithOverviewMode(true);
		
		/* JavaScript must be enabled if you want it to work, obviously */  
		randomPic.getSettings().setJavaScriptEnabled(true);  
		  
		  

		
		AdView adview = (AdView)findViewById(R.id.adView);
		AdRequest re = new AdRequest();
		adview.loadAd(re);


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
	                	Log.d(getClass().getName(),"Emailing...");
	                	shareOnEmail();
	                }
	            });

	    builder.setNeutralButton("Facebook",
	            new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int id) {
	                	Log.d(getClass().getName(),"Facebooking...");
	                }
	            });

	    builder.setNegativeButton("Twitter",
	            new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int id) {
	                	Log.d(getClass().getName(),"Tweeting...");

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
    	/* WebViewClient must be set BEFORE calling loadUrl! */  
		randomPic.setWebViewClient(new WebViewClient() {  
		    public void onPageFinished(WebView view, String url)  
		    {  
		    	randomPic.loadUrl("javascript:(function() { " +
		                "document.getElementById(\"image\").getAttribute(\"src\")");
		    	
		    }  
		});
    	
        randomPic.loadUrl("http://www.funcage.com/app-image.php");
        //String imageURL = randomPic.loadUrl("javascript:PerformSimpleCalculation(document.getElementById(\"image\").getAttribute(\"src\"))");
        randomPic.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        randomPic.setScrollbarFadingEnabled(false);
        randomPic.setBackgroundColor(0x00000000);
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
    	final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
    	String imageURL = "";
    	emailIntent.setType("text/html");
    	emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Check out this funny picture from FunCage");
    	emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Check out this funny picture from FunCage: <br><br><a href=\"http://www.funcage.com/m/funnypicture.php?image="+imageURL+"</a>\n\nIf you liked the funny pic above, you will enjoy the FunCage app on your Android");
    	startActivity(Intent.createChooser(emailIntent, "Email:"));
    }
}