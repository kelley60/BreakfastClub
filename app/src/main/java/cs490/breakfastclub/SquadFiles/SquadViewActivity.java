package cs490.breakfastclub.SquadFiles;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import cs490.breakfastclub.CameraAndPhotos.GalleryActivity;
import cs490.breakfastclub.Classes.Notification;
import cs490.breakfastclub.DisplayMessageActivity;
import cs490.breakfastclub.MyApplication;
import cs490.breakfastclub.ProfileViewActivity;
import cs490.breakfastclub.R;
import cs490.breakfastclub.UserFiles.User;

public class SquadViewActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    // TODO: Populate UI with information from database

    private ArrayList<User> member;
    private ArrayList<User> mFriends;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private TextView mDrawerTitle;
    CharSequence[] items;
    List<CharSequence> userNames = new ArrayList<>();
    List<CharSequence> userIDs = new ArrayList<>();

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
        final User currentUser = ((MyApplication) getApplication()).getCurrentUser();

        setContentView(R.layout.activity_squad_view);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("[SQUAD_NAME]");

        member = new ArrayList<User>();
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
                Intent intent = new Intent(SquadViewActivity.this, GalleryActivity.class);
                intent.putExtra("photo set", 1);
                // 0 is users, 1 is squad, 2 is breakfast feed
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

/*
        final User currentUser = ((MyApplication) getApplication()).getCurrentUser();
        final LinkedHashMap<String, URL> linkedHashMap = ((MyApplication) getApplication()).getCurrentPhotos().getUserPhotos();
        final ArrayList<URL> currentPhotos = new ArrayList<URL>(linkedHashMap.values());
        final ArrayList<String> photoids = new ArrayList<String>(linkedHashMap.keySet());
        final ImageView imgView = (ImageView) findViewById(R.id.squadPhoto);

*/
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
                        R.layout.squad_member_list_item, member));
                // Set the list's click listener
                mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(SquadViewActivity.this, ProfileViewActivity.class);
                        intent.putExtra("MemberFromSquadView", ((User)parent.getItemAtPosition(position)).getUserId());
                        intent.putExtra("Squad", currentUser.getSquad().getSquadName());
                        mDrawerLayout.closeDrawer(GravityCompat.END);
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
                        intent.putExtra("MemberToAddName", ((User)parent.getItemAtPosition(position)).getName());
                        intent.putExtra("MemberToAddImageUrl", ((User)parent.getItemAtPosition(position)).getProfileImageUrl());
                        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                        mDrawerLayout.closeDrawer(GravityCompat.END);
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
                                Log.d("HI", "deleting the only user in the sq - " + currentUser.getSquad().getSquadID());
                                mDatabase.child("Users/" + currentUser.getUserId()).child("squad").removeValue();
                                mDatabase.child("Users/" + currentUser.getUserId()).child("squadRole").removeValue();
                                mDatabase.child("Squads/" + currentUser.getSquad().getSquadID()).removeValue();
                                Notification n = new Notification("Squad Removal",
                                        "You have been removed from squad " + currentUser.getSquad().getSquadName(),
                                        currentUser.getUserId());
                                n.addToFirebase();
                                currentUser.setSquad(null);
                                currentUser.setSquadRole("");
                                finish();
                            }
                            // Captain is not the only user. Ask to change captain or delete squad
                            else
                            {
                                new AlertDialog.Builder(SquadViewActivity.this)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setTitle("Dun dun dunnnnnnn")
                                .setMessage("Would you like to delete your squad or change ownership?")
                                .setPositiveButton("Change Owner", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        // Start listener
                                        DatabaseReference squadRef = FirebaseDatabase.getInstance().getReference("Squads/" + currentUser.getSquad().getSquadID());
                                        squadRef.orderByPriority().addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {

                                                // Build radio button list for user to click
                                                ListIterator<User> iter = currentUser.getSquad().getUserList().listIterator();
                                                while(iter.hasNext()) {
                                                    User temp = iter.next();
                                                    String name = temp.getName();
                                                    String ID = temp.getUserId();
                                                    if (!ID.equals(currentUser.getUserId())) {
                                                        userNames.add(name);
                                                        userIDs.add(ID);
                                                    }
                                                }
                                                AlertDialog.Builder builder = new AlertDialog.Builder(SquadViewActivity.this);
                                                builder.setTitle("Pick a new captain")
                                                        .setItems(userNames.toArray(new CharSequence[userNames.size()]), new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                // The 'which' argument contains the index position of the selected item
                                                                // Make that user captain
                                                                Log.d("User selected as cptn", "" + userNames.get(which).toString() + " " + userIDs.get(which).toString());
                                                                mDatabase.child("Users/"  + userIDs.get(which).toString()).child("squadRole").setValue("captain");
                                                                mDatabase.child("Squads/" + currentUser.getSquad().getSquadID()).child("Members").child(userIDs.get(which).toString()).child("squadRole").setValue("captain");

                                                                // Delete current user from Squad
                                                                Notification n = new Notification("Squad Removal",
                                                                        "You have been removed from squad " + currentUser.getSquad().getSquadName(),
                                                                        currentUser.getUserId());
                                                                n.addToFirebase();
                                                                currentUser.setPartOfSquad(false);
                                                                mDatabase.child("Users/" + currentUser.getUserId()).child("squad").removeValue();
                                                                mDatabase.child("Users/" + currentUser.getUserId()).child("squadRole").removeValue();
                                                                mDatabase.child("Squads/" + currentUser.getSquad().getSquadID()).child("Members").child(currentUser.getUserId()).removeValue();
                                                                currentUser.setSquad(null);
                                                                currentUser.setSquadRole("");

                                                                finish();
                                                            }
                                                        });
                                                builder.create();
                                                builder.show();
                                                // end of radio button list
                                            }
                                            @Override public void onCancelled(DatabaseError databaseError) {}
                                        });  // end of listener
                                    }
                                })
                                .setNegativeButton("Delete Squad", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        DatabaseReference squadRef = FirebaseDatabase.getInstance().getReference("Squads/" + currentUser.getSquad().getSquadID());
                                        squadRef.orderByPriority().addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                HashMap<String, String> members = (HashMap) dataSnapshot.child("Members").getValue();
                                                for (Map.Entry<String, String> mem : members.entrySet()) {
                                                    mDatabase.child("Users/" + mem.getKey()).child("squad").removeValue();
                                                    mDatabase.child("Users/" + mem.getKey()).child("squadRole").removeValue();
                                                }
                                                mDatabase.child("Squads/" + currentUser.getSquad().getSquadID()).removeValue();
                                                Notification n = new Notification("Squad Removal",
                                                        "You have been removed from squad " + currentUser.getSquad().getSquadName(),
                                                        currentUser.getUserId());
                                                n.addToFirebase();
                                                currentUser.setPartOfSquad(false);
                                                currentUser.setSquad(null);
                                                currentUser.setSquadRole("");
                                                finish();
                                            }

                                            @Override public void onCancelled(DatabaseError databaseError) {}
                                        });  // end of listener on squad
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
                            Notification n = new Notification("Squad Removal",
                                    "You have been removed from squad " + currentUser.getSquad().getSquadName(),
                                    currentUser.getUserId());
                            n.addToFirebase();
                            currentUser.setSquad(null);
                            currentUser.setSquadRole("");
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
                    member.clear();

                    // Sets properties for current user and UI
                    Log.v("Cleared Members", "Cleared Members");
                    HashMap<String, HashMap> members = (HashMap) dataSnapshot.child("Members").getValue();
                    TextView squadNameView = (TextView) findViewById(R.id.txtSquadName);
                    currentUser.getSquad().setSquadName((String) dataSnapshot.child("name").getValue());
                    squadNameView.setText((String) dataSnapshot.child("name").getValue());
                    TextView squadDescView = (TextView) findViewById(R.id.txtSquadDesc);
                    currentUser.getSquad().setSquadDesc((String) dataSnapshot.child("description").getValue());
                    squadDescView.setText((String) dataSnapshot.child("description").getValue());

                    ImageView squadImageView = (ImageView) findViewById(R.id.squadPhoto);
                    Picasso.with(getApplicationContext()).load((String) dataSnapshot.child("profileImageUrl").getValue()).into(squadImageView);


                    for (Map.Entry<String, HashMap> memberEntry : members.entrySet()) {
                        Log.v("Each Member", memberEntry.toString());
                        HashMap<String, String> memberInfo = memberEntry.getValue();
                        User currentMember = new User();
                        currentMember.setName(memberInfo.get("name"));
                        currentMember.setProfileImageUrl(memberInfo.get("profileImageUrl"));
                        currentMember.setUserId(memberEntry.getKey());
                        member.add(currentMember);
                    }
                    currentUser.getSquad().setUserList(member);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        mFriends.clear();
        for (int i = 0; i < currentUser.getFriends().size(); i++) {
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


        super.onResume();
    }




}