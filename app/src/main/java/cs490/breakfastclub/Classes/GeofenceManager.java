package cs490.breakfastclub.Classes;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;

import java.util.UUID;

import cs490.breakfastclub.GeofenceTransitionsIntentService;

/**
 * Created by Sean on 11/2/16.
 */

public class GeofenceManager implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback<Status> {

    private Context context;
    GoogleApiClient mGoogleApiClient;
    Geofence mGeoFence;
    private String TAG = "Geofence manager";

    private static final double GEOFENCE_LATITUDE = 40.4237;
    private static final double GEOFENCE_LONGITUDE = -86.9100;
    private static final float GEOFENCE_RADIUS = 10500;
    private PendingIntent mGeofencePendingIntent;

    private static GeofenceManager INSTANCE;

    public static GeofenceManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GeofenceManager();
        }
        return INSTANCE;
    }

    public void init(Context context) {
        Log.d(TAG, "Geofence manager created");
        this.context = context.getApplicationContext();
        googleApiInit();
    }

    private void geofenceInit(Context context) {
        String id = UUID.randomUUID().toString();
        mGeoFence = new Geofence.Builder()
                .setRequestId(id)
                .setCircularRegion(
                        GEOFENCE_LATITUDE,
                        GEOFENCE_LONGITUDE,
                        GEOFENCE_RADIUS
                )
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                .build();
        try {
            LocationServices.GeofencingApi.addGeofences(
                    mGoogleApiClient,
                    getGeofencingRequest(),
                    getGeofencePendingIntent()
            ).setResultCallback(new ResultCallback<Status>() {

                @Override
                public void onResult(Status status) {
                    if (status.isSuccess()) {
                        Log.e(TAG, "Registering geofence success");
                    } else {
                        Log.e(TAG, "Registering geofence failed: " + status.getStatusMessage() +
                                " : " + status.getStatusCode());
                    }
                }
            });
        }
        catch(SecurityException E){

        }
    }

    protected void onStart() {
        mGoogleApiClient.connect();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // An unresolvable error has occurred and a connection to Google APIs
        // could not be established. Display an error message, or handle
        // the failure silently

        // ...
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "geofence manager connected");
        geofenceInit(context);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }



    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER | GeofencingRequest.INITIAL_TRIGGER_EXIT);
        builder.addGeofence(mGeoFence);
        return builder.build();
    }

    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(context, GeofenceTransitionsIntentService.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
        // calling addGeofences() and removeGeofences().
        return PendingIntent.getService(context, 0, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);
    }

    @Override
    public void onResult(@NonNull Status status) {

    }

    private void googleApiInit() {
        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(context)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        mGoogleApiClient.connect();
    }
}
