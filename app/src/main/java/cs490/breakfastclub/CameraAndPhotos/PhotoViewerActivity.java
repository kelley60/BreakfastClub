package cs490.breakfastclub.CameraAndPhotos;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import cs490.breakfastclub.R;

public class PhotoViewerActivity extends AppCompatActivity {

    //URL picItem,picPrevItem,picNextItem;
    String picItem,picPrevItem,picNextItem;
    int picPosition;
    private ImageView m_vwImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_viewer);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Repeat Offenders");
        Intent i = getIntent();
/*
        try {
            picPrevItem = new URL (i.getStringExtra("picturePrevId"));
            picNextItem = new URL(i.getStringExtra("pictureNextId"));
            picItem = new URL(i.getStringExtra("pictureId"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
*/
        picPrevItem = i.getStringExtra("picturePrevId");
        picNextItem = i.getStringExtra("pictureNextId");
        picItem = i.getStringExtra("pictureId");

        picPosition = i.getIntExtra("picturePosition", -1);
        m_vwImage = (ImageView) findViewById(R.id.imgDisplay);

        Picasso.with((Context)getApplicationContext()).load(picItem.toString()).fit().into(m_vwImage);

        ImageButton startButton = (ImageButton) findViewById(R.id.grid_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finish();
            }
        });

        ImageButton leftButton = (ImageButton) findViewById(R.id.left_button);
        leftButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
//                Picasso.with(ImageAdapter.getmContext()).load(picPrevItem.toString()).into(m_vwImage);
                Picasso.with(ImageAdapter.getmContext()).load(picPrevItem).into(m_vwImage);

                if(--picPosition<0) picPosition = ImageAdapter.mSize -1;
                picPrevItem = (ImageAdapter.getPrevItemURL(picPosition).toString());
                picNextItem = (ImageAdapter.getNextItemURL(picPosition).toString());

            }
        });

        ImageButton rightButton = (ImageButton) findViewById(R.id.right_button);
        rightButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                //Drawable imageNext = getResources().getDrawable((int)picNextItem);
                //m_vwImage.setImageDrawable(imageNext);
                Picasso.with(ImageAdapter.getmContext()).load(picNextItem.toString()).into(m_vwImage);

                if(++picPosition> ImageAdapter.mSize - 1) picPosition = 0;
                picNextItem = (ImageAdapter.getNextItemURL(picPosition).toString());
                picPrevItem = (ImageAdapter.getPrevItemURL(picPosition).toString());
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_photo_viewer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
