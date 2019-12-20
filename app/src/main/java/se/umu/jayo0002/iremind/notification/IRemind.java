package se.umu.jayo0002.iremind.notification;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import se.umu.jayo0002.iremind.R;
import se.umu.jayo0002.iremind.Tags;

public class IRemind extends Application {

    @Override
    public void onCreate(){
        super.onCreate();
        createChannel();
    }

    private void createChannel(){
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(Tags.CHANNEL_ID,Tags.CHANNEL_Name,
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(getString(R.string.notifier_title));
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MOVIE)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();
           channel.setSound(alarmSound, audioAttributes);
            NotificationManager mng = getSystemService(NotificationManager.class);
            assert mng != null;
            mng.createNotificationChannel(channel);
        }
    }
}
