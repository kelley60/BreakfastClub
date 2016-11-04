package cs490.breakfastclub.Camera;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;

import cs490.breakfastclub.R;

public class PhotoViewerActivity extends AppCompatActivity {

    URL picItem,picPrevItem,picNextItem;
    int picPosition;
    private ImageView m_vwImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_viewer);
        Intent i = getIntent();

        try {
            picItem = new URL(i.getStringExtra("pictureId"));
            picPrevItem = new URL (i.getStringExtra("picturePrevId"));
            picNextItem = new URL(i.getStringExtra("pictureNextId"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        picPosition = i.getIntExtra("picturePosition", -1);
        m_vwImage = (ImageView) findViewById(R.id.imgDisplay);



     //   Drawable image = getResources().getDrawable((int)picItem);
     //   m_vwImage.setImageDrawable(image);
        Picasso.with((Context)getApplicationContext()).load(picItem.toString()).into(m_vwImage);

        ImageButton startButton = (ImageButton) findViewById(R.id.grid_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finish();
            }
        });

        ImageButton leftButton = (ImageButton) findViewById(R.id.left_button);
        leftButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Picasso.with(ImageAdapter.getmContext()).load(picPrevItem.toString()).into(m_vwImage);
                //Drawable imagePrev = getResources().getDrawable((int)picPrevItem);
                //m_vwImage.setImageDrawable(imagePrev);

                if(--picPosition<0) picPosition = ImageAdapter.mSize;
                picPrevItem = (ImageAdapter.getPrevItemURL(picPosition));
                picNextItem = (ImageAdapter.getNextItemURL(picPosition));

            }
        });

        ImageButton rightButton = (ImageButton) findViewById(R.id.right_button);
        rightButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                //Drawable imageNext = getResources().getDrawable((int)picNextItem);
                //m_vwImage.setImageDrawable(imageNext);
                Picasso.with(ImageAdapter.getmContext()).load(picNextItem.toString()).into(m_vwImage);

                if(++picPosition> ImageAdapter.mSize) picPosition = 0;
                picNextItem = (ImageAdapter.getNextItemURL(picPosition));
                picPrevItem = (ImageAdapter.getPrevItemURL(picPosition));
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
