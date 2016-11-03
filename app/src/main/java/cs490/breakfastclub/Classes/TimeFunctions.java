package cs490.breakfastclub.Classes;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Sean on 11/3/16.
 */

public class TimeFunctions {

    public TimeFunctions(){

    }

    public static String timeUntilEventString(Calendar breakfastTime) {
        Calendar currentTime = getCurrentTime();
        String timeUntil = "";
        long secondsDifference = secondsToBreakfast(breakfastTime, currentTime);

        long seconds = secondsDifference % 60;
        secondsDifference /= 60;
        long minutes = secondsDifference % 60;
        secondsDifference /= 60;
        long hours = secondsDifference % 24;
        secondsDifference /= 24;
        long days = secondsDifference;

        timeUntil += days;
        timeUntil += " Days ";
        timeUntil += hours;
        timeUntil += " Hours ";
        timeUntil += minutes;
        timeUntil += " Minutes ";
        timeUntil += seconds;
        timeUntil += " Seconds ";

        return timeUntil;
    }

    public static void setBreakfastTime(Calendar breakfastTime) {
        breakfastTime.set(2016, 11, 5, 3, 0, 0);
    }

    public static Calendar getCurrentTime(){
        Calendar currentTime = Calendar.getInstance(TimeZone.getTimeZone("America/Fort_Wayne"));
        return currentTime;
    }

    public static long secondsToBreakfast(Calendar breakfastTime, Calendar currentTime){
        long secondsDifference = (breakfastTime.getTimeInMillis() - currentTime.getTimeInMillis()) / 1000;
        return secondsDifference;
    }
}
