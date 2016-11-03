package cs490.breakfastclub.Classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import cs490.breakfastclub.R;

/**
 * Created by Trevor on 11/2/2016.
 */

public class UserAdapter extends ArrayAdapter<User>
{

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

        // TODO: Figure out how to populate this with the users profile pic
        ImageView imgView = (ImageView) convertView.findViewById(R.id.imgProfilePic);

        // Populate the data into the template view using the data object
        lblName.setText(user.getName());


        Button addButton = (Button) convertView.findViewById(R.id.btnAddPermissions);
        Button removeButton = (Button) convertView.findViewById(R.id.btnRemovePermissions);

        // Cache user object inside the button using `setTag`
        addButton.setTag(user);
        removeButton.setTag(user);

        // Attach the click event handler
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Access user from within the tag
                User user = (User) view.getTag();
                // TODO: Update the user in database
                user.setPermissions(User.Permissions.Moderator);
            }
        });

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Access user from within the tag
                User user = (User) view.getTag();
                // TODO: Update the user in database
                user.setPermissions(User.Permissions.Member);
            }
        });

        // Return the completed view to render on screen
        return convertView;
    }


}
