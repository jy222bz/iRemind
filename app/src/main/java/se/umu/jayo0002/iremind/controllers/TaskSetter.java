package se.umu.jayo0002.iremind.controllers;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import se.umu.jayo0002.iremind.Tags;
import se.umu.jayo0002.iremind.models.LocationInfo;
import se.umu.jayo0002.iremind.models.Task;
import se.umu.jayo0002.iremind.models.model_controllers.ObjectController;

/**
 * A class that helps to produce a task or sets the values for a bundle.
 */
public class TaskSetter {

    /**
     * A private constructor.
     */
    private TaskSetter() {}


    /**
     * It sets the values upon saving the operation and it returns a Task.
     *
     * @param title
     * @param text
     * @param hour
     * @param minute
     * @param year
     * @param month
     * @param day
     * @param locationInfo
     * @return Task
     */
    public static Task getTask(String title, String text, int hour, int minute, int year, int month,
                               int day, LocationInfo locationInfo, Task task) {
        ObjectController objectController = new ObjectController();
        if (!objectController.isObjectValid(task))
            task = new Task();
        task.setTitle(title);
        task.setNote(text);
        if (objectController.isObjectValid(locationInfo))
            task.setLocation(locationInfo);
        task.setYear(year);
        task.setMonth(month);
        task.setDay(day);
        task.setHour(hour);
        task.setMinute(minute);
        return task;
    }

    /**
     * It sets the values of the out state bundle.
     *
     * @param outState
     * @param datePickerDialog
     * @param timePickerDialog
     * @param pickedDate
     * @param pickedTime
     * @param year
     * @param month
     * @param day
     * @param hour
     * @param minute
     * @param title
     * @param info
     * @param locationInfo
     */
    public static void setOutStateBundle(Bundle outState, DatePickerDialog datePickerDialog, TimePickerDialog timePickerDialog,
                                  String pickedDate, String pickedTime, int year, int month, int day,
                                  int hour, int minute, String title, String info, LocationInfo locationInfo) {
        boolean isDatePickerShown = false;
        boolean isTimePickerShown = false;
        if (datePickerDialog.isShowing()) {
            isDatePickerShown = true;
            Bundle b = datePickerDialog.onSaveInstanceState();
            outState.putBundle(Tags.DATE_PICKER_OUT_STATE, b);
            datePickerDialog.dismiss();
        } else if (timePickerDialog.isShowing()) {
            isTimePickerShown = true;
            Bundle b1 = timePickerDialog.onSaveInstanceState();
            outState.putBundle(Tags.TIME_PICKER_OUT_STATE, b1);
            timePickerDialog.dismiss();
        }
        outState.putBoolean(Tags.DATE_PICKER_STATUS, isDatePickerShown);
        outState.putBoolean(Tags.TIME_PICKER_STATUS, isTimePickerShown);
        outState.putString(Tags.DATE, pickedDate);
        outState.putString(Tags.PICKED_TIME, pickedTime);
        outState.putString(Tags.EVENT_TITLE, title);
        outState.putString(Tags.EVENT_INFO, info);
        outState.putInt(Tags.EVENT_YEAR, year);
        outState.putInt(Tags.EVENT_MONTH, month);
        outState.putInt(Tags.EVENT_DAY, day);
        outState.putInt(Tags.EVENT_TIME_HOUR, hour);
        outState.putInt(Tags.EVENT_TIME_MINUTES, minute);
        outState.putParcelable(Tags.LOCATION_OBJECT, locationInfo);
    }
}
