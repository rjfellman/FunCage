package com.funcage.android;

import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class OAuth extends Activity {

	private static final String APP = 	"FunCage";

	private Twitter twitter;
	private OAuthProvider provider;
	private CommonsHttpOAuthConsumer consumer;

	private String CONSUMER_KEY = 		"cnSk1UkMf4FbsWNqWhopQ";
	private String CONSUMER_SECRET = 	"GCch7JWFEEbr5W4bqQfYL7vDGeqeZwDhVwyGw0zSec";
	private String CALLBACK_URL = 		"funcage-android://twitter-callback";
	
/**
	 * Open the browser and asks the user to authorize the app.
	 * Afterwards, we redirect the user back here!
	 */
	public void askOAuth() {
		try {
			consumer = new CommonsHttpOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
			provider = new DefaultOAuthProvider("http://twitter.com/oauth/request_token",
												"http://twitter.com/oauth/access_token",
												"http://twitter.com/oauth/authorize");
			String authUrl = provider.retrieveRequestToken(consumer, CALLBACK_URL);
			Toast.makeText(this, "Please authorize this app!", Toast.LENGTH_LONG).show();
			this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(authUrl)));
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

		super.onNewIntent(intent);

		Uri uri = intent.getData();
		if (uri != null && uri.toString().startsWith(CALLBACK_URL)) {

			String verifier = uri.getQueryParameter(oauth.signpost.OAuth.OAUTH_VERIFIER);

			try {
				// this will populate token and token_secret in consumer
				provider.retrieveAccessToken(consumer, verifier);
				
				// TODO: you might want to store token and token_secret in you app settings!!!!!!!!
				AccessToken a = new AccessToken(consumer.getToken(), consumer.getTokenSecret());
				
				// initialize Twitter4J
				twitter = new TwitterFactory().getInstance();
				twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
				twitter.setOAuthAccessToken(a);
				
				// create a tweet
				String tweet = "#OAuth working! ";

				// send the tweet
				twitter.updateStatus(tweet);
				
			} catch (Exception e) {
				Log.e(APP, e.getMessage());
			}

		}
	}

}