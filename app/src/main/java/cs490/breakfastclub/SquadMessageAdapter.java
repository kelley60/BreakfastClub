package cs490.breakfastclub;



import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import cs490.breakfastclub.Classes.Post;
import cs490.breakfastclub.Classes.User;

public class SquadMessageAdapter extends BaseAdapter implements ListAdapter {
    ArrayList<Post> messages;
    Context context;
    int layout;

    public SquadMessageAdapter(Context context, int layout, ArrayList<Post> messages) {
        this.messages = messages;
        this.context = context;
        this.layout = layout;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout, null);
        }

        //Handle TextView and display string from your list

        User currentUser = ((MyApplication) ((DisplayMessageActivity)context).getApplication()).getCurrentUser();

        Log.v("MessageAdapter", Integer.toString(getCount()));
        Log.v("MessageAdapter", Integer.toString(position));
        Log.v("MessageAdapter", messages.get(position).getMessage());
        Log.v("MessagePostsAdapter", messages.toString());
        if (messages.get(position).getSenderID().equals(currentUser.getUserId())) {
            view.findViewById(R.id.leftSide).setVisibility(View.GONE);

            TextView contentTextView = (TextView) view.findViewById(R.id.msg_contentsRight);
            TextView senderTextView = (TextView) view.findViewById(R.id.msg_senderRight);
            TextView dateTextView = (TextView) view.findViewById(R.id.msg_timestampRight);
            ImageView imageView = (ImageView) view.findViewById(R.id.msg_prof_picRight);

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
        }
        else
        {
            view.findViewById(R.id.rightSide).setVisibility(View.GONE);

            TextView contentTextView = (TextView) view.findViewById(R.id.msg_contentsLeft);
            TextView senderTextView = (TextView) view.findViewById(R.id.msg_senderLeft);
            TextView dateTextView = (TextView) view.findViewById(R.id.msg_timestampLeft);
            ImageView imageView = (ImageView) view.findViewById(R.id.msg_prof_picLeft);

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
        }

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
