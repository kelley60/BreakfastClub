package cs490.breakfastclub.BreakfastFiles;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import cs490.breakfastclub.R;

public class CreateBreakfastActivity2 extends AppCompatActivity {

    DatePicker datePicker;
    Button nextButton;

    String descriptionString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_breakfast2);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Create Breakfast");

        Bundle bundle = getIntent().getExtras();
        descriptionString = bundle.getString("Description");

        UIInit();
    }

    private void UIInit() {
        datePicker = (DatePicker) findViewById(R.id.createBreakfastDatePickerId);
        nextButton = (Button) findViewById(R.id.createBreakfastNextButton2Id);


        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int day = datePicker.getDayOfMonth();
                int year = datePicker.getYear();
                int month = datePicker.getMonth();
                Intent intent = new Intent(CreateBreakfastActivity2.this, CreateBreakfastActivity3.class);
                intent.putExtra("description", descriptionString);
                intent.putExtra("day", day);
                intent.putExtra("month", month);
                intent.putExtra("year", year);
                startActivity(intent);
            }
        });

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
