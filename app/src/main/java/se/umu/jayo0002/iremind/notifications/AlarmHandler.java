package se.umu.jayo0002.iremind.notifications;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import se.umu.jayo0002.iremind.Tags;
import se.umu.jayo0002.iremind.models.Task;

/**
 * This class is responsible for scheduling and canceling the alarm.
 *
 * @author Jacob Yousif
 * @version 1.0
 * @since 2019 -12-09
 */
public class AlarmHandler {

    /**
     * It schedules the alarm.
     * @param context
     * @param task
     */
    public static void scheduleAlarm(Context context, Task task) {
        /*ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);*/
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Bundle bundle = new Bundle();
        bundle.putParcelable(Tags.TASK, task);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(Tags.BUNDLE, bundle);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, task.getId(), intent, PendingIntent.FLAG_ONE_SHOT);
        assert alarmManager != null;
        if(Build.VERSION.SDK_INT < 23){
            alarmManager.setExact(AlarmManager.RTC_WAKEUP,task.getAlarmDateAndTime().getTimeInMillis(),pendingIntent);
        } else{
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, task.getAlarmDateAndTime().getTimeInMillis(), pendingIntent);
        }
    }


    /**
     * It cancels the alarm.
     * @param context
     * @param task
     */
    public static void cancelAlarm(Context context, Task task) {
       /* ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);*/
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, task.getId(), intent, PendingIntent.FLAG_ONE_SHOT);
        assert alarmManager != null;
        alarmManager.cancel(pendingIntent);
        pendingIntent.cancel();
    }
}
