package cs490.breakfastclub.SquadFiles;



import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.Date;

import cs490.breakfastclub.Classes.Post;
import cs490.breakfastclub.UserFiles.User;
import cs490.breakfastclub.DisplayMessageActivity;
import cs490.breakfastclub.MyApplication;
import cs490.breakfastclub.R;

public class SquadMessageAdapter extends BaseAdapter implements ListAdapter {
    ArrayList<Post> messages;
    Context context;

    public SquadMessageAdapter(Context context, ArrayList<Post> messages) {
        this.messages = messages;
        this.context = context;
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int pos) {
        return messages.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        // Define a way to determine which layout to use, (chere it's just evens and odds.
        User currentUser = ((MyApplication) ((Activity)context).getApplication()).getCurrentUser();
        if (messages.get(position).getSenderID().equals(currentUser.getUserId()))
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
            if (getItemViewType(position) == 0)
            {
                view = inflater.inflate(R.layout.squad_message_list_item_right, null);
            }
            else
            {
                view = inflater.inflate(R.layout.squad_message_list_item_left, null);
            }

            // Try inflating
        }

        //Handle TextView and display string from your list


        Log.v("MessageAdapter", Integer.toString(getCount()));
        Log.v("MessageAdapter", Integer.toString(position));
        Log.v("MessageAdapter", messages.get(position).getMessage());
        Log.v("MessagePostsAdapter", messages.toString());




        TextView contentTextView = (TextView) view.findViewById(R.id.msg_contents);
        TextView senderTextView = (TextView) view.findViewById(R.id.msg_sender);
        TextView dateTextView = (TextView) view.findViewById(R.id.msg_timestamp);
        ImageView imageView = (ImageView) view.findViewById(R.id.msg_prof_pic);

        imageView.setVisibility(View.GONE);
        contentTextView.setText(messages.get(position).getMessage());
        senderTextView.setText(messages.get(position).getSenderName());
        Date date = messages.get(position).getDate();
        String month = convertMonth(date.getMonth());
        int day = date.getDate();
        int year = date.getYear() + 1900;
        String time = convertTime(date.getHours(), date.getMinutes());
        String dateText = month + " " + day + ", " + year + " " + time;
        dateTextView.setText(dateText);



        return view;

    }

    public String convertMonth(int month)
    {
        String monthString;
        switch (month + 1) {
            case 1:  monthString = "January";
                break;
            case 2:  monthString = "February";
                break;
            case 3:  monthString = "March";
                break;
            case 4:  monthString = "April";
                break;
            case 5:  monthString = "May";
                break;
            case 6:  monthString = "June";
                break;
            case 7:  monthString = "July";
                break;
            case 8:  monthString = "August";
                break;
            case 9:  monthString = "September";
                break;
            case 10: monthString = "October";
                break;
            case 11: monthString = "November";
                break;
            case 12: monthString = "December";
                break;
            default: monthString = "Invalid month";
                break;
        }
        return monthString;
    }

    public String convertTime(int hour, int minutes)
    {
        String time;
        if (hour > 12)
        {
            time = String.format("%02d", hour - 12) + ":" + String.format("%02d", minutes) + "PM";
        }
        else
        {
            time = String.format("%02d", hour) + ":" + String.format("%02d", minutes) + "AM";
        }

        return time;
    }


}
