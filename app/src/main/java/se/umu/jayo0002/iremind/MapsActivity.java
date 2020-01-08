package se.umu.jayo0002.iremind;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import se.umu.jayo0002.iremind.models.LocationInfo;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

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
        mSearchBar = findViewById(R.id.search_map);
        mMyLocation = findViewById(R.id.to_my_location);
        mMyLocation.setVisibility(View.INVISIBLE);
        mMyLocation.setEnabled(false);
        mIsItOutState = false;
        mLatLng = new LatLng(0, 0);

        if (savedInstanceState !=null)
            updateUI(savedInstanceState);
        else if (!mPermitted)
            requestPermission();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment == null) throw new AssertionError();
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMarker=  mMap.addMarker(new MarkerOptions()
                .position(mLatLng)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .draggable(true));
        if (mPermitted){
            mMyLocation.setVisibility(View.VISIBLE);
            mMyLocation.setEnabled(true);
        } if (getIntent().hasExtra(Tags._LAT_LNG) && !mIsItOutState){
            mLatLng = Objects.requireNonNull(getIntent().getExtras()).getParcelable(Tags._LAT_LNG);
            onCallMoveToLocation(mLatLng);
        }  else if (mPermitted && !mIsItOutState){
            getCurrentLocation();
        } else if (mIsItOutState) {
            onCallMoveToLocation(mLatLng);
            mIsItOutState = false;
        }
        mMap.setOnCameraMoveListener(() -> mLatLng = mMap.getCameraPosition().target);
        mMap.setOnCameraIdleListener(() -> mMarker.setPosition(mLatLng));

        onSearch();
        onBackToMyLocation();
        onDrag();
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
            mMyLocation.setVisibility(View.VISIBLE);
            getCurrentLocation();
        }
    }

    private void getCurrentLocation(){
        FusedLocationProviderClient locationProvider = LocationServices.getFusedLocationProviderClient(this);
        try {
            Task<Location> location = locationProvider.getLastLocation();
            location.addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    Location currentLocation = task.getResult();
                    assert currentLocation != null;
                    onCallMoveToLocation(new LatLng(currentLocation.getLatitude(),
                            currentLocation.getLongitude()));
                    mLatLng = new LatLng(currentLocation.getLatitude(),
                            currentLocation.getLongitude());
                }
            });

        } catch (Exception e) {
            Toast.makeText(this, Tags.NO_LOCATION, Toast.LENGTH_SHORT).show();
        }
    }

    private void onCallMoveToLocation(LatLng latLng){
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,Tags.MAP_ZOOM));
        mLatLng = latLng;
        mMarker.setPosition(mLatLng);
    }

    private void onSearch(){
        mSearchBar.setOnEditorActionListener((textView, i, keyEvent) -> {
            if(i == EditorInfo.IME_ACTION_SEARCH
                    || i == EditorInfo.IME_ACTION_DONE
                    || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                    || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER){
                List<Address> list = geoCoder(null);
                if(list.size() >0){
                    Address address = list.get(0);
                    mLatLng = new LatLng(address.getLatitude(), address.getLongitude());
                    onCallMoveToLocation(new LatLng(address.getLatitude(), address.getLongitude()));
                }
            }
            mSearchBar.getText().clear();
            return false;
        });
    }

    private void onBackToMyLocation(){
        mMyLocation.setOnClickListener(view -> getCurrentLocation());
    }

    private List<Address> geoCoder(LatLng latLng){
        Geocoder geocoder = new Geocoder(MapsActivity.this);
        List<Address> list = new ArrayList<>();
        if (latLng == null){
            String str = mSearchBar.getText().toString();
            try{
                list = geocoder.getFromLocationName(str, 1);
            }catch (IOException ignored){
            }
        } else {
            try {
                list = geocoder.getFromLocation(latLng.latitude,
                        latLng.longitude, 1);
            } catch (IOException ignored) {
            }
        }
        return list;
    }

    private void onLongClick(){
        mMap.setOnMapLongClickListener(latLng -> {
            List<Address> list = geoCoder(latLng);
            LocationInfo locationInfo = new LocationInfo();
            locationInfo.setLatLng(latLng);
            locationInfo.setAddress(list.get(0).getAddressLine(0));
            Toast.makeText(this, list.get(0).getAddressLine(0), Toast.LENGTH_SHORT).show();
            Intent back_to_create_events = new Intent();
            back_to_create_events.putExtra(Tags.LOCATION_OBJECT, locationInfo);
            setResult(RESULT_OK, back_to_create_events);
            finish();
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mIsItOutState = true;
        outState.putParcelable(Tags.LAT_LNG_OUT_STATE, mLatLng);
        outState.putBoolean(Tags.STATE_CONDITION, mIsItOutState);
        outState.putBoolean(Tags.PERMISSION_STATE, mPermitted);
    }

    public void onBackPressed() {
        Intent back_to_create_events = new Intent();
        setResult(RESULT_CANCELED, back_to_create_events);
        finish();
    }

    private void onDrag() {
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker arg0) {
            }

            @Override
            public void onMarkerDragEnd(Marker arg0) {
                mMap.animateCamera(CameraUpdateFactory.newLatLng(arg0.getPosition()));
                mLatLng = new LatLng(arg0.getPosition().latitude, arg0.getPosition().longitude);
            }

            @Override
            public void onMarkerDrag(Marker arg0) {
            }
        });
    }

    private void updateUI(Bundle savedInstanceState) {
        mLatLng = savedInstanceState.getParcelable(Tags.LAT_LNG_OUT_STATE);
        mIsItOutState = savedInstanceState.getBoolean(Tags.STATE_CONDITION);
        mPermitted = savedInstanceState.getBoolean(Tags.PERMISSION_STATE);
    }

    private void requestPermission(){
        String[] permission = {Manifest.permission.ACCESS_FINE_LOCATION};
        if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this.getApplicationContext(),
                Tags.ACCESS_FINE_LOCATION)){ mPermitted = true;
        } else {
            ActivityCompat.requestPermissions(this, permission, Tags.LOCATION_PERMISSION_REQUEST_CODE);
        }
    }
}