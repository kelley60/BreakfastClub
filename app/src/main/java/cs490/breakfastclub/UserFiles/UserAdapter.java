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

import java.util.ArrayList;

import cs490.breakfastclub.DownloadImageAsyncTask;
import cs490.breakfastclub.R;

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
            // TODO: Check what the parent view is in order to determine which list is trying to
            // access this array adapter
            // Will either be admin_member_list_item OR squad_member_list_item
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.admin_member_list_item, parent, false);
        }

        // Lookup view for data population
        TextView lblName = (TextView) convertView.findViewById(R.id.lblMemberName);
        final TextView lblPermissions = (TextView) convertView.findViewById(R.id.lblPermissions);

        // TODO: Figure out how to populate this with the users profile pic
        ImageView imgView = (ImageView) convertView.findViewById(R.id.imgProfilePic);
        new DownloadImageAsyncTask(imgView)
                .execute((String) user.getProfileImageUrl());

        // Populate the data into the template view using the data object
        if(user != null)
            lblName.setText(user.getName());
        else
            lblName.setText("ERROR: No user found");

        switch(user.getPermissions())
        {
            case Moderator:
                lblPermissions.setText("Moderator");
                break;
            case Member:
                lblPermissions.setText("Member");
                break;
            default:
                lblPermissions.setText("Member");
        }

        Button addButton = (Button) convertView.findViewById(R.id.btnAddPermissions);
        Button removeButton = (Button) convertView.findViewById(R.id.btnRemovePermissions);

        // Cache user object inside the button using `setTag`
        if(user != null) {
            addButton.setTag(user);
            removeButton.setTag(user);
        }

        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Attach the click event handler
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Access user from within the tag
                User user = (User) view.getTag();
                // TODO: Update the user in database
                user.setPermissions(User.Permissions.Moderator);
                mDatabase.child("Users").child(user.getUserId()).child("permissions").setValue(user.getPermissions());
                Log.d("Permissions", user.getName() + " - Add");
                lblPermissions.setText("Moderator");
            }
        });

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Access user from within the tag
                User user = (User) view.getTag();
                // TODO: Update the user in database
                user.setPermissions(User.Permissions.Member);
                mDatabase.child("Users").child(user.getUserId()).child("permissions").setValue(user.getPermissions());
                Log.d("Permissions", user.getName() + " - Remove");
                lblPermissions.setText("Member");
            }
        });

        // Return the completed view to render on screen
        return convertView;
    }


}
