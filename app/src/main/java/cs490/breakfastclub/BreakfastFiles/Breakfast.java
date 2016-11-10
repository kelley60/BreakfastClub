package cs490.breakfastclub.BreakfastFiles;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

import cs490.breakfastclub.Classes.Post;

/**
 * Created by Sean on 10/17/16.
 */

public class Breakfast {

    private String description;
    private ArrayList<Post> campusFeed;
    private Post topPhoto;
    private boolean currentBreakfast;
    private String breakfastId;
    private int year;
    private int month;
    private int day;

    public Breakfast(int year, int month, int day, String descriptionString) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.description = descriptionString;
    }


    public static Breakfast getCurrentBreakfast(DatabaseReference mDatabase){
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
                    String name = (String) messageSnapshot.child("name").getValue();
                    String message = (String) messageSnapshot.child("message").getValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return null;
    }

    public static void endCurrentBreakfast(){
        final DatabaseReference breakfastReference = FirebaseDatabase.getInstance().getReference("Breakfasts/");
        breakfastReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String isCurrentBreakfast = (String) dataSnapshot.child("name").getValue();
                if (isCurrentBreakfast.equals("true")){
                    //
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void writeNewBreakfast(DatabaseReference mDatabase) {

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

    public String getBreakfastId() {
        return breakfastId;
    }

    public void setBreakfastId(String breakfastId) {
        this.breakfastId = breakfastId;
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
