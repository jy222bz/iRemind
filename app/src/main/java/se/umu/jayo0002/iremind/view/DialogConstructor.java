package se.umu.jayo0002.iremind.view;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import java.util.Calendar;
import se.umu.jayo0002.iremind.R;
import se.umu.jayo0002.iremind.models.date.Date;


/**
 * This class for dat and time pickers dialogs.
 *
 * @author Jacob Yousif
 * @version 1.0
 * @since 2020-01-04
 */
public class DialogConstructor {

    /**
     * A private field represents the activity.
     */
    private final Activity activity;

    /**
     * It constructs the object.
     * @param activity
     */
    public DialogConstructor(Activity activity){
        this.activity = activity;
    }

    /**
     * It returns DatePickerDialog.
     * @param year
     * @param month
     * @param day
     * @param endMonth
     * @param listener
     * @return DatePickerDialog
     */
    public DatePickerDialog getDateDialog(int year, int month, int day, int endMonth,
                                          DatePickerDialog.OnDateSetListener listener){
        DatePickerDialog dialog = new DatePickerDialog(
                activity, R.style.PickerTheme, listener,year, month, day);
        dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        dialog.getDatePicker().setMaxDate(Date.getTimeInTheFuture(endMonth));
        return dialog;
    }

    /**
     * It returns TimePickerDialog.
     * @param listener
     * @return TimePickerDialog
     */
    public TimePickerDialog getTimeDialog(TimePickerDialog.OnTimeSetListener listener){
        return new TimePickerDialog(activity,
                R.style.PickerTheme, listener,
                Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                Calendar.getInstance().get(Calendar.MINUTE), false);
    }
}
