package cs490.breakfastclub.SquadFiles;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;

import cs490.breakfastclub.UserFiles.User;
import cs490.breakfastclub.UserFiles.UserAdapter;
import cs490.breakfastclub.R;

public class SearchMemberActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_member);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("[SQUAD_NAME]");

        // Populate the list view
        ArrayList<User> userList = new ArrayList<>();
        for(int i = 0; i < 10; i++)
        {
            User u = new User("Name " + i, "[url_here]", null, null);
            userList.add(u);
        }

        UserAdapter uAdapter = new UserAdapter(this, userList);

        ListView lView = (ListView) findViewById(R.id.lstSearchMembers);
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
