package cs490.breakfastclub.Classes;

import java.util.ArrayList;

/**
 * Created by Sean on 10/17/16.
 */

public class User {

    private String name;
    private String userId;
    private String profileImageUrl;
    private boolean receivesPushNotifications;
    private ArrayList<User> friends;


    public User(String name, String userId, String profileImageUrl, ArrayList<User> friends){
        this.name = name;
        this.userId = userId;
        this.profileImageUrl = profileImageUrl;
        this.receivesPushNotifications = true;
        this.friends = friends;
    }

    public User(String name, String userId, String profileImageUrl)
    {
        this(name, userId, profileImageUrl, new ArrayList<User>());
    }

    public User()
    {
        this("DEFAULT", "DEFAULT", "DEFAULT", new ArrayList<User>());
    }

    //TODO
    //add squad to DB
    private void createSquad(String squadName){
        Squad squad = new Squad(squadName, User.this);
    }

























    //GETTERS AND SETTERS --------------------------------------------------------------

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public boolean isReceivesPushNotifications() {
        return receivesPushNotifications;
    }

    public void setReceivesPushNotifications(boolean receivesPushNotifications) {
        this.receivesPushNotifications = receivesPushNotifications;
    }

    public String toString()
    {
        return "Name: " + name + " UserId: " + userId + " ProfileImageUrl: " + profileImageUrl
                + " ReceivesPushNotifications: " + receivesPushNotifications
                + " Friends: " + friends;
    }

    public void setFriends(ArrayList<User> friends) {
        this.friends = friends;
    }

    public ArrayList<User> getFriends()
    {
        return friends;
    }
}
