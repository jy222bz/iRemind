package se.umu.jayo0002.iremind.models.date;

import java.util.Calendar;

/**
 * This class represents an object that provides one static method.
 *
 * @author Jacob Yousif
 * @version 1.0
 * @since 2019 -12-09
 * */
public class DateValidator {

    /**
     * It validates whether the time and the date for the alarm of the event is in the future.
     *
     * @param hour
     * @param minute
     * @param year
     * @param month
     * @param day
     * @return boolean
     */
    public static boolean isDateValid(int hour, int minute, int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        c.set(Calendar.YEAR, year);
        return c.after(Calendar.getInstance());
    }
}
