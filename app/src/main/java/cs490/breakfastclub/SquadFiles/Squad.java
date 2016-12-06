package cs490.breakfastclub.SquadFiles;

import java.util.ArrayList;

import cs490.breakfastclub.UserFiles.User;

/**
 * Created by Sean on 10/17/16.
 */

public class Squad {

    private String squadName;
    private String squadID;
    private String squadDesc;
    private String squadImageUrl;
    private String squadImageID;
    private ArrayList<User> userList;
    private SquadFeed squadFeed;

    public Squad()
    {
        this.userList = new ArrayList<User>();
        this.squadFeed = new SquadFeed();
    }


    public Squad(String squadName, String squadID, String squadImageURL, String squadImageID, String squadDesc){
        this.squadName = squadName;
        this.squadID = squadID;
        this.squadImageUrl = squadImageURL;
        this.squadImageID = squadImageID;
        this.squadDesc = squadDesc;
        userList = new ArrayList<User>();
        squadFeed = new SquadFeed();
    }






























    //GETTERS AND SETTERS --------------------------------------------------------------

    public ArrayList<User> getUserList() {
        return userList;
    }

    public void setUserList(ArrayList<User> userList) {
        this.userList = userList;
    }

    public SquadFeed getSquadFeed() {
        return squadFeed;
    }

    public void setSquadFeed(SquadFeed squadFeed) {
        this.squadFeed = squadFeed;
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

    public String getSquadImageUrl() {
        return squadImageUrl;
    }

    public void setSquadImageUrl(String squadImageUrl) {
        this.squadImageUrl = squadImageUrl;
    }

    public String getSquadImageID() {
        return squadImageID;
    }

    public void setSquadImageID(String squadImageID) {
        this.squadImageID = squadImageID;
    }
}
