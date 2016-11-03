package cs490.breakfastclub;

/**
 * Created by michaelrollberg on 10/18/16.
 */
import android.app.Application;

import cs490.breakfastclub.Classes.User;

public class MyApplication extends Application {

    private User currentUser;

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


}
