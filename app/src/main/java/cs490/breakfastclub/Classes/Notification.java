package cs490.breakfastclub.Classes;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import cs490.breakfastclub.UserFiles.User;

/**
 * Created by Trevor on 12/6/2016.
 */

/*
 * TODO:
 * Need arraylist<notification> in user class
 * Need to be able to add notifications to firebase
 *      DO IN CONSTRUCTOR
 * Update firebase anytime a setXXX() function is called
 * Create Notification activity
 * Create button in ProfileView to navigate to Notification activity
 *
 */

public class Notification {
    private DateFormat format;
    private Date timestamp;
    private String timestampStr;
    private String content;
    private String title;
    private String userID;

    private DatabaseReference mDatabase;// = FirebaseDatabase.getInstance().getReference();

    private DatabaseReference notificationsRef;// = mDatabase.child("Users").child(userID).child("Notifications");

    public Notification()
    {
        this(new Date(), "DEFAULT_TITLE", "DEFAULT_CONTENT", "DEFAULT_USER_ID");
    }

    public Notification(String t, String c, String u)
    {
        this(new Date(), t, c, u);
    }

    public Notification(Date d, String t, String c, String u)
    {
        format = new SimpleDateFormat("MM/dd/yy HH:mm");
        timestamp = d;
        timestampStr = dateToString();
        title = t;
        content = c;
        userID = u;
    }

    public void removeNotification()
    {
        mDatabase = FirebaseDatabase.getInstance().getReference();

        notificationsRef = mDatabase.child("Users").child(userID).child("Notifications");
        notificationsRef.child(timestampStr).removeValue();
    }

    public void addToFirebase()
    {
        mDatabase = FirebaseDatabase.getInstance().getReference();

        notificationsRef = mDatabase.child("Users").child(userID).child("Notifications");

        HashMap<String, String> n = new HashMap<>();
        n.put("title", title);
        n.put("content", content);
        n.put("userID", userID);
        n.put("timestamp", timestampStr);
        String id = timestampStr;
        id = id.replace(" ", "-");
        id = id.replace("/", "_");
        notificationsRef.child(id).setValue(n);

    }

    public void setTimestamp(String s)
    {
        timestampStr = s;
    }

    public String getTimestamp()
    {
        return timestampStr;
    }

    private String dateToString()
    {
        return format.format(timestamp);
    }

    public void setTitle(String t)
    {
        title = t;
    }

    public String getTitle()
    {
        return title;
    }

    public void setContent(String c)
    {
        content = c;
    }

    public String getContent()
    {
        return content;
    }

    public String getUserID() {
        return userID;
    }
}
