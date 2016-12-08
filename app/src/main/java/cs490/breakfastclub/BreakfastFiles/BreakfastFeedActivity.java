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

import java.util.Calendar;

import cs490.breakfastclub.CameraAndPhotos.CameraActivity;
import cs490.breakfastclub.CameraAndPhotos.Photos;
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
    Photos currentPhotos;
    int tempScore;
    int currentPositionInFeed;
    int breakfastPhotoCount;

    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        String layout = bundle.getString("Layout Type");
        setLayoutFromIntentString(layout);

    }

    private void setLayoutFromIntentString(String layout) {
        if (layout.equals("Campus Feed")) {
            setContentView(R.layout.activity_campus_feed);
            //loadCurrentBreakfast();
            campusFeedInit();
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

        currentUser = ((MyApplication) getApplication()).getCurrentUser();
        currentBreakfast = ((MyApplication) getApplication()).getCurrentBreakfast();
        currentPhotos = ((MyApplication) getApplication()).getCurrentPhotos();

        upArrow = (ImageButton) findViewById(R.id.upArrowId);
        downArrow = (ImageButton) findViewById(R.id.downArrowId);
        removePictureButton = (ImageButton) findViewById(R.id.removePhotoButtonId);
        cameraButton = (ImageButton) findViewById(R.id.cameraButtonId);
        pictureScore = (TextView) findViewById(R.id.pictureScoreId);
        image = (ImageView) findViewById(R.id.campusFeedImageId);
        currentPositionInFeed = currentUser.getCurrentPositionInFeed();
        //TODO
        breakfastPhotoCount = 10;
        currentUser.increaseVotingArrays(breakfastPhotoCount);


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
                //removeCurrentPicture();
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
            cameraButton.setVisibility(View.INVISIBLE);
        }

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

        if (currentPositionInFeed < breakfastPhotoCount) {

            currentPositionInFeed += 1;
            currentUser.setCurrentPositionInFeed(currentPositionInFeed);
            mDatabase.child("Users").child(currentUser.getUserId()).child("currentPositionInFeed").setValue(currentPositionInFeed);
            //TODO set image
            //String nextImageUrl = currentPost.getImgURL();
            //image.setImageDrawable();
        }

        if (currentPositionInFeed == breakfastPhotoCount){
            //TODO check if photocount has increased
            //breakfastPhotoCount = 0
            if (currentPositionInFeed == breakfastPhotoCount) {
                currentPositionInFeed = 0;
                currentUser.setCurrentPositionInFeed(currentPositionInFeed);
                mDatabase.child("Users").child(currentUser.getUserId()).child("currentPositionInFeed").setValue(currentPositionInFeed);
            }
            //number of photos has increased, load next photo
            else{
                currentUser.increaseVotingArrays(breakfastPhotoCount);
                currentPositionInFeed += 1;
                currentUser.setCurrentPositionInFeed(currentPositionInFeed);
                mDatabase.child("Users").child(currentUser.getUserId()).child("currentPositionInFeed").setValue(currentPositionInFeed);

                //TODO set image

                //String nextImageUrl = currentPost.getImgURL();
                //image.setImageDrawable();
            }
        }

    }

    private void launchCameraActivity() {
        Intent intent = new Intent(BreakfastFeedActivity.this, CameraActivity.class);
        startActivity(intent);
    }

    private void decreaseCurrentPostScore() {

        boolean hasVotedDown = currentUser.getHasVotedDown().get(currentPositionInFeed);
        boolean hasVotedUp = currentUser.getHasVotedUp().get(currentPositionInFeed);


        if (hasVotedDown){
            currentPost.setScore(currentPost.getScore() + 1);
            currentUser.getHasVotedDown().set(currentPositionInFeed , false);
            pictureScore.setText(currentPost.getScore());
        }
        else if(hasVotedUp){
            currentPost.setScore(currentPost.getScore() - 2);
            currentUser.getHasVotedDown().set(currentPositionInFeed , true);
            currentUser.getHasVotedUp().set(currentPositionInFeed , false);
            pictureScore.setText(currentPost.getScore());
        }
        else{
            currentPost.setScore(currentPost.getScore() - 1);
            currentUser.getHasVotedDown().set(currentPositionInFeed , true);
            pictureScore.setText(currentPost.getScore());

            if (currentPost.getScore() <= -5){
                removeCurrentPicture();
            }
        }


    }

    private void increaseCurrentPostScore() {

        boolean hasVotedDown = currentUser.getHasVotedDown().get(currentPositionInFeed);
        boolean hasVotedUp = currentUser.getHasVotedUp().get(currentPositionInFeed);


        if (hasVotedDown){
            currentPost.setScore(currentPost.getScore() + 2);
            currentUser.getHasVotedDown().set(currentPositionInFeed , false);
            currentUser.getHasVotedUp().set(currentPositionInFeed , true);
            pictureScore.setText(currentPost.getScore());
        }
        else if(hasVotedUp){
            currentPost.setScore(currentPost.getScore() - 1);
            currentUser.getHasVotedUp().set(currentPositionInFeed , false);
            pictureScore.setText(currentPost.getScore());
        }
        else{
            currentPost.setScore(currentPost.getScore() + 1);
            currentUser.getHasVotedUp().set(currentPositionInFeed , true);
            pictureScore.setText(currentPost.getScore());
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

    public void removeCurrentPicture(){
        //currentPost.getSenderId();
        // ^ Use that to retrieve User info from firebase
        // Increase numberOffensive by 1
        // Update User in database
        // Remove picture from BreakfastFeed
        // Update database with picture removed
    }

}
