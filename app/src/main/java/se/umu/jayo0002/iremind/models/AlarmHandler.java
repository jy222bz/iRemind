package se.umu.jayo0002.iremind.models;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import java.util.Calendar;
import se.umu.jayo0002.iremind.Tags;

/**
 *
 */
public class AlarmHandler  {

    /**
     *
     */
    private Context mContext;
    /**
     *
     */
    private Calendar mCalendar;

    /**
     * @param context
     */
    public AlarmHandler(Context context) {
        mContext = context;
    }

    /**
     * @param calendar
     * @param code
     * @param task
     */
    public void startAlarm(Calendar calendar, int code, Task task) {
        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(mContext, AlarmReceiver.class);
        intent.putExtra(Tags.TASK, task);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, code, intent, 0);
        assert alarmManager != null;
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    /**
     * @param code
     */
    public void cancelAlarm(int code) {
        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(mContext, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, code, intent, 0);
        assert alarmManager != null;
        alarmManager.cancel(pendingIntent);
    }
}
