package cs490.breakfastclub.BreakfastFiles;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import cs490.breakfastclub.CameraAndPhotos.CameraActivity;
import cs490.breakfastclub.Classes.Post;
import cs490.breakfastclub.Classes.TimeFunctions;
import cs490.breakfastclub.MyApplication;
import cs490.breakfastclub.UserFiles.User;
import cs490.breakfastclub.R;


import static cs490.breakfastclub.Classes.TimeFunctions.isDuringBreakfast;
import static cs490.breakfastclub.GeofenceFiles.GeofenceTransitionsIntentService.MYPREFERENCES;

public class BreakfastFeedActivity extends AppCompatActivity {

    ImageButton upArrow;
    ImageButton downArrow;
    ImageButton cameraButton;
    ImageButton removePictureButton;
    TextView pictureScore;
    TextView countDownText;

    Breakfast currentBreakfast;
    User currentUser;
    Post currentPost;
    ImageView image;
    int tempScore;

    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        String layout = bundle.getString("Layout Type");
        setLayoutFromIntentString(layout);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Repeat Offenders");

    }



    private void setLayoutFromIntentString(String layout) {
        if (layout.equals("Campus Feed")) {
            setContentView(R.layout.activity_campus_feed);
            loadCurrentBreakfast();
        }
        if (layout.equals("Not on Campus")){
            setContentView(R.layout.not_on_campus);
        }
        if (layout.equals("Not Time")){
            setContentView(R.layout.time_until_breakfast);
            notTimeInit();
        }
    }

    private void notTimeInit() {
        countDownText = (TextView) findViewById(R.id.countdownTextId);
        createCountdownTimer();
    }

    private void createCountdownTimer() {

        final Calendar breakfastTime = TimeFunctions.getCurrentTime();
        breakfastTime.set(2016, 10, 5, 3, 0, 0);
        Calendar currentTime = TimeFunctions.getCurrentTime();

        long secondsToEvent = TimeFunctions.secondsToBreakfast(breakfastTime, currentTime);


        new CountDownTimer(secondsToEvent, 1000) {

            public void onTick(long millisUntilFinished) {
                String timeRemainingString = TimeFunctions.timeUntilEventString(breakfastTime);
                countDownText.setText(timeRemainingString);
            }

            public void onFinish() {
                countDownText.setText("done!");
            }
        }.start();
    }

    private void campusFeedInit() {

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Campus Feed");
        mDatabase = FirebaseDatabase.getInstance().getReference();

        upArrow = (ImageButton) findViewById(R.id.upArrowId);
        downArrow = (ImageButton) findViewById(R.id.downArrowId);
        removePictureButton = (ImageButton) findViewById(R.id.removePhotoButtonId);
        cameraButton = (ImageButton) findViewById(R.id.cameraButtonId);
        pictureScore = (TextView) findViewById(R.id.pictureScoreId);
        image = (ImageView) findViewById(R.id.campusFeedImageId);


        upArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //increaseCurrentPostScore();
            }
        });

        downArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //decreaseCurrentPostScore();
            }
        });

        removePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                // TODO: Uncomment when functional
                //removePost();



            }
        });

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCameraActivity();
            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setNextImage();
            }
        });

        cameraButton.setVisibility(View.VISIBLE);
        if (isDuringBreakfast() == false || currentUser.getNumberOfOffensives() >= 3){
            //cameraButton.setVisibility(View.INVISIBLE);
        }


        currentUser = ((MyApplication) getApplication()).getCurrentUser();
        //currentPost = currentBreakfast.getCampusFeed().get(currentUser.getCurrentPositionInFeed());
        //pictureScore.setText(currentPost.getScore());
        //String imgUrl = currentPost.getImgURL();
        //image.setImageDrawable(null);

        User.Permissions Permission = currentUser.getPermissions();
        if (Permission == User.Permissions.Developer || Permission == User.Permissions.Moderator){
            removePictureButton.setVisibility(View.VISIBLE);
        }

        tempScore = 0;

    }

    private void setNextImage() {

        int currentPosition = currentUser.getCurrentPositionInFeed();
        //TODO
        int breakfastPhotoCount = 0;

        if (currentPosition < breakfastPhotoCount) {
            currentUser.setCurrentPositionInFeed(currentPosition + 1);
            mDatabase.child("Users").child(currentUser.getUserId()).child("currentPositionInFeed").setValue(currentPosition+1);
            currentPost = currentBreakfast.getCampusFeed().get(currentPosition+1);
            //TODO set image
            //String nextImageUrl = currentPost.getImgURL();
            //image.setImageDrawable();
        }

        else if (currentPosition == breakfastPhotoCount){
            currentUser.setCurrentPositionInFeed(0);
            mDatabase.child("Users").child(currentUser.getUserId()).child("currentPositionInFeed").setValue(0);
            mDatabase.child("Users").child(currentUser.getUserId()).setValue("hasVotedUp");
            mDatabase.child("Users").child(currentUser.getUserId()).setValue("hasVotedDown");
        }

    }

    private void launchCameraActivity() {
        Intent intent = new Intent(BreakfastFeedActivity.this, CameraActivity.class);
        startActivity(intent);
    }

    private void decreaseCurrentPostScore() {

        boolean hasVotedDown = currentUser.getHasVotedDown().get(currentUser.getCurrentPositionInFeed());

       /*
        if (hasVotedOnThisPic == false){
            currentPost.setScore(currentPost.getScore() - 1);
            currentUser.getHasVoted().set(currentUser.getCurrentPositionInFeed() ,true);
            pictureScore.setText(currentPost.getScore());
            //TODO save to DB
        }
        */

        if (tempScore > -5) {
            tempScore -= 1;
            pictureScore.setText(tempScore + "");
        }
    }

    private void increaseCurrentPostScore() {
        /*
        boolean hasVotedOnThisPic = currentUser.getHasVoted().get(currentUser.getCurrentPositionInFeed());
        if (hasVotedOnThisPic == false){
            currentPost.setScore(currentPost.getScore() + 1);
            currentUser.getHasVoted().set(currentUser.getCurrentPositionInFeed() ,true);
            pictureScore.setText(currentPost.getScore());
            //TODO save to DB
        }
        */
        tempScore += 1;
        pictureScore.setText(tempScore + "");
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

    public static Intent chooseCampusFeedActivity(Context context) {
        Intent intent = new Intent(context, BreakfastFeedActivity.class);

        SharedPreferences sharedpreferences = context.getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        boolean onCampus = sharedpreferences.getBoolean("IsInGeofence", true);
        boolean isDuringBreakfast = sharedpreferences.getBoolean("isDuringBreakfast", true);

        //Breakfast currentBreakfast = getCurrentBreakfast();

        if (onCampus == false) {
            intent.putExtra("Layout Type", "Not on Campus");
        }
        else if (1 == 0){
        //else if (TimeFunctions.isDuringVotingPeriod() == true){
            //not breakfast club time
            intent.putExtra("Layout Type", "Not Time");
        }
        else{
            intent.putExtra("Layout Type", "Campus Feed");
        }
        return intent;
    }


    public void loadCurrentBreakfast() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Breakfasts");
        ref.orderByChild("isCurrentBreakfast").equalTo("true").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                int year = Integer.parseInt(dataSnapshot.child("year").getValue().toString());
                int month = Integer.parseInt(dataSnapshot.child("month").getValue().toString());
                int day =  Integer.parseInt(dataSnapshot.child("day").getValue().toString());
                String description = dataSnapshot.child("description").getValue().toString();
                String breakfastKey = dataSnapshot.getKey();
                currentBreakfast = new Breakfast(year, month, day, description, breakfastKey);
                campusFeedInit();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void removePost()
    {
        String sender = currentPost.getSenderID();
        // ^ Use that to retrieve User info from firebase
        final DatabaseReference memberRef = FirebaseDatabase.getInstance().getReference("Users/" + sender);
        memberRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
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
                        u.setNumberOfOffensives(i.intValue() + 1);
                        Log.d("Offensives", "Updated value for user");
                    }
                    else
                    {
                        u.setNumberOfOffensives(1);
                        Log.d("Offensives", "Added Field numberOfOffensives for " + u.getName());
                        memberRef.child(u.getUserId()).child("numberOfOffensives").setValue(0);
                        Log.d("Offensives", "Success");
                    }

                    // Update user in the database
                    mDatabase.child("Users").child(u.getUserId()).child("numberOfOffensives").setValue(u.getNumberOfOffensives());

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // TODO: @Emma
        // Remove picture from BreakfastFeed

        // Update database with picture removed

        // TODO: Trevor
        // Send push notification to user if they have push notifications enabled

    }
}
