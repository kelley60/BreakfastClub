package cs490.breakfastclub;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

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

        final User currentUser = ((MyApplication) getApplication()).getCurrentUser();
        if (currentUser.getPermissions() == User.Permissions.Member)
        {
            setContentView(R.layout.request_admin_moderater);
            Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
            setSupportActionBar(myToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            Button requestModerator = (Button) findViewById(R.id.btnRequestModerator);
            Button requestAdmin = (Button) findViewById(R.id.btnRequestAdmin);

            final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

            requestModerator.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDatabase.child("Users/").child(currentUser.getUserId()).child("requestAdminModerator").setValue("moderator");
                }
            });

            requestAdmin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDatabase.child("Users/").child(currentUser.getUserId()).child("requestAdminModerator").setValue("admin");
                }
            });
        }
        else
        {
            setContentView(R.layout.activity_admin_view_users);

            Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
            setSupportActionBar(myToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);


            final ListView adminView = (ListView) findViewById(R.id.lstAdmins);
            final ListView moderatorView = (ListView) findViewById(R.id.lstModerators);
            final ListView requestAdminView = (ListView) findViewById(R.id.lstRequestAdmin);
            final ListView requestModeratorView = (ListView) findViewById(R.id.lstRequestModerator);

            if (currentUser.getPermissions() == User.Permissions.Moderator)
            {
                requestAdminView.setVisibility(View.GONE);
                findViewById(R.id.requestAdminLabel).setVisibility(View.GONE);
                findViewById(R.id.requestAdminLine).setVisibility(View.GONE);
            }
            // TODO: Populate with actual data
            // Populate the list view
            final ArrayList<User> adminList = new ArrayList<>();
            final AdminViewAdapter adminAdapter = new AdminViewAdapter(this, adminList);

            final ArrayList<User> moderatorList = new ArrayList<>();
            final ModeratorViewAdapter moderatorAdapter = new ModeratorViewAdapter(this, moderatorList, adminView);

            final ArrayList<User> requestAdminList = new ArrayList<>();
            final RequestAdminViewAdapter requestAdminAdapter = new RequestAdminViewAdapter(this, requestAdminList, adminView);

            final ArrayList<User> requestModeratorList = new ArrayList<>();
            final RequestModeratorViewAdapter requestModeratorAdapter = new RequestModeratorViewAdapter(this, requestModeratorList, moderatorView);




            adminView.setAdapter(adminAdapter);
            moderatorView.setAdapter(moderatorAdapter);
            requestAdminView.setAdapter(requestAdminAdapter);
            requestModeratorView.setAdapter(requestModeratorAdapter);
            DatabaseReference memberRef = FirebaseDatabase.getInstance().getReference("Users/");
            memberRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                        Log.v("Indiv User", d.toString());
                        User u = new User();
                        u.setName((String) d.child("name").getValue());
                        u.setProfileImageUrl((String) d.child("profileImageUrl").getValue());
                        u.setUserId((String) d.getKey());
                        if (User.Permissions.valueOf((String) d.child("permissions").getValue()) == User.Permissions.Developer) {
                            adminList.add(u);
                        } else if (User.Permissions.valueOf((String) d.child("permissions").getValue()) == User.Permissions.Moderator) {
                            moderatorList.add(u);
                        }

                        if (d.hasChild("requestAdminModerator"))
                        {
                            if (d.child("requestAdminModerator").getValue().equals("admin"))
                            {
                                requestAdminList.add(u);
                                Log.v("RequestList", requestAdminList.toString());
                            }
                            else if (d.child("requestAdminModerator").getValue().equals("moderator"))
                            {

                                requestModeratorList.add(u);
                            }
                        }

                    }
                    setListViewHeightBasedOnChildren(adminView);
                    setListViewHeightBasedOnChildren(moderatorView);
                    setListViewHeightBasedOnChildren(requestAdminView);
                    setListViewHeightBasedOnChildren(requestModeratorView);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }



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

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;


        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

}
