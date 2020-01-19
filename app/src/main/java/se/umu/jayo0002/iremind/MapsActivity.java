package se.umu.jayo0002.iremind;

import androidx.annotation.NonNull;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.List;
import java.util.Objects;
import se.umu.jayo0002.iremind.models.map.GoogleMapHelper;
import se.umu.jayo0002.iremind.models.LocationInfo;
import se.umu.jayo0002.iremind.view.Toaster;

public class MapsActivity extends GoogleMapHelperClass implements OnMapReadyCallback {

    private GoogleMap mMap;
    private boolean mPermitted = false;
    private EditText mSearchBar;
    private ImageView mMyLocation;
    private LatLng mLatLng;
    private boolean mIsItOutState;
    private Marker mMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        prepareUI();
        if (savedInstanceState != null)
            updateState(savedInstanceState);
        else if (!mPermitted)
            getPermission();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment == null) throw new AssertionError();
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMarker = mMap.addMarker(new MarkerOptions()
                .position(mLatLng)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .draggable(true));
        doCheckTheConditions();
        mMap.setOnCameraMoveListener(() -> mLatLng = mMap.getCameraPosition().target);
        mMap.setOnCameraIdleListener(() -> mMarker.setPosition(mLatLng));
        setTheSearch(mSearchBar);
        onBackToMyLocation();
        setOnDragListener(mMap);
        onLongClick();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == Tags.LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {
                for (int grantResult : grantResults) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED) return;
                }
            }
            mPermitted = true;
            doCheckTheConditions();
        }
        Toaster.displayToast(this, Tags.ADDRESS_GUIDE, Tags.LONG_TOAST);
    }

    private void onCallMoveToLocation(LatLng latLng) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, Tags.MAP_ZOOM));
        mLatLng = latLng;
        mMarker.setPosition(mLatLng);
    }

    private void onBackToMyLocation() {
        mMyLocation.setOnClickListener(view -> getDeviceCurrentLocation());
    }

    private void onLongClick() {
        mMap.setOnMapLongClickListener(latLng -> {
            List<Address> list = GoogleMapHelper.geoCoder(latLng,
                    this, mSearchBar.getText().toString());
            LocationInfo locationInfo = new LocationInfo();
            locationInfo.setLatLng(latLng);
            locationInfo.setAddress(list.get(0).getAddressLine(0));
            Intent backToCreateEvents = new Intent();
            backToCreateEvents.putExtra(Tags.LOCATION_OBJECT, locationInfo);
            setResult(RESULT_OK, backToCreateEvents);
            this.finish();
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(Tags.LAT_LNG_OUT_STATE, mLatLng);
        outState.putBoolean(Tags.STATE_CONDITION, true);
        outState.putBoolean(Tags.PERMISSION_STATE, mPermitted);
    }

    private void updateState(Bundle savedInstanceState) {
        mLatLng = savedInstanceState.getParcelable(Tags.LAT_LNG_OUT_STATE);
        mIsItOutState = savedInstanceState.getBoolean(Tags.STATE_CONDITION);
        mPermitted = savedInstanceState.getBoolean(Tags.PERMISSION_STATE);
    }

    private void doCheckTheConditions() {
        if (mPermitted) {
            mMyLocation.setVisibility(View.VISIBLE);
            mMyLocation.setEnabled(true);
        }
        if (getIntent().hasExtra(Tags.LAT_LNG) && !mIsItOutState) {
            mLatLng = Objects.requireNonNull(getIntent().getExtras()).getParcelable(Tags.LAT_LNG);
            onCallMoveToLocation(mLatLng);
        } else if (mPermitted && !mIsItOutState) {
            getDeviceCurrentLocation();
        } else if (mIsItOutState) {
            onCallMoveToLocation(mLatLng);
            mIsItOutState = false;
        }
    }

    private void prepareUI() {
        mSearchBar = findViewById(R.id.search_map);
        mSearchBar.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        mMyLocation = findViewById(R.id.to_my_location);
        mMyLocation.setVisibility(View.INVISIBLE);
        mMyLocation.setEnabled(false);
        mIsItOutState = false;
        mLatLng = new LatLng(0, 0);
    }

    @Override
    void updateCoordinates(LatLng latLng) {
        mLatLng = latLng;
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    @Override
    void notifyPositiveSearchResult(LatLng latLng) {
        mLatLng = latLng;
        onCallMoveToLocation(latLng);
    }

    @Override
    void notifyNegativeSearchResult() {
        Toaster.displayToast(this, Tags.NO_LOCATION_FOUND, Tags.LONG_TOAST);
    }

    @Override
    void itIsPermitted() {
        mPermitted = true;
    }

    @Override
    void deviceCoordinates(LatLng latLng) {
        onCallMoveToLocation(latLng);
    }

    @Override
    void reportFailureOfGettingTheDeviceLocation() {
        Toaster.displayToast(this, Tags.NO_LOCATION, Tags.LONG_TOAST);
    }
}