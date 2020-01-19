package se.umu.jayo0002.iremind.controllers;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import se.umu.jayo0002.iremind.Tags;

public class Runner {

    private Runner(){}


    public static void runDialogsAfterStateOut(Bundle outState, TimePickerDialog timePickerDialog, DatePickerDialog datePickerDialog){
        if (outState.getBoolean(Tags.TIME_PICKER_STATUS)) {
            timePickerDialog.onRestoreInstanceState(outState.getBundle(Tags.TIME_PICKER_OUT_STATE));
            timePickerDialog.show();
        } else if (outState.getBoolean(Tags.DATE_PICKER_STATUS)) {
            datePickerDialog.onRestoreInstanceState(outState.getBundle(Tags.DATE_PICKER_OUT_STATE));
            datePickerDialog.show();
        }
    }

}
