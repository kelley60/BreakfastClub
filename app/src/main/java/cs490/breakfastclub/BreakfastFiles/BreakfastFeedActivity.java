package cs490.breakfastclub.BreakfastFiles;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;

import cs490.breakfastclub.CameraAndPhotos.CameraActivity;
import cs490.breakfastclub.CameraAndPhotos.Photos;
import cs490.breakfastclub.Classes.Post;
import cs490.breakfastclub.Classes.TimeFunctions;
import cs490.breakfastclub.MyApplication;
import cs490.breakfastclub.R;
import cs490.breakfastclub.ToolBarBreakfastClub;
import cs490.breakfastclub.UserFiles.User;

import static cs490.breakfastclub.Classes.TimeFunctions.isDuringBreakfast;
import static cs490.breakfastclub.GeofenceFiles.GeofenceTransitionsIntentService.MYPREFERENCES;

public class BreakfastFeedActivity extends AppCompatActivity {

    private static final String ToolbarTitle = "Campus Feed";

    ImageButton upArrow;
    ImageButton downArrow;
    ImageButton cameraButton;
    ImageButton removePictureButton;
    TextView pictureScoreTextView;
    ImageView image;

    Breakfast currentBreakfast;
    User currentUser;
    Post currentPost;
    Photos currentPhotos;

    int currentPositionInFeed;
    int breakfastPhotoCount;
    int currentScore;
    String currentPhotoId;

    private ArrayList<URL> photos;
    private ArrayList<String> photoids;
    LinkedHashMap<String, URL> linkedHashMap;

    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campus_feed);
        ToolBarBreakfastClub.toolBarInit(this, ToolbarTitle);
        campusFeedInit();
    }


    private void campusFeedInit() {

        widgetInit();
        variableInit();
        setCameraButtonVisibility();
        setRemovePictureButtonVisibility();

        if (breakfastPhotoCount == 0){
            //no photos, don't set anything
        }
        else {
            setCurrentPicture();
            setPictureScore();
        }
    }




    private void setNextImage() {

        //Fencepost
        breakfastPhotoCount = photos.size() - 1;

        if (currentPositionInFeed < breakfastPhotoCount) {
            currentPositionInFeed += 1;
            updateCurrentPositionInFeed();
            setCurrentPicture();
            setPictureScore();
        }

        if (currentPositionInFeed == breakfastPhotoCount) {
            currentPositionInFeed = 0;
            updateCurrentPositionInFeed();
            setCurrentPicture();
            setPictureScore();
        }

    }

    private void updateCurrentPositionInFeed() {
        currentUser.setCurrentPositionInFeed(currentPositionInFeed);
        mDatabase.child("Users").child(currentUser.getUserId()).child("currentPositionInFeed").setValue(currentPositionInFeed);
    }


    private void decreaseCurrentPostScore() {

        boolean hasVotedDown = currentUser.getHasVotedDown().get(currentPositionInFeed);
        boolean hasVotedUp = currentUser.getHasVotedUp().get(currentPositionInFeed);

        if (hasVotedDown){
            currentScore += 1;
            currentUser.getHasVotedDown().set(currentPositionInFeed , false);
            mDatabase.child("Users").child(currentUser.getUserId()).child("hasVotedDown").child(currentPositionInFeed+"").child("false");
            updateCurrentPictureScore();
            setPictureScore();
        }
        else if(hasVotedUp){
            currentScore -= 2;
            currentUser.getHasVotedDown().set(currentPositionInFeed , true);
            currentUser.getHasVotedUp().set(currentPositionInFeed , false);
            mDatabase.child("Users").child(currentUser.getUserId()).child("hasVotedDown").child(currentPositionInFeed+"").child("true");
            mDatabase.child("Users").child(currentUser.getUserId()).child("hasVotedUp").child(currentPositionInFeed+"").child("false");
            updateCurrentPictureScore();
            setPictureScore();
        }
        else{
            currentScore -= 1;
            currentUser.getHasVotedDown().set(currentPositionInFeed , true);
            mDatabase.child("Users").child(currentUser.getUserId()).child("hasVotedUp").child(currentPositionInFeed+"").child("true");
            updateCurrentPictureScore();
            setPictureScore();

            if (currentScore <= -5){
                removeCurrentPicture();
            }
        }



    }

    private void increaseCurrentPostScore() {

        boolean hasVotedDown = currentUser.getHasVotedDown().get(currentPositionInFeed);
        boolean hasVotedUp = currentUser.getHasVotedUp().get(currentPositionInFeed);


        if (hasVotedDown){
            currentScore += 2;
            currentUser.getHasVotedDown().set(currentPositionInFeed , false);
            currentUser.getHasVotedUp().set(currentPositionInFeed , true);
            mDatabase.child("Users").child(currentUser.getUserId()).child("hasVotedDown").child(currentPositionInFeed+"").child("false");
            mDatabase.child("Users").child(currentUser.getUserId()).child("hasVotedUp").child(currentPositionInFeed+"").child("true");
            updateCurrentPictureScore();
            setPictureScore();
        }
        else if(hasVotedUp){
            currentScore -= 1;
            currentUser.getHasVotedUp().set(currentPositionInFeed , false);
            mDatabase.child("Users").child(currentUser.getUserId()).child("hasVotedUp").child(currentPositionInFeed+"").child("false");
            updateCurrentPictureScore();
            setPictureScore();
        }
        else{
            currentScore += 1;
            currentUser.getHasVotedUp().set(currentPositionInFeed , true);
            mDatabase.child("Users").child(currentUser.getUserId()).child("hasVotedUp").child(currentPositionInFeed+"").child("true");
            updateCurrentPictureScore();
            setPictureScore();
        }

   }

    private void updateCurrentPictureScore() {
        currentPhotoId = photoids.get(currentPositionInFeed);
        currentUser.getCurrentPhotos().getBreakfastVotes().put(currentPhotoId, currentScore);
        mDatabase.child("Breakfasts").child(currentBreakfast.getBreakfastKey()).child("Votes").child(currentPhotoId).setValue(currentScore);
    }


    public static Intent chooseCampusFeedActivity(Context context) {
        Intent intent = new Intent(context, BreakfastFeedActivity.class);

        SharedPreferences sharedpreferences = context.getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        boolean onCampus = sharedpreferences.getBoolean("IsInGeofence", true);
        boolean isDuringBreakfast = sharedpreferences.getBoolean("isDuringBreakfast", true);

        if (onCampus == false) {
            intent.putExtra("Layout Type", "Not on Campus");
        }
        else if (1 == 0){
        //else if (TimeFunctions.isDuringVotingPeriod() == true){
            //not breakfast club time
            intent.putExtra("Layout Type", "Not Time");
        }
        else{
            return intent;
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

    private void removePost() {
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
    }

    private void setPictureScore(){
        currentScore = currentUser.getCurrentPhotos().getBreakfastVotes().get(photoids.get(currentPositionInFeed));
        pictureScoreTextView.setText(currentScore + "");
    }


    private void setCurrentPicture() {
            Picasso.with(getApplicationContext()).load(getItemURL(currentPositionInFeed).toString()).fit().into(image);
    }

    private void variableInit() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        currentUser = ((MyApplication) getApplication()).getCurrentUser();
        currentBreakfast = ((MyApplication) getApplication()).getCurrentBreakfast();
        currentPhotos = currentUser.getCurrentPhotos();

        linkedHashMap = currentPhotos.getBreakfastPhotos();
        photos = new ArrayList<URL>(linkedHashMap.values());
        photoids = new ArrayList<String>(linkedHashMap.keySet());
        breakfastPhotoCount = photos.size();

        currentPositionInFeed = currentUser.getCurrentPositionInFeed();
        currentUser.increaseVotingArrays(breakfastPhotoCount);
        currentPost = null;
    }

    public URL getItemURL(int position) {
        return photos.get(position);
    }

    private void launchCameraActivity() {
        Intent intent = new Intent(BreakfastFeedActivity.this, CameraActivity.class);
        startActivity(intent);
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

    private void setRemovePictureButtonVisibility() {
        User.Permissions Permission = currentUser.getPermissions();
        if (Permission == User.Permissions.Developer || Permission == User.Permissions.Moderator){
            removePictureButton.setVisibility(View.VISIBLE);
        }
    }

    private void setCameraButtonVisibility() {
        cameraButton.setVisibility(View.VISIBLE);
        if (isDuringBreakfast() == false || currentUser.getNumberOfOffensives() >= 3){
            //cameraButton.setVisibility(View.INVISIBLE);
        }
    }

    private void widgetInit() {
        upArrow = (ImageButton) findViewById(R.id.upArrowId);
        downArrow = (ImageButton) findViewById(R.id.downArrowId);
        removePictureButton = (ImageButton) findViewById(R.id.removePhotoButtonId);
        cameraButton = (ImageButton) findViewById(R.id.cameraButtonId);
        pictureScoreTextView = (TextView) findViewById(R.id.pictureScoreId);
        image = (ImageView) findViewById(R.id.campusFeedImageId);


        upArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increaseCurrentPostScore();
            }
        });

        downArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decreaseCurrentPostScore();
            }
        });

        removePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeCurrentPicture();
                removePost();
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
                setNextImage();
            }
        });
    }

}
