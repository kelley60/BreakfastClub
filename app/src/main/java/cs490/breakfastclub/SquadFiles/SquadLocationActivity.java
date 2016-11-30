package cs490.breakfastclub.SquadFiles;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import cs490.breakfastclub.UserFiles.User;
import cs490.breakfastclub.MyApplication;
import cs490.breakfastclub.R;

public class SquadLocationActivity extends AppCompatActivity implements OnMapReadyCallback {

    private ArrayList<User> mSquad;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_squad_location);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("[SQUAD_NAME]");


        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        Button btnAddMarkers = (Button) findViewById(R.id.btnAddMarkers);
        btnAddMarkers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Add marker on map for each member
                Random rand = new Random();
                double latBase = 40.427608;
                double lngBase = -86.917040;
                latBase += rand.nextDouble() / 1000;
                lngBase += rand.nextDouble() / 10000;

                Log.d("Location", "Sizeof mSquad is " + mSquad.size() + "\n");
                for(int i = 0; i < mSquad.size(); i++)
                {
                    mSquad.get(i).updateLocation(latBase, lngBase);
                    Log.d("Location", "SquadMember + " + mSquad.get(i).getName() + "\n");
                    mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(mSquad.get(i).getLat(), mSquad.get(i).getLng()))
                            .title(mSquad.get(i).getName())
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                    Log.d("SquadLocation", "loop iteration: " + i + "\n");
                }
            }
        });


    }

    @Override
    public void onMapReady(GoogleMap map) {
        /*map.addMarker(new MarkerOptions()
                .position(new LatLng(42.000, -87.000))
                .title("Trevor Edris"));*/
        mMap = map;

        mSquad = new ArrayList<>();
        final User currentUser = ((MyApplication) getApplication()).getCurrentUser();

        //==================================================================
        //              Hard coded map markers
        //==================================================================
        // Purdue
        map.addMarker(new MarkerOptions()
                .position(new LatLng(40.428569, -86.913852))
                .title("Purdue")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));

        // Brothers
        map.addMarker(new MarkerOptions()
                .position(new LatLng(40.424060, -86.908379))
                .title("Brothers")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));

        // Harry's
        map.addMarker(new MarkerOptions()
                .position(new LatLng(40.423879, -86.909072))
                .title("Harry's")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));

        // 308
        map.addMarker(new MarkerOptions()
                .position(new LatLng(40.424076, -86.908487))
                .title("308")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));

        // Jakes
        map.addMarker(new MarkerOptions()
                .position(new LatLng(40.423399, -86.908000))
                .title("Jakes")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));

        // Cactus
        map.addMarker(new MarkerOptions()
                .position(new LatLng(40.423374, -86.900683))
                .title("Neon Cactus")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));

        // Where Else
        map.addMarker(new MarkerOptions()
                .position(new LatLng(40.422978, -86.907973))
                .title("Where Else")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));



        // TODO: Get current location somehow
        Random rand = new Random();
        double latBase = 40.427608;
        double lngBase = -86.917040;
        latBase += rand.nextDouble() / 1000;
        lngBase += rand.nextDouble() / 10000;
        currentUser.updateLocation(latBase, lngBase);
        //Log.d("SquadLocation", "currentUserLocation: " + currentUser.getName() + "  (" + currentUser.getLat() + ", " + currentUser.getLng() + ")\n");
        map.addMarker(new MarkerOptions()
                .position(new LatLng(currentUser.getLat(), currentUser.getLng()))
                .title(currentUser.getName())
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));


        // TODO: Grab squad members from the database
        if (currentUser.isPartOfSquad()) {
            DatabaseReference squadRef = FirebaseDatabase.getInstance().getReference("Squads/" + currentUser.getSquad().getSquadID());
            squadRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    HashMap<String, String> members = (HashMap) dataSnapshot.child("Members").getValue();
                    Log.v("Members", members.toString());

                    for (Map.Entry<String, String> member : members.entrySet()) {
                        //Log.v("Member", member.getKey() + " " + member.getValue());
                        DatabaseReference memberRef = FirebaseDatabase.getInstance().getReference("Users/" + member.getKey());
                        memberRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                User currentMember = new User();
                                currentMember.setName((String) dataSnapshot.child("name").getValue());
                                currentMember.setProfileImageUrl((String) dataSnapshot.child("profileImageUrl").getValue());
                                currentMember.setUserId(dataSnapshot.getKey());
                                mSquad.add(currentMember);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                    //DatabaseReference memberRef = FirebaseDatabase.getInstance().getReference("Users/" + member.)

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }


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
