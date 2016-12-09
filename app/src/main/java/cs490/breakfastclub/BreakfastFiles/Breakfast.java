package cs490.breakfastclub.BreakfastFiles;

import android.content.Context;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;

import cs490.breakfastclub.Classes.Post;
import cs490.breakfastclub.Classes.TimeFunctions;
import cs490.breakfastclub.MyApplication;

/**
 * Created by Sean on 10/17/16.
 */

public class Breakfast {

    private String description;
    private ArrayList<Post> campusFeed;
    private Post topPhoto;
    private boolean currentBreakfast;
    private String breakfastKey;
    private int year;
    private int month;
    private int day;

    private static final int BREAKFAST_START_HOUR = 3;
    private static final int BREAKFAST_START_MINUTE = 0;
    private static final int BREAKFAST_START_SECOND = 0;

    public Breakfast(int year, int month, int day, String descriptionString, String breakfastKey) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.description = descriptionString;
        this.breakfastKey = breakfastKey;
    }


    public static void endCurrentBreakfast(){
        final DatabaseReference breakfastReference = FirebaseDatabase.getInstance().getReference("Breakfasts");

        breakfastReference.orderByChild("isCurrentBreakfast").equalTo("true").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                breakfastReference.child(dataSnapshot.getKey()).child("isCurrentBreakfast").setValue("false");
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

    public static void createBreakfastEvent(int year, int month, int day, String descriptionString, Context context) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        Calendar calendar = TimeFunctions.getCurrentTime();
        calendar.set(year, month, day, BREAKFAST_START_HOUR, BREAKFAST_START_MINUTE, BREAKFAST_START_SECOND);
        String breakfastKey = mDatabase.child("Breakfasts").push().getKey();

        mDatabase.child("Breakfasts").child(breakfastKey).child("year").setValue(year);
        mDatabase.child("Breakfasts").child(breakfastKey).child("month").setValue(month);
        mDatabase.child("Breakfasts").child(breakfastKey).child("day").setValue(day);
        mDatabase.child("Breakfasts").child(breakfastKey).child("description").setValue(descriptionString);
        mDatabase.child("Breakfasts").child(breakfastKey).child("isCurrentBreakfast").setValue("true");

        Breakfast currentBreakfast = new Breakfast(year, month, day , descriptionString, breakfastKey);
        ((MyApplication)context.getApplicationContext()).setCurrentBreakfast(currentBreakfast);
    }

























    //GETTERS AND SETTERS --------------------------------------------------------------
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<Post> getCampusFeed() {
        return campusFeed;
    }

    public void setCampusFeed(ArrayList<Post> campusFeed) {
        this.campusFeed = campusFeed;
    }

    public Post getTopPhoto() {
        return topPhoto;
    }

    public void setTopPhoto(Post topPhoto) {
        this.topPhoto = topPhoto;
    }

    public boolean isCurrentBreakfast() {
        return currentBreakfast;
    }

    public void setCurrentBreakfast(boolean currentBreakfast) {
        this.currentBreakfast = currentBreakfast;
    }

    public String getBreakfastKey() {
        return breakfastKey;
    }

    public void setBreakfastKey(String breakfastKey) {
        this.breakfastKey = breakfastKey;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

}
