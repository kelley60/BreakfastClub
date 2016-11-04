package cs490.breakfastclub.Classes;

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
    private DatabaseReference ref;
    private ChildEventListener userChildEventListener;
    private ChildEventListener squadChildEventListener;
    private ChildEventListener breakfastChildEventListener;

    public Photos(User currentUser){
        userPhotos = new LinkedHashMap<String, URL>();
        squadPhotos = new LinkedHashMap<String, URL>();
        breakfastPhotos = new LinkedHashMap<String, URL>();

        ref = FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUserId()).child("Breakfast1/Photos");
        initPhotosRef(userChildEventListener, USER_PHOTOS);
        initPhotosRef(userChildEventListener, SQUAD_PHOTOS);
        initPhotosRef(userChildEventListener, BREAKFAST_PHOTOS);
    }

    private void initPhotosRef( ChildEventListener childEventListener, final int code) {

        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
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

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
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

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());

                String key = dataSnapshot.getKey();
                getPhotoSet(code).remove(key);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());

                // A comment has changed position, use the key to determine if we are
                // displaying this comment and if so move it.
                Comment movedComment = dataSnapshot.getValue(Comment.class);
                String commentKey = dataSnapshot.getKey();

                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "postComments:onCancelled", databaseError.toException());

            }
        };
        ref.addChildEventListener(childEventListener);

    }

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

/*
    //todo:get image from download url and use the iamges to fill the imageView
    public BitMap getBitmapFromUrl( URL url)
    {

        StorageReference islandRef = storageRef.child(url);

        File localFile = File.createTempFile("images", "jpg");

        islandRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                // Local temp file has been created
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });


        byte[] decodedString = Base64.decode(value, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }*/
}
