package se.umu.jayo0002.iremind.controllers;

import android.content.Context;
import se.umu.jayo0002.iremind.Tags;
import se.umu.jayo0002.iremind.models.date.DateValidator;
import se.umu.jayo0002.iremind.models.LocationInfo;
import se.umu.jayo0002.iremind.models.Task;
import se.umu.jayo0002.iremind.models.text.TextValidator;
import se.umu.jayo0002.iremind.models.model_controllers.ObjectController;
import se.umu.jayo0002.iremind.view.Toaster;

/**
 * A content controller class.
 */
public class CreateTaskHelper {

    /**
     * A private field for the Context.
     */
    private final Context mContext;

    /**
     * A constructor, to construct the object and to initializes the context.
     * @param context
     */
    public CreateTaskHelper(Context context) {
        mContext = context;
    }

    /**
     * It takes the context and control if they are valid.
     * If they are not valid it displays a toast message with clarification.
     *
     * @param title
     * @param text
     * @param hour
     * @param minute
     * @param year
     * @param month
     * @param day
     * @return boolean
     */
    public boolean areContentsValid(String title, String text, int hour, int minute, int year, int month, int day){
        boolean val = false;
        if (!DateValidator.isDateValid(hour, minute, year, month, day))
            Toaster.displayToast(mContext, Tags.INVALID_DATE, Tags.LONG_TOAST);
        else if (!TextValidator.isTitleValid(title, Tags.THE_ALLOWED_AMOUNT_FOR_TITLE))
            Toaster.displayToast(mContext, Tags.TITLE_IS_INVALID, Tags.LONG_TOAST);
        else if (!TextValidator.isNoteValid(text, Tags.THE_ALLOWED_AMOUNT_FOR_NOTE))
            Toaster.displayToast(mContext, Tags.NOTE_INVALID_SIZE, Tags.LONG_TOAST);
        else
            val = true;

        return val;
    }

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
    public Task getTask(String title, String text, int hour, int minute, int year, int month,
                        int day, LocationInfo locationInfo, Task task){
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
}
