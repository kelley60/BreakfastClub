package cs490.breakfastclub.CameraAndPhotos;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Comment;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedHashMap;

import cs490.breakfastclub.SquadFiles.Squad;
import cs490.breakfastclub.UserFiles.User;


/**
 * Created by Emma on 10/17/16.
 */

public class Photos {

    /**
     * Tag for the {@link Log}.
     */
    private static final String TAG = "Photos";

    private static final int USER_PHOTOS = 0;
    private static final int SQUAD_PHOTOS = 1;
    private static final int BREAKFAST_PHOTOS = 2;
    private static final int BREAKFAST_VOTES = 3;

    private Photo profilePic;
    private LinkedHashMap<String, URL> userPhotos;
    private LinkedHashMap<String, URL> squadPhotos;
    private LinkedHashMap<String, URL> breakfastPhotos;
    private LinkedHashMap<String, Integer> breakfastPhotosVotes;
    private DatabaseReference ref1, ref2, ref3, ref4;
    private ChildEventListener userChildEventListener;
    private ChildEventListener squadChildEventListener;
    private ChildEventListener breakfastChildEventListener;
    private ChildEventListener breakfastVotesChildEventListener;
    private String breakfastId;

    public Photos(User currentUser, String currentBreakfast) {
        userPhotos = new LinkedHashMap<String, URL>();
        squadPhotos = new LinkedHashMap<String, URL>();
        breakfastPhotos = new LinkedHashMap<String, URL>();
        breakfastPhotosVotes = new LinkedHashMap<String, Integer>();

        String userId = currentUser.getUserId();
        Squad squad = currentUser.getSquad();
        breakfastId = currentBreakfast;


        ref1 = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("Photos").child(breakfastId);
        initPhotosRef(userChildEventListener, USER_PHOTOS);

        if (squad != null)
        {
            if (currentUser.isPartOfSquad() == true) {
                setUserSquadPhotos(currentUser.getSquad().getSquadID());
            }
        }

        //Todo change to be Breakfasts
        ref3 = FirebaseDatabase.getInstance().getReference("Breakfasts").child(currentBreakfast).child("Photos");
        initPhotosRef(breakfastChildEventListener, BREAKFAST_PHOTOS);

        ref4 = FirebaseDatabase.getInstance().getReference("Breakfasts").child(currentBreakfast).child("Votes");
        initPhotosRef(breakfastVotesChildEventListener, BREAKFAST_VOTES);

    }

    private void initPhotosRef( ChildEventListener childEventListener, final int code) {

        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                childAdded(dataSnapshot, code);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                childChanged(dataSnapshot, code);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                childRemoved(dataSnapshot, code);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                childMoved(dataSnapshot, code);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "postComments:onCancelled", databaseError.toException());

            }
        };
        getRefSet(code).addChildEventListener(childEventListener);
    }

    public LinkedHashMap<String, URL> getPhotoSet( int code )
    {
        switch (code)
        {
            case USER_PHOTOS:
                return userPhotos;
            case SQUAD_PHOTOS:
                return squadPhotos;
            case BREAKFAST_PHOTOS:
                return breakfastPhotos;
        }
        return null;
    }

    private DatabaseReference getRefSet( int code )
    {
        switch (code)
        {
            case USER_PHOTOS:
                return ref1;
            case SQUAD_PHOTOS:
                return ref2;
            case BREAKFAST_PHOTOS:
                return ref3;
            case BREAKFAST_VOTES:
                return ref4;
        }
        return null;
    }

    private ChildEventListener getChildSet( int code )
    {
        switch (code)
        {
            case USER_PHOTOS:
                return userChildEventListener;
            case SQUAD_PHOTOS:
                return squadChildEventListener;
            case BREAKFAST_PHOTOS:
                return breakfastChildEventListener;
            case BREAKFAST_VOTES:
                return breakfastVotesChildEventListener;
        }
        return null;
    }

    private void childAdded(DataSnapshot dataSnapshot, final int code) {
        Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());

        // A new comment has been added, add it to the displayed list

        String key = dataSnapshot.getKey();

        if (code == BREAKFAST_VOTES) {
            breakfastPhotosVotes.put(key, Integer.parseInt(dataSnapshot.getValue().toString()));
        } else {
            try {

                URL url = new URL(dataSnapshot.getValue().toString());
                getPhotoSet(code).put(key, url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

        }
    }

    private void childChanged(DataSnapshot dataSnapshot, final int code) {
        Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());

        // A new comment has been added, add it to the displayed list

        String key = dataSnapshot.getKey();

        if (code == BREAKFAST_VOTES) {
            breakfastPhotosVotes.put(key, Integer.parseInt(dataSnapshot.getValue().toString()));
        } else {
            try {
                URL url = new URL(dataSnapshot.getValue().toString());
                getPhotoSet(code).put(key, url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }

    private void childRemoved(DataSnapshot dataSnapshot, final int code)
    {
        Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());

        String key = dataSnapshot.getKey();

        if (code == BREAKFAST_VOTES) {
            breakfastPhotosVotes.remove(key);
        } else {
            getPhotoSet(code).remove(key);
        }
    }

    private void childMoved(DataSnapshot dataSnapshot, final int code) {
        Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());

        // A comment has changed position, use the key to determine if we are
        // displaying this comment and if so move it.
        Comment movedComment = dataSnapshot.getValue(Comment.class);
        String commentKey = dataSnapshot.getKey();

    }



    //GETTERS AND SETTERS --------------------------------------------------------------

    public Photo getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(Photo profilePic) {
        this.profilePic = profilePic;
    }

    public LinkedHashMap<String, URL> getUserPhotoURLs() {
        return userPhotos;
    }
    public LinkedHashMap<String, URL> getSquadPhotoURLs() {
        return squadPhotos;
    }
    public LinkedHashMap<String, URL> getBreakfastPhotoURLs() {
        return breakfastPhotos;
    }


    public LinkedHashMap<String, URL> getUserPhotos() {
        return userPhotos;
    }
    public LinkedHashMap<String, URL> getSquadPhotos() {
        return squadPhotos;
    }
    public LinkedHashMap<String, URL> getBreakfastPhotos() {
        return breakfastPhotos;
    }
    public LinkedHashMap<String, Integer> getBreakfastVotes() {
        return breakfastPhotosVotes;
    }

    public void setBreakfastPhotosVotes(LinkedHashMap<String, Integer> breakfastPhotosVotes) {
        this.breakfastPhotosVotes = breakfastPhotosVotes;
    }

    public URL getUserPhotoURL(String name) {
        return userPhotos.get(name);
    }
    public URL getSquadPhotoURL(String name) {
        return squadPhotos.get(name);
    }
    public URL getBreakfastPhotoURL(String name) {
        return breakfastPhotos.get(name);
    }

    public URL getUserPhoto(String name) {
        return userPhotos.get(name);
    }
    public URL getSquadPhoto(String name) {
        return squadPhotos.get(name);
    }
    public URL getBreakfastPhoto(String name) {
        return breakfastPhotos.get(name);
    }

    public void setUserSquadPhotos(String squadId)
    {
        ref2 = FirebaseDatabase.getInstance().getReference("Squads").child(squadId).child("Photos").child(breakfastId);
        initPhotosRef(squadChildEventListener, SQUAD_PHOTOS);
    }

}
