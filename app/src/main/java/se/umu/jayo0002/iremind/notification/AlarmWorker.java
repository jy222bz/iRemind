package se.umu.jayo0002.iremind.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import java.util.Calendar;
import se.umu.jayo0002.iremind.Tags;
import se.umu.jayo0002.iremind.models.Task;

public class AlarmWorker {

    /**
     *
     */
    private final Context mContext;
    /**
     *
     */
    private Calendar mCalendar;

    /**
     * @param context
     */
    public AlarmWorker(Context context) {
        mContext = context;
    }

    /**
     * @param calendar
     * @param code
     * @param task
     */
    public void startAlarm(Calendar calendar, int code, Task task) {
        /*ComponentName receiver = new ComponentName(mContext, AlarmReceiver.class);
        PackageManager pm = mContext.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);*/

        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        Bundle bundle = new Bundle();
        bundle.putParcelable(Tags.TASK, task);
        Intent intent = new Intent(mContext, AlarmReceiver.class);
        intent.putExtra(Tags.BUNDLE, bundle);
        intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, code, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        assert alarmManager != null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, task.getAlarmDateAndTime().getTimeInMillis(), pendingIntent);
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, task.getAlarmDateAndTime().getTimeInMillis(), pendingIntent);
        }
    }

    /**
     * @param code
     */
    public void cancelAlarm(int code) {
        /*ComponentName receiver = new ComponentName(mContext, AlarmReceiver.class);
        PackageManager pm = mContext.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);*/

        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(mContext, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, code, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        assert alarmManager != null;
        alarmManager.cancel(pendingIntent);
    }
}
