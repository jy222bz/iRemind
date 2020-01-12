package se.umu.jayo0002.iremind.models.map;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import com.google.android.gms.maps.model.LatLng;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import se.umu.jayo0002.iremind.Tags;

/**
 * A class help to geo code locations.
 */
public class GoogleMapHelper {

    /**
     * It geo codes in reverse order, i.e. it takes the LatLng and transferring it to list of addresses.
     * When the LatLng is null, it uses the name of the location to transfer it to list of addresses.
     *
     * @param latLng
     * @param context
     * @param address
     * @return List<Address>
     */
    public static List<Address> geoCoder(LatLng latLng, Context context, String address){
        Geocoder geocoder = new Geocoder(context);
        List<Address> list = new ArrayList<>();
        if (latLng == null){
            try{
                list = geocoder.getFromLocationName(address, Tags.MAX_AMOUNT_OF_ADDRESSES);
            }catch (IOException ignored){
            }
        } else {
            try {
                list = geocoder.getFromLocation(latLng.latitude,
                        latLng.longitude, Tags.MAX_AMOUNT_OF_ADDRESSES);
            } catch (IOException ignored) {
            }
        }
        return list;
    }
}
