package se.umu.jayo0002.iremind.controllers;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.util.Calendar;
import se.umu.jayo0002.iremind.R;
import se.umu.jayo0002.iremind.models.Date;
import se.umu.jayo0002.iremind.models.StringFormatter;

public class CreateTaskActivityHelper {
    /*
    public void init(Activity activity, String mEvent, boolean mIsDatePickerShown, boolean mIsTimePickerShown, String mPickedDate,
                     String mPickedTime, Button mButtonSave, Button mButtonClose,
                     Button mButtonAddDate, Button mButtonAddLocation, Button mButtonAddStartTime, EditText mEventTitle,
                     EditText mMoreInfo, DatePickerDialog mDateDialog, TimePickerDialog mTimeDialog, View.OnClickListener listener,
                     Date mDate, TextWatcher textWatcher){
        mEvent ="";
        mIsDatePickerShown =false;
        mIsTimePickerShown = false;
        Calendar mCalendar= Calendar.getInstance();
        mPickedDate = mCalendar.get(Calendar.YEAR) + "-" + StringFormatter.getFormattedString(
                (mCalendar.get(Calendar.MONTH) +1))+ "-" +
                StringFormatter.getFormattedString(mCalendar.get(Calendar.DAY_OF_MONTH));
        mPickedTime = activity.getString(R.string.starting_time);
        mButtonClose = activity.findViewById(R.id.close_button);
        mButtonClose.setOnClickListener(listener);
        mButtonSave = activity.findViewById(R.id.save_button);
        mButtonSave.setOnClickListener(listener);
        mButtonAddDate = activity.findViewById(R.id.btDate);
        mButtonAddDate.setOnClickListener(listener);
        mButtonAddLocation = activity.findViewById(R.id.btLocation);
        mButtonAddLocation.setOnClickListener(listener);
        mButtonAddStartTime = activity.findViewById(R.id.btStartingTime);
        mButtonAddStartTime.setOnClickListener(listener);
        mEventTitle = activity.findViewById(R.id.tvTitle);
        mMoreInfo = activity.findViewById(R.id.tvMoreInfo);
        mDateDialog = new DatePickerDialog(
                activity,
                R.style.PickerTheme, this, 0,0,0);
        mDateDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        mDateDialog.getDatePicker().setMaxDate(mDate.getPlus(5));
        mTimeDialog  = new TimePickerDialog(activity,
                R.style.PickerTheme,this,
                mCalendar.get(Calendar.HOUR_OF_DAY),mCalendar.get(Calendar.MINUTE),false);
        mMoreInfo.addTextChangedListener(textWatcher);
        mEventTitle.addTextChangedListener(textWatcher);
    }*/
}
