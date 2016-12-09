package cs490.breakfastclub;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

/**
 * Created by Sean on 12/8/16.
 */

public class ToolBarBreakfastClub {

    public ToolBarBreakfastClub(){
    }

    public static void toolBarInit(Context context, String title) {
        Toolbar myToolbar = (Toolbar) ((AppCompatActivity)context).findViewById(R.id.my_toolbar);
        ((AppCompatActivity)context).setSupportActionBar(myToolbar);
        ((AppCompatActivity)context).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)context).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((AppCompatActivity)context).getSupportActionBar().setTitle(title);
    }

}
