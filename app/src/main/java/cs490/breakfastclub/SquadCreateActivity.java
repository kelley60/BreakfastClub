package cs490.breakfastclub;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import cs490.breakfastclub.Classes.User;

public class SquadCreateActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private StorageReference mStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_squad_create);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Create A Squad");

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance().getReferenceFromUrl("gs://breakfastclubapp-437bd.appspot.com");
        final User currentUser = ((MyApplication) getApplication()).getCurrentUser();

        Button createSquad = (Button) findViewById(R.id.createSquad);
        createSquad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentSquadKey = mDatabase.child("Squads").push().getKey();
                EditText squadName = (EditText) findViewById(R.id.txtSquadName);
                EditText squadDesc = (EditText) findViewById(R.id.txtSquadDesc);
                ImageView squadPhoto = (ImageView) findViewById(R.id.squadPhoto);
                mDatabase.child("Squads/" + currentSquadKey).child("name").setValue(squadName.getText().toString());
                mDatabase.child("Squads/" + currentSquadKey).child("description").setValue(squadDesc.getText().toString());
                mDatabase.child("Squads/" + currentSquadKey).child("captain").setValue(currentUser.getUserId());
                StorageReference squadRef = mStorage.child("Squads/" + currentSquadKey);
                Bitmap bitmap = ((BitmapDrawable)squadPhoto.getDrawable()).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data = baos.toByteArray();

                UploadTask uploadTask = squadRef.putBytes(data);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.v("Image Upload", "Failure Image Upload");
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        Log.v("Image Upload", "SuccessFul Image Upload " + downloadUrl.toString());
                    }
                });


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
}
