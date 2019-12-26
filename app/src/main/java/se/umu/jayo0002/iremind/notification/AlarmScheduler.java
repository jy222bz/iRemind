package se.umu.jayo0002.iremind.notification;

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
import android.os.PowerManager;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Calendar;
import java.util.Objects;

import se.umu.jayo0002.iremind.OpenTaskActivity;
import se.umu.jayo0002.iremind.R;
import se.umu.jayo0002.iremind.Tags;
import se.umu.jayo0002.iremind.models.Task;

import static se.umu.jayo0002.iremind.MainActivity.update;

public class AlarmScheduler extends BroadcastReceiver {

    /**
     *
     */
    private final Context mContext;


    /**
     *
     */
    private Task mTask;

    /**
     * @param context
     */
    public AlarmScheduler(Context context) {
        mContext = context;
    }

    /**
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Objects.equals(intent.getAction(), Tags.BOOT)) {
            Bundle bundle = intent.getBundleExtra(Tags.BUNDLE);
            mTask = Objects.requireNonNull(bundle).getParcelable(Tags.TASK);
            startAlarm(Objects.requireNonNull(mTask).getAlarmDateAndTime(), mTask.getId(), mTask);
        } else {
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            PowerManager.WakeLock wl = Objects.requireNonNull(pm).newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Tag: iRemind");
            wl.acquire(10*60*1000L);
            Bundle bundle = intent.getBundleExtra(Tags.BUNDLE);
            Task task = Objects.requireNonNull(bundle).getParcelable(Tags.TASK);
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            Intent activityIntent = new Intent(context, OpenTaskActivity.class);
            Bundle bundle1= new Bundle();
            bundle1.putParcelable(Tags.TASK,task);
            activityIntent.putExtra(Tags.BUNDLE, bundle1);
            Objects.requireNonNull(task).setStatus(false);
            update(task);
            PendingIntent contentIntent = PendingIntent.getActivity(context,
                    Objects.requireNonNull(task).getId(), activityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            Bitmap icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.iremind_image);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(Objects.requireNonNull(context), Tags.CHANNEL_ID);
            builder.setDefaults(Notification.DEFAULT_SOUND|Notification.DEFAULT_LIGHTS|Notification.DEFAULT_VIBRATE);
            builder.setSmallIcon(R.drawable.task_image);
            builder.setContentTitle(Tags.REMINDER);
            builder.setLargeIcon(icon);
            builder.setStyle(new NotificationCompat.BigTextStyle().bigText(task.getNote())
                    .setBigContentTitle(task.getTitle()));
            builder.setContentText(task.getTitle());
            builder.setPriority(NotificationCompat.PRIORITY_HIGH);
            builder.setContentIntent(contentIntent);
            builder.setDefaults(NotificationCompat.DEFAULT_SOUND);
            builder.setAutoCancel(true);
            builder.setOnlyAlertOnce(true);
            Notification notification = builder.build();
            notificationManager.notify(task.getId(), notification);
            wl.release();
        }
    }

    /**
     * @param calendar
     * @param code
     * @param task
     */
    public void startAlarm(Calendar calendar, int code, Task task) {
        mTask = task;
        ComponentName receiver = new ComponentName(mContext, AlarmScheduler.class);
        PackageManager pm = mContext.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);

        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        Bundle bundle = new Bundle();
        bundle.putParcelable(Tags.TASK, task);
        Intent intent = new Intent(mContext, AlarmScheduler.class);
        intent.putExtra(Tags.BUNDLE, bundle);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, code, intent, PendingIntent.FLAG_ONE_SHOT);
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
        ComponentName receiver = new ComponentName(mContext, AlarmScheduler.class);
        PackageManager pm = mContext.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(mContext, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, code, intent, PendingIntent.FLAG_ONE_SHOT);
        pendingIntent.cancel();
        assert alarmManager != null;
        alarmManager.cancel(pendingIntent);
    }
}
