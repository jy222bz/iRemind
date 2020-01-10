package se.umu.jayo0002.iremind.models;


/**
 * This class represents an object that provides one static method.
 *
 * @author Jacob Yousif
 * @version 1.0
 * @since 2019 -12-09
 * */
public class StringFormatter {

    /**
     * A static method that check whether the number lower than ten.
     * When the number lower than ten, it adds a prefix to it.
     * Otherwise, it does not need any format.
     *
     * @param target
     * @return String
     */
    public static String getFormattedString(int target){
        if (target < 10)
            return String.valueOf(target) + target;
        return String.valueOf(target);
    }

    /**
     * It returns a formatted time.
     * @param hour
     * @param minute
     * @return String
     */
    public static String getFormattedTime(int hour, int minute){
        return "Time: " + getFormattedString(hour) + ":" + getFormattedString(minute);
    }

    /**
     * It returns a formatted date.
     * @param year
     * @param month
     * @param day
     * @return String
     */
    public static String getFormattedDate(int year, int month, int day){
        return "Date: " +  year + "-" + getFormattedString((month)) + "-" + getFormattedString(day);
    }
}
