package cs490.breakfastclub.CameraAndPhotos;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.share.widget.ShareDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import cs490.breakfastclub.MyApplication;
import cs490.breakfastclub.R;
import cs490.breakfastclub.UserFiles.User;

public class PhotoActivity extends AppCompatActivity {


    private ImageView m_vwImage;
    public static CallbackManager callbackManager;
    public AccessTokenTracker accessTokenTracker;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;
    User currentUser;
    String currentBreakfast;
    Photo photo;
    String path;
    String name;
    Bitmap b;
    Uri photoUri;
    Context c;
    ShareDialog shareDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Repeat Offenders");

       // shareDialog = new ShareDialog(this);
        // this part is optional
       // shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {...     });


        currentUser = ((MyApplication) getApplication()).getCurrentUser();
        currentBreakfast = ((MyApplication) getApplication()).getCurrentBreakfast().getBreakfastKey();
        c = this;
        Intent i = getIntent();

        path = i.getStringExtra("Path");
        photoUri = Uri.parse(i.getStringExtra("Uri"));

        photo = new Photo(path, currentUser.getUserId());

        m_vwImage = (ImageView) findViewById(R.id.imgDisplay);
        m_vwImage.setImageBitmap(photo.getbMap());
        //Picasso.with(this).load(photoUri).into(m_vwImage);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        callbackManager = CallbackManager.Factory.create();

  /*      ImageButton undoButton = (ImageButton) findViewById(R.id.undo_button);
        undoButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(PhotoActivity.this, CameraActivity.class);
                startActivity(intent);
            }
        });
*/
        Button saveButton = (Button) findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                addPhotoToGallery();
                photo.addPhotoToFirebase(currentUser, currentBreakfast);
                Toast toast = Toast.makeText(getApplicationContext(), "photo saved", Toast.LENGTH_SHORT);
                toast.show();
                //Todo:add to db
            }
        });

        Button shareButton = (Button) findViewById(R.id.share_button);
        shareButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                addPhotoToGallery();

                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(PhotoActivity.this, view);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.popup_share_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        photo.setPhotoSquadId(currentUser.getSquad().getSquadID().toString());
                        photo.setPhotoUserId(currentUser.getUserId());
                        switch(item.getItemId())
                        {
                            case R.id.set_profile_pic: {
                                photo.setIsProfilePhoto(true);
                                photo.addPhotoToFirebase(currentUser, currentBreakfast);
                               // mDatabase.child("Users").child(currentUser.getUserId()).child("profileImageUrl").setValue(photo.getPhotoName());
                                break;
                            }
                            case R.id.set_squad_profile_pic: {
                                photo.setIsSquadProfilePhoto(true);
                                photo.setIsSquadFeed(true);
                                photo.addPhotoToFirebase(currentUser, currentBreakfast);
                              //  mDatabase.child("Squads").child(currentUser.getSquad().getSquadID()).child("profileImageUrl").setValue(photo.getPhotoName());
                                break;
                            }
                            case R.id.squad_post: {
                                photo.setIsSquadFeed(true);
                                photo.addPhotoToFirebase(currentUser, currentBreakfast);
                                break;
                            }
                            case R.id.breakfast_post: {
                                photo.setIsBreakfastFeed(true);
                                photo.addPhotoToFirebase(currentUser, currentBreakfast);
                                break;
                            }
                            case R.id.facebook_share: {
                                photo.addPhotoToFirebase(currentUser, currentBreakfast);

                                Intent sharingIntent = new Intent(Intent.ACTION_SEND);

                                sharingIntent.setType("image/jpeg");
                                sharingIntent.putExtra(Intent.EXTRA_STREAM, photoUri);
                                startActivity(Intent.createChooser(sharingIntent, "Share image using"));

                                break;
                            }

                        }
                        Toast.makeText(PhotoActivity.this,"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });

                popup.show();//showing popup menu

                //Todo:add to facebook
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
/* Stuff above works in all other activities,
    stuff commented out does not make back arrow functional
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
*/

    /**
     * Add the picture to the photo gallery.
     * Must be called on all camera images or they will
     * disappear once taken.
     */
    protected void addPhotoToGallery() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(photoUri);
        this.sendBroadcast(mediaScanIntent);

    }

    public static int getOrientation(Context context, Uri photoUri) {
    /* it's on the external media. */
        Cursor cursor = context.getContentResolver().query(photoUri,
                new String[]{MediaStore.Images.ImageColumns.ORIENTATION}, null, null, null);

        if (cursor.getCount() != 1) {
            return -1;
        }

        cursor.moveToFirst();
        return cursor.getInt(0);
    }



}
