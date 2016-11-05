package cs490.breakfastclub.SquadFiles;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;


import java.util.ArrayList;

import cs490.breakfastclub.UserFiles.User;
import cs490.breakfastclub.DownloadImageAsyncTask;
import cs490.breakfastclub.R;

public class SquadMemberAdapter extends BaseAdapter implements ListAdapter {
    ArrayList<User> members;
    Context context;
    int layout;

    public SquadMemberAdapter(Context context, int layout, ArrayList<User> members) {
        this.members = members;
        this.context = context;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return members.size();
    }

    @Override
    public Object getItem(int pos) {
        return members.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        //return list.get(pos).;
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout, null);
        }

        //Handle TextView and display string from your list
        final TextView listItemText = (TextView) view.findViewById(R.id.lblMemberName);
        String listText = members.get(position).getName();
        listItemText.setText(listText);
        final ImageView listItemImage = (ImageView) view.findViewById(R.id.imgProfilePic);


        new DownloadImageAsyncTask(listItemImage)
                .execute(members.get(position).getProfileImageUrl());


        return view;
    }


}
