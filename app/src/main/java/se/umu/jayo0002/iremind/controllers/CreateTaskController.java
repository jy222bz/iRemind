package se.umu.jayo0002.iremind.controllers;

import android.widget.Button;
import android.widget.EditText;
import com.google.android.gms.maps.model.LatLng;
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
        if (objectController.isStringValid(address)&& objectController.isObjectValid(latLng)) {
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
    public void setTextForButtons(EditText e1, EditText e2, String s1, String s2){
        if (objectController.isStringValid(s1))
            e1.setText(s1);
        if (objectController.isStringValid(s2))
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
}
