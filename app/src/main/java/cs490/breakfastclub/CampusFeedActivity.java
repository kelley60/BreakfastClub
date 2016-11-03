package cs490.breakfastclub;

import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import cs490.breakfastclub.Classes.Breakfast;
import cs490.breakfastclub.Classes.Post;
import cs490.breakfastclub.Classes.TimeFunctions;
import cs490.breakfastclub.Classes.User;

import static cs490.breakfastclub.Classes.TimeFunctions.isDuringBreakfast;

public class CampusFeedActivity extends AppCompatActivity {

    ImageButton upArrow;
    ImageButton downArrow;
    ImageButton cameraButton;
    TextView pictureScore;
    TextView countDownText;
    Breakfast currentBreakfast;
    User currentUser;
    Post currentPost;
    ImageView image;


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

        upArrow = (ImageButton) findViewById(R.id.upArrowId);
        downArrow = (ImageButton) findViewById(R.id.downArrowId);
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

        cameraButton.setVisibility(View.INVISIBLE);
        if (isDuringBreakfast()){
            cameraButton.setVisibility(View.VISIBLE);
        }

        /*
        currentUser = User.getCurrentUser();
        currentBreakfast = Breakfast.getCurrentBreakfast();
        currentPost = currentBreakfast.getCampusFeed().get(currentUser.getCurrentPositionInFeed());
        pictureScore.setText(currentPost.getScore());
        String imgUrl = currentPost.getImgURL();
        image.setImageDrawable(null);
        */

    }

    private void setNextImage() {
        currentUser.setCurrentPositionInFeed(currentUser.getCurrentPositionInFeed()+1);
        currentPost = currentBreakfast.getCampusFeed().get(currentUser.getCurrentPositionInFeed());
        String nextImageUrl = currentPost.getImgURL();
        //image.setImageDrawable
        //save users position in campusfeed to db
    }

    //TODO for Emma
    private void launchCameraActivity() {

    }

    private void decreaseCurrentPostScore() {
        boolean hasVotedOnThisPic = currentUser.getHasVoted().get(currentUser.getCurrentPositionInFeed());
        if (hasVotedOnThisPic == false){
            currentPost.setScore(currentPost.getScore() - 1);
            currentUser.getHasVoted().set(currentUser.getCurrentPositionInFeed() ,true);
            //TODO save to DB
        }
    }

    private void increaseCurrentPostScore() {
        boolean hasVotedOnThisPic = currentUser.getHasVoted().get(currentUser.getCurrentPositionInFeed());
        if (hasVotedOnThisPic == false){
            currentPost.setScore(currentPost.getScore() + 1);
            currentUser.getHasVoted().set(currentUser.getCurrentPositionInFeed() ,true);
            //TODO save to DB
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
