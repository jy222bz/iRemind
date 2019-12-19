package se.umu.jayo0002.iremind.models;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.google.android.gms.maps.model.LatLng;
import java.util.Calendar;

/**
 * This class represents the Task object and the information that holds for the event to be noted.
 * This class contains SQLite notations, for Database purposes.
 * It implements Parcelable, to serialize it.
 * It provides various methods and most of the methods for the Database purposes.
 *
 * @author Jacob Yousif
 * @version 1.0
 * @since 2019 -12-09
 * */

@Entity(tableName = "task_table")
public class Task implements Parcelable{

    /**
     * The id for the event.
     * It is auto generated, in order to retrieve it from the Database.
     * Every Note has a unique id.
     */
    @PrimaryKey(autoGenerate = true)
    private int id;

    /**
     * The title of the event.
     */
    private String title;

    /**
     * More information about the event.
     */
    private String note;


    /**
     * These fields for the time and the date of the event.
     */
    private int year, month, day, hour, minute;


    /**
     * The address of the event.
     */
    private String address;

    /**
     * The coordinates for the location of the event.
     */
    private String latitude;

    /**
     * The coordinates for the location of the event.
     */
    private String longitude;

    /**
     * The status of the event.
     */
    @ColumnInfo(name= "status_column")
    private int status;

    /**
     * A constructor, creating a new object and its status will be active.
     */
    public Task(){
        this.status = 1;
        this.address = null;
        this.latitude = null;
        this.longitude = null;
    }

    /**
     * It sets the title of the event.
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * It sets the information about the event.
     * @param note
     */
    public void setNote(String note) {
        this.note  = note;
    }

    /**
     * It sets the status of the event.
     * @param isActive
     */
    public void setStatus(boolean isActive) {
        if (!isActive)
            this.status = 0;
    }

    /**
     * It returns the status of the event.
     * @return boolean
     */
    public boolean isItActive() {
        return this.status ==1;
    }

    /**
     * It sets the time and the date for the alarm of the event.
     *
     * @param hour
     * @param minute
     * @param year
     * @param month
     * @param day
     */
    public void setTheAlarmDate(int hour, int minute, int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
    }

    /**
     * It checks whether the date and the time of the alarm for the event.
     * @return boolean
     */
    public boolean isAlarmDateValid(){
        return DateValidator.isDateValid(hour,minute, year,month,day);
    }

    /**
     * It returns the date the time of the alarm for the event.
     * It returns null when the date is invalid.
     * @return Calendar
     */
    public Calendar getAlarmDateAndTime(){
        if (!DateValidator.isDateValid(hour,minute, year,month,day))
            return null;
        else {
            Calendar c = Calendar.getInstance();
            c.set(Calendar.HOUR_OF_DAY, hour);
            c.set(Calendar.MINUTE, minute);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MONTH, month);
            c.set(Calendar.DAY_OF_MONTH, day);
            c.set(Calendar.YEAR, year);
            return c;
        }
    }

    /**
     * It sets the information of the location.
     * @param locationInfo
     */
    public void setLocation(LocationInfo locationInfo) {
        if (locationInfo != null){
            LatLng latLng = locationInfo.getLatLng();
            this.address = locationInfo.getAddress();
            double lat= latLng.latitude;
            double lng= latLng.longitude;
            latitude= Double.toString(lat);
            longitude= Double.toString(lng);
        }
    }


    /**
     * It sets the month.
     * @param month
     */
    public void setMonth(int month){
        this.month = month;
    }

    /**
     * It sets the address.
     * @param address
     */
    public void setAddress(String address){
        this.address = address;
    }

    /**
     * It sets the coordinates.
     * @param latitude
     */
    public void setLatitude(String latitude){
        this.latitude = latitude;
    }

    /**
     * It sets the coordinates.
     * @param longitude
     */
    public void setLongitude(String longitude){
        this.longitude = longitude;
    }

    /**
     * It sets the day.
     * @param day
     */
    public void setDay(int day){
        this.day = day;
    }

    /**
     * It sets the hour.
     * @param hour
     */
    public void setHour(int hour){
        this.hour= hour;
    }

    /**
     * It sets the minute.
     * @param minute
     */
    public void setMinute(int minute){
        this.minute = minute;
    }

    /**
     * It sets the status.
     * @param status
     */
    public void setStatus(int status){
        this.status = status;
    }

    /**
     * The sets the year.
     * @param year
     */
    public void setYear(int year){
        this.year = year;
    }

    /**
     * It returns the coordinates.
     * @return String
     */
    public String getLongitude(){
        return this.longitude;
    }

    /**
     * It returns coordinates.
     * @return String
     */
    public String getLatitude(){
        return this.latitude;
    }



    /**
     * It returns the title.
     * @return String
     */
    public String getTitle() {
        return this.title.toUpperCase();
    }

    /**
     * It returns the information about the event.
     * @return String
     */
    public String getNote() {
        if (this.note.isEmpty())
            return "No additional information was provided!";
        return this.note;
    }

    /**
     * It returns the address.
     * @return String
     */
    public String getAddress() {
        if (address == null)
            return "No address is assigned!";
        else
        return this.address;
    }

    /**
     * It returns the year.
     * @return int
     */
    public int getYear() {
        return this.year;
    }

    /**
     * it returns the month.
     * @return int
     */
    public int getMonth() {
        return this.month;
    }

    /**
     * It returns the day.
     * @return int
     */
    public int getDay() {
        return this.day;
    }

    /**
     * It returns the hour.
     * @return int
     */
    public int getHour() {
        return this.hour;
    }

    /**
     * It returns the minute.
     * @return int
     */
    public int getMinute() {
        return this.minute;
    }

    /**
     * It returns the status.
     * @return int
     */
    public int getStatus() {
        return this.status;
    }

    /**
     * It returns the status.
     * @return int
     */
    public int getId() {
        return this.id;
    }

    /**
     * It sets the id.
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * It returns the full date.
     * @return int
     */
    public String getDate(){
        return "Date: " + year+ "-" + StringFormatter.getFormattedString((month+1)) + "-" +
                StringFormatter.getFormattedString(day);
    }

    /**
     * It returns the full time.
     * @return String
     */
    public String getTime(){
        return "Time: " + StringFormatter.getFormattedString(hour) +":" +
                StringFormatter.getFormattedString(minute);
    }

    /**
     * It return the coordinates.
     * @return LatLng
     */
    public LatLng getLatLng() {
        if (getLatitude() == null || getLongitude() == null)
            return null;
        else
        return new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
    }

    /**
     * Describe the kinds of special objects contained in this Parcelable
     * instance's marshaled representation. For example, if the object will
     * include a file descriptor in the output of {@link #writeToParcel(Parcel, int)},
     * the return value of this method must include the
     * {@link #CONTENTS_FILE_DESCRIPTOR} bit.
     *
     * @return a bitmask indicating the set of special object types marshaled
     * by this Parcelable object instance.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten this object in to a Parcel.
     *
     * @param dest  The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     *              May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(note);
        dest.writeInt(year);
        dest.writeInt(month);
        dest.writeInt(day);
        dest.writeInt(hour);
        dest.writeInt(minute);
        dest.writeString(address);
        dest.writeString(latitude);
        dest.writeString(longitude);
        dest.writeInt(status);
    }

    protected Task(Parcel in) {
        id = in.readInt();
        title = in.readString();
        note = in.readString();
        year = in.readInt();
        month = in.readInt();
        day = in.readInt();
        hour = in.readInt();
        minute = in.readInt();
        address = in.readString();
        latitude = in.readString();
        longitude = in.readString();
        status = in.readInt();
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };
}
