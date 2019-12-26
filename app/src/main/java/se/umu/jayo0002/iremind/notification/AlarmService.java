package se.umu.jayo0002.iremind.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import androidx.annotation.Nullable;
import java.util.Calendar;
import se.umu.jayo0002.iremind.Tags;
import se.umu.jayo0002.iremind.models.Task;

public class AlarmService extends Service {

    private Context mContext;

    public AlarmService(Context context){
        mContext = context;
    }

    /**
     * @param calendar
     * @param code
     * @param task
     */
    public void startAlarm(Calendar calendar, int code, Task task) {
        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        Bundle bundle = new Bundle();
        bundle.putParcelable(Tags.TASK, task);
        Intent intent = new Intent(mContext, AlarmReceiver.class);
       // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(Tags.BUNDLE, bundle);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, code, intent, PendingIntent.FLAG_ONE_SHOT);
        assert alarmManager != null;

        if(Build.VERSION.SDK_INT < 23){
            alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(task.getAlarmDateAndTime().getTimeInMillis(),pendingIntent), pendingIntent);
        } else{
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, task.getAlarmDateAndTime().getTimeInMillis(), pendingIntent);
        }
    }

    /**
     * @param code
     */
    public void cancelAlarm(int code) {
        AlarmManager alarmManager = (AlarmManager)mContext.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(mContext, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, code, intent, PendingIntent.FLAG_ONE_SHOT);
        pendingIntent.cancel();
        assert alarmManager != null;
        alarmManager.cancel(pendingIntent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
