package se.umu.jayo0002.iremind;

import android.Manifest;

public class Tags {

    private Tags(){ }

    public static final String CHANNEL_ID = "Notification Channel.";
    public static final int MAX_AMOUNT_OF_ADDRESSES =1;
    public static final String DB_NAME="note_database";
    public static final String CHANNEL_NAME = "A ToDo-Task Reminder!";
    static final String NOT_SUPPORTED = "Your Phone does NOT support this version of Google Map service.";
    public static final int LOCATION_PERMISSION_REQUEST_CODE  = 189;
    public static final String ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String NO_LOCATION= "Error. Failed to get the device location.";
    static final float MAP_ZOOM= 15f;
    public static final String LOCATION_OBJECT = "LOCATION_INFO";
    static final String LAT_LNG_OUT_STATE = "LatLng Out-State";
    static final String STATE_CONDITION = "State Condition";
    static final String PERMISSION_STATE = "Permission Condition";
    public static final String PICKED_TIME= "PICKED_TIME";
    static final int REQUEST_CODE_MAP = 1;
    static final int REQUEST_CODE_CREATE_EVENT = 2;
    static final int REQUEST_CODE_EDIT_EVENT = 1320;
    public static final String DATE_PICKER_STATUS= "Whether the DatePicker is showing";
    public static final String TIME_PICKER_STATUS = "Whether the TimePicker is showing";
    static final String LAT_LNG = "The last known coordinates for the last location.";
    public static final String TASK= "A Task Object.";
    public static final String STATE_OUT_TASK= "A Task Object To Be Saved.";
    public static final String INVALID_DATE = "DATE and TIME in the future should be provided!";
    public static final String TITLE_IS_INVALID = "There should be a title that is not longer than " +
            Tags.THE_ALLOWED_AMOUNT_FOR_TITLE + " CHARACTERS!";
    public static final String[] MONTHS = {"January", "February", "March", "April", "May", "June", "July",
            "August", "September", "October", "November", "December"};
    public static final String EVENT_TITLE = "THE TITLE FOR THE EVENT";
    public static final String EVENT_INFO = "More info about the event.";
    public static final String EVENT_TIME_HOUR = "Starting hour.";
    public static final String EVENT_TIME_MINUTES = "Starting minutes";
    public static final String EVENT_REMINDER = "Reminder about the event";
    public static final String EVENT_YEAR = "Year";
    public static final String EVENT_MONTH = "Month";
    public static final String EVENT_DAY = "Day";
    public static final String DATE = "Date";
    public static final String EVENT_DELETED = "The event is deleted!";
    public static final String EVENT_DEACTIVATED = "The REMINDER is deactivated!";
    public static final String EVENT_ACTIVATED = "The REMINDER is activated now!";
    public static final String EVENT_INVALID = "It is permanently inactive. Only EVENTS with DATE and Time in the future can be reactivated again!";
    public static final String BUNDLE_FROM_I_REMIND = "A Bundle Object carrying data.";
    public static final String REMINDER = "A Task-ToDo Reminder!";
    public static final String TASK_LAUNCHED_FROM_AN_ACTIVITY = "SHOW TASK";
    public static final String BOOLEAN = "Whether to go to the Main Activity or not.";
    public static final String NOTE_INVALID_SIZE = "NOTE CANNOT BE LONGER THAN " + Tags.THE_ALLOWED_AMOUNT_FOR_NOTE +" CHARACTERS.";
    public static final long[] VIBRATION_PATTERN = {0, 100, 500, 100, 500, 100, 500, 100, 500, 100};
    public static final String NO_ARCHIVE = "The Archive Is Already Empty!";
    public static final String SEARCH_QUERY = "THE QUERY OF THE SEARCH VIEW:";
    public static final String STATE_OF_THE_SEARCH_VIEW = "THE STATE OF THE SEARCH VIEW:";
    public static final String NO_GOOGLE_MAP_APP = "This Device Does Not Have Google Map Directions!";
    public static final String GOOGLE_MAP_APP = "com.google.android.apps.maps";
    public static final String NON_APPLICABLE= "No Directions!";
    public static final String FAILED_ATTEMPT_TO_GET_THE_LOCATION= "It Failed To Get The Current Location. Try Again Later.";
    public static final int THE_ALLOWED_AMOUNT_FOR_TITLE = 34;
    public static final int THE_ALLOWED_AMOUNT_FOR_NOTE = 600;
    public static final int LONG_TOAST = 1;
    public static final int SHORT_SNACK = -1;
    public static final String ALARM_SET= "The Reminder Is Set!";
    public static final String ALARM_IS_UPDATED= "The Reminder Is Updated and Set!.";
    public static final String ADDRESS_GUIDE= "To Select An Address, LONG-PRESS The Position Of The Location.";
    public static final String LOCATION_IS_NULL="Failed to fetch the location. Try again later!";
    public static final String NO_LOCATION_FOUND="The Provided Address Could Not Be Found!";
    public static final String DATE_PICKER_OUT_STATE = "State of DATE_PICKER";
    public static final String TIME_PICKER_OUT_STATE = "State of TIME_PICKER";
    public static final String NO_INFO ="No additional information was provided!";
    public static final String REQUIRED_INFO ="In order to create an event, provide a Title and valid Date and Time.";
    public static final String STANDRAD_TIME_LABEL ="Time: 00:00  ";
}
