package se.umu.jayo0002.iremind.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import se.umu.jayo0002.iremind.models.exceptions.Classification;
import se.umu.jayo0002.iremind.models.exceptions.ExceptionBuilder;


/**
 * This class represents an object that stores information about a location.
 * It provides four methods.
 * It implements the Interface Parcelable, to serialize it.
 *
 * @author Jacob Yousif
 * @version 1.0
 * @since 2019 -12-09
 * */

public class LocationInfo implements Parcelable {


    /**
     * This is a private field that holds the address of the location.
     */
    private String address;

    /**
     * This is a private field that holds the coordinates of the location.
     */
    private LatLng latLng;

    /**
     * Constructor, creates a new LocationInfo object.
     */
    public LocationInfo() {

    }

    /**
     * It sets the address of the locations.
     * @param address
     */
    public void setAddress(String address){
            this.address = address;
    }

    /**
     * It sets the coordinates of the location.
     * @throws Exception if the object null.
     * @param latLng
     */
    public void setLatLng(LatLng latLng){
        if (latLng == null)
            throw new ExceptionBuilder(Classification.LAT_LNG_NULL);
        else
            this.latLng = latLng;
    }

    /**
     * It returns the address of the location.
     * @return String
     */
    public String getAddress(){
        return this.address;
    }

    /**
     * It returns the coordinates of the location.
     * @return LatLng
     */
    public LatLng getLatLng(){
        return this.latLng;
    }


    private LocationInfo(Parcel in) {
        address = in.readString();
        latLng = in.readParcelable(LatLng.class.getClassLoader());
    }

    public static final Creator<LocationInfo> CREATOR = new Creator<LocationInfo>() {
        @Override
        public LocationInfo createFromParcel(Parcel in) {
            return new LocationInfo(in);
        }

        @Override
        public LocationInfo[] newArray(int size) {
            return new LocationInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(address);
        parcel.writeParcelable(latLng, i);
    }
}
