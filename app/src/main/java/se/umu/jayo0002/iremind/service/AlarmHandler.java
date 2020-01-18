package se.umu.jayo0002.iremind.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import se.umu.jayo0002.iremind.Tags;
import se.umu.jayo0002.iremind.models.Task;

/**
 * This class is responsible for scheduling and canceling the alarms.
 *
 * @author Jacob Yousif
 * @version 1.0
 * @since 2019 -12-09
 */
public class AlarmHandler {

    private AlarmHandler(){}

    /**
     * It schedules the alarm.
     * @param context
     * @param task
     */
    public static void scheduleAlarm(Context context, Task task) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Bundle bundle = new Bundle();
        bundle.putParcelable(Tags.TASK, task);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(Tags.BUNDLE_FROM_I_REMIND, bundle);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, task.getId(), intent, PendingIntent.FLAG_ONE_SHOT);
        assert alarmManager != null;
        AlarmManager.AlarmClockInfo alarmClockInfo=
                new AlarmManager.AlarmClockInfo(task.getAlarmDateAndTime().getTimeInMillis(), pendingIntent);
        alarmManager.setAlarmClock(alarmClockInfo, pendingIntent);
    }


    /**
     * It cancels the alarm.
     * @param context
     * @param task
     */
    public static void cancelAlarm(Context context, Task task) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, task.getId(), intent, PendingIntent.FLAG_ONE_SHOT);
        assert alarmManager != null;
        alarmManager.cancel(pendingIntent);
        pendingIntent.cancel();
    }
}
