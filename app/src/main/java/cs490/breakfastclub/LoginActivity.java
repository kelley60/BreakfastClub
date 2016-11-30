package cs490.breakfastclub;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;

import cs490.breakfastclub.CameraAndPhotos.Photos;
import cs490.breakfastclub.UserFiles.User;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    private TextView info;
    public static LoginButton loginButton;
    public static CallbackManager callbackManager;
    public AccessTokenTracker accessTokenTracker;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;

    @Override
    //provide the onCreate method to apply the Friends layout to the activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_login);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (getIntent().hasExtra("CameFromDrawer"))
        {
            boolean cameFromDrawer = getIntent().getBooleanExtra("CameFromDrawer", false);
            if (cameFromDrawer == true)
            {
                myToolbar.setVisibility(View.VISIBLE);
            }

        }


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


        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        callbackManager = CallbackManager.Factory.create();
        //gets the login button from activity_login.xml
        loginButton = (LoginButton) findViewById(R.id.fb_button);
        loginButton.setReadPermissions("email", "public_profile", "user_friends");
        //gets the textview from activity_login.xml
        info = (TextView) findViewById(R.id.fb_info);
        final Button btnnav = (Button) findViewById(R.id.btn_nav);
        btnnav.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent init = new Intent(LoginActivity.this, DrawerActivity.class);
                init.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(init);
                finish();
            }
        });


        if (isLoggedIn())
        {
            info.setText("User is Logged In.");
            getSupportActionBar().setTitle("Sign Out");
            btnnav.setVisibility(View.VISIBLE);
            handleFacebookAccessToken(AccessToken.getCurrentAccessToken());
        }
        else
        {
            info.setText("User is not Logged In.");
            getSupportActionBar().setTitle("Sign In");
        }



        //Creates a callback function to handle the results of the login attempts
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // DO something with information you got
                btnnav.setVisibility(View.VISIBLE);
                handleFacebookAccessToken(loginResult.getAccessToken());
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

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("Signed in", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("Signed Out", "onAuthStateChanged:signed_out");
                }
                // ...
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

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("Signed in Credential", "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w("Signed In Credential", "signInWithCredential", task.getException());
                        }

                        // ...
                    }
                });


        final User currentUser = new User();

        Bundle params = new Bundle();
        params.putString("fields", "id, name, picture.type(large), friends");

        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me",
                params,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
            /* handle the result */
                        JSONObject object = response.getJSONObject();
                        JSONObject profileImageObject = null;
                        JSONArray friendsObject = null;

                        try {
                            profileImageObject = object.getJSONObject("picture").getJSONObject("data");
                            friendsObject = object.getJSONObject("friends").getJSONArray("data");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.v("Public Profile Object: " , object.toString());
                        
                        Log.v("Image Object: ", profileImageObject.toString());
                        Log.v("Friends Object: ", friendsObject.toString());


                        try {
                            currentUser.setUserId(object.getString("id"));
                            currentUser.setName(object.getString("name"));
                            currentUser.setProfileImageUrl(profileImageObject.getString("url"));

                            ArrayList<User> friends = new ArrayList<User>();
                            for (int i = 0; i < friendsObject.length(); i++)
                            {
                                User friend = new User();
                                JSONObject friendObject = (JSONObject) friendsObject.get(i);
                                friend.setName(friendObject.getString("name"));
                                friend.setUserId(friendObject.getString("id"));
                                friends.add(friend);
                            }
                            currentUser.setFriends(friends);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.v("CurrentUser Object: ", currentUser.toString());
                        ((MyApplication) getApplication()).setCurrentUser(currentUser);
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users/" + currentUser.getUserId());
                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                // Add the user to the database if they are not already there
                                if (!dataSnapshot.exists()) {
                                    Log.v("User does not exisit", "User does not already exists");
                                    writeNewUser(currentUser);
                                    currentUser.setCurrentPhotos(new Photos(currentUser));
                                    HashMap<String, URL> photos = currentUser.getCurrentPhotos().getUserPhotos();


                                }
                                // Get the information needed and update the user
                                else {
                                    Log.v("User already exists", "User already exists");
                                    currentUser.setReceivesPushNotifications((boolean) dataSnapshot.child("receivesPushNotifications").getValue());

                                    // Load the current application photos from firebase
                                    currentUser.setPermissions(User.Permissions.valueOf((String) dataSnapshot.child("permissions").getValue()));
                                    ((MyApplication)getApplication()).setCurrentPhotos(new Photos(currentUser));
                                    HashMap<String, URL> photos = ((MyApplication)getApplication()).getCurrentPhotos().getUserPhotos();


                                    if(dataSnapshot.child("squad").exists()) {
                                        currentUser.createSquad((String) dataSnapshot.child("squad").getValue());
                                        currentUser.setPartOfSquad(true);
                                        currentUser.setSquadRole((String) dataSnapshot.child("squadRole").getValue());
                                    }
                                    else
                                    {
                                        currentUser.setPartOfSquad(false);
                                    }

                                    currentUser.setCurrentPhotos(new Photos(currentUser));

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                System.out.println("The read failed: " + databaseError.getCode());
                            }
                        });

                    }
                }
        ).executeAsync();

    }

    private void writeNewUser(User user) {
        mDatabase.child("Users").child(user.getUserId()).child("name").setValue(user.getName());
        mDatabase.child("Users").child(user.getUserId()).child("profileImageUrl").setValue(user.getProfileImageUrl());
        mDatabase.child("Users").child(user.getUserId()).child("receivesPushNotifications").setValue(user.isReceivesPushNotifications());
        mDatabase.child("Users").child(user.getUserId()).child("currentPositionInFeed").setValue(0);
        mDatabase.child("Users").child(user.getUserId()).setValue("hasVotedUp");
        mDatabase.child("Users").child(user.getUserId()).setValue("hasVotedDown");
        mDatabase.child("Users").child(user.getUserId()).child("numberOfOffensives").setValue(0);
        mDatabase.child("Users").child(user.getUserId()).child("permissions").setValue(User.Permissions.Member);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }



}