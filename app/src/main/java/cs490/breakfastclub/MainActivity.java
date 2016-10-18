package cs490.breakfastclub;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;
import com.facebook.FacebookSdk;

public class MainActivity extends AppCompatActivity {

    public static String EXTRA_MESSAGE="KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        // Hides the bottom navigation bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE;
        decorView.setSystemUiVisibility(uiOptions);
    }

    public void loginWithFacebook(View view)
    {
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
}
