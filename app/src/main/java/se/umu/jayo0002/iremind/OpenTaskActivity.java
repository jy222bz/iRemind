package se.umu.jayo0002.iremind;

import androidx.annotation.NonNull;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.google.android.gms.maps.model.LatLng;
import java.util.Objects;
import se.umu.jayo0002.iremind.models.Task;


public class OpenTaskActivity extends BaseOpenTask implements View.OnClickListener {

    private Task mTask;
    private LatLng mLatLng;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_task);
        if (savedInstanceState == null && getIntent().hasExtra(Tags.TASK_LAUNCHED_FROM_AN_ACTIVITY)) {
            mTask = Objects.requireNonNull(getIntent().getExtras()).getParcelable(Tags.TASK_LAUNCHED_FROM_AN_ACTIVITY);
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

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        this.finish();
    }
}
