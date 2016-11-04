package cs490.breakfastclub;

/**
 * Created by michaelrollberg on 10/18/16.
 */
import android.app.Application;

import cs490.breakfastclub.Classes.Photos;
import cs490.breakfastclub.Classes.User;

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


    public Photos getCurrentPhotos() {
        return currentPhotos;
    }

    public void setCurrentPhotos(Photos currentPhotos) {
        this.currentPhotos = currentPhotos;
    }

}
