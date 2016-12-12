package cs490.breakfastclub.BreakfastFiles;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import cs490.breakfastclub.DrawerActivity;
import cs490.breakfastclub.MyApplication;
import cs490.breakfastclub.R;
import cs490.breakfastclub.UserFiles.User;

public class CreateBreakfastActivity extends AppCompatActivity {

    EditText description;
    Button nextButton;
    Button endBreakfastButton;
    ImageView image;
    User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_breakfast1);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Create Breakfast");

        currentUser = ((MyApplication) getApplication()).getCurrentUser();

        UIInit();
    }

    private void UIInit() {
        description = (EditText) findViewById(R.id.createBreakfastDescriptionEditTextId);
        image = (ImageView) findViewById(R.id.createBreakfastImageId);
        nextButton = (Button) findViewById(R.id.createBreakfastNextButton1Id);
        endBreakfastButton = (Button) findViewById(R.id.endBreakfastButton);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateBreakfastActivity.this, CreateBreakfastActivity2.class);
                String descriptionString = description.getText().toString();
                intent.putExtra("description", descriptionString);
                startActivity(intent);
            }
        });


        endBreakfastButton.setVisibility(View.INVISIBLE);

        //if (currentUser.getPermissions() == User.Permissions.Developer){
            endBreakfastButton.setVisibility(View.VISIBLE);
        //}

        endBreakfastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Breakfast.endCurrentBreakfast();
                Intent intent = new Intent(CreateBreakfastActivity.this, DrawerActivity.class);
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
