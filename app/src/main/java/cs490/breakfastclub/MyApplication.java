package cs490.breakfastclub;

/**
 * Created by michaelrollberg on 10/18/16.
 */

import android.app.Application;

import cs490.breakfastclub.CameraAndPhotos.Photos;
import cs490.breakfastclub.UserFiles.User;

public class MyApplication extends Application {

    private User currentUser;
    private Photos currentPhotos;


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

    // Use to get the application's current student
    public Photos getCurrentPhotos()
    {
        return currentPhotos;
    }

    // Use to set the application's current student
    public void setCurrentPhotos(Photos photos)
    {
        this.currentPhotos = photos;
    }



}
