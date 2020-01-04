package se.umu.jayo0002.iremind.notifications;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import se.umu.jayo0002.iremind.database.TaskRepo;
import se.umu.jayo0002.iremind.models.Task;

public class AlarmService extends Service {

    public AlarmService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
       return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        scheduleTasks();
        stopSelf();
        return START_STICKY;
    }

    private void scheduleTasks() {
        TaskRepo taskRepo = new TaskRepo(this.getApplicationContext());
        taskRepo.getAll().observeForever(tasks -> {
            for (Task task : tasks){
                System.out.println(task.getTitle());
                if(task.isActive())
                    AlarmHandler.scheduleAlarm(this, task);
            }
        });
    }
}
