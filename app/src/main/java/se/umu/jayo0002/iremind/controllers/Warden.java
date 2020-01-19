package se.umu.jayo0002.iremind.controllers;

import android.content.Context;
import se.umu.jayo0002.iremind.Tags;
import se.umu.jayo0002.iremind.models.date.DateValidator;
import se.umu.jayo0002.iremind.models.text.TextValidator;
import se.umu.jayo0002.iremind.view.Toaster;

/**
 * A content controller class.
 */
public class Warden {

    /**
     * A private field for the Context.
     */
    private final Context mContext;

    /**
     * A constructor, to construct the object and to initializes the context.
     *
     * @param context
     */
    public Warden(Context context) {
        mContext = context;
    }

    /**
     * It takes the context and control if the user input is valid.
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
    public boolean isInputValid(String title, String text, int hour, int minute, int year, int month, int day) {
        boolean val = false;
        if (!DateValidator.isDateValid(hour, minute, year, month, day) &&
                TextValidator.isTitleNotValid(title, Tags.THE_ALLOWED_AMOUNT_FOR_TITLE))
            Toaster.displayToast(mContext, Tags.REQUIRED_INFO, Tags.LONG_TOAST);
        else if (!DateValidator.isDateValid(hour, minute, year, month, day))
            Toaster.displayToast(mContext, Tags.INVALID_DATE, Tags.LONG_TOAST);
        else if (TextValidator.isTitleNotValid(title, Tags.THE_ALLOWED_AMOUNT_FOR_TITLE))
            Toaster.displayToast(mContext, Tags.TITLE_IS_INVALID, Tags.LONG_TOAST);
        else if (!TextValidator.isNoteValid(text, Tags.THE_ALLOWED_AMOUNT_FOR_NOTE))
            Toaster.displayToast(mContext, Tags.NOTE_INVALID_SIZE, Tags.LONG_TOAST);
        else
            val = true;

        return val;
    }
}
