package cs490.breakfastclub;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    public static String EXTRA_MESSAGE="KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void loginWithFacebook(View view)
    {
        // Do something in response to button click

        // TODO:
        // Change DisplayMessageActivity.class to activity after login occurs
        Intent intent = new Intent(this, DisplayMessageActivity.class);

        EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = editText.getText().toString();

        // This is how data is sent through different views
        intent.putExtra(MainActivity.EXTRA_MESSAGE, message);

        // Starts the next 'activity', which is just the next view
        startActivity(intent);
    }
}
