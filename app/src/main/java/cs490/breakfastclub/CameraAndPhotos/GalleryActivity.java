package cs490.breakfastclub.CameraAndPhotos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import cs490.breakfastclub.MyApplication;
import cs490.breakfastclub.R;
import cs490.breakfastclub.UserFiles.User;

public class GalleryActivity extends AppCompatActivity {

    private URL prev, next, current;
    private static final int USER_PHOTOS = 0;
    private static final int SQUAD_PHOTOS = 1;
    private static final int BREAKFAST_PHOTOS = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();

        final User currentUser = ((MyApplication) getApplication()).getCurrentUser();
        final LinkedHashMap<String, URL> linkedHashMap =
                currentUser.getCurrentPhotos().getPhotoSet(intent.getIntExtra("photo set", 0));
        final ArrayList<URL> currentPhotos = new ArrayList<URL>(linkedHashMap.values());
        final ArrayList<String> photoids = new ArrayList<String>(linkedHashMap.keySet());
        final GridView squadGallery = (GridView) findViewById(R.id.gallery);

        squadGallery.setAdapter(new ImageAdapter(this, currentUser, currentPhotos, photoids));

        squadGallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {


                prev = ImageAdapter.getPrevItemURL(position);
                next = ImageAdapter.getNextItemURL(position);
                current = ImageAdapter.getItemURL(position);

                showImage(position);

            }
        });
    }


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

    private void showImage(int pos){
        Intent pictureViewer = new Intent(this, PhotoViewerActivity.class);
        pictureViewer.putExtra("pictureId",current.toString() );
        pictureViewer.putExtra("picturePosition", pos);
        pictureViewer.putExtra("picturePrevId", prev.toString());
        pictureViewer.putExtra("pictureNextId", next.toString());

        startActivityForResult(pictureViewer,0);

    }
}
