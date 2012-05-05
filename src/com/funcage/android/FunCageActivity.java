package com.funcage.android;

import java.util.Date;

import oauth.signpost.OAuth;

import com.android.hardware.*;
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
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.PostDialogListener;
import com.google.ads.*;

public class FunCageActivity extends Activity implements DialogListener{
	//Facebook//
	//FUNCAGE APP ID = 473922950706	
	//Test App ID = 293669467362284
    Facebook facebook = new Facebook("293669467362284");
	
	WebView randomPic;
	Button generateRandomPic;
	Button shareRandomPic;

	ProgressBar progBar;
	TextView loadText;

	String appImage;
	FuncageJavaScriptInterface javaInterface = new FuncageJavaScriptInterface();
	private ShakeListener mShaker;

	private SharedPreferences prefs;
	private final Handler mTwitterHandler = new Handler();
	private TextView loginStatus;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		

		this.prefs = PreferenceManager.getDefaultSharedPreferences(this);

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
				shareOnFacebook();
			}
		});

		builder.setNegativeButton("Twitter",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				Log.d(getClass().getName(),"Tweeting...");
				shareOnTwitter();

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
		//Facebooks

		facebook.authorize(FunCageActivity.this, new String[]{ "user_photos,publish_checkins,publish_actions,publish_stream"},new DialogListener() {
            @Override
            public void onComplete(Bundle values) {
            	
            	postOnWall("Check out this funny picture from FunCage");
            }

            @Override
            public void onFacebookError(FacebookError error) {
            }

            @Override
            public void onError(DialogError e) {
            }

            @Override
            public void onCancel() {
            }
        });


				
				postOnWall("FunCage Test");
				
				Toast.makeText(this, "Picture successfully posted to facebook!", Toast.LENGTH_LONG).show();
	}

	public void shareOnTwitter() {
		//share on twitter
		getImageLocation();
		if (TwitterUtils.isAuthenticated(prefs)) {
			sendTweet();
		} else {
			Intent i = new Intent(getApplicationContext(), PrepareRequestTokenActivity.class);
			i.putExtra("tweet_msg",getTweetMsg());
			startActivity(i);
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
	
	final Runnable mUpdateTwitterNotification = new Runnable() {
	       public void run() {
	       	Toast.makeText(getBaseContext(), "Tweet sent !", Toast.LENGTH_LONG).show();
	       }
	   };
	   
	   private String getTweetMsg() {
		   return "Tweeting from Android App";
		   } 

	public void sendTweet() {
		Thread t = new Thread() {
			public void run() {

				try {
					TwitterUtils.sendTweet(prefs,"Hello");
					mTwitterHandler.post(mUpdateTwitterNotification);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

		};
		t.start();
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        facebook.authorizeCallback(requestCode, resultCode, data);
    }
	
	public void postOnWall(String msg) {
        Log.d("Tests", "Testing graph API wall post");
         try {
                String response = facebook.request("me");
                Bundle parameters = new Bundle();
                parameters.putString("message", msg);
                parameters.putString("picture", "http://www.funcage.com"+appImage);

                response = facebook.request("me/feed", parameters, 
                        "POST");
                Log.d("Tests", "got response: " + response);
                if (response == null || response.equals("") || 
                        response.equals("false")) {
                   Log.v("Error", "Blank response");
               }
         } catch(Exception e) {
             e.printStackTrace();
         }


	}

	@Override
	public void onComplete(Bundle values) {
		// TODO Auto-generated method stub
		postOnWall("R2D2");
	}

	@Override
	public void onFacebookError(FacebookError e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onError(DialogError e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCancel() {
		// TODO Auto-generated method stub
		
	}
}