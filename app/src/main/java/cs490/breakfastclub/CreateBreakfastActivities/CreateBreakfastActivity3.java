package cs490.breakfastclub.CreateBreakfastActivities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

import java.util.Calendar;

import cs490.breakfastclub.Classes.TimeFunctions;
import cs490.breakfastclub.R;

public class CreateBreakfastActivity3 extends AppCompatActivity {

    TimePicker timePicker;
    Button createButton;

    String descriptionString;
    int day;
    int month;
    int year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_breakfast3);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Create Breakfast");

        Bundle bundle = getIntent().getExtras();
        descriptionString = bundle.getString("Description");
        day = bundle.getInt("day");
        month = bundle.getInt("month");
        year = bundle.getInt("year");

        UIInit();
    }

    private void UIInit() {
        timePicker = (TimePicker) findViewById(R.id.createBreakfastTimePickerId);
        createButton = (Button) findViewById(R.id.createBreakfastButtonId);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = timePicker.getCurrentHour();
                int minute = timePicker.getCurrentMinute();
                createBreakfastEvent(hour, minute);
            }
        });
    }

    //TODO
    //when this is pushed, breakfast event is added to the database
    private void createBreakfastEvent(int hour, int minute) {
        Calendar calendar = TimeFunctions.getCurrentTime();
        calendar.set(year, month, day, hour, minute, 0);


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
