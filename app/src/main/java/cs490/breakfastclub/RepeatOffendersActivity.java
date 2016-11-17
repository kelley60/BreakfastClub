package cs490.breakfastclub;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import cs490.breakfastclub.UserFiles.User;
import cs490.breakfastclub.UserFiles.UserAdapter;

public class RepeatOffendersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repeat_offenders);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Repeat Offenders");

        // TODO: Populate with actual data
        // Populate the list view
        final ArrayList<User> userList = new ArrayList<>();
        final DatabaseReference memberRef = FirebaseDatabase.getInstance().getReference("Users/");
        memberRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot d : dataSnapshot.getChildren())
                {
                    User u = new User();
                    u.setName((String) d.child("name").getValue());
                    u.setProfileImageUrl((String) d.child("profileImageUrl").getValue());
                    u.setUserId((String) d.getKey());


                    if(d.child("numberOfOffensives").exists())
                    {
                        Log.d("Offensives", "Field numberOfOffensives exists for " + u.getName());
                        Long i = (Long) d.child("numberOfOffensives").getValue();
                        Log.d("Offensives", "Got value from database");
                        u.setNumberOfOffensives(i.intValue());
                        Log.d("Offensives", "Updated value for user");
                    }
                    else
                    {
                        u.setNumberOfOffensives(0);
                        Log.d("Offensives", "Added Field numberOfOffensives for " + u.getName());
                        memberRef.child(u.getUserId()).child("numberOfOffensives").setValue(0);
                        Log.d("Offensives", "Success");
                    }


                    Log.d("Offensives", "Checking number of offensives for " + u.getName());
                    if(u.getNumberOfOffensives() >= 3)
                    { // If user has been flagged as a repeat offender
                        Log.d("Offensives", u.getName() + " Was added to the list\nOffensives: " + u.getNumberOfOffensives());
                        userList.add(u);
                    }
                    else
                    {
                        Log.d("Offensives", u.getName() + " Was not added to the list\nOffensives: " + u.getNumberOfOffensives());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        UserAdapter uAdapter = new UserAdapter(this, userList);

        ListView lView = (ListView) findViewById(R.id.lstAllUsers);
        lView.setAdapter(uAdapter);
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
