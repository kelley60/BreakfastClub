package cs490.breakfastclub;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Calendar;

import cs490.breakfastclub.BreakfastFiles.BreakfastFeedActivity;
import cs490.breakfastclub.CameraAndPhotos.CameraActivity;
import cs490.breakfastclub.BreakfastFiles.Breakfast;
import cs490.breakfastclub.GeofenceFiles.GeofenceManager;
import cs490.breakfastclub.SquadFiles.SquadInviteActivity;
import cs490.breakfastclub.UserFiles.User;
import cs490.breakfastclub.BreakfastFiles.CreateBreakfastActivity;
import cs490.breakfastclub.SquadFiles.SquadCreateActivity;
import cs490.breakfastclub.SquadFiles.SquadViewActivity;

public class DrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Enable the Up button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent locationService = new Intent(getApplicationContext(), LocationService.class);
//        getApplicationContext().startService(locationService);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        GeofenceManager.getInstance().init(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_campusFeed) {
            Intent intent = BreakfastFeedActivity.chooseCampusFeedActivity(this);
            startActivity(intent);
        } else if (id == R.id.nav_createBreakfast) {
            Intent intent = new Intent(DrawerActivity.this, CreateBreakfastActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_displayMessage) {
            Intent intent = new Intent(DrawerActivity.this, DisplayMessageActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_login) {
            Intent intent = new Intent(DrawerActivity.this, LoginActivity.class);
            intent.putExtra("CameFromDrawer", true);
            startActivity(intent);
        } else if (id == R.id.nav_profile) {
            Intent intent = new Intent(DrawerActivity.this, ProfileViewActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_squadCreate) {
            Intent intent = new Intent(DrawerActivity.this, SquadCreateActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_squadView) {
            // disable SquadViewActivity if user is not in squad
            final User currentUser = ((MyApplication) getApplication()).getCurrentUser();
            if (currentUser.isPartOfSquad()){
                Intent intent = new Intent(DrawerActivity.this, SquadViewActivity.class);
                startActivity(intent);
            }
            else{
                //Intent intent = new Intent(DrawerActivity.this, SquadInviteActivity.class);
                //startActivity(intent);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("You must be in a Squad to access that.")
                        .setCancelable(false)
                        .setPositiveButton("OK", null);
                AlertDialog alert = builder.create();
                alert.show();
            }
        }else if (id == R.id.nav_adminViewUsers) {
            Intent intent = new Intent(DrawerActivity.this, AdminViewUsersActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_camera) {
            Intent intent = new Intent(DrawerActivity.this, CameraActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_repeatOffenders) {
            Intent intent = new Intent(DrawerActivity.this, RepeatOffendersActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



}
