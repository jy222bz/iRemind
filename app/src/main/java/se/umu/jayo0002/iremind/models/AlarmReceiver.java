package se.umu.jayo0002.iremind.models;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;
import java.util.Objects;
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
        Task task = Objects.requireNonNull(intent.getExtras()).getParcelable(Tags.TASK);
        NotificationHelper notificationHelper = new NotificationHelper(context, task);
        NotificationCompat.Builder nb = notificationHelper.getChannelNotification();
        notificationHelper.getManager().notify(0b1, nb.build());
    }
}
