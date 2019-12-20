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
import android.widget.Toast;
import com.google.android.gms.maps.model.LatLng;
import java.util.Calendar;
import java.util.Objects;
import se.umu.jayo0002.iremind.models.Date;
import se.umu.jayo0002.iremind.models.DateValidator;
import se.umu.jayo0002.iremind.models.LocationInfo;
import se.umu.jayo0002.iremind.models.StringFormatter;
import se.umu.jayo0002.iremind.models.Task;
import se.umu.jayo0002.iremind.models.TextValidator;
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
    private Date mDate;
    private String mTitle, mEvent, mPickedDate, mPickedTime;
    private boolean mIsDatePickerShown, mIsTimePickerShown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        mDate = new Date();
        prepareUI();
        if (getIntent().hasExtra(TASK) && savedInstanceState == null)
            update();
        else if(savedInstanceState != null)
            updateUI(savedInstanceState);
        checkService();
    }

    private void onDismiss(){
        mDateDialog.setOnCancelListener(dialog -> {
            mDateDialog.dismiss();
            mIsDatePickerShown = false;
        });
        mTimeDialog.setOnCancelListener(dialog -> {
            mTimeDialog.dismiss();
            mIsTimePickerShown = false;
        });
    }

    private TextWatcher textWatcher = new TextWatcher() {
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
                mDateDialog.show();
                break;
            case R.id.btStartingTime:
                mTimeDialog.show();
                break;
            case R.id.btLocation:
                Intent maps= new Intent(this, MapsActivity.class);
                if (mLocationInfo != null)
                    maps.putExtra(Tags.LATLNG, mLocationInfo.getLatLng());
                startActivityForResult(maps,Tags.REQUEST_CODE_MAP);
                break;
            default:
                break;
        }
    }

    private void checkService(){
        if (MapServiceController.isServiceSupported(this)){
            mButtonAddLocation.setEnabled(false);
            mButtonAddLocation.setText(R.string.not_supported);
        }
    }

    public void onBackPressed() {
        destroyActivity();
    }

    private void destroyActivity(){
        Intent main = new Intent();
        setResult(RESULT_CANCELED, main);
        this.finish();
    }

    private void onSave(){
        if (!DateValidator.isDateValid(mStartHour, mStartMinute, mYear, mMonth, mDay))
            Toast.makeText(this, Tags.INVALID_DATE, Toast.LENGTH_LONG).show();
        else if (!TextValidator.isTitleValid(mTitle))
            Toast.makeText(this, Tags.TITLE_IS_INVALID, Toast.LENGTH_LONG).show();
        else if (!TextValidator.isNoteValid(mEvent))
                Toast.makeText(this, Tags.NOTE_SIZE, Toast.LENGTH_LONG).show();
        else {
            Intent main = new Intent();
            Task task = new Task();
            task.setTitle( mTitle);
            task.setNote(mEvent);
            task.setLocation(mLocationInfo);
            task.setYear(mYear);
            task.setMonth(mMonth);
            task.setDay(mDay);
            task.setHour(mStartHour);
            task.setMinute(mStartMinute);
            main.putExtra(TASK, task);
            setResult(RESULT_OK, main);
            this.finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Tags.REQUEST_CODE_MAP && resultCode == RESULT_OK){
            mLocationInfo = Objects.requireNonNull(data).getParcelableExtra(Tags.LOCATION_OBJECT);
            assert mLocationInfo != null;
            mButtonAddLocation.setText(mLocationInfo.getAddress());
        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        mYear = year; mMonth = month; mDay = day;
        mPickedDate = year + "-" + StringFormatter.getFormattedString((month+1)) + "-" + StringFormatter.getFormattedString(day);
        mButtonAddDate.setText(mPickedDate);
        mDateDialog.dismiss();
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        mStartHour = hour; mStartMinute = minute;
        mPickedTime = "Time: " + StringFormatter.getFormattedString(mStartHour) +":" + StringFormatter.getFormattedString(mStartMinute);
        mButtonAddStartTime.setText(mPickedTime);
        mTimeDialog.dismiss();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mDateDialog.isShowing()){
            mDateDialog.dismiss();
            mIsDatePickerShown = true;
        } else if (mTimeDialog.isShowing()){
            mTimeDialog.dismiss();
            mIsTimePickerShown = true;
        }
        outState.putString(Tags.DATE, mPickedDate);
        outState.putString(Tags.PICKED_TIME, mPickedTime);
        outState.putString(Tags.EVENT_TITLE, mTitle);
        outState.putString(Tags.EVENT_INFO, mEvent);
        outState.putInt(Tags.EVENT_YEAR, mYear);
        outState.putInt(Tags.EVENT_MONTH, mMonth);
        outState.putInt(Tags.EVENT_DAY, mDay);
        outState.putInt(Tags.EVENT_TIME_HOUR, mStartHour);
        outState.putInt(Tags.EVENT_TIME_MINUTES, mStartMinute);
        outState.putParcelable(Tags.LOCATION_OBJECT,mLocationInfo);
        outState.putBoolean(Tags.DATE_PICKER_STATUS, mIsDatePickerShown);
        outState.putBoolean(Tags.TIME_PICKER_STATUS, mIsTimePickerShown);
    }

    private void update(){
        Task task = Objects.requireNonNull(getIntent().getExtras()).getParcelable(TASK);
        assert task != null;
        mTitle = task.getTitle();
        mEvent = task.getNote();
        mYear = task.getYear();
        mMonth = task.getMonth();
        mDay = task.getDay();
        mStartHour = task.getHour();
        mStartMinute = task.getMinute();
        mPickedDate = task.getDate();
        mPickedTime = task.getTime();
        mButtonAddDate.setText(mPickedDate);
        mButtonAddStartTime.setText(mPickedTime);
        mEventTitle.setText(mTitle);
        LatLng latLng = task.getLatLng();
        String address = task.getAddress();
        if (address != null && latLng != null){
            mLocationInfo = new LocationInfo();
            mLocationInfo.setLatLng(latLng);
            mLocationInfo.setAddress(address);
            mButtonAddLocation.setText(address);
        }
    }

    private void updateUI(Bundle outState) {
        if(outState.getParcelable(Tags.LOCATION_OBJECT) != null){
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
        mButtonAddDate.setText(mPickedDate);
        mButtonAddStartTime.setText(mPickedTime);
        if (mEvent != null && !mEvent.isEmpty())
            mMoreInfo.setText(mEvent);
        if (mTitle != null && !mTitle.isEmpty())
            mEventTitle.setText(mTitle);
        mIsTimePickerShown = outState.getBoolean(Tags.TIME_PICKER_STATUS);
        mIsDatePickerShown = outState.getBoolean(Tags.DATE_PICKER_STATUS);
        if (mIsTimePickerShown)
            mTimeDialog.show();
        else if (mIsDatePickerShown)
            mDateDialog.show();
    }

    private void prepareUI() {
        mEvent ="";
        mIsDatePickerShown =false;
        mIsTimePickerShown = false;
        Calendar mCalendar= Calendar.getInstance();
        mPickedDate = mCalendar.get(Calendar.YEAR) + "-" + StringFormatter.getFormattedString(
                (mCalendar.get(Calendar.MONTH) +1))+ "-" +
                StringFormatter.getFormattedString(mCalendar.get(Calendar.DAY_OF_MONTH));
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
        mDateDialog = new DatePickerDialog(
                CreateTaskActivity.this,
                R.style.PickerTheme, this, mYear,mMonth,mDay);
        mDateDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        mDateDialog.getDatePicker().setMaxDate(mDate.getPlusOneMonth());
        mTimeDialog  = new TimePickerDialog(CreateTaskActivity.this,
                R.style.PickerTheme,this,
                mCalendar.get(Calendar.HOUR_OF_DAY),mCalendar.get(Calendar.MINUTE),false);
        mMoreInfo.addTextChangedListener(textWatcher);
        mEventTitle.addTextChangedListener(textWatcher);
    }
}
