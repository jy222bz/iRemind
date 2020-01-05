package se.umu.jayo0002.iremind.models;

import java.util.Calendar;

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
      String[] mMonth = {"January", "February",
                "March", "April", "May", "June", "July",
                "August", "September", "October", "November",
                "December"};
        String month = mMonth[Calendar.getInstance().get(Calendar.MONTH)];
        Calendar.getInstance();
        int day = (Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        return StringFormatter.getFormattedString(day)  + " " + month;
    }

    /**
     * It returns a time in the near future.
     * The parameter is the amount of months in future.
     * @param amountOfMonths
     * @return long
     */
    public long getPlus (int amountOfMonths){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, +amountOfMonths);
        return calendar.getTimeInMillis();
    }
}
