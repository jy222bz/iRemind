package se.umu.jayo0002.iremind.models;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import java.util.Objects;
import se.umu.jayo0002.iremind.OpenTaskActivity;
import se.umu.jayo0002.iremind.R;
import se.umu.jayo0002.iremind.Tags;


/**
 *
 */
public class AlarmReceiver extends BroadcastReceiver {

    /**
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getBundleExtra(Tags.BUNDLE);
        Task task = Objects.requireNonNull(bundle).getParcelable(Tags.TASK);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        Intent activityIntent = new Intent(context, OpenTaskActivity.class);
        activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Bundle bundle1 = new Bundle();
        bundle.putParcelable(Tags.TASK,task);
        activityIntent.putExtra(Tags.BUNDLE, bundle);
        PendingIntent contentIntent = PendingIntent.getActivity(context,
                Objects.requireNonNull(task).getId(), activityIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri uri = Settings.System.DEFAULT_RINGTONE_URI;
        long[] vibrate = { 0, 100, 200, 300 };
        Notification notification = new NotificationCompat.Builder(Objects.requireNonNull(context), Tags.CHANNEL_ID)
                .setSmallIcon(R.drawable.task_image)
                .setContentTitle(Tags.REMINDER)
                .setVibrate(vibrate)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setContentText(task.getTitle())
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setSound(uri, AudioManager.STREAM_ALARM)
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .build();

        notificationManager.notify(1, notification);
    }
}
