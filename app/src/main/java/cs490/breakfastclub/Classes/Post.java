package cs490.breakfastclub.Classes;

import java.util.Date;

/**
 * Created by Sean on 10/17/16.
 */


/*
    Contains data for posting to either a group feed or the campus feed
 */

public class Post {

    private String message;
    private String imgURL;
    private Date date;
    private int score;

    public Post(String message, String imgURL, Date date){
        this.message = message;
        this.imgURL = imgURL;
        this.date = date;
        this.score = 0;
    }





























    //GETTERS AND SETTERS --------------------------------------------------------------
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
