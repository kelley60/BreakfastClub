package cs490.breakfastclub.UserFiles;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import cs490.breakfastclub.SquadFiles.Squad;

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
    private DatabaseReference mDatabase;

    private ArrayList<Boolean> hasVotedUp;
    private ArrayList<Boolean> hasVotedDown;
    private int currentPositionInFeed;
    private int numberOfOffensives;

    private Squad squad;
    private String squadRole;
    private boolean partOfSquad;
    private double lat, lng;
    private LatLng location;

    public User(String name, String userId, String profileImageUrl, ArrayList<User> friends){
        this.name = name;
        this.userId = userId;
        this.profileImageUrl = profileImageUrl;
        this.receivesPushNotifications = true;
        this.friends = friends;
        this.permissions = Permissions.Member;
        this.hasVotedUp = new ArrayList<Boolean>();
        this.hasVotedDown = new ArrayList<Boolean>();
        this.currentPositionInFeed = 0;
        numberOfOffensives = 0;
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


    public ArrayList<Boolean> getHasVotedUp() {
        return hasVotedUp;
    }

    public void setHasVotedUp(ArrayList<Boolean> hasVoted) {
        this.hasVotedUp = hasVoted;
    }

    public ArrayList<Boolean> getHasVotedDown() {
        return hasVotedDown;
    }

    public void setHasVotedDown(ArrayList<Boolean> hasVotedDown) {
        this.hasVotedDown = hasVotedDown;
    }

    public int getCurrentPositionInFeed() {
        return currentPositionInFeed;
    }

    public void setCurrentPositionInFeed(int currentPositionInFeed) {
        this.currentPositionInFeed = currentPositionInFeed;
    }

    public void updateLocation(double lt, double lg) { lat = lt; lng = lg; }
    public void updateLocation(LatLng loc) { location = loc; }
    public double getLat() { return lat; }
    public double getLng() { return lng; }
    public LatLng getLocation() {
        return location;
    }

    public int getNumberOfOffensives() {
        return numberOfOffensives;
    }

    public void setNumberOfOffensives(int numberOfOffensives) {
        this.numberOfOffensives = numberOfOffensives;
    }


    public void increaseVotingArrays(int photoCount) {
        if (photoCount > this.getHasVotedDown().size()){

            mDatabase = FirebaseDatabase.getInstance().getReference();

            for (int i = this.getHasVotedDown().size(); i <= photoCount; i++){
                this.getHasVotedDown().add(false);
                this.getHasVotedUp().add(false);
                mDatabase.child("Users").child(this.getUserId()).child("hasVotedUp").child(i+"").setValue("false");
                mDatabase.child("Users").child(this.getUserId()).child("hasVotedDown").child(i+"").setValue("false");
            }
        }
    }

}
