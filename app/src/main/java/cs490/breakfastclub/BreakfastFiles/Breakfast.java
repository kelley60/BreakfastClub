package cs490.breakfastclub.BreakfastFiles;

import com.google.firebase.database.DatabaseReference;

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


    //TODO
    //gets current breakfast from database
    public static Breakfast getCurrentBreakfast(){
        Breakfast breakfast = null;
        //LOAD FIRST 10 OR LESS PICTURES FROM DB
        return breakfast;
    }

    public static void loadPictures(){
        //LOAD 10 PICTURES INTO CURRENTBREAKFAST
    }

    private void writeNewBreakfast(DatabaseReference mDatabase) {
        /*
        mDatabase.child("Breakfast").child(this.getBreakfastId()).child("Description").setValue(description);
        mDatabase.child("Breakfast").child(this.getBreakfastId()).child("").setValue(user.getProfileImageUrl());
        mDatabase.child("Breakfast").child(this.getBreakfastId()).child("receivesPushNotifications").setValue(user.isReceivesPushNotifications());
        */
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
