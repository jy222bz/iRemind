package se.umu.jayo0002.iremind.service;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import se.umu.jayo0002.iremind.Tags;

/**
 * This class creates the channel for notifications.
 */
public class IRemind extends Application {

    private static Application mApplication;

    public static Application getApplication() {
        return mApplication;
    }

    public static Context getContext() {
        return getApplication().getApplicationContext();
    }

    /**
     * It calls the private method to create the channel for notifications.
     */
    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        createChannel();
    }

    /**
     * It creates the channel for notification.
     */
    private void createChannel() {
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(Tags.CHANNEL_ID, Tags.CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setVibrationPattern(Tags.VIBRATION_PATTERN);
            channel.setDescription(Tags.REMINDER);
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MOVIE)
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .build();
            channel.setSound(alarmSound, audioAttributes);
            NotificationManager mng = getSystemService(NotificationManager.class);
            assert mng != null;
            mng.createNotificationChannel(channel);
        }
    }
}
