package cs490.breakfastclub.Classes;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Sean on 10/17/16.
 */

public class Breakfast {

    private Date date;
    private String eventInfo;
    private ArrayList<Post> campusFeed;
    private Post topPhoto;

    public Breakfast(Date date, String eventInfo){
        this.date = date;
        this.eventInfo = eventInfo;
        campusFeed = new ArrayList<Post>();
        topPhoto = null;
    }






























    //GETTERS AND SETTERS --------------------------------------------------------------
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getEventInfo() {
        return eventInfo;
    }

    public void setEventInfo(String eventInfo) {
        this.eventInfo = eventInfo;
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
}
