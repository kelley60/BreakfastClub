package cs490.breakfastclub;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;

import cs490.breakfastclub.Classes.Notification;
import cs490.breakfastclub.Classes.NotificationAdapter;

public class NotificationViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_view);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Display Message");

        ArrayList<Notification> nots = ((MyApplication) getApplication()).getCurrentUser().getNotifications();
        Collections.reverse(nots); // Most recent notification should be on top

        NotificationAdapter na = new NotificationAdapter(this, nots);
        ListView notificationList = (ListView) findViewById(R.id.notification_list);
        notificationList.setAdapter(na);
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
