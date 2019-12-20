package se.umu.jayo0002.iremind.models;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

        Bitmap icon = BitmapFactory.decodeResource(context.getResources(),R.drawable.iremind_image);
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification notification = new NotificationCompat.Builder(Objects.requireNonNull(context), Tags.CHANNEL_ID)
                .setSmallIcon(R.drawable.task_image)
                .setContentTitle(Tags.REMINDER)
                .setLargeIcon(icon)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(task.getNote())
                        .setBigContentTitle(task.getTitle()))
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setContentText(task.getTitle())
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setSound(uri, AudioManager.STREAM_ALARM)
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .build();
        notificationManager.notify(task.getUniqueNumber(), notification);
    }
}
