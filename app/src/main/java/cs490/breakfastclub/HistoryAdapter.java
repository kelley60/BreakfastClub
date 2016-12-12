package cs490.breakfastclub;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
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

public class HistoryAdapter extends BaseAdapter implements ListAdapter {
    ArrayList<String> breakfastKeys;
    Context context;
    ArrayList<String> descriptions;

    public HistoryAdapter(Context context, ArrayList<String> breakfastKeys, ArrayList<String> descriptions) {
        this.breakfastKeys = breakfastKeys;
        this.context = context;
        this.descriptions = descriptions;
    }

    @Override
    public int getCount() {
        return descriptions.size();
    }

    @Override
    public Object getItem(int pos) {
        return descriptions.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        //return list.get(pos).;
        return 0;
    }

    public String getBreakfastKey(int pos){
        return breakfastKeys.get(pos);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(android.R.layout.simple_list_item_1, null);
        }

        //Handle TextView and display string from your list
        final TextView listItemText = (TextView) view.findViewById(android.R.id.text1);
        String listText = descriptions.get(position);
        listItemText.setText(listText);
        listItemText.setTextColor(Color.WHITE);
        Log.d("HISTORY", descriptions.get(position));
        return view;
    }
}
