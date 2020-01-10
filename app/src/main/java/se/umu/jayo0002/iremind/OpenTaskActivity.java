package se.umu.jayo0002.iremind;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.util.Objects;

import se.umu.jayo0002.iremind.models.Task;
import se.umu.jayo0002.iremind.view.GesturesDetector;
import se.umu.jayo0002.iremind.view.Toaster;

public class OpenTaskActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {
    private Task mTask;
    private boolean mIsGoingMainActivity;
    private LatLng mLatLng;
    Button mGoogleMapDirectionsButton;
    private float mScale = 1f;
    private ScaleGestureDetector mScaleGestureDetector;
    private GestureDetector mGestureDetector;
    private ScrollView mZoomableScrollView;
    private float mX, mY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_task);
        if (savedInstanceState == null && getIntent().hasExtra(Tags.BUNDLE)) {
            Bundle bundle = getIntent().getBundleExtra(Tags.BUNDLE);
            mTask = Objects.requireNonNull(bundle).getParcelable(Tags.TASK);
        } else if (savedInstanceState == null && getIntent().hasExtra(Tags.NEW_LAUNCH)) {
            mIsGoingMainActivity = true;
            mTask = Objects.requireNonNull(getIntent().getExtras()).getParcelable(Tags.NEW_LAUNCH);
        } else if (savedInstanceState != null) {
            updateUI(savedInstanceState);
        }
        mGestureDetector = new GestureDetector(this, new GesturesDetector());
        prepareUI();
        onScale();
    }

    private void onScale() {
        mScaleGestureDetector = new ScaleGestureDetector(this, new ScaleGestureDetector.SimpleOnScaleGestureListener() {
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                float scale = 1 - detector.getScaleFactor();
                float prevScale = mScale;
                mScale += scale;

                if (mScale > 10f)
                    mScale = 10f;

                ScaleAnimation scaleAnimation = new ScaleAnimation(1f / prevScale, 1f / mScale, 1f / prevScale, 1f / mScale, detector.getFocusX(), detector.getFocusY());
                scaleAnimation.setDuration(0);
                scaleAnimation.setFillAfter(true);
                mZoomableScrollView.startAnimation(scaleAnimation);
                return true;
            }
        });
    }

    private void prepareUI() {
        mZoomableScrollView = findViewById(R.id.zoomable_task);
        mX = mZoomableScrollView.getX();
        mY = mZoomableScrollView.getY();
        Button mCLose = findViewById(R.id.close_buttons);
        mGoogleMapDirectionsButton = findViewById(R.id.googleMapButton);
        TextView mTextViewTitle = findViewById(R.id.tvTitles);
        TextView mTextViewInfo = findViewById(R.id.tvMoreInfos);
        Button mButtonDate = findViewById(R.id.btDates);
        Button mButtonTime = findViewById(R.id.btStartingTimes);
        Button mLocation = findViewById(R.id.btLocations);
        mTextViewTitle.setText(mTask.getTitle());
        mButtonDate.setText(mTask.getDate());
        mButtonTime.setText(mTask.getTime());
        mTextViewInfo.setText(mTask.getNote());
        mLocation.setText(mTask.getAddress());
        mLatLng = mTask.getLatLng();
        mCLose.setOnClickListener(this);
        checkLatLng();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(Tags.TASK, mTask);
        outState.putBoolean(Tags.BOOLEAN, mIsGoingMainActivity);
    }

    private void updateUI(Bundle outState) {
        mTask = outState.getParcelable(Tags.TASK);
        mIsGoingMainActivity = outState.getBoolean(Tags.BOOLEAN);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.googleMapButton) {
            onDemandLaunchGoogleMapDirections();
        } else if (view.getId() == R.id.close_buttons) {
            if (!mIsGoingMainActivity)
                this.finish();
            else {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                this.finish();
            }
        }
    }

    public void onBackPressed() {
        if (mIsGoingMainActivity) {
            startActivity(new Intent(this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            this.finish();
        } else
            super.onBackPressed();
    }

    private void onDemandLaunchGoogleMapDirections() {
        Uri navUri = Uri.parse("google.navigation:q=" + mLatLng.latitude + "," +
                mLatLng.longitude);
        Intent googleMapDirections = new Intent(Intent.ACTION_VIEW, navUri);
        googleMapDirections.setPackage(Tags.GOOGLE_MAP_APP);
        try {
            startActivity(googleMapDirections);
            this.finish();
        } catch (ActivityNotFoundException e) {
            Toaster.displayToast(this, Tags.NO_GOOGLE_MAP_APP, Tags.LONG_SNACK);
        }
    }

    private void checkLatLng() {
        if (mLatLng == null) {
            mGoogleMapDirectionsButton.setText(Tags.NON_APPLICABLE);
            mGoogleMapDirectionsButton.setEnabled(false);
        } else {
            mGoogleMapDirectionsButton.setOnClickListener(this);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        super.dispatchTouchEvent(event);
        mScaleGestureDetector.onTouchEvent(event);
        mGestureDetector.onTouchEvent(event);
        return mGestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mZoomableScrollView.setScaleX(mX);
                mZoomableScrollView.setScaleY(mY);
                break;
            case MotionEvent.ACTION_BUTTON_RELEASE:
                v.performClick();
                break;
            default:
                break;
        }
        return true;
    }
}
