package cs490.breakfastclub;


import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    private TextView info;
    public static LoginButton loginButton;
    public static CallbackManager callbackManager;
    public AccessTokenTracker accessTokenTracker;

    @Override
    //provide the onCreate method to apply the Friends layout to the activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_login);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "cs490.breakfastclub",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("No KeyHash", "No KeyHash");

        } catch (NoSuchAlgorithmException e) {
            Log.d("No KeyHash", "No KeyHash");
        }



        callbackManager = CallbackManager.Factory.create();
        //gets the login button from activity_login.xml
        loginButton = (LoginButton) findViewById(R.id.fb_button);
        loginButton.setReadPermissions("public_profile");
        loginButton.setReadPermissions("user_friends");
        //gets the textview from activity_login.xml
        info = (TextView) findViewById(R.id.fb_info);
        final Button btnnav = (Button) findViewById(R.id.btn_nav);
        btnnav.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent init = new Intent(LoginActivity.this, MainActivity.class);
                init.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(init);
                finish();
            }
        });


        if (isLoggedIn())
        {
            info.setText("User is Logged In.");
            btnnav.setVisibility(View.VISIBLE);
        }
        else
        {
            info.setText("User is not Logged In.");
        }



        //Creates a callback function to handle the results of the login attempts
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // DO something with information you got
                btnnav.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancel() {
                info.setText("Login attempt canceled.");
            }

            @Override
            public void onError(FacebookException error) {
                info.setText("Login attempt failed.");
            }

        });

        AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
                if (currentAccessToken == null){
                    Intent init = new Intent(LoginActivity.this, LoginActivity.class);
                    init.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(init);
                    finish();
                }
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }



}