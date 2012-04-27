package com.funcage.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.easy.facebook.android.*;
import com.easy.facebook.android.apicall.GraphApi;
import com.easy.facebook.android.data.User;
import com.easy.facebook.android.error.EasyFacebookError;
import com.easy.facebook.android.facebook.FBLoginManager;
import com.easy.facebook.android.facebook.Facebook;
import com.easy.facebook.android.facebook.LoginListener;

public class FacebookConnect extends Activity   implements LoginListener {

        private FBLoginManager fbManager;
        
        @Override
        public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                shareFacebook();
        }

        
        public void shareFacebook() {
        
        //change the permissions according to the function you want to use 
                String permissions[] = { "read_stream", "user_relationship_details",
                                "user_religion_politics", "user_work_history",
                                "user_relationships", "user_interests", "user_likes",
                                "user_location", "user_hometown", "user_education_history",
                                "user_activities", "offline_access" };

                //change the parameters with those of your application
                fbManager = new FBLoginManager(this, R.layout.black,
                		"FacebookApplicationID", permissions);

                if (fbManager.existsSavedFacebook()) {
                        fbManager.loadFacebook();
                } else {

                        fbManager.login();
                }
        }
        
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
                fbManager.loginSuccess(data);
        }

        public void loginFail() {
                fbManager.displayToast("Login failed!");

        }

        public void logoutSuccess() {
                fbManager.displayToast("Logout success!");
        }

        public void loginSuccess(Facebook facebook) {
                
                //library use example
                GraphApi graphApi = new GraphApi(facebook);

                User user = new User();
                try {
                        user = graphApi.getMyAccountInfo();
                } catch (EasyFacebookError e) {
                        e.toString();
                }

                
                

        }
}