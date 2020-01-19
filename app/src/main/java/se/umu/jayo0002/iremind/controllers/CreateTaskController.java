package se.umu.jayo0002.iremind.controllers;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.model.LatLng;

import java.util.Calendar;
import java.util.Objects;

import se.umu.jayo0002.iremind.Tags;
import se.umu.jayo0002.iremind.models.LocationInfo;
import se.umu.jayo0002.iremind.models.model_controllers.ObjectController;

import static se.umu.jayo0002.iremind.Tags.TASK;

/**
 * It is a Controller Class to manages the UI.
 *
 * @author Jacob Yousif
 * @version 1.0
 * @since 2020-01-04
 */
public class CreateTaskController {

    /**
     * A constructor to construct an object.
     */
    private final ObjectController objectController = new ObjectController();

    /**
     * It checks whether the objects are valid.
     * If they are it sets the button.
     *
     * @param button
     * @param latLng
     * @param address
     * @return LocationInfo
     */
    public LocationInfo setLocationInfo(Button button, LatLng latLng, String address, LocationInfo locationInfo) {
        if (objectController.isStringValid(address) && objectController.isObjectValid(latLng)) {
            LocationInfo location = new LocationInfo();
            button.setText(address);
            location.setAddress(address);
            location.setLatLng(latLng);
            return location;
        }
        return locationInfo;
    }

    /**
     * It checks whether the contents are valid.
     * If they are valid, it sets the contents of the EditTexts.
     *
     * @param e1
     * @param e2
     * @param s1
     * @param s2
     */
    public void setTextForEditTexts(EditText e1, EditText e2, String s1, String s2) {
        if (objectController.isStringValid(s1))
            e1.setText(s1);
        if (objectController.isStringValid(s2) && !s2.equals(Tags.NO_INFO))
            e2.setText(s2);
    }

    /**
     * @param dateButton
     * @param timeButton
     * @param date
     * @param time
     */
    public void setDateButtons(Button dateButton, Button timeButton, String date, String time) {
        dateButton.setText(date);
        timeButton.setText(time);
    }

    /**
     * It validates the object and it acts accordingly in terms of setting the button.
     *
     * @param locationButton
     * @param locationInfo
     * @return
     */
    public LocationInfo validateLocationInfo(Button locationButton, LocationInfo locationInfo) {
        if (objectController.isObjectValid(locationInfo))
            locationButton.setText(Objects.requireNonNull(locationInfo).getAddress());
        return locationInfo;
    }

    /**
     * It run the shown the dialog before the out-state if there was any shown.
     *
     * @param outState
     * @param timePickerDialog
     * @param datePickerDialog
     */
    public void runDialogs(Bundle outState, TimePickerDialog timePickerDialog, DatePickerDialog datePickerDialog) {
        if (outState.getBoolean(Tags.TIME_PICKER_STATUS)) {
            timePickerDialog.onRestoreInstanceState(outState.getBundle(Tags.TIME_PICKER_OUT_STATE));
            timePickerDialog.show();
        } else if (outState.getBoolean(Tags.DATE_PICKER_STATUS)) {
            datePickerDialog.onRestoreInstanceState(outState.getBundle(Tags.DATE_PICKER_OUT_STATE));
            datePickerDialog.show();
        }
    }

    /**
     * It checks which time suites the situation.
     * If it is an update operation, then the users time applies.
     * Otherwise, the current time applies.
     *
     * @param hour
     * @param minute
     * @param intent
     * @return
     */
    public int[] getValidTime(int hour, int minute, Intent intent) {
        int[] numbers = new int[2];
        if (intent.hasExtra(TASK)) {
            numbers[0] = hour;
            numbers[1] = minute;

        } else {
            numbers[0] = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
            numbers[1] = Calendar.getInstance().get(Calendar.MINUTE);
        }
        return numbers;
    }
}
