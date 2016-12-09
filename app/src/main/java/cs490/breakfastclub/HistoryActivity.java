package cs490.breakfastclub;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.util.Log;
import android.widget.GridView;

import org.w3c.dom.Comment;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import cs490.breakfastclub.CameraAndPhotos.ImageAdapter;
import cs490.breakfastclub.UserFiles.User;

/**
 * Created by Kunal on 12/8/2016.
 */

public class HistoryActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private LinkedHashMap<String, URL> breakfastPhotos;
    private DatabaseReference ref3;
    private ChildEventListener breakfastChildEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Breakfast History");

        Log.d("OnCreate", "In History!");

        // Instead of get current photos, use your own hash map
        breakfastPhotos = new LinkedHashMap<String, URL>();

        ref3 = FirebaseDatabase.getInstance().getReference("Breakfasts").child("-KYVVXdcSoVv5ILF7qxI").child("Photos");
        initPhotosRef(breakfastChildEventListener);

        Intent intent = getIntent();

        final Context c = this;

        final User currentUser = ((MyApplication) getApplication()).getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        DatabaseReference breakfastRef = FirebaseDatabase.getInstance().getReference("Breakfasts/" + "-KYVVXdcSoVv5ILF7qxI");
        breakfastRef.orderByPriority().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                LinkedHashMap<String, URL> linkedHashMap =  (LinkedHashMap<String, URL>) dataSnapshot.child("Photos").getValue();
                final ArrayList<URL> currentPhotos = new ArrayList<URL>(linkedHashMap.values());
                final ArrayList<String> photoids = new ArrayList<String>(linkedHashMap.keySet());
                final GridView squadGallery = (GridView) findViewById(R.id.gallery);

                squadGallery.setAdapter(new ImageAdapter(c, currentUser, currentPhotos, photoids));
            }
            @Override public void onCancelled(DatabaseError databaseError) {}
        });  // end of listener on squad
    }

    private void initPhotosRef(ChildEventListener childEventListener) {

        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                childAdded(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                childChanged(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                childRemoved(dataSnapshot);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                childMoved(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("History", "postComments:onCancelled", databaseError.toException());

            }
        };
        ref3.addChildEventListener(childEventListener);
    }

    private void childAdded(DataSnapshot dataSnapshot) {
        Log.d("History", "onChildAdded:" + dataSnapshot.getKey());

        // A new comment has been added, add it to the displayed list
        String key = dataSnapshot.getKey();

        try {
            URL url = new URL(dataSnapshot.getValue().toString());
            breakfastPhotos.put(key, url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private void childChanged(DataSnapshot dataSnapshot) {
        Log.d("History", "onChildChanged:" + dataSnapshot.getKey());

        // A new comment has been added, add it to the displayed list
        String key = dataSnapshot.getKey();
        try {
            URL url = new URL(dataSnapshot.getValue().toString());
            breakfastPhotos.put(key, url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private void childRemoved(DataSnapshot dataSnapshot)
    {
        Log.d("History", "onChildRemoved:" + dataSnapshot.getKey());

        String key = dataSnapshot.getKey();
        breakfastPhotos.remove(key);
    }

    private void childMoved(DataSnapshot dataSnapshot) {
        Log.d("History", "onChildMoved:" + dataSnapshot.getKey());

        // A comment has changed position, use the key to determine if we are
        // displaying this comment and if so move it.
        Comment movedComment = dataSnapshot.getValue(Comment.class);
        String commentKey = dataSnapshot.getKey();

    }

    public URL getBreakfastPhotoURL(String name) {
        return breakfastPhotos.get(name);
    }
    public URL getBreakfastPhoto(String name) {
        return breakfastPhotos.get(name);
    }
    public LinkedHashMap<String, URL> getBreakfastPhotos() {
        return breakfastPhotos;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
