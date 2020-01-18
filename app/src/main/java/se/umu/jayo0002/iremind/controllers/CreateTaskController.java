package se.umu.jayo0002.iremind.controllers;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.model.LatLng;

import se.umu.jayo0002.iremind.Tags;
import se.umu.jayo0002.iremind.models.LocationInfo;
import se.umu.jayo0002.iremind.models.model_controllers.ObjectController;

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
     * @return boolean
     */
    public boolean setLocationButton(Button button, LatLng latLng, String address) {
        if (objectController.isStringValid(address) && objectController.isObjectValid(latLng)) {
            button.setText(address);
            return true;
        }
        return false;
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
     * It returns a LocationInfo Object
     *
     * @return boolean
     */
    public LocationInfo getLocationInfo(LatLng latLng, String address) {
        LocationInfo locationInfo = new LocationInfo();
        locationInfo.setLatLng(latLng);
        locationInfo.setAddress(address);
        return locationInfo;
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
    public void setOutStateBundle(Bundle outState, DatePickerDialog datePickerDialog, TimePickerDialog timePickerDialog,
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

    public void setDateButtons(Button dateButton, Button timeButton, String date, String time) {
        dateButton.setText(date);
        timeButton.setText(time);
    }
}
