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
    private String senderID;
    private String senderName;

    public Post()
    {
        message = null;
        imgURL = null;
        date = null;
        score = 0;
        senderID = null;
        senderName = null;
    }

    public Post(String message, String imgURL, Date date){
        this.message = message;
        this.imgURL = imgURL;
        this.date = date;
        this.score = 0;
        senderID = null;
        senderName = null;
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

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    @Override
    public String toString() {
        return "Post{" +
                "message='" + message + '\'' +
                ", imgURL='" + imgURL + '\'' +
                ", date=" + date +
                ", score=" + score +
                ", senderID='" + senderID + '\'' +
                ", senderName='" + senderName + '\'' +
                '}';
    }
}
