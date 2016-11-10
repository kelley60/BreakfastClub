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

public class AdminViewUsersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_users);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("[SQUAD_NAME]");


        // TODO: Populate with actual data
        // Populate the list view
        final ArrayList<User> userList = new ArrayList<>();
        DatabaseReference memberRef = FirebaseDatabase.getInstance().getReference("Users/");
        memberRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot d : dataSnapshot.getChildren())
                {
                    User u = new User();
                    u.setName((String) d.child("name").getValue());
                    u.setProfileImageUrl((String) d.child("profileImageUrl").getValue());
                    u.setUserId((String) d.getKey());
                    userList.add(u);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Log.d("Users", "NUMBER OF USERS: " + userList.size());


        /*
        ArrayList<User> userList = new ArrayList<>();
        final User currentUser = ((MyApplication) getApplication()).getCurrentUser();
        if(currentUser != null)
        {
            userList.add(currentUser);
            Log.d("Name", currentUser.getName());
        }
        else
        {
            Log.d("Null", "CURRENT USER IS NULL!!!");

            int i = 0;
            User u = new User("Name " + i, "[url_here]", null);
            userList.add(u);
        }*/

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
