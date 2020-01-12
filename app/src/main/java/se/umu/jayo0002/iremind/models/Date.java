package se.umu.jayo0002.iremind.models;

import java.util.Calendar;
import static se.umu.jayo0002.iremind.Tags.MONTHS;

/**
 * This class represents an object that provides two methods about date and time.
 *
 * @author Jacob Yousif
 * @version 1.0
 * @since 2019 -12-09
 * */
public class Date {

    /**
     * It returns the the current date; Day and Month.
     * @return String
     */
    public String getDate() {
        String month = MONTHS[Calendar.getInstance().get(Calendar.MONTH)];
        Calendar.getInstance();
        int day = (Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        return StringFormatter.getFormattedString(day)  + " " + month;
    }

    /**
     * It returns the the current date; Day, Month and Year.
     * @return String
     */
    public static String getFullDate(){
        return Calendar.getInstance().get(Calendar.YEAR) + "-" + StringFormatter.getFormattedString(
                (Calendar.getInstance().get(Calendar.MONTH) + 1)) + "-" +
                StringFormatter.getFormattedString(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
    }

    /**
     * It returns a time in the near future.
     * The parameter is the amount of months in future.
     * @param amountOfMonths
     * @return long
     */
    public static long getPlus (int amountOfMonths){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, +amountOfMonths);
        return calendar.getTimeInMillis();
    }
}
