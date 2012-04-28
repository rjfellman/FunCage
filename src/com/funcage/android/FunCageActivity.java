package com.funcage.android;

import com.android.hardware.*;
import com.easy.facebook.*;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import android.webkit.*;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.ads.*;

public class FunCageActivity extends Activity {
	WebView randomPic;
	Button generateRandomPic;
	Button shareRandomPic;
	
	ProgressBar progBar;
	TextView loadText;
	
	String appImage;
	FuncageJavaScriptInterface javaInterface = new FuncageJavaScriptInterface();
	private ShakeListener mShaker;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		progBar = (ProgressBar) findViewById(R.id.progressBar1);
		loadText = (TextView) findViewById(R.id.loadingText);
		
		
		randomPic = (WebView) findViewById(R.id.randomPicWebView);
		randomPic.getSettings().setUseWideViewPort(true);
		randomPic.getSettings().setLoadWithOverviewMode(true);
		
		/* JavaScript must be enabled if you want it to work, obviously */  
		randomPic.getSettings().setJavaScriptEnabled(true);  
		
		randomPic.addJavascriptInterface(javaInterface, "HTMLOUT");
		
		
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
	                	shareOnEmail();
	                }
	            });

	    builder.setNeutralButton("Facebook",
	            new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int id) {
	                	Log.d(getClass().getName(),"Facebooking...");
	                	//shareOnFacebook();
	                }
	            });

	    builder.setNegativeButton("Twitter",
	            new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int id) {
	                	Log.d(getClass().getName(),"Tweeting...");
	                	//shareOnTwitter();

	                }
	            });
	    
	    shareRandomPic = (Button) findViewById(R.id.shareButton);
		shareRandomPic.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				builder.show();
			}
		});
		
		mShaker = new ShakeListener(this);
	    mShaker.setOnShakeListener(new ShakeListener.OnShakeListener () {
	      public void onShake()
	      {
	    	  generateNewRandomPic();
	      }
	    });
    }
    
    public void generateNewRandomPic() {
    	/* WebViewClient must be set BEFORE calling loadUrl! */  
		randomPic.setWebViewClient(new WebViewClient() {  
		    public void onPageFinished(WebView view, String url)  
		    {  
		    	progBar.setVisibility(ProgressBar.GONE);
		    	loadText.setVisibility(TextView.GONE);
		    	randomPic.loadUrl("javascript:( function () { var resultSrc = document.getElementById(\"image\").getAttribute(\"src\"); window.HTMLOUT.appimageCallback(resultSrc); } ) ()");
		    }  
		});
    	
        randomPic.loadUrl("http://www.funcage.com/app-image.php");
        progBar.setVisibility(ProgressBar.VISIBLE);
    	loadText.setVisibility(TextView.VISIBLE);
        //String imageURL = randomPic.loadUrl("javascript:PerformSimpleCalculation(document.getElementById(\"image\").getAttribute(\"src\"))");
        randomPic.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        randomPic.setScrollbarFadingEnabled(false);
        randomPic.setBackgroundColor(0x00000000);
    }

	public void getImageLocation() {
    	FuncageJavaScriptInterface getAppImageFile = new FuncageJavaScriptInterface();
    	appImage = javaInterface.getAppImage();
    }
    
    public void pullUpActionSheet() {
    	//pull up the actionsheet with the sharing buttons
    }
    
    public void shareOnFacebook() {
    	//share on facebook
    	getImageLocation();
    	Intent intent;
    	intent = new Intent(this, FacebookConnect.class);
        startActivity(intent);

    }
    
    public void shareOnTwitter() {
    	//share on twitter
    	getImageLocation();
        Twitter twitter = new TwitterFactory().getInstance();
        try {
			Status status = twitter.updateStatus("TestUpdate");
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public void shareOnEmail() {
    	//write a prewritten email to share the picture
    	final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
    	getImageLocation();
    	emailIntent.setType("text/html");
    	emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Check out this funny picture from FunCage");
    	emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Check out this funny picture from FunCage: \n\nhttp://www.funcage.com/m/funnypicture.php?image="+appImage+"\n\nIf you liked the funny pic above, you will enjoy the FunCage app on your Android");
    	startActivity(Intent.createChooser(emailIntent, "Email:"));
    }

    
}