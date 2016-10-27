package cs490.breakfastclub;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.facebook.FacebookSdk;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends AppCompatActivity implements ConnectionCallbacks, OnConnectionFailedListener{

    public static String EXTRA_MESSAGE="KEY";
    Button campusFeedButton;
    Button createBreakfastButton;
    Button displayMessageButton;
    Button loginButton;
    GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonInit();
        googleApiInit();
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        // Hides the bottom navigation bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE;
        decorView.setSystemUiVisibility(uiOptions);
    }

    private void buttonInit() {
        campusFeedButton = (Button) findViewById(R.id.btn_campus_feed);
        createBreakfastButton = (Button) findViewById(R.id.btn_create_brkfst);
        displayMessageButton = (Button) findViewById(R.id.btn_disp_msg);
        loginButton = (Button) findViewById(R.id.btn_login);


        campusFeedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = chooseCampusFeedActivity();
                startActivity(intent);
            }
        });

        createBreakfastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateBreakfastActivity.class);
                startActivity(intent);
            }
        });

        displayMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DisplayMessageActivity.class);
                startActivity(intent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private Intent chooseCampusFeedActivity() {
        Intent intent = new Intent(MainActivity.this, CampusFeedActivity.class);
        if (1 == 0) {
            //not on campus
            intent.putExtra("Layout Type", "Not on Campus");
        }
        else if (1 == 0){
            //not breakfast club time
            intent.putExtra("Layout Type", "Not Time");
        }
        else{
            intent.putExtra("Layout Type", "Campus Feed");
        }
        return intent;
    }

    public void loginWithFacebook(View view) {
        // Do something in response to button click

        // TODO:
        // Change DisplayMessageActivity.class to activity after login occurs
        Intent intent = new Intent(this, DisplayMessageActivity.class);


        //EditText editText = (EditText) findViewById(R.id.edit_message);
        //String message = editText.getText().toString();

        // This is how data is sent through different views
        //intent.putExtra(MainActivity.EXTRA_MESSAGE, message);

        // Starts the next 'activity', which is just the next view
        startActivity(intent);
    }



    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();

    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // An unresolvable error has occurred and a connection to Google APIs
        // could not be established. Display an error message, or handle
        // the failure silently

        // ...
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    private void googleApiInit() {
        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }
}
