package cs490.breakfastclub.SquadFiles;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import cs490.breakfastclub.UserFiles.User;
import cs490.breakfastclub.MyApplication;
import cs490.breakfastclub.R;

public class SquadCreateActivity extends AppCompatActivity {

    // TODO: On successful creation, redirect user to different page

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
                if (!currentUser.isPartOfSquad()) {
                    final String currentSquadKey = mDatabase.child("Squads").push().getKey();
                    final EditText squadName = (EditText) findViewById(R.id.txtSquadName);
                    final EditText squadDesc = (EditText) findViewById(R.id.txtSquadDesc);
                    final ImageView squadPhoto = (ImageView) findViewById(R.id.squadPhoto);
                    mDatabase.child("Squads/" + currentSquadKey).child("name").setValue(squadName.getText().toString());
                    mDatabase.child("Squads/" + currentSquadKey).child("description").setValue(squadDesc.getText().toString());
                    mDatabase.child("Squads/" + currentSquadKey).child("Members").child(currentUser.getUserId()).child("squadRole").setValue("captain");
                    mDatabase.child("Squads/" + currentSquadKey).child("Members").child(currentUser.getUserId()).child("name").setValue(currentUser.getName());
                    mDatabase.child("Squads/" + currentSquadKey).child("Members").child(currentUser.getUserId()).child("profileImageUrl").setValue(currentUser.getProfileImageUrl());
                    mDatabase.child("Squads/" + currentSquadKey).child("Members").child(currentUser.getUserId()).setPriority(0);
                    mDatabase.child("Users/" + currentUser.getUserId()).child("squad").setValue(currentSquadKey);
                    mDatabase.child("Users/" + currentUser.getUserId()).child("squadRole").setValue("captain");
                    StorageReference squadRef = mStorage.child("Squads/" + currentSquadKey);
                    Bitmap bitmap = ((BitmapDrawable) squadPhoto.getDrawable()).getBitmap();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] data = baos.toByteArray();

                    UploadTask uploadTask = squadRef.putBytes(data);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Log.v("Image Upload", "Failure Image Upload");
                            // Do something if this fails to work
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            Log.v("Image Upload", "SuccessFul Image Upload " + downloadUrl.toString());
                            Squad currentSquad = new Squad(squadName.getText().toString(),
                                    currentSquadKey, ((BitmapDrawable) squadPhoto.getDrawable()).getBitmap(), squadDesc.getText().toString());
                            currentUser.setSquad(currentSquad);
                            currentUser.setSquadRole("captain");
                            currentUser.setPartOfSquad(true);
                            finish();
                        }
                    });

                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(SquadCreateActivity.this);
                    builder.setMessage("Squad " + squadName.getText() + " has been successfully created.")
                            .setCancelable(false)
                            .setPositiveButton("OK", null);
                    android.app.AlertDialog alert = builder.create();
                    alert.show();
                }
                else
                {
                    new AlertDialog.Builder(SquadCreateActivity.this)
                            .setTitle("Create Squad")
                            .setMessage("You cannot create a squad because you are already a part of a squad.")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }


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
