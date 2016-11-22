package cs490.breakfastclub;

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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map;

import cs490.breakfastclub.Classes.Post;
import cs490.breakfastclub.UserFiles.User;

/**
 * Created by michaelrollberg on 11/16/16.
 */
public class RequestModeratorViewAdapter extends BaseAdapter implements ListAdapter {
    ArrayList<User> requestModerators;
    ListView moderatorView;
    Context context;

    public RequestModeratorViewAdapter(Context context, ArrayList<User> requestModerators, ListView moderatorView) {
        this.requestModerators = requestModerators;
        this.moderatorView = moderatorView;
        this.context = context;
    }

    @Override
    public int getCount() {
        return requestModerators.size();
    }

    @Override
    public Object getItem(int pos) {
        return requestModerators.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        //return list.get(pos).;
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.request_moderator_item, null);
        }

        ImageView profilePic = (ImageView) view.findViewById(R.id.imgProfilePic);
        TextView name = (TextView) view.findViewById(R.id.lblMemberName);
        Button accept = (Button) view.findViewById(R.id.btnAccept);
        Button decline = (Button) view.findViewById(R.id.btnDecline);

        final User requestModeratorUser = requestModerators.get(position);

        name.setText(requestModeratorUser.getName());
        new DownloadImageAsyncTask(profilePic)
                .execute((String) requestModeratorUser.getProfileImageUrl());

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Set Moderator")
                        .setMessage("Are you sure you want to set " + requestModeratorUser.getName() + " as a Moderator?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                                mDatabase.child("Users/").child(requestModeratorUser.getUserId()).child("requestAdminModerator").removeValue();
                                requestModerators.remove(position);
                                notifyDataSetChanged();
                                AdminViewUsersActivity.setListViewHeightBasedOnChildren((ListView) parent);

                                mDatabase.child("Users/").child(requestModeratorUser.getUserId()).child("permissions").setValue("Moderator");
                                ((ModeratorViewAdapter) moderatorView.getAdapter()).addModerator(requestModeratorUser);
                                AdminViewUsersActivity.setListViewHeightBasedOnChildren(moderatorView);

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

        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                mDatabase.child("Users/").child(requestModeratorUser.getUserId()).child("requestAdminModerator").removeValue();
                requestModerators.remove(position);
                notifyDataSetChanged();
                AdminViewUsersActivity.setListViewHeightBasedOnChildren((ListView) parent);
            }
        });

        return view;
    }
}

