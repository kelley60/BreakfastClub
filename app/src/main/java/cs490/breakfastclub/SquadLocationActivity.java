package cs490.breakfastclub;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import cs490.breakfastclub.Classes.User;

public class SquadLocationActivity extends AppCompatActivity implements OnMapReadyCallback {

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
        map.addMarker(new MarkerOptions()
                .position(new LatLng(40.4237, -86.9100))
                .title("Purdue"));

        // TODO: Grab squad members from the database
        ArrayList<User> members = new ArrayList<>();

        // TODO: Add marker on map for each member
        for(int i = 0; i < members.size(); i++)
        {
            map.addMarker(new MarkerOptions()
            .position(new LatLng(members.get(i).getLat(), members.get(i).getLng()))
            .title(members.get(i).getName()));
        }
    }
}
