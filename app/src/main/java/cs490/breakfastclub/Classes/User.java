package cs490.breakfastclub.Classes;

import java.util.ArrayList;

/**
 * Created by Sean on 10/17/16.
 */

public class User {

    public enum Permissions {
        Member,
        Moderator,
        Developer
    }

    private String name;
    private String userId;
    private String profileImageUrl;
    private boolean receivesPushNotifications;
    private ArrayList<User> friends;
    private Permissions permissions;
    private Squad squad;
    private String squadRole;
    private boolean partOfSquad;

    public User(String name, String userId, String profileImageUrl, ArrayList<User> friends){
        this.name = name;
        this.userId = userId;
        this.profileImageUrl = profileImageUrl;
        this.receivesPushNotifications = true;
        this.friends = friends;
        this.permissions = Permissions.Member;
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
    public void createSquad(String squadID){
        this.squad = new Squad(null, squadID, null, null);
    }

    public void setSquad(Squad squad)
    {
        this.squad = squad;
    }

    public Squad getSquad()
    {
        return squad;
    }
    //GETTERS AND SETTERS --------------------------------------------------------------

    public String getSquadRole() {
        return squadRole;
    }

    public void setSquadRole(String squadRole) {
        this.squadRole = squadRole;
    }

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

    public boolean isPartOfSquad() {
        return partOfSquad;
    }

    public void setPartOfSquad(boolean partOfSquad) {
        this.partOfSquad = partOfSquad;
    }

    public Permissions getPermissions() { return permissions; }
    public void setPermissions(Permissions p) { permissions = p; }
}
