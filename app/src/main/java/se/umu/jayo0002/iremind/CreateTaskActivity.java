package se.umu.jayo0002.iremind;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.google.android.gms.maps.model.LatLng;

import java.util.Calendar;
import java.util.Objects;

import se.umu.jayo0002.iremind.controllers.ContentController;
import se.umu.jayo0002.iremind.models.Date;
import se.umu.jayo0002.iremind.models.LocationInfo;
import se.umu.jayo0002.iremind.models.StringFormatter;
import se.umu.jayo0002.iremind.models.Task;
import se.umu.jayo0002.iremind.models.model_controllers.ObjectController;
import se.umu.jayo0002.iremind.system_controllers.MapServiceController;

import static se.umu.jayo0002.iremind.Tags.TASK;

public class CreateTaskActivity extends AppCompatActivity implements View.OnClickListener,
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private Button mButtonAddDate;
    private Button mButtonAddLocation;
    private Button mButtonAddStartTime;
    private EditText mEventTitle, mMoreInfo;
    private DatePickerDialog mDateDialog;
    private TimePickerDialog mTimeDialog;
    private int mYear, mMonth, mDay;
    private int mStartHour, mStartMinute, mReminder;
    private LocationInfo mLocationInfo;
    private String mTitle, mEvent, mPickedDate, mPickedTime;
    private boolean mIsDatePickerShown, mIsTimePickerShown;
    private ContentController mController;
    private ObjectController mObjectController;
    private Task mTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        mObjectController = new ObjectController();
        prepareUI();
        if (getIntent().hasExtra(TASK) && !mObjectController.isObjectValid(savedInstanceState))
            update();
        else if (mObjectController.isObjectValid(savedInstanceState))
            updateTheStateOfUI(savedInstanceState);
        checkService();
        mDateDialog.setOnCancelListener(dialogInterface -> mIsDatePickerShown = false);
        mTimeDialog.setOnCancelListener(dialogInterface -> mIsTimePickerShown = false);
    }

    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            String str = editable.toString();
            if (editable == mEventTitle.getEditableText())
                mTitle = str;
            else if (editable == mMoreInfo.getEditableText())
                mEvent = str;
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.close_button:
                destroyActivity();
                break;
            case R.id.save_button:
                onSave();
                break;
            case R.id.btDate:
                mIsDatePickerShown = true;
                mDateDialog.show();
                break;
            case R.id.btStartingTime:
                mIsTimePickerShown = true;
                mTimeDialog.show();
                break;
            case R.id.btLocation:
                Intent maps = new Intent(this, MapsActivity.class);
                if (mObjectController.isObjectValid(mLocationInfo))
                    maps.putExtra(Tags.LAT_LNG, mLocationInfo.getLatLng());
                startActivityForResult(maps, Tags.REQUEST_CODE_MAP);
                break;
            default:
                break;
        }
    }

    private void checkService() {
        if (MapServiceController.isServiceSupported(this)) {
            mButtonAddLocation.setEnabled(false);
            mButtonAddLocation.setText(R.string.not_supported);
        }
    }

    public void onBackPressed() {
        destroyActivity();
    }

    private void destroyActivity() {
        Intent main = new Intent();
        setResult(RESULT_CANCELED, main);
        this.finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Tags.REQUEST_CODE_MAP && resultCode == RESULT_OK) {
            mLocationInfo = Objects.requireNonNull(data).getParcelableExtra(Tags.LOCATION_OBJECT);
            if(mObjectController.isObjectValid(mLocationInfo))
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
        mIsDatePickerShown = false;
        mDateDialog.dismiss();
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        mStartHour = hour;
        mStartMinute = minute;
        mPickedTime = StringFormatter.getFormattedTime(hour, minute);
        mButtonAddStartTime.setText(mPickedTime);
        mIsTimePickerShown = false;
        mTimeDialog.dismiss();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean(Tags.DATE_PICKER_STATUS, mIsDatePickerShown);
        outState.putBoolean(Tags.TIME_PICKER_STATUS, mIsTimePickerShown);
        outState.putString(Tags.DATE, mPickedDate);
        outState.putString(Tags.PICKED_TIME, mPickedTime);
        outState.putString(Tags.EVENT_TITLE, mTitle);
        outState.putString(Tags.EVENT_INFO, mEvent);
        outState.putInt(Tags.EVENT_YEAR, mYear);
        outState.putInt(Tags.EVENT_MONTH, mMonth);
        outState.putInt(Tags.EVENT_DAY, mDay);
        outState.putInt(Tags.EVENT_TIME_HOUR, mStartHour);
        outState.putInt(Tags.EVENT_TIME_MINUTES, mStartMinute);
        outState.putParcelable(Tags.LOCATION_OBJECT, mLocationInfo);
        if (mIsDatePickerShown)
            mDateDialog.dismiss();
        else if (mIsTimePickerShown)
            mTimeDialog.dismiss();
        super.onSaveInstanceState(outState);
    }

    private void update() {
        mTask = Objects.requireNonNull(getIntent().getExtras()).getParcelable(TASK);
        if (mObjectController.isObjectValid(mTask)){
            mTitle = mTask.getTitle();
            mEvent = mTask.getNote();
            mYear = mTask.getYear();
            mMonth = mTask.getMonth();
            mDay = mTask.getDay();
            mStartHour = mTask.getHour();
            mStartMinute = mTask.getMinute();
            mPickedDate = mTask.getDate();
            mPickedTime = mTask.getTime();
            mButtonAddDate.setText(mPickedDate);
            mButtonAddStartTime.setText(mPickedTime);
            mEventTitle.setText(mTitle);
            checkLatLng(mTask.getLatLng(), mTask.getAddress());
        }
    }

    private void updateTheStateOfUI(Bundle outState) {
        if (mObjectController.isObjectValid(outState.getParcelable(Tags.LOCATION_OBJECT))) {
            mLocationInfo = outState.getParcelable(Tags.LOCATION_OBJECT);
            mButtonAddLocation.setText(Objects.requireNonNull(mLocationInfo).getAddress());
        }
        mIsTimePickerShown = outState.getBoolean(Tags.TIME_PICKER_STATUS);
        mIsDatePickerShown = outState.getBoolean(Tags.DATE_PICKER_STATUS);
        mPickedDate = outState.getString(Tags.DATE);
        mPickedTime = outState.getString(Tags.PICKED_TIME);
        mTitle = outState.getString(Tags.EVENT_TITLE);
        mEvent = outState.getString(Tags.EVENT_INFO);
        mYear = outState.getInt(Tags.EVENT_YEAR);
        mMonth = outState.getInt(Tags.EVENT_MONTH);
        mDay = outState.getInt(Tags.EVENT_DAY);
        mStartHour = outState.getInt(Tags.EVENT_TIME_HOUR);
        mStartMinute = outState.getInt(Tags.EVENT_TIME_MINUTES);
        mButtonAddDate.setText(mPickedDate);
        mButtonAddStartTime.setText(mPickedTime);
        checkStrings();
        if (mIsTimePickerShown)
            mTimeDialog.show();
        else if (mIsDatePickerShown)
            mDateDialog.show();
    }

    private void onSave() {
        if (mController.areContentsValid(mTitle, mEvent, mStartHour, mStartMinute, mYear, mMonth, mDay)) {
            Intent main = new Intent();
            main.putExtra(TASK, mController.getTask(mTitle, mEvent, mStartHour, mStartMinute,
                    mYear, mMonth, mDay, mLocationInfo, mTask));
            setResult(RESULT_OK, main);
            this.finish();
        }
    }

    private void prepareUI() {
        mEvent = "";
        mController = new ContentController(this);
        mPickedDate = Date.getFullDate();
        mPickedTime = getString(R.string.starting_time);
        Button mButtonClose = findViewById(R.id.close_button);
        mButtonClose.setOnClickListener(this);
        Button mButtonSave = findViewById(R.id.save_button);
        mButtonSave.setOnClickListener(this);
        mButtonAddDate = findViewById(R.id.btDate);
        mButtonAddDate.setOnClickListener(this);
        mButtonAddLocation = findViewById(R.id.btLocation);
        mButtonAddLocation.setOnClickListener(this);
        mButtonAddStartTime = findViewById(R.id.btStartingTime);
        mButtonAddStartTime.setOnClickListener(this);
        mEventTitle = findViewById(R.id.tvTitle);
        mMoreInfo = findViewById(R.id.tvMoreInfo);
        prepareDialogs();
        mMoreInfo.addTextChangedListener(textWatcher);
        mEventTitle.addTextChangedListener(textWatcher);
    }

    private void prepareDialogs() {
        mDateDialog = new DatePickerDialog(
                CreateTaskActivity.this,
                R.style.PickerTheme, this, mYear, mMonth, mDay);
        mDateDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        mDateDialog.getDatePicker().setMaxDate(Date.getPlus(6));
        mTimeDialog = new TimePickerDialog(CreateTaskActivity.this,
                R.style.PickerTheme, this,
                Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                Calendar.getInstance().get(Calendar.MINUTE), false);
    }

    private void checkLatLng(LatLng latLng, String address) {
        if (mObjectController.isObjectValid(address) && mObjectController.isObjectValid(latLng)) {
            mLocationInfo = new LocationInfo();
            mLocationInfo.setLatLng(latLng);
            mLocationInfo.setAddress(address);
            mButtonAddLocation.setText(address);
        }
    }

    private void checkStrings() {
        if (mObjectController.isObjectValid(mEvent) && !mEvent.isEmpty())
            mMoreInfo.setText(mEvent);
        if (mObjectController.isObjectValid(mTitle)&& !mTitle.isEmpty())
            mEventTitle.setText(mTitle);
    }
}
