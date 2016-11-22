package cs490.breakfastclub;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;

import cs490.breakfastclub.Classes.Post;
import cs490.breakfastclub.UserFiles.User;

/**
 * Created by michaelrollberg on 11/16/16.
 */
public class AdminViewAdapter extends BaseAdapter implements ListAdapter {
    ArrayList<User> admins;
    Context context;

    public AdminViewAdapter(Context context, ArrayList<User> admins) {
        this.admins = admins;
        this.context = context;
    }

    @Override
    public int getCount() {
        return admins.size();
    }

    @Override
    public Object getItem(int pos) {
        return admins.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        //return list.get(pos).;
        return 0;
    }

    public void addAdmin(User user)
    {
        admins.add(user);
        notifyDataSetChanged();
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.admin_list_item, null);
        }

        ImageView profilePic = (ImageView) view.findViewById(R.id.imgProfilePic);
        TextView name = (TextView) view.findViewById(R.id.lblMemberName);

        User adminUser = admins.get(position);

        name.setText(adminUser.getName());
        new DownloadImageAsyncTask(profilePic)
                .execute((String) adminUser.getProfileImageUrl());

        return view;
    }
}
