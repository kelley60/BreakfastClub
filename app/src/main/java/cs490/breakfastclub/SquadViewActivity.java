package cs490.breakfastclub;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cs490.breakfastclub.Classes.Squad;
import cs490.breakfastclub.Classes.User;

public class SquadViewActivity extends AppCompatActivity {
    private ArrayList<User> mMemberNames;
    private ArrayList<User> mFriends;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private TextView mDrawerTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_squad_view);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("[SQUAD_NAME]");

        mMemberNames = new ArrayList<User>();
        mFriends = new ArrayList<User>();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.right_drawer);
        mDrawerTitle = (TextView) findViewById(R.id.drawerTitle);






        final User currentUser = ((MyApplication) getApplication()).getCurrentUser();
        if (currentUser.isPartOfSquad()) {
            DatabaseReference squadRef = FirebaseDatabase.getInstance().getReference("Squads/" + currentUser.getSquad().getSquadID());
            squadRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    HashMap<String, String> members = (HashMap) dataSnapshot.child("Members").getValue();
                    Log.v("Members", members.toString());

                    for (Map.Entry<String, String> member : members.entrySet()) {
                        Log.v("Member", member.getKey() + " " + member.getValue());
                        DatabaseReference memberRef = FirebaseDatabase.getInstance().getReference("Users/" + member.getValue());
                        memberRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                User currentMember = new User();
                                currentMember.setName((String) dataSnapshot.child("name").getValue());
                                currentMember.setProfileImageUrl((String) dataSnapshot.child("profileImageUrl").getValue());
                                currentMember.setUserId((String) dataSnapshot.getKey());
                                mMemberNames.add(currentMember);

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                    //DatabaseReference memberRef = FirebaseDatabase.getInstance().getReference("Users/" + member.)

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        for (int i = 0; i < currentUser.getFriends().size(); i++) {
            DatabaseReference memberRef = FirebaseDatabase.getInstance().getReference("Users/" + currentUser.getFriends().get(i).getUserId());
            memberRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User currentMember = new User();
                    currentMember.setName((String) dataSnapshot.child("name").getValue());
                    currentMember.setProfileImageUrl((String) dataSnapshot.child("profileImageUrl").getValue());
                    currentMember.setUserId((String) dataSnapshot.getKey());
                    mFriends.add(currentMember);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.squadviewbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        else if (item.getItemId() == R.id.viewSquad)
        {
            if (mDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                mDrawerLayout.closeDrawer(Gravity.RIGHT);
            } else {
                mDrawerTitle.setText("Squad Members");
                mDrawerList.setAdapter(new SquadMemberAdapter(SquadViewActivity.this,
                        R.layout.squad_member_list_item, mMemberNames));
                // Set the list's click listener
                mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    }
                });
                mDrawerLayout.openDrawer(Gravity.RIGHT);
            }
        }
        else if (item.getItemId() == R.id.addToSquad)
        {
            if (mDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                mDrawerLayout.closeDrawer(Gravity.RIGHT);
            } else {
                mDrawerTitle.setText("Add a Friend to Squad");
                mDrawerList.setAdapter(new SquadMemberAdapter(SquadViewActivity.this,
                        R.layout.squad_member_list_item, mFriends));
                // Set the list's click listener
                mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    }
                });
                mDrawerLayout.openDrawer(Gravity.RIGHT);
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
