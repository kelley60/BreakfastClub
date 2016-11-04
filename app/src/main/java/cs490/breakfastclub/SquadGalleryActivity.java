package cs490.breakfastclub;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import cs490.breakfastclub.Camera.ImageAdapter;
import cs490.breakfastclub.Camera.PhotoViewerActivity;
import cs490.breakfastclub.Classes.User;

public class SquadGalleryActivity extends AppCompatActivity {

    private URL prev, next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_squad_gallery);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        final User currentUser = ((MyApplication) getApplication()).getCurrentUser();
        final LinkedHashMap<String, URL> linkedHashMap = ((MyApplication) getApplication()).getCurrentPhotos().getUserPhotos();
        final ArrayList<URL> currentPhotos = new ArrayList<URL>(linkedHashMap.values());
        final ArrayList<String> photoids = new ArrayList<String>(linkedHashMap.keySet());
        final GridView squadGallery = (GridView) findViewById(R.id.squadGallery);
        Intent intent = getIntent();
        squadGallery.setAdapter(new ImageAdapter(this, currentUser, currentPhotos, photoids));

        squadGallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {


                prev = ImageAdapter.getPrevItemURL(position);
                next = ImageAdapter.getNextItemURL(position);

                showImage(squadGallery.getAdapter().getItemId(position), position);


            }
        });
    }

    /* TODO: Populate gridview with info from this link
    http://stacktips.com/tutorials/android/android-gridview-example-building-image-gallery-in-android
    */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showImage(long id, int pos){
        Intent pictureViewer = new Intent(this, PhotoViewerActivity.class);
        pictureViewer.putExtra("pictureId",id );
        pictureViewer.putExtra("picturePosition", pos);
        pictureViewer.putExtra("picturePrevId", prev);
        pictureViewer.putExtra("pictureNextId", next);

        startActivityForResult(pictureViewer,0);

    }
}
