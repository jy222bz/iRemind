package se.umu.jayo0002.iremind;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import net.yslibrary.android.keyboardvisibilityevent.util.UIUtil;
import java.util.Calendar;
import java.util.Objects;
import se.umu.jayo0002.iremind.controllers.CreateTaskController;
import se.umu.jayo0002.iremind.controllers.CreateTaskHelper;
import se.umu.jayo0002.iremind.models.LocationInfo;
import se.umu.jayo0002.iremind.models.text.StringFormatter;
import se.umu.jayo0002.iremind.models.Task;
import se.umu.jayo0002.iremind.models.model_controllers.ObjectController;


import static se.umu.jayo0002.iremind.Tags.TASK;

public class CreateTaskActivity extends HelperBase implements View.OnClickListener,
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private Button mButtonAddDate, mButtonAddLocation, mButtonAddStartTime;
    private EditText mEventTitle, mMoreInfo;
    private DatePickerDialog mDateDialog;
    private TimePickerDialog mTimeDialog;
    private int mYear, mMonth, mDay, mStartHour, mStartMinute;
    private LocationInfo mLocationInfo;
    private String mTitle, mEvent, mPickedDate, mPickedTime;
    private CreateTaskHelper mCreateTaskHelper;
    private ObjectController mObjectController;
    private Task mTask;
    private CreateTaskController mCreateTaskController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        mCreateTaskController = new CreateTaskController();
        mObjectController = new ObjectController();
        prepareUI();
        if (getIntent().hasExtra(TASK) && !mObjectController.isObjectValid(savedInstanceState))
            update();
        else if (mObjectController.isObjectValid(savedInstanceState))
            updateTheStateOfUI(savedInstanceState);
        checkMapService(mButtonAddLocation);
        mDateDialog.setOnCancelListener(dialogInterface -> {
            mDateDialog.dismiss();
            mDateDialog = getDateDialog(mYear, mMonth, mDay, 6, this);
        });
        mTimeDialog.setOnCancelListener(dialogInterface -> {
            mTimeDialog.dismiss();
            mTimeDialog = getTimeDialog(mStartHour, mStartMinute, this);
        });
    }

    @Override
    public void onClick(View view) {
        UIUtil.hideKeyboard(this);
        if (view.getId() == R.id.save_button)
            onSave();
        else if (view.getId() == R.id.btDate)
            mDateDialog.show();
        else if (view.getId() == R.id.btStartingTime)
            mTimeDialog.show();
        else if (view.getId() == R.id.btLocation) {
            Intent maps = new Intent(this, MapsActivity.class);
            if (mObjectController.isObjectValid(mLocationInfo))
                maps.putExtra(Tags.LAT_LNG, mLocationInfo.getLatLng());
            startActivityForResult(maps, Tags.REQUEST_CODE_MAP);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Tags.REQUEST_CODE_MAP && resultCode == RESULT_OK) {
            mLocationInfo = Objects.requireNonNull(data).getParcelableExtra(Tags.LOCATION_OBJECT);
            if (mObjectController.isObjectValid(mLocationInfo))
                mButtonAddLocation.setText(mLocationInfo.getAddress());
        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        mYear = year;
        mMonth = month;
        mDay = day;
        mPickedDate = StringFormatter.getFormattedDate(year, (month + 1), day);
        mButtonAddDate.setText(mPickedDate);
        mDateDialog.dismiss();
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        mStartHour = hour;
        mStartMinute = minute;
        mPickedTime = StringFormatter.getFormattedTime(hour, minute);
        mButtonAddStartTime.setText(mPickedTime);
        mTimeDialog.dismiss();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        mCreateTaskController.setOutStateBundle(outState, mDateDialog,
                mTimeDialog, mPickedDate, mPickedTime,
                mYear, mMonth, mDay, mStartHour, mStartMinute, mTitle, mEvent, mLocationInfo);
        super.onSaveInstanceState(outState);
    }

    private void update() {
        mTask = Objects.requireNonNull(getIntent().getExtras()).getParcelable(TASK);
        if (mObjectController.isObjectValid(mTask)) {
            mTitle = mTask.getTitle();
            mEvent = mTask.getNote();
            mYear = mTask.getYear();
            mMonth = mTask.getMonth();
            mDay = mTask.getDay();
            mStartHour = mTask.getHour();
            mStartMinute = mTask.getMinute();
            mPickedDate = mTask.getDate();
            mPickedTime = mTask.getTime();
            mCreateTaskController.setDateButtons(mButtonAddDate, mButtonAddStartTime, mPickedDate, mPickedTime);
            mCreateTaskController.setTextForEditTexts(mEventTitle, mMoreInfo, mTitle, mEvent);
            mDateDialog = getDateDialog(mYear, mMonth, mDay, 6, this);
            mTimeDialog = getTimeDialog(mStartHour, mStartMinute, this);
            if (mCreateTaskController.setLocationButton(mButtonAddLocation, mTask.getLatLng(), mTask.getAddress()))
                mLocationInfo = mCreateTaskController.getLocationInfo(mTask.getLatLng(), mTask.getAddress());
        }
    }

    private void updateTheStateOfUI(Bundle outState) {
        if (mObjectController.isObjectValid(outState.getParcelable(Tags.LOCATION_OBJECT))) {
            mLocationInfo = outState.getParcelable(Tags.LOCATION_OBJECT);
            mButtonAddLocation.setText(Objects.requireNonNull(mLocationInfo).getAddress());
        }
        mPickedDate = outState.getString(Tags.DATE);
        mPickedTime = outState.getString(Tags.PICKED_TIME);
        mTitle = outState.getString(Tags.EVENT_TITLE);
        mEvent = outState.getString(Tags.EVENT_INFO);
        mYear = outState.getInt(Tags.EVENT_YEAR);
        mMonth = outState.getInt(Tags.EVENT_MONTH);
        mDay = outState.getInt(Tags.EVENT_DAY);
        mStartHour = outState.getInt(Tags.EVENT_TIME_HOUR);
        mStartMinute = outState.getInt(Tags.EVENT_TIME_MINUTES);
        mCreateTaskController.setDateButtons(mButtonAddDate, mButtonAddStartTime, mPickedDate, mPickedTime);
        mCreateTaskController.setTextForEditTexts(mEventTitle, mMoreInfo, mTitle, mEvent);
        if (outState.getBoolean(Tags.TIME_PICKER_STATUS)) {
            mTimeDialog.onRestoreInstanceState(outState.getBundle(Tags.TIME_PICKER_OUT_STATE));
            mTimeDialog.show();
        } else if (outState.getBoolean(Tags.DATE_PICKER_STATUS)) {
            mDateDialog.onRestoreInstanceState(outState.getBundle(Tags.DATE_PICKER_OUT_STATE));
            mDateDialog.show();
        }
    }

    private void onSave() {
        if (mCreateTaskHelper.areContentsValid(mTitle, mEvent, mStartHour, mStartMinute, mYear, mMonth, mDay)) {
            Intent main = new Intent();
            main.putExtra(TASK, mCreateTaskHelper.getTask(mTitle, mEvent, mStartHour, mStartMinute,
                    mYear, mMonth, mDay, mLocationInfo, mTask));
            setResult(RESULT_OK, main);
            this.finish();
        }
    }

    private void prepareUI() {
        mEvent = "";
        mCreateTaskHelper = new CreateTaskHelper(this);
        mPickedDate = getString(R.string.date);
        mPickedTime = getString(R.string.starting_time);
        setSaveButton(this);
        mButtonAddDate = getDateButton(this);
        mButtonAddLocation = getLocationButton(this);
        mButtonAddStartTime = getTimeButton(this);
        mEventTitle = getTitleEditText();
        mMoreInfo = getInfoEditText();
        mStartHour =Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        mStartMinute = Calendar.getInstance().get(Calendar.MINUTE);
        mTimeDialog = getTimeDialog(mStartHour,
                mStartMinute, this);
        mDateDialog = getDateDialog(mYear, mMonth, mDay, 6, this);
    }

    @Override
    void setTitle(String title) {
        mTitle = title;
    }

    @Override
    void setInfo(String info) {
        mEvent = info;
    }
}