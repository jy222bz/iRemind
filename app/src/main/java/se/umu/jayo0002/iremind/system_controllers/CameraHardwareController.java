package se.umu.jayo0002.iremind.system_controllers;

import android.content.Context;
import android.content.pm.PackageManager;

class CameraHardwareController {

    public static boolean hasCameraHardware(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }
}
