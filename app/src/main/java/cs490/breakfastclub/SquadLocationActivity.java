package cs490.breakfastclub;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
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

import cs490.breakfastclub.Classes.User;

public class SquadLocationActivity extends AppCompatActivity implements OnMapReadyCallback {

    private ArrayList<User> mSquad;

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

    }

    @Override
    public void onMapReady(GoogleMap map) {
        /*map.addMarker(new MarkerOptions()
                .position(new LatLng(42.000, -87.000))
                .title("Trevor Edris"));*/

        mSquad = new ArrayList<>();
        final User currentUser = ((MyApplication) getApplication()).getCurrentUser();

        map.addMarker(new MarkerOptions()
                .position(new LatLng(40.4237, -86.9100))
                .title("Purdue"));

        currentUser.updateLocation(40.5000, -87.000);
        Log.d("SquadLocation", "currentUserLocation: " + currentUser.getName() + "  (" + currentUser.getLat() + ", " + currentUser.getLng() + ")\n");
        map.addMarker(new MarkerOptions()
                .position(new LatLng(currentUser.getLat(), currentUser.getLng()))
                .title(currentUser.getName()));


        // TODO: Grab squad members from the database
        if (currentUser.isPartOfSquad()) {
            DatabaseReference squadRef = FirebaseDatabase.getInstance().getReference("Squads/" + currentUser.getSquad().getSquadID());
            squadRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    HashMap<String, String> members = (HashMap) dataSnapshot.child("Members").getValue();
                    Log.v("Members", members.toString());

                    for (Map.Entry<String, String> member : members.entrySet()) {
                        Log.v("Member", member.getKey() + " " + member.getValue());
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

        // TODO: Add marker on map for each member
        for(int i = 0; i < mSquad.size(); i++)
        {
            /*map.addMarker(new MarkerOptions()
            .position(new LatLng(mSquad.get(i).getLat(), mSquad.get(i).getLng()))
            .title(mSquad.get(i).getName()));*/
            Log.d("SquadLocation", "loop iteration: " + i + "\n");
        }
    }
}
