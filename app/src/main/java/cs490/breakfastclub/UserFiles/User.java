package cs490.breakfastclub.UserFiles;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import cs490.breakfastclub.Classes.Notification;

import cs490.breakfastclub.CameraAndPhotos.Photos;
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
    private String profileImageID;
    private boolean receivesPushNotifications;
    private ArrayList<User> friends;
    private Permissions permissions;
    private DatabaseReference mDatabase;
    
    LinkedHashMap<String, Boolean> hasVotedUp;
    LinkedHashMap<String, Boolean> hasVotedDown;
    private ArrayList<Notification> notifications;

    private int currentPositionInFeed;
    private int numberOfOffensives;

    private Squad squad;
    private String squadRole;
    private boolean partOfSquad;

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    private double lat, lng;
    private LatLng location;
    private Photos currentPhotos;

    private boolean gotNotificationsFromFirebase = false;

    public User(String name, String userId, String profileImageUrl, String profileImageID, ArrayList<User> friends){
        this.name = name;
        this.userId = userId;
        this.profileImageUrl = profileImageUrl;
        this.profileImageID = profileImageID;
        this.receivesPushNotifications = true;
        this.friends = friends;
        this.permissions = Permissions.Member;
        hasVotedUp = new LinkedHashMap<String, Boolean>();
        hasVotedDown = new LinkedHashMap<String, Boolean>();
        this.currentPositionInFeed = 0;
        this.numberOfOffensives = 0;
        notifications = new ArrayList<>();
    }

    public User(String name, String userId, String profileImageUrl, String profileImageID)
    {
        this(name, userId, profileImageUrl, profileImageID, new ArrayList<User>());
    }

    public User()
    {
        this("DEFAULT", "DEFAULT", "DEFAULT", "DEFAULT", new ArrayList<User>());
    }

    //TODO
    //add squad to DB
    public void createSquad(String squadID){
        this.squad = new Squad(null, squadID, null, null, null);
    }

    public void setSquad(Squad squad)
    {
        this.squad = squad;
        getCurrentPhotos().setUserSquadPhotos(this.squad.getSquadID());
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


    public LinkedHashMap<String, Boolean> getHasVotedUp() {
        return hasVotedUp;
    }

    public void setHasVotedUp(LinkedHashMap<String, Boolean> hasVotedUp) {
        this.hasVotedUp = hasVotedUp;
    }

    public LinkedHashMap<String, Boolean> getHasVotedDown() {
        return hasVotedDown;
    }

    public void setHasVotedDown(LinkedHashMap<String, Boolean> hasVotedDown) {
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

    private void getNotificationsFromFirebase()
    {
        final DatabaseReference memberRef = FirebaseDatabase.getInstance().getReference("Users/" + userId + "/Notifications/");
        memberRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot d : dataSnapshot.getChildren())
                {
                    Notification n = new Notification((String)d.child("title").getValue(),
                                                      (String)d.child("content").getValue(),
                                                      userId);
                    n.setTimestamp((String)d.child("timestamp").getValue());
                    notifications.add(n);
                    Log.d("Notifications", "FROM FIREBASE: Timestamp=" + n.getTimestamp());
                    Log.d("Notifications", "FROM FIREBASE: User=" + name);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public ArrayList<Notification> getNotifications()
    {
        if(gotNotificationsFromFirebase == false)
        {
            gotNotificationsFromFirebase = true;
            Log.d("Notifications", "SIZE 0, Getting notifications from Firebase");
            getNotificationsFromFirebase();
        }
        return notifications;
    }

    public void addNotification(Notification n)
    {
        if(gotNotificationsFromFirebase == false)
        {
            gotNotificationsFromFirebase = true;
            getNotificationsFromFirebase();
        }
        notifications.add(n);
        n.addToFirebase();
    }

    public boolean removeNotification(Notification toRemove)
    {
        for(Notification n : notifications)
        {
            if(n.getContent().equals(toRemove.getContent()) &&
                    n.getTimestamp().equals(toRemove.getContent()))
            {
                n.removeNotification();
                notifications.remove(n);
                return true;
            }
        }
        return false;
    }

    public boolean removeNotification(int pos)
    {
        if(pos >= 0 && pos < notifications.size())
        {
            notifications.get(pos).removeNotification();
            notifications.remove(pos);
            return true;
        }
        return false;
    }



    public void increaseVotingArrays(int photoCount, ArrayList<String> photoids) {
        if (photoCount > this.getHasVotedDown().size()){

            mDatabase = FirebaseDatabase.getInstance().getReference();

            for (int i = this.getHasVotedDown().size(); i < photoCount; i++){
                this.getHasVotedDown().put(photoids.get(i), false);
                this.getHasVotedUp().put(photoids.get(i), false);
                mDatabase.child("Users").child(this.getUserId()).child("hasVotedUp").child(photoids.get(i)).setValue("false");
                mDatabase.child("Users").child(this.getUserId()).child("hasVotedDown").child(photoids.get(i)).setValue("false");
            }
        }
    }


    public Photos getCurrentPhotos() {
        return currentPhotos;
    }

    public void setCurrentPhotos(Photos currentPhotos) {
        this.currentPhotos = currentPhotos;
    }


    public String getProfileImageID() {
        return profileImageID;
    }

    public void setProfileImageID(String profileImageID) {
        this.profileImageID = profileImageID;
    }



}
