package cs490.breakfastclub.Classes;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import cs490.breakfastclub.MyApplication;
import cs490.breakfastclub.R;
import cs490.breakfastclub.UserFiles.User;

/**
 * Created by Trevor on 12/6/2016.
 */

public class NotificationAdapter extends BaseAdapter implements ListAdapter {

    Context context;
    ArrayList<Notification> notifications;

    public NotificationAdapter(Context context, ArrayList<Notification> notifications) {
        this.notifications = notifications;
        this.context = context;
        Log.d("Notifications", "SIZEOF(notifications) = " + notifications.size());
    }

    @Override
    public int getCount() {
        return notifications.size();
    }

    @Override
    public Object getItem(int pos) {
        return notifications.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        // Define a way to determine which layout to use, (chere it's just evens and odds.
        User currentUser = ((MyApplication) ((Activity)context).getApplication()).getCurrentUser();
        if (notifications.get(position).getUserID().equals(currentUser.getUserId()))
        {
            return 0;
        }
        else
        {
            return 1;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2; // Count of different layouts
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.notification_list_item, null);
        }

        // TODO: Populate notification_list_item here
        TextView lblTitle = (TextView) view.findViewById(R.id.lblTitle);
        TextView lblContent = (TextView) view.findViewById(R.id.lblContent);
        TextView lblTimestamp = (TextView) view.findViewById(R.id.lblTimestamp);

        String title = notifications.get(position).getTitle();
        String content = notifications.get(position).getContent();
        String timestamp = notifications.get(position).getTimestamp();
        String userID = notifications.get(position).getUserID();

        Log.d("Notifications", "title: " + title);
        Log.d("Notifications", "content: " + content);
        Log.d("Notifications", "timestamp: " + timestamp);

        lblTitle.setText(title);
        lblContent.setText(content);
        lblTimestamp.setText(timestamp);

        return view;

    }

}
