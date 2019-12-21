package se.umu.jayo0002.iremind.notification;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import java.util.Objects;
import se.umu.jayo0002.iremind.OpenTaskActivity;
import se.umu.jayo0002.iremind.R;
import se.umu.jayo0002.iremind.Tags;
import se.umu.jayo0002.iremind.models.Task;
import static se.umu.jayo0002.iremind.MainActivity.mTaskViewModel;


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
        Bundle bundle1= new Bundle();
        bundle.putParcelable(Tags.TASK,task);
        activityIntent.putExtra(Tags.BUNDLE, bundle1);
        Objects.requireNonNull(task).setStatus(false);
        mTaskViewModel.update(task);
        PendingIntent contentIntent = PendingIntent.getActivity(context,
                Objects.requireNonNull(task).getId(), activityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Bitmap icon = BitmapFactory.decodeResource(context.getResources(),R.drawable.iremind_image);
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
        builder.setAutoCancel(true);
        builder.setOnlyAlertOnce(true);
        Notification notification = builder.build();
        notificationManager.notify(task.getId(), notification);
    }
}
