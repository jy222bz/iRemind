package se.umu.jayo0002.iremind;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class IRemind extends Application {

    @Override
    public void onCreate(){
        super.onCreate();
        createChannel();
    }

    private void createChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(Tags.CHANNEL_ID,Tags.CHANNEL_Name,
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Hy");
            NotificationManager mng = getSystemService(NotificationManager.class);
            assert mng != null;
            mng.createNotificationChannel(channel);
        }
    }
}
