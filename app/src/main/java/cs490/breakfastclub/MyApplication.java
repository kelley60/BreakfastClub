package cs490.breakfastclub;

/**
 * Created by michaelrollberg on 10/18/16.
 */
import android.app.Application;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

import cs490.breakfastclub.BreakfastFiles.Breakfast;
import cs490.breakfastclub.CameraAndPhotos.Photos;
import cs490.breakfastclub.UserFiles.User;

public class MyApplication extends Application {

    private User currentUser;
    private Photos currentPhotos;
    private Breakfast breakfast;

    // Use to get the application's current student
    public User getCurrentUser()
    {
        return currentUser;
    }

    // Use to set the application's current student
    public void setCurrentUser(User user)
    {
        this.currentUser = user;
    }


    public Photos getCurrentPhotos() {
        return currentPhotos;
    }

    public void setCurrentPhotos(Photos currentPhotos) {
        this.currentPhotos = currentPhotos;
    }

    public Breakfast getBreakfast() {
        return breakfast;
    }

    public void setBreakfast(Breakfast breakfast) {
        this.breakfast = breakfast;
    }


}
