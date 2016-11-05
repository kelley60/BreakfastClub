package cs490.breakfastclub;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
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
import android.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
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
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cs490.breakfastclub.Classes.Squad;
import cs490.breakfastclub.Classes.User;
import java.util.ArrayList;
import cs490.breakfastclub.Classes.User;
import cs490.breakfastclub.Classes.UserAdapter;

public class SquadViewActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    // TODO: Populate UI with information from database

    private boolean captainDeletingSquad = false;
    private ArrayList<User> mMemberNames;
    private ArrayList<User> mFriends;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private TextView mDrawerTitle;

    private DatabaseReference mDatabase;

    private GoogleApiClient mGoogleApiClient = null;


    @Override
    public void onConnectionSuspended(int s)
    {
        // Empty
    }

    @Override
    public void onConnectionFailed(ConnectionResult c)
    {
        // Empty
    }

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


        /*
        Button btnSearch = (Button) findViewById(R.id.btnInviteMember);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SquadViewActivity.this, SearchMemberActivity.class);
                startActivity(intent);
            }
        });
        */

        Button btnMsgs = (Button) findViewById(R.id.btnViewSquadMsgs);
        btnMsgs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SquadViewActivity.this, DisplayMessageActivity.class);
                startActivity(intent);
            }
        });

        Button btnLocate = (Button) findViewById(R.id.btnLocation);
        btnLocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SquadViewActivity.this, SquadLocationActivity.class);
                startActivity(intent);
            }
        });

        Button btnGallery = (Button) findViewById(R.id.btnViewSquadPhotos);
        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SquadViewActivity.this, SquadGalleryActivity.class);
                startActivity(intent);
            }
        });

        // Populate the list view
        /*
        ArrayList<User> userList = new ArrayList<>();
        for(int i = 0; i < 2; i++)
        {
            User u = new User("Name " + i, "[url_here]", null);
            userList.add(u);
        }

        UserAdapter uAdapter = new UserAdapter(this, userList);

        ListView lView = (ListView) findViewById(R.id.lstSquadMembers);
        lView.setAdapter(uAdapter);
        */


        // Get user's last known location
        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }


    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    // TODO: Test with an actual device
    @Override
    public void onConnected(Bundle connectionHint) {
        Log.d("LocationServices", "Tryna locate dat user, nah wuh ahm sayun?\n");
        User currentUser = ((MyApplication) getApplication()).getCurrentUser();
        Location loc = null;
        try {
            loc = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
        }catch(SecurityException e){
            Log.d("LocationServices", "Ah ah ah! Mudduhsukka you cant do that.\n");
        }
        if (loc != null) {
            //mLatitudeText.setText(String.valueOf(mLastLocation.getLatitude()));
            //mLongitudeText.setText(String.valueOf(mLastLocation.getLongitude()));
            currentUser.updateLocation(new LatLng(loc.getLatitude(), loc.getLongitude()));
            Log.d("LocationServices", "GOT LOCATION: (" + loc.getLatitude() + ", " + loc.getLongitude() + ")\n");
        }else{
            Log.d("LocationServices", "SHIT BRUH, could not locate\n");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.squadviewbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final User currentUser = ((MyApplication) getApplication()).getCurrentUser();
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
                        Intent intent = new Intent(SquadViewActivity.this, ProfileViewActivity.class);
                        intent.putExtra("MemberFromSquadView", ((User)parent.getItemAtPosition(position)).getUserId());
                        intent.putExtra("Squad", currentUser.getSquad().getSquadName());
                        startActivity(intent);
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
                        Intent intent = new Intent(SquadViewActivity.this, ProfileViewActivity.class);
                        intent.putExtra("AddMemberFromSquadView", ((User)parent.getItemAtPosition(position)).getUserId());
                        startActivity(intent);
                    }
                });
                mDrawerLayout.openDrawer(Gravity.RIGHT);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    //@Override
    public void leaveSquadConf(View view) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        final User currentUser = ((MyApplication) getApplication()).getCurrentUser();

        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setCancelable(false)
                .setTitle("Leaving Squad")
                .setMessage("Are you sure you want to leave your Squad " + currentUser.getSquad().getSquadName() + "?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // If leaver is captain, ask to change captain or delete squad
                        if (currentUser.getSquadRole().equals("captain")) {
                            // Captain is the only user in here. Delete the squad
                            if (currentUser.getSquad().getUserList().size() == 1) {
                                currentUser.setPartOfSquad(false);
                                mDatabase.child("Users/" + currentUser.getUserId()).child("squad").removeValue();
                                mDatabase.child("Users/" + currentUser.getUserId()).child("squadRole").removeValue();
                                mDatabase.child("Squads/" + currentUser.getSquad().getSquadID()).removeValue();
                                finish();
                            }
                            // Captain is not the only user. Ask to change captain or delete squad
                            else
                            {
                                new AlertDialog.Builder(SquadViewActivity.this)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setTitle("Dun dun dunnnnnnn")
                                .setMessage("Would you like to delete your squad or change ownership?")
                                .setPositiveButton("Change Owner", null)
                                .setNegativeButton("Delete Squad", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        currentUser.setPartOfSquad(false);
                                        captainDeletingSquad = true;
                                        mDatabase.child("Users/" + currentUser.getUserId()).child("squad").removeValue();
                                        mDatabase.child("Users/" + currentUser.getUserId()).child("squadRole").removeValue();
                                        mDatabase.child("Squads/" + currentUser.getSquad().getSquadID()).removeValue();
                                    }
                                })
                                .show();
                            }
                        }
                        else
                        {
                            // Delete user from Squad
                            currentUser.setPartOfSquad(false);
                            mDatabase.child("Users/" + currentUser.getUserId()).child("squad").removeValue();
                            mDatabase.child("Users/" + currentUser.getUserId()).child("squadRole").removeValue();
                            mDatabase.child("Squads/" + currentUser.getSquad().getSquadID()).child("Members").child(currentUser.getUserId()).removeValue();
                            finish();
                        }
                    }   // end of onclick

                })  // end of setPositiveButton
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    protected void onResume() {
        final User currentUser = ((MyApplication) getApplication()).getCurrentUser();
        if (currentUser.isPartOfSquad()) {
            DatabaseReference squadRef = FirebaseDatabase.getInstance().getReference("Squads/" + currentUser.getSquad().getSquadID());
            squadRef.orderByPriority().addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mMemberNames.clear();
                    Log.v("Cleared Members", "Cleared Members");
                    HashMap<String, String> members = (HashMap) dataSnapshot.child("Members").getValue();
                    TextView squadNameView = (TextView) findViewById(R.id.txtSquadName);
                    currentUser.getSquad().setSquadName((String) dataSnapshot.child("name").getValue());
                    squadNameView.setText((String) dataSnapshot.child("name").getValue());
                    TextView squadDescView = (TextView) findViewById(R.id.txtSquadDesc);
                    currentUser.getSquad().setSquadDesc((String) dataSnapshot.child("description").getValue());
                    squadDescView.setText((String) dataSnapshot.child("description").getValue());

                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference storageRef = storage.getReferenceFromUrl("gs://breakfastclubapp-437bd.appspot.com");
                    StorageReference squadImageRef = storageRef.child("Squads/" + dataSnapshot.getKey());

                    final long ONE_MEGABYTE = 1024 * 1024;
                    squadImageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            // Data for "images/island.jpg" is returns, use this as needed
                            Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            ImageView squadImageView = (ImageView) findViewById(R.id.squadPhoto);

                            squadImageView.setImageBitmap(bmp);
                            currentUser.getSquad().setSquadPhoto(bmp);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle any errors
                        }
                    });



                    for (Map.Entry<String, String> member : members.entrySet()) {
                        DatabaseReference memberRef = FirebaseDatabase.getInstance().getReference("Users/" + member.getKey());
                        memberRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Log.v("Member", (String) dataSnapshot.child("name").getValue());
                                User currentMember = new User();
                                currentMember.setName((String) dataSnapshot.child("name").getValue());
                                currentMember.setProfileImageUrl((String) dataSnapshot.child("profileImageUrl").getValue());
                                currentMember.setUserId((String) dataSnapshot.getKey());
                                mMemberNames.add(currentMember);
                                currentUser.getSquad().setUserList(mMemberNames);
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

        mFriends.clear();
        for (int i = 0; i < currentUser.getFriends().size(); i++) {
            boolean friendInSquad = false;
            for (int j = 0; j < mMemberNames.size(); j++)
            {
                if (mMemberNames.get(j).getUserId().equals(currentUser.getFriends().get(i).getUserId()))
                {
                    friendInSquad = true;
                    break;
                }
            }
            if (!friendInSquad) {
                DatabaseReference memberRef = FirebaseDatabase.getInstance().getReference("Users/" + currentUser.getFriends().get(i).getUserId());
                memberRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User currentMember = new User();
                        if (!dataSnapshot.child("squad").exists()) {
                            currentMember.setName((String) dataSnapshot.child("name").getValue());
                            currentMember.setProfileImageUrl((String) dataSnapshot.child("profileImageUrl").getValue());
                            currentMember.setUserId((String) dataSnapshot.getKey());
                            mFriends.add(currentMember);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        }
        super.onResume();
    }




}