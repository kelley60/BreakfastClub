package cs490.breakfastclub;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import cs490.breakfastclub.CameraAndPhotos.GalleryActivity;
import cs490.breakfastclub.Classes.Notification;
import cs490.breakfastclub.UserFiles.User;


public class ProfileViewActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;

    // TODO: Populate UI with data from database


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("[PROFILE_NAME]");

        mDatabase = FirebaseDatabase.getInstance().getReference();
        final User currentUser = ((MyApplication) getApplication()).getCurrentUser();



        if (getIntent().hasExtra("MemberFromSquadView"))
        {
            String userID = getIntent().getStringExtra("MemberFromSquadView");
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users/" + userID);
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.v("Member", dataSnapshot.toString());
                    TextView profileNameView = (TextView) findViewById(R.id.lblProfileName);
                    profileNameView.setText((String) dataSnapshot.child("name").getValue());
                    ImageView profileImageView = (ImageView) findViewById(R.id.lblProfileImage);
                   // new DownloadImageAsyncTask(profileImageView)
                     //       .execute((String) dataSnapshot.child("profileImageUrl").getValue());
                    Picasso.with(getApplicationContext()).load(currentUser.getProfileImageUrl().toString()).into(profileImageView);

                    TextView squadNameView = (TextView) findViewById(R.id.lblSquadName);
                    squadNameView.setText(getIntent().getStringExtra("Squad"));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            if(userID.equals(currentUser.getUserId())) {
                Button btnGallery = (Button) findViewById(R.id.btnViewSquadPhotos);
                btnGallery.setVisibility(View.VISIBLE);
                btnGallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ProfileViewActivity.this, GalleryActivity.class);
                        intent.putExtra("photo set", 0);
                        startActivity(intent);
                    }
                });

                Button btnNotifications = (Button) findViewById(R.id.btnNotifications);
                btnNotifications.setVisibility(View.VISIBLE);
                btnNotifications.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentUser.clearNotifications();
                        currentUser.getNotifications();
                        Intent intent = new Intent(ProfileViewActivity.this, NotificationViewActivity.class);
                        startActivity(intent);
                    }
                });
            }


        }
        else if (getIntent().hasExtra(("AddMemberFromSquadView")))
        {
            Button addToSquadButton = (Button) findViewById(R.id.btn_addToSqaud);
            final String userID = getIntent().getStringExtra("AddMemberFromSquadView");
            addToSquadButton.setVisibility(View.VISIBLE);
            addToSquadButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDatabase.child("Users/" + userID).child("squad").setValue(currentUser.getSquad().getSquadID());
                    mDatabase.child("Users/" + userID).child("squadRole").setValue("regular");
                    mDatabase.child("Squads/" + currentUser.getSquad().getSquadID()).child("Members").child(userID).child("squadRole").setValue("regular");
                    mDatabase.child("Squads/" + currentUser.getSquad().getSquadID()).child("Members").child(userID).child("name").setValue(getIntent().getStringExtra("MemberToAddName"));
                    mDatabase.child("Squads/" + currentUser.getSquad().getSquadID()).child("Members").child(userID).child("profileImageUrl").setValue(getIntent().getStringExtra("MemberToAddImageUrl"));
                    mDatabase.child("Squads/" + currentUser.getSquad().getSquadID()).child("Members").child(userID).setPriority(2);
                    Notification n = new Notification( "Squad Addition","You have been added to squad " + currentUser.getSquad().getSquadName(),
                            userID);
                    n.addToFirebase();
                    finish();
                }
            });

            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users/" + userID);
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.v("Member", dataSnapshot.toString());
                    TextView profileNameView = (TextView) findViewById(R.id.lblProfileName);
                    profileNameView.setText((String) dataSnapshot.child("name").getValue());
                    ImageView profileImageView = (ImageView) findViewById(R.id.lblProfileImage);
                    TextView squadNameView = (TextView) findViewById(R.id.lblSquadName);
                    squadNameView.setText("");
                    new DownloadImageAsyncTask(profileImageView)
                            .execute((String) dataSnapshot.child("profileImageUrl").getValue());

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        else
        {
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users/" + currentUser.getUserId());
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.v("Member", dataSnapshot.toString());
                    TextView profileNameView = (TextView) findViewById(R.id.lblProfileName);
                    profileNameView.setText((String) dataSnapshot.child("name").getValue());
                    ImageView profileImageView = (ImageView) findViewById(R.id.lblProfileImage);
                    new DownloadImageAsyncTask(profileImageView)
                            .execute((String) dataSnapshot.child("profileImageUrl").getValue());
                    final TextView squadNameView = (TextView) findViewById(R.id.lblSquadName);
                    if (currentUser.isPartOfSquad())
                    {
                        DatabaseReference squadRef = FirebaseDatabase.getInstance().getReference("Squads/" + (String) dataSnapshot.child("squad").getValue());
                        squadRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                squadNameView.setText((String) dataSnapshot.child("name").getValue());
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            Button btnGallery = (Button) findViewById(R.id.btnViewSquadPhotos);
            btnGallery.setVisibility(View.VISIBLE);
            btnGallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ProfileViewActivity.this, GalleryActivity.class);
                    intent.putExtra("photo set", 0);
                    startActivity(intent);
                }
            });

            //((MyApplication) getApplication()).getCurrentUser().addTestNotification();

            Button btnNotifications = (Button) findViewById(R.id.btnNotifications);
            btnNotifications.setVisibility(View.VISIBLE);
            btnNotifications.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentUser.clearNotifications();
                    currentUser.getNotifications();
                    Intent intent = new Intent(ProfileViewActivity.this, NotificationViewActivity.class);
                    startActivity(intent);
                }
            });
        }
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
