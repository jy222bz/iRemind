package se.umu.jayo0002.iremind;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.maps.model.LatLng;
import se.umu.jayo0002.iremind.models.Task;
import se.umu.jayo0002.iremind.view.Toaster;

/**
 * It is a base class for OpenTaskActivity and OneLaunchActivity.
 */
public abstract class BaseOpenTask extends AppCompatActivity {


    /**
     * It launches the Google Map Directions app.
     * @param latLng
     */
    public void onDemandLaunchGoogleMapDirections(LatLng latLng) {
        Uri navUri = Uri.parse("google.navigation:q=" + latLng.latitude + "," +
                latLng.longitude);
        Intent googleMapDirections = new Intent(Intent.ACTION_VIEW, navUri);
        googleMapDirections.setPackage(Tags.GOOGLE_MAP_APP);
        try {
            startActivity(googleMapDirections);
            this.finish();
        } catch (ActivityNotFoundException e) {
            Toaster.displayToast(this, Tags.NO_GOOGLE_MAP_APP, Tags.SHORT_SNACK);
        }
    }

    /**
     * It either disable the button or sets a listener on it.
     * It depends on the LatLng object.
     * @param latLng
     * @param demandMapDirections
     * @param listener
     */
    public void checkLatLng(LatLng latLng, Button demandMapDirections, View.OnClickListener listener) {
        if (latLng == null) {
            demandMapDirections.setText(Tags.NON_APPLICABLE);
            demandMapDirections.setEnabled(false);
        } else {
            demandMapDirections.setOnClickListener(listener);
        }
    }

    /**
     * It sets UI for the Child activities.
     * @param listener
     * @param task
     * @param latLng
     */
    public void prepareTheUI(View.OnClickListener listener, Task task, LatLng latLng) {
        Button demandMapDirections = findViewById(R.id.googleMapButton);
        TextView mTextViewTitle = findViewById(R.id.tvTitles);
        TextView mTextViewInfo = findViewById(R.id.tvMoreInfos);
        Button mButtonDate = findViewById(R.id.btDates);
        Button mButtonTime = findViewById(R.id.btStartingTimes);
        Button mLocation = findViewById(R.id.btLocations);
        mTextViewTitle.setText(task.getTitle());
        mButtonDate.setText(task.getDate());
        mButtonTime.setText(task.getTime());
        mTextViewInfo.setText(task.getNote());
        mLocation.setText(task.getAddress());
        checkLatLng(latLng, demandMapDirections, listener);
    }

    /**
     * It returns the saved task upon out state.
     * @param outState
     * @return
     */
    public Task getTheSavedTask(Bundle outState) {
        return outState.getParcelable(Tags.TASK);
    }
}
