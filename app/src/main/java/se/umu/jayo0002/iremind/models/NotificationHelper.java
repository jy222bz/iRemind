package se.umu.jayo0002.iremind.models;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import se.umu.jayo0002.iremind.OpenTaskActivity;
import se.umu.jayo0002.iremind.R;
import se.umu.jayo0002.iremind.Tags;

/**
 *
 */
class NotificationHelper extends ContextWrapper {
    /**
     *
     */
    private NotificationManager mNotificationManager;
    /**
     *
     */
    private Task mTask;

    /**
     * @param base
     * @param task
     */
    public NotificationHelper(Context base, Task task) {
        super(base);
        mTask = task;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            prepareChannel();
    }

    /**
     *
     */
    @TargetApi(Build.VERSION_CODES.O)
    private void prepareChannel() {
        NotificationChannel channel = new NotificationChannel(Tags.CHANNEL_ID, Tags.CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
        channel.enableVibration(true);
        channel.enableLights(true);
        getManager().createNotificationChannel(channel);
    }

    /**
     * @return
     */
    public NotificationManager getManager() {
        if (mNotificationManager == null)
            mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        return mNotificationManager;
    }

    /**
     * @return
     */
    public NotificationCompat.Builder getChannelNotification() {
        Uri path = Uri.parse("android.resource://" + getPackageName() +"/"+ R.raw.notification_tone);
        Intent result = new Intent(this, OpenTaskActivity.class);
        result.putExtra(Tags.TASK, mTask);
        PendingIntent resultPending = PendingIntent.getActivity(this, Tags.REQUEST_CODE_NOTIFICATION,result,  PendingIntent.FLAG_CANCEL_CURRENT);
        return new NotificationCompat.Builder(getApplicationContext(), Tags.CHANNEL_ID)
                .setContentTitle(getString(R.string.notifier_title))
                .setContentText(mTask.getTitle()).setSound(path).setSmallIcon(R.drawable.alarm).setAutoCancel(true)
                .setPriority(3).setContentIntent(resultPending);
    }
}