package cs490.breakfastclub.Classes;

/**
 * Created by Sean on 10/17/16.
 */

public class Venue {
    private String name;
    private String location;
    private int waitTimeMinutes;
    private String hoursOfOperation;
    private boolean isOpen;

    public Venue(String name, String location, int waitTimeMinutes, String hoursOfOperation){
        this.name = name;
        this.location = location;
        this.waitTimeMinutes = waitTimeMinutes;
        this.hoursOfOperation = hoursOfOperation;
        isOpen = false;
    }


































    //GETTERS AND SETTERS --------------------------------------------------------------
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getWaitTimeMinutes() {
        return waitTimeMinutes;
    }

    public void setWaitTimeMinutes(int waitTimeMinutes) {
        this.waitTimeMinutes = waitTimeMinutes;
    }

    public String getHoursOfOperation() {
        return hoursOfOperation;
    }

    public void setHoursOfOperation(String hoursOfOperation) {
        this.hoursOfOperation = hoursOfOperation;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }
}
