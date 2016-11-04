package cs490.breakfastclub.Classes;

import android.graphics.Bitmap;
import android.widget.ImageView;

import java.security.acl.Group;
import java.util.ArrayList;

/**
 * Created by Sean on 10/17/16.
 */

public class Squad {

    private String squadName;
    private String squadID;
    private String squadDesc;
    private Bitmap squadPhoto;
    private ArrayList<User> userList;
    private GroupFeed groupFeed;

    public Squad()
    {
        this.userList = new ArrayList<User>();
        this.groupFeed = new GroupFeed();
    }


    public Squad(String squadName, String squadID, Bitmap squadPhoto, String squadDesc){
        this.squadName = squadName;
        this.squadID = squadID;
        this.squadPhoto = squadPhoto;
        this.squadDesc = squadDesc;
        userList = new ArrayList<User>();
        groupFeed = new GroupFeed();
    }






























    //GETTERS AND SETTERS --------------------------------------------------------------

    public ArrayList<User> getUserList() {
        return userList;
    }

    public void setUserList(ArrayList<User> userList) {
        this.userList = userList;
    }

    public GroupFeed getGroupFeed() {
        return groupFeed;
    }

    public void setGroupFeed(GroupFeed groupFeed) {
        this.groupFeed = groupFeed;
    }

    public String getSquadName() {
        return squadName;
    }

    public void setSquadName(String squadName) {
        this.squadName = squadName;
    }

    public String getSquadID() {
        return squadID;
    }

    public void setSquadID(String squadID) {
        this.squadID = squadID;
    }

    public String getSquadDesc() {
        return squadDesc;
    }

    public void setSquadDesc(String squadDesc) {
        this.squadDesc = squadDesc;
    }

    public Bitmap getSquadPhoto() {
        return squadPhoto;
    }

    public void setSquadPhoto(Bitmap squadPhoto) {
        this.squadPhoto = squadPhoto;
    }
}
