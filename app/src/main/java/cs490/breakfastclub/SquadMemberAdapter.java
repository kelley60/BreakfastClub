package cs490.breakfastclub;

/**
 * Created by Kenny on 10/17/2015.
 */

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
import android.widget.TextView;


import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import cs490.breakfastclub.Classes.User;

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


        new DownloadImageTask(listItemImage)
                .execute(members.get(position).getProfileImageUrl());


        return view;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}