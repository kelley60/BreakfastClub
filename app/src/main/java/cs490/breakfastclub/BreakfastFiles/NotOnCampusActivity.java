package cs490.breakfastclub.BreakfastFiles;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.function.ToLongBiFunction;

import cs490.breakfastclub.ToolBarBreakfastClub;
import cs490.breakfastclub.R;

public class NotOnCampusActivity extends AppCompatActivity {

    private static final String ToolbarTitle = "Not On Campus";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.not_on_campus);
        ToolBarBreakfastClub.toolBarInit(this, ToolbarTitle);
    }


}
