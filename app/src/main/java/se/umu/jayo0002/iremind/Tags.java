package se.umu.jayo0002.iremind;

import android.Manifest;

public class Tags {

    public static final String CHANNEL_ID = "Notification Channel.";
    public static final String CHANNEL_NAME = "A ToDo-Task Reminder!";
    static final String NOT_SUPPORTED = "Your Phone does NOT support this version of Google Map service.";
    public static final int LOCATION_PERMISSION_REQUEST_CODE  = 189;
    public static final String ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String NO_LOCATION= "Error. Failed to get the location.";
    static final float MAP_ZOOM= 15f;
    public static final String LOCATION_OBJECT = "LOCATION_INFO";
    static final String LAT_LNG_OUT_STATE = "LatLng Out-State";
    static final String STATE_CONDITION = "State Condition";
    static final String PERMISSION_STATE = "Permission Condition";
    static final String PICKED_TIME= "PICKED_TIME";
    static final int REQUEST_CODE_MAP = 1;
    static final int REQUEST_CODE_CREATE_EVENT = 2;
    static final int REQUEST_CODE_EDIT_EVENT = 1320;
    static final String DATE_PICKER_STATUS= "Whether it is showing";
    static final String TIME_PICKER_STATUS = "Whether it is showing";
    static final String _LAT_LNG = "The last known coordinates for the last location.";
    public static final String TASK= "A Task Object.";
    public static final String INVALID_DATE = "The DATE and the TIME of the reminder should be in the future!";
    public static final String TITLE_IS_INVALID = "There should be a title that is not longer than 12 letters!";
    public static final String EVENT_TITLE = "THE TITLE FOR THE EVENT";
    public static final String EVENT_INFO = "More info about the event.";
    public static final String EVENT_TIME_HOUR = "Starting hour.";
    public static final String EVENT_TIME_MINUTES = "Starting minutes";
    public static final String EVENT_REMINDER = "Reminder about the event";
    public static final String EVENT_YEAR = "Year";
    public static final String EVENT_MONTH = "Month";
    public static final String EVENT_DAY = "Day";
    static final String DATE = "Date";
    public static final String EVENT_DELETED = "The event is deleted!";
    public static final String EVENT_ARCHIVED = "The event is archived!";
    public static final String EVENT_UNARCHIVED = "The event is active now!";
    public static final String EVENT_INVALID = "The DATE and the TIME should be in the future to be put into an active mode!";
    public static final String BUNDLE = "A Bundle Object carrying data.";
    public static final String REMINDER = "A Task-ToDo Reminder!";
    public static final String NEW_LAUNCH = "SHOW TASK";
    public static final String BOOLEAN = "Whether to go to the Main Activity or not.";
    public static final String NOTE_SIZE = "NOTE CANNOT BE LONGER THAN 300 LETTERS.";
    public static final long[] VIBRATION_PATTERN = {0, 100, 500, 100, 500, 100, 500, 100, 500, 100};
    public static final String NO_ARCHIVE = "The Archive Is Already Empty!";
    public static final String SEARCH_QUERY = "THE QUERY OF THE SEARCH VIEW:";
    public static final String STATE_OF_THE_SEARCH_VIEW = "THE STATE OF THE SEARCH VIEW:";
    public static final int THE_ALLOWED_AMOUNT_FOR_TITLE = 12;
    public static final int THE_ALLOWED_AMOUNT_FOR_NOTE = 300;
    public static final int LONG_TOAST = 1;
    public static final int LONG_SNACK = 0;
}
