package com.funcage.android;

import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.hardware.ShakeListener;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.google.ads.AdRequest;
import com.google.ads.AdView;

public class FunCageActivity extends Activity{
	//Facebook//
	//FUNCAGE APP ID = 473922950706	
	//Test App ID = 293669467362284
    Facebook facebook = new Facebook("293669467362284");
	
	WebView randomPic;
	Button generateRandomPic;
	Button shareRandomPic;

	ProgressBar progBar;
	TextView loadText;
	
	//Preferences
	String fblogin;
	String twitterOAuthToken;
	String twitterOAuthTokenSecret;

	String appImage;
	FuncageJavaScriptInterface javaInterface = new FuncageJavaScriptInterface();
	private ShakeListener mShaker;
	
	//Twitter
	private static final String APP = 	"FunCage";//

	private Twitter twitter;
	private OAuthProvider provider;
	private CommonsHttpOAuthConsumer consumer;

	private String CONSUMER_KEY = 		"RysEZCje1inzDQUGfTp8Ww";//
	private String CONSUMER_SECRET = 	"Th5NF42U1D3YYhiozzeVh0dEIOOKltrkBx65N2E";
	private String CALLBACK_URL = 		"funcage-android://twitter-callback";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		LoadPreferences();

		progBar = (ProgressBar) findViewById(R.id.progressBar1);
		loadText = (TextView) findViewById(R.id.loadingText);


		randomPic = (WebView) findViewById(R.id.randomPicWebView);
		randomPic.getSettings().setUseWideViewPort(true);
		randomPic.getSettings().setLoadWithOverviewMode(true);
		randomPic.setScrollbarFadingEnabled(true);
		randomPic.setScrollBarStyle(WebView.SCROLLBARS_INSIDE_OVERLAY);
		randomPic.getSettings().setBuiltInZoomControls(true);

		/* JavaScript must be enabled if you want it to work, obviously */ //
		randomPic.getSettings().setJavaScriptEnabled(true);  

		randomPic.addJavascriptInterface(javaInterface, "HTMLOUT");


		AdView adview = (AdView)findViewById(R.id.adView);
		AdRequest re = new AdRequest();//
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
		randomPic.setScrollBarStyle(WebView.SCROLLBARS_INSIDE_OVERLAY);
		randomPic.setScrollbarFadingEnabled(true);
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
				postOnWall("FunCage");
				
				Toast.makeText(this, "Picture successfully posted to facebook!", Toast.LENGTH_LONG).show();
	}

	public void shareOnTwitter() {
		//share on twitter
		getImageLocation();
		LoadPreferences();
		if(twitterOAuthToken.isEmpty() || twitterOAuthTokenSecret.isEmpty()){
			askOAuth();
		}
		else{
			postTweet();
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
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        facebook.authorizeCallback(requestCode, resultCode, data);
    }
	
	public void postOnWall(String msg) {
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
	
	/**
	 * Open the browser and asks the user to authorize the app.
	 * Afterwards, we redirect the user back here!
	 */
	private void askOAuth() {
		try {
			consumer = new CommonsHttpOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
			provider = new DefaultOAuthProvider("http://twitter.com/oauth/request_token",
												"http://twitter.com/oauth/access_token",
												"http://twitter.com/oauth/authorize");//
			String authUrl = provider.retrieveRequestToken(consumer, CALLBACK_URL);
			Toast.makeText(this, "Please authorize this app!", Toast.LENGTH_LONG).show();
			//this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(authUrl)));
			Intent intent = new Intent(Intent.ACTION_VIEW,
					Uri.parse(authUrl)); 
				//intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TASK);
				this.startActivity(intent); 
		} catch (Exception e) {
			Log.e(APP, e.getMessage());
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}
	
	/**
	 * As soon as the user successfully authorized the app, we are notified
	 * here. Now we need to get the verifier from the callback URL, retrieve
	 * token and token_secret and feed them to twitter4j (as well as
	 * consumer key and secret).
	 */
	@Override
	protected void onNewIntent(Intent intent) {
		Log.i("got","GOT HERE");

		super.onNewIntent(intent);

		Uri uri = intent.getData();
		if (uri != null && uri.toString().startsWith(CALLBACK_URL)) {

			String verifier = uri.getQueryParameter(oauth.signpost.OAuth.OAUTH_VERIFIER);
			Log.i("OAuth",verifier);

			
				// this will populate token and token_secret in consumer
				try {
					provider.retrieveAccessToken(consumer, verifier);
				} catch (OAuthMessageSignerException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					Log.i("OAuth","SignerException");
				} catch (OAuthNotAuthorizedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					Log.i("OAuth","NotAuthorizedEx");
				} catch (OAuthExpectationFailedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					Log.i("OAuth","FailedException");
				} catch (OAuthCommunicationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					Log.i("OAuth","CommunicationException");
				} finally {
					Log.i("OAuth","Was OK!");
				}
				
				Log.i("OAuth","Token: "+consumer.getToken());
				Log.i("OAuth","Secret Token: "+consumer.getTokenSecret());
				
				SavePreferences("twitterOAuthToken", consumer.getToken());
				SavePreferences("twitterOAuthTokenSecret", consumer.getTokenSecret());
				
				postTweet();
		}
	}
	
	public void postTweet(){
				LoadPreferences();
				AccessToken a = new AccessToken(twitterOAuthToken, twitterOAuthTokenSecret);
				
				// initialize Twitter4J//
				twitter = new TwitterFactory().getInstance();
				twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
				twitter.setOAuthAccessToken(a);
				
				// create a tweet
				String tweet = "Check out this fun photo from FunCage.com: http://www.funcage.com/m/funnypicture.php?image="+appImage;

				// send the tweet//
				try {
					twitter.updateStatus(tweet);
				} catch (TwitterException e) {
					// TODO Auto-generated catch block
					Log.i("OAuth","Twitter Broke");
					Log.e(APP, e.getMessage());
					e.printStackTrace();	
				}
				Toast.makeText(this, "Posted photo to twitter!", Toast.LENGTH_LONG).show();
		}
	
	private void SavePreferences(String key, String value){
	    SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
	    SharedPreferences.Editor editor = sharedPreferences.edit();
	    editor.putString(key, value);
	    editor.commit();
	   }
	  
	   private void LoadPreferences(){
	    SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
	    fblogin = sharedPreferences.getString("fblogin", "");
	    twitterOAuthToken = sharedPreferences.getString("twitterOAuthToken", "");
	    twitterOAuthTokenSecret = sharedPreferences.getString("twitterOAuthTokenSecret", "");
	   }
}