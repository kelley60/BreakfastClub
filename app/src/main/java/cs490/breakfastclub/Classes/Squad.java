package cs490.breakfastclub.Classes;

import java.security.acl.Group;
import java.util.ArrayList;

/**
 * Created by Sean on 10/17/16.
 */

public class Squad {

    private String squadName;
    private User squadLeader;
    private ArrayList<User> userList;
    private GroupFeed groupFeed;


    public Squad(String squadName, User squadLeader){
        this.squadName = squadName;
        this.squadLeader = squadLeader;
        userList = new ArrayList<User>();
        groupFeed = new GroupFeed();
    }






























    //GETTERS AND SETTERS --------------------------------------------------------------

    public ArrayList<User> getUserList() {
        return userList;
    }

    public void setUserList(ArrayList<User> userList) {
        this.userList = userList;
    }

    public User getSquadLeader() {
        return squadLeader;
    }

    public void setSquadLeader(User squadLeader) {
        this.squadLeader = squadLeader;
    }

    public GroupFeed getGroupFeed() {
        return groupFeed;
    }

    public void setGroupFeed(GroupFeed groupFeed) {
        this.groupFeed = groupFeed;
    }

    public String getSquadName() {
        return squadName;
    }

    public void setSquadName(String squadName) {
        this.squadName = squadName;
    }
}
