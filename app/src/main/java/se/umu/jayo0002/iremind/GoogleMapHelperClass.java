package se.umu.jayo0002.iremind;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.Task;
import net.yslibrary.android.keyboardvisibilityevent.util.UIUtil;
import java.util.List;
import se.umu.jayo0002.iremind.models.map.GoogleMapHelper;
import se.umu.jayo0002.iremind.view.Toaster;

/**
 * An abstract class handles google map.
 */
public abstract class GoogleMapHelperClass extends FragmentActivity {


    /**
     * It sets the new coordinates.
     * @param latLng
     */
    abstract void updateCoordinates(LatLng latLng);

    /**
     * It send the coordinates upon positive result.
     * @param latLng
     */
    abstract void notifyPositiveSearchResult(LatLng latLng);

    /**
     * It notifies that the search did not meet any known location.
     * @param
     */
    abstract void notifyNegativeSearchResult();

    /**
     * It notifies that it is already permitted.
     */
    abstract void itIsPermitted();


    /**
     * It provides the coordinates of the device current location.
     * @param latLng
     */
    abstract void deviceCoordinates(LatLng latLng);

    /**
     * It notifies the failure of getting the current location.
     */
    abstract void reportFailureOfGettingTheDeviceLocation();

    /**
     * It sets the Map on drag Listener.
     * @param map
     */
    public void setOnDragListener(GoogleMap map) {
        map.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker arg0) {
                /*It is not useful in this context.*/
            }

            @Override
            public void onMarkerDragEnd(Marker arg0) {
                updateCoordinates(new LatLng(arg0.getPosition().latitude, arg0.getPosition().longitude));
            }

            @Override
            public void onMarkerDrag(Marker arg0) {
                /*It is not useful in this context.*/
            }
        });
    }

    /**
     * It requests a permission to use the device location.
     * @return
     */
    public void getPermission() {
        String[] permission = {Manifest.permission.ACCESS_FINE_LOCATION};
        if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this.getApplicationContext(),
                Tags.ACCESS_FINE_LOCATION)) {
            itIsPermitted();
        } else {
            ActivityCompat.requestPermissions(this, permission, Tags.LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    /**
     * It gets the device current location.
     */
    public void getDeviceCurrentLocation() {
        FusedLocationProviderClient provider = LocationServices.getFusedLocationProviderClient(this);
        try {
            final Task<Location> location = provider.getLastLocation();
            location.addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Location currentPosition = task.getResult();
                    if (currentPosition != null)
                        deviceCoordinates(new LatLng(currentPosition.getLatitude(),
                                currentPosition.getLongitude()));
                    else
                        reportFailureOfGettingTheDeviceLocation();
                }
            });
        } catch (Exception e) {
            Toaster.displayToast(this, Tags.NO_LOCATION, Tags.LONG_TOAST);
        }
    }

    /**
     * It sets the edit text on listener to activate the search function.
     * @param editText
     */
    public void setTheSearch(EditText editText){
        editText.setOnEditorActionListener((textView, i, keyEvent) -> {
            boolean val = false;
            if (i == EditorInfo.IME_ACTION_SEARCH
                    || i == EditorInfo.IME_ACTION_DONE
                    || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                    || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER) {
                UIUtil.hideKeyboard(this);
                val = true;
                List<Address> list = GoogleMapHelper.geoCoder(null,
                        this, editText.getText().toString());
                if (!list.isEmpty()) {
                    Address address = list.get(0);
                    notifyPositiveSearchResult(new LatLng(address.getLatitude(), address.getLongitude()));
                } else {
                    notifyNegativeSearchResult();
                }
            }
            editText.getText().clear();
            return val;
        });
    }
}
