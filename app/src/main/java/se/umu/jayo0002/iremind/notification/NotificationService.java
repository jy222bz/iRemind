package se.umu.jayo0002.iremind.notification;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.IBinder;
import android.app.Service;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import java.util.Objects;
import se.umu.jayo0002.iremind.OpenTaskActivity;
import se.umu.jayo0002.iremind.R;
import se.umu.jayo0002.iremind.Tags;
import se.umu.jayo0002.iremind.models.Task;
import static se.umu.jayo0002.iremind.MainActivity.update;


public class NotificationService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle bundle = intent.getBundleExtra(Tags.BUNDLE);
        Task task = Objects.requireNonNull(bundle).getParcelable(Tags.TASK);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        Intent activityIntent = new Intent(this, OpenTaskActivity.class);
        activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Bundle bundle1= new Bundle();
        bundle1.putParcelable(Tags.TASK,task);
        activityIntent.putExtra(Tags.BUNDLE, bundle1);
        Objects.requireNonNull(task).setStatus(false);
        update(task);
        PendingIntent contentIntent = PendingIntent.getActivity(this,
                Objects.requireNonNull(task).getId(), activityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Bitmap icon = BitmapFactory.decodeResource(getResources(),R.drawable.iremind_image);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, Tags.CHANNEL_ID);
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


        startForeground(task.getId(), notification);

        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}