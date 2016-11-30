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

    private Photo profilePic;
    private LinkedHashMap<String, URL> userPhotos;
    private LinkedHashMap<String, URL> squadPhotos;
    private LinkedHashMap<String, URL> breakfastPhotos;
    private DatabaseReference ref1, ref2, ref3;
    private ChildEventListener userChildEventListener;
    private ChildEventListener squadChildEventListener;
    private ChildEventListener breakfastChildEventListener;

    public Photos(User currentUser){
        userPhotos = new LinkedHashMap<String, URL>();
        squadPhotos = new LinkedHashMap<String, URL>();
        breakfastPhotos = new LinkedHashMap<String, URL>();

        ref1 = FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUserId()).child("Breakfast1/Photos");
        initPhotosRef(userChildEventListener, USER_PHOTOS);
        //ref2 = FirebaseDatabase.getInstance().getReference("Squad").child(currentUser.getSquad().getSquadID()).child("Breakfast1/Photos");

        if(currentUser.isPartOfSquad() == true) {
           setUserSquadPhotos(currentUser.getSquad().getSquadID());
        }

        ref3 = FirebaseDatabase.getInstance().getReference("Breakfast/Breakfast1/Photos");
        initPhotosRef(breakfastChildEventListener, BREAKFAST_PHOTOS);

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
    /*

        private void initSquadPhotosRef( ChildEventListener childEventListener, final int code) {

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
            ref2.addChildEventListener(squadChildEventListener);
        }


        private void initBreakfastPhotosRef( ChildEventListener childEventListener, final int code) {

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
            ref3.addChildEventListener(breakfastChildEventListener);
        }
    */
    private LinkedHashMap<String, URL> getPhotoSet( int code )
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
        }
        return null;
    }

    private void childAdded(DataSnapshot dataSnapshot, final int code)
    {
        Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());

        // A new comment has been added, add it to the displayed list

        String key = dataSnapshot.getKey();

        try {
            URL url = new URL(dataSnapshot.getValue().toString());
            getPhotoSet(code).put(key, url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private void childChanged(DataSnapshot dataSnapshot, final int code)
    {
        Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());

        // A new comment has been added, add it to the displayed list

        String key = dataSnapshot.getKey();

        try {
            URL url = new URL(dataSnapshot.getValue().toString());
            getPhotoSet(code).put(key, url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private void childRemoved(DataSnapshot dataSnapshot, final int code)
    {
        Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());

        String key = dataSnapshot.getKey();
        getPhotoSet(code).remove(key);
    }

    private void childMoved(DataSnapshot dataSnapshot, final int code)
    {
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
        ref2 = FirebaseDatabase.getInstance().getReference("Squads").child(squadId).child("/Breakfast1/Photos");
        initPhotosRef(squadChildEventListener, SQUAD_PHOTOS);
    }

}
