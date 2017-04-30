package com.example.pice.twittercheckclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.*;
import android.util.Log;
import android.view.View;
import android.widget.*;
import android.widget.Toast;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.*;
import com.twitter.sdk.android.core.identity.*;
import com.twitter.sdk.android.core.models.*;
import com.twitter.sdk.android.core.services.*;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import io.fabric.sdk.android.Fabric;
import retrofit2.Call;

import java.util.*;

public class MainActivity extends AppCompatActivity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "*************************";
    private static final String TWITTER_SECRET = "********************************************";

    private TwitterLoginButton loginButton;
    private Button postButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_main);

        loginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // The TwitterSession is also available through:
                // Twitter.getInstance().core.getSessionManager().getActiveSession()
                TwitterSession session = result.data;
                // TODO: Remove toast and use the TwitterSession's userID
                // with your app's user model
                String msg = "@" + session.getUserName() + " logged in! (#" + session.getUserId() + ")";
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
            @Override
            public void failure(TwitterException exception) {
                Log.d("TwitterKit", "Login with Twitter failure", exception);
            }
        });

        postButton = (Button) findViewById(R.id.post_button);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tweet("test post("
                        +DateFormat.format("yyyy/MM/dd kk:mm:ss", Calendar.getInstance()).toString()
                        +")");
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Make sure that the loginButton hears the result from any
        // Activity that it triggered.
        loginButton.onActivityResult(requestCode, resultCode, data);
    }

    private void tweet(String message){
        TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
        StatusesService statusesService = twitterApiClient.getStatusesService();

        /*
        Call<Tweet> call = statusesService.show(524971209851543553L, null, null, null);
        call.enqueue(new Callback<Tweet>(){
            @Override
            public void success(Result<Tweet> result){
                Toast.makeText(getApplicationContext(), "post success", Toast.LENGTH_LONG).show();
            }

            public void failure(TwitterException exception){
                Toast.makeText(getApplicationContext(), "post fail", Toast.LENGTH_LONG).show();
            }
        });
        */

        Call<Tweet> call = statusesService.update(message, null, false, null, null, null, false, null, null);
        call.enqueue(new Callback<Tweet>(){
            @Override
            public void success(Result<Tweet> result){
                Toast.makeText(getApplicationContext(), "post success", Toast.LENGTH_LONG).show();
            }

            public void failure(TwitterException exception){
                Toast.makeText(getApplicationContext(), "post fail", Toast.LENGTH_LONG).show();
            }
        });

    }

}
