package se.umu.jayo0002.iremind.system_controllers;

import android.app.Activity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

/**
 * This class check whether the device supports the Google Map Service.
 * It provides one static method.
 *
 * @author Jacob Yousif
 * @version 1.0
 * @since 2019 -12-09
 * */
public class MapServiceController {

    /**
     * Constructor, creates a new object.
     */
    public MapServiceController(){}


    /**
     * This static method checks whether the user's device supports Google Map Service.
     * @param activity
     * @return boolean
     */
    public static boolean isServiceSupported(Activity activity) {
        return GoogleApiAvailability.getInstance()
                .isGooglePlayServicesAvailable(activity) != ConnectionResult.SUCCESS;
    }
}