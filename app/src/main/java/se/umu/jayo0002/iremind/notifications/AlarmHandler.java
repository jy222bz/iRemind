package se.umu.jayo0002.iremind.notifications;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import java.util.Objects;
import se.umu.jayo0002.iremind.OpenTaskActivity;
import se.umu.jayo0002.iremind.R;
import se.umu.jayo0002.iremind.Tags;
import se.umu.jayo0002.iremind.models.Task;
import static se.umu.jayo0002.iremind.MainActivity.update;

/**
 * This class is responsible for scheduling and canceling the alarm.
 * It extends the BroadcastReceiver, to receive the intent and fire the notification when the alarm triggers.
 *
 * @author Jacob Yousif
 * @version 1.0
 * @since 2019 -12-09
 */
public class AlarmHandler extends BroadcastReceiver {

    /**
     * It receives the intent and fires the notification.
     * It updates the Task to inactive when the alarm triggers.
     * It reschedules the alarm after reboot.
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getBundleExtra(Tags.BUNDLE);
        Task task = Objects.requireNonNull(bundle).getParcelable(Tags.TASK);
        if (intent.getAction() != null && context != null){
            if (Objects.equals(intent.getAction(), Tags.BOOT)) {
                scheduleAlarm(context, task);
            }
        } else {
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(Objects.requireNonNull(context));
            Intent activityIntent = new Intent(context, OpenTaskActivity.class);
            Bundle bundle1= new Bundle();
            bundle1.putParcelable(Tags.TASK,task);
            activityIntent.putExtra(Tags.BUNDLE, bundle1);
            Objects.requireNonNull(task).setInactive();
            update(task);
            PendingIntent pendingIntent = PendingIntent.getActivity(context,
                    Objects.requireNonNull(task).getId(), activityIntent, PendingIntent.FLAG_ONE_SHOT);
            Bitmap icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.iremind_image);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(Objects.requireNonNull(context), Tags.CHANNEL_ID);
            builder.setDefaults(Notification.DEFAULT_SOUND|Notification.DEFAULT_VIBRATE);
            builder.setSmallIcon(R.drawable.task_image);
            builder.setContentTitle(Tags.REMINDER);
            builder.setLargeIcon(icon);
            builder.setStyle(new NotificationCompat.BigTextStyle().bigText(task.getNote())
                    .setBigContentTitle(task.getTitle()));
            builder.setContentText(task.getTitle());
            builder.setPriority(NotificationCompat.PRIORITY_HIGH);
            builder.setContentIntent(pendingIntent);
            builder.setAutoCancel(true);
            Notification notification = builder.build();
            notificationManager.notify(task.getId(), notification);
        }
    }


    /**
     * It schedules the alarm.
     * @param context
     * @param task
     */
    public static void scheduleAlarm(Context context, Task task) {
        ComponentName receiver = new ComponentName(context, AlarmHandler.class);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Bundle bundle = new Bundle();
        bundle.putParcelable(Tags.TASK, task);
        Intent intent = new Intent(context, AlarmHandler.class);
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
        ComponentName receiver = new ComponentName(context, AlarmHandler.class);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmHandler.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, task.getId(), intent, PendingIntent.FLAG_ONE_SHOT);
        assert alarmManager != null;
        alarmManager.cancel(pendingIntent);
    }
}
