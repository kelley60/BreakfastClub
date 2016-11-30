package cs490.breakfastclub.UserFiles;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.ArrayList;

import cs490.breakfastclub.AdminViewUsersActivity;
import cs490.breakfastclub.DownloadImageAsyncTask;
import cs490.breakfastclub.R;
import cs490.breakfastclub.RepeatOffendersActivity;

/**
 * Created by Trevor on 11/2/2016.
 */

public class UserAdapter extends ArrayAdapter<User>
{

    private DatabaseReference mDatabase;

    public UserAdapter(Context context, ArrayList<User> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        User user = getItem(position);



        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            // Check what the parent view is in order to determine which list is trying to
            // access this array adapter
            // Will either be admin_member_list_item OR squad_member_list_item
            if(getContext().getClass() == AdminViewUsersActivity.class)
            {
                Log.d("Class", "AdminViewUsersActivity");
            }
            else if(getContext().getClass() == RepeatOffendersActivity.class)
            {
                Log.d("Class", "RepeatOffendersActivity");
            }
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.admin_member_list_item, parent, false);
        }

        // Lookup view for data population
        TextView lblName = (TextView) convertView.findViewById(R.id.lblMemberName);
        final TextView lblTitle = (TextView) convertView.findViewById(R.id.lblTitle);
        TextView lblPermissions = (TextView) convertView.findViewById(R.id.lblPermissions);

        // Populate the data into the template view using the data object
        if(user != null)
        {
            lblName.setText(user.getName());
        }
        else
        {   // If no user found, return
            Log.d("NULL_ERROR", "User is null");
            lblName.setText("ERROR: No user found");
            return convertView;
        }

        // Populate this with the users profile pic
        ImageView imgView = (ImageView) convertView.findViewById(R.id.imgProfilePic);
        new DownloadImageAsyncTask(imgView)
                .execute((String) user.getProfileImageUrl());


        // Set title according to parent activity
        if(getContext().getClass() == AdminViewUsersActivity.class)
        {
            switch(user.getPermissions()) {
                case Moderator:
                    lblPermissions.setText("Moderator");
                    break;
                case Member:
                    lblPermissions.setText("Member");
                    break;
                default:
                    lblPermissions.setText("Member");
            }
        }
        else if(getContext().getClass() == RepeatOffendersActivity.class)
        {
            lblTitle.setText("Posting Privelages");
        }

        Button addButton = (Button) convertView.findViewById(R.id.btnAdd);
        Button removeButton = (Button) convertView.findViewById(R.id.btnRemove);

        // Cache user object inside the button using `setTag`
        if(user != null) {
            addButton.setTag(user);
            removeButton.setTag(user);
        }
        else if(getContext().getClass() == RepeatOffendersActivity.class)
        {
            addButton.setText("Block Posting");
            removeButton.setText("Allow Posting");
            addButton.setTextSize(14.0f);
            removeButton.setTextSize(14.0f);
        }

        // Cache user object inside the button using `setTag`
        addButton.setTag(user);
        removeButton.setTag(user);

        // Get reference to the database
        mDatabase = FirebaseDatabase.getInstance().getReference();

        /*
            Need a way for Admin/Moderator to block users from posting
                Set user.numberOfOffensives to 3
            Need a way for Admin/Moderator to reinstate posting privelages for users
                Set user.numberOfOffensives to 0
         */

        // Attach the click event handler
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            // Access user from within the tag
            User user = (User) view.getTag();

            if(getContext().getClass() == AdminViewUsersActivity.class)
            {
                user.setPermissions(User.Permissions.Moderator);
                mDatabase.child("Users").child(user.getUserId()).child("permissions").setValue(user.getPermissions());
                Log.d("Permissions", user.getName() + " - Add");
                lblTitle.setText("Moderator");
            }
            else if(getContext().getClass() == RepeatOffendersActivity.class)
            {
                user.setNumberOfOffensives(4);
                Log.d("Class", user.getName() + " numberOfOffensives = " + user.getNumberOfOffensives());
                mDatabase.child("Users").child(user.getUserId()).child("numberOfOffensives").setValue(user.getNumberOfOffensives());
            }

            }
        });

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            // Access user from within the tag
            User user = (User) view.getTag();

            if(getContext().getClass() == AdminViewUsersActivity.class)
            {
                user.setPermissions(User.Permissions.Member);
                mDatabase.child("Users").child(user.getUserId()).child("permissions").setValue(user.getPermissions());
                Log.d("Permissions", user.getName() + " - Remove");
                lblTitle.setText("Member");
            }
            else if(getContext().getClass() == RepeatOffendersActivity.class)
            {
                user.setNumberOfOffensives(0);
                Log.d("Class", user.getName() + " numberOfOffensives = " + user.getNumberOfOffensives());
                mDatabase.child("Users").child(user.getUserId()).child("numberOfOffensives").setValue(user.getNumberOfOffensives());
            }

            }
        });

        // Return the completed view to render on screen
        return convertView;
    }


}
