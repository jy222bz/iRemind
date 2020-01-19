package se.umu.jayo0002.iremind;

import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import com.google.android.gms.maps.model.LatLng;
import java.util.Objects;
import se.umu.jayo0002.iremind.models.Task;


public class OneLaunchActivity extends BaseOpenTask implements View.OnClickListener {


    private LatLng mLatLng;
    private Task mTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_task);
        if (getIntent().hasExtra(Tags.BUNDLE_FROM_I_REMIND) && savedInstanceState == null) {
            Bundle bundle = getIntent().getBundleExtra(Tags.BUNDLE_FROM_I_REMIND);
            mTask = Objects.requireNonNull(bundle).getParcelable(Tags.TASK);
        } else if (savedInstanceState != null) {
            mTask = getTheSavedTask(savedInstanceState);
        }
        mLatLng = Objects.requireNonNull(mTask).getLatLng();
        prepareTheUI(this, mTask, mLatLng);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(Tags.TASK, mTask);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.googleMapButton) {
            onDemandLaunchGoogleMapDirections(mLatLng);
        }
    }
}
