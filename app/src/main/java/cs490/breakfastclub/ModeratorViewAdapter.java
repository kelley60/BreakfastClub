package cs490.breakfastclub;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;

import cs490.breakfastclub.Classes.Post;
import cs490.breakfastclub.UserFiles.User;

/**
 * Created by michaelrollberg on 11/16/16.
 */
public class ModeratorViewAdapter extends BaseAdapter implements ListAdapter {
    ArrayList<User> moderators;
    ListView adminView;
    Context context;

    public ModeratorViewAdapter(Context context, ArrayList<User> moderators, ListView adminView) {
        this.moderators = moderators;
        this.adminView = adminView;
        this.context = context;
    }

    @Override
    public int getCount() {
        return moderators.size();
    }

    @Override
    public Object getItem(int pos) {
        return moderators.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        //return list.get(pos).;
        return 0;
    }

    public void addModerator(User user)
    {
        moderators.add(user);
        notifyDataSetChanged();
    }


    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.moderator_list_item, null);
        }

        ImageView profilePic = (ImageView) view.findViewById(R.id.imgProfilePic);
        TextView name = (TextView) view.findViewById(R.id.lblMemberName);
        Button promote = (Button) view.findViewById(R.id.btnPromote);
        Button demote = (Button) view.findViewById(R.id.btnDemote);

        User currentUser = ((MyApplication) ((Activity)context).getApplication()).getCurrentUser();
        if (currentUser.getPermissions() == User.Permissions.Moderator)
        {
            promote.setVisibility(View.INVISIBLE);
            demote.setVisibility(View.INVISIBLE);
        }

        final User moderatorUser = moderators.get(position);

        name.setText(moderatorUser.getName());
        new DownloadImageAsyncTask(profilePic)
                .execute((String) moderatorUser.getProfileImageUrl());

        promote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Promote Moderator")
                        .setMessage("Are you sure you want to promote " + moderatorUser.getName() + " to an Admin?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                                mDatabase.child("Users/").child(moderatorUser.getUserId()).child("permissions").setValue("Developer");
                                moderators.remove(position);
                                notifyDataSetChanged();
                                AdminViewUsersActivity.setListViewHeightBasedOnChildren((ListView) parent);

                                ((AdminViewAdapter) adminView.getAdapter()).addAdmin(moderatorUser);
                                AdminViewUsersActivity.setListViewHeightBasedOnChildren(adminView);

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();


            }
        });

        demote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Demote Moderator")
                        .setMessage("Are you sure you want to demote " + moderatorUser.getName() + " to a Member?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                                mDatabase.child("Users/").child(moderatorUser.getUserId()).child("permissions").setValue("Member");
                                moderators.remove(position);
                                notifyDataSetChanged();
                                AdminViewUsersActivity.setListViewHeightBasedOnChildren((ListView) parent);

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();



            }
        });

        return view;
    }
}
