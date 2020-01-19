package se.umu.jayo0002.iremind;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import se.umu.jayo0002.iremind.system_controllers.MapServiceController;
import se.umu.jayo0002.iremind.view.DialogConstructor;


/**
 * It is a base activity for the Create Activity.
 */
public abstract class HelperBase extends AppCompatActivity {


    /**
     * A private field for the title edit text.
     */
    private EditText mTitle;

    /**
     * A private field for the info edit text.
     */
    private EditText mInfo;

    /**
     * It sets the  title.
     * @param title
     */
    abstract void setTitle(String title);

    /**
     * It sets the info.
     * @param info
     */
    abstract void setInfo(String info);

    /**
     * It checks whether the device supports Google Map Service.
     * @param button
     */
    public void checkMapService(Button button) {
        if (MapServiceController.isServiceNOTSupported(this)) {
            button.setEnabled(false);
            button.setText(R.string.not_supported);
        }
    }

    /**
     * It returns a button.
     * @param listener
     * @return Button
     */
    public Button getDateButton(View.OnClickListener listener) {
        Button bt = findViewById(R.id.btDate);
        bt.setOnClickListener(listener);
        return bt;
    }

    /**
     * It returns a button.
     * @param listener
     * @return Button
     */
    public Button getTimeButton(View.OnClickListener listener) {
        Button bt = findViewById(R.id.btStartingTime);
        bt.setOnClickListener(listener);
        return bt;
    }

    /**
     * It sets the button with listener.
     * @param listener
     */
    public void setSaveButton(View.OnClickListener listener) {
        Button bt = findViewById(R.id.save_button);
        bt.setOnClickListener(listener);
    }

    /**
     * It returns a button.
     * @param listener
     * @return Button
     */
    public Button getLocationButton(View.OnClickListener listener) {
        Button bt = findViewById(R.id.btLocation);
        bt.setOnClickListener(listener);
        return bt;
    }

    /**
     * It returns an EditText for the title.
     * @return EditText
     */
    public EditText getTitleEditText() {
        mTitle = findViewById(R.id.tvTitle);
        mTitle.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        mTitle.addTextChangedListener(textWatcher);
        return mTitle;
    }

    /**
     * It returns an EditText for the information.
     * @return EditText
     */
    public EditText getInfoEditText() {
        mInfo = findViewById(R.id.tvMoreInfo);
        mInfo.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        mInfo.addTextChangedListener(textWatcher);
        return mInfo;
    }

    /**
     * A private text watcher class.
     */
    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            /*It is not useful in this context.*/
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            /*It is not useful in this context.*/
        }

        @Override
        public void afterTextChanged(Editable editable) {
            String str = editable.toString();
            if (editable == mTitle.getEditableText())
                setTitle(str);
            else if (editable == mInfo.getEditableText())
                setInfo(str);
        }
    };

    /**
     * It returns a TimePicker Dialog.
     * @param hour
     * @param minute
     * @param listener
     * @return TimePickerDialog
     */
    public TimePickerDialog getTimeDialog(int hour, int minute, TimePickerDialog.OnTimeSetListener listener) {
        DialogConstructor dialog = new DialogConstructor(this);
        return dialog.getTimeDialog(listener, hour, minute);
    }

    /**
     * It returns a DatePicker Dialog.
     * @param year
     * @param month
     * @param day
     * @param endMonth
     * @param listener
     * @return DatePickerDialog
     */
    public DatePickerDialog getDateDialog(int year, int month, int day, int endMonth, DatePickerDialog.OnDateSetListener listener) {
        DialogConstructor dialog = new DialogConstructor(this);
        return dialog.getDateDialog(year, month, day, endMonth, listener);
    }
}
