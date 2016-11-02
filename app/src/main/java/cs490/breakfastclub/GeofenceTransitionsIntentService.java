package cs490.breakfastclub;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.google.android.gms.location.GeofenceStatusCodes;

import java.util.List;

/**
 * Created by Sean on 10/27/16.
 */

public class GeofenceTransitionsIntentService extends IntentService {

    private String TAG = "Geofence Transitions";
    public static final String MYPREFERENCES = "MyPrefs";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public GeofenceTransitionsIntentService(String name) {
        super(name);
    }

    protected void onHandleIntent(Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            String errorMessage = "Error has occurred with Geofencing";
            Log.e(TAG, errorMessage);
            return;
        }

        // Get the transition type.
        int geofenceTransition = geofencingEvent.getGeofenceTransition();
        SharedPreferences sharedpreferences = getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);

        // Test that the reported transition was of interest.
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putBoolean("IsInGeofence", true);
            editor.commit();
        } else if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putBoolean("IsInGeofence", false);
            editor.commit();
        } else {
            // Log the error.
            Log.e(TAG, "Error with geofence transition.");
        }
    }
}