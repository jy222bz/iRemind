package se.umu.jayo0002.iremind.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import java.util.Objects;
import se.umu.jayo0002.iremind.OpenTaskActivity;
import se.umu.jayo0002.iremind.R;
import se.umu.jayo0002.iremind.Tags;
import se.umu.jayo0002.iremind.database.TaskRepo;
import se.umu.jayo0002.iremind.models.Task;



/**
 * This class is responsible for receiving the scheduled intent.
 * It extends the BroadcastReceiver.
 *
 * @author Jacob Yousif
 * @version 1.0
 * @since 2019 -12-09
 */
public class AlarmReceiver extends BroadcastReceiver {

    /**
     * It receives the intent and fires the notification.
     *
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        TaskRepo taskRepo = new TaskRepo(context);
        Bundle bundle = intent.getBundleExtra(Tags.BUNDLE_FROM_iREMIND);
        Task task = Objects.requireNonNull(bundle).getParcelable(Tags.TASK);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(Objects.requireNonNull(context));
        Intent activityIntent = new Intent(context, OpenTaskActivity.class);
        Bundle bundle1 = new Bundle();
        bundle1.putParcelable(Tags.TASK, task);
        activityIntent.putExtra(Tags.BUNDLE_FROM_iREMIND, bundle1);
        Objects.requireNonNull(task).setInactive();
        taskRepo.update(task);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,
                Objects.requireNonNull(task).getId(), activityIntent, PendingIntent.FLAG_ONE_SHOT);
        Bitmap icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.iremind_image);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(Objects.requireNonNull(context), Tags.CHANNEL_ID);
        builder.setSound(alarmSound);
        builder.setSmallIcon(R.drawable.task_image);
        builder.setContentTitle(task.getTitle());
        builder.setLargeIcon(icon);
        builder.setVibrate(Tags.VIBRATION_PATTERN);
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(task.getNote()));
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);
        Notification notification = builder.build();
        notificationManager.notify(task.getId(), notification);
    }
}
