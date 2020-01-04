package se.umu.jayo0002.iremind.notifications;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;
import se.umu.jayo0002.iremind.Tags;
import se.umu.jayo0002.iremind.database.TaskRepo;
import se.umu.jayo0002.iremind.models.Task;

public class TaskScheduler extends JobIntentService {

    static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, TaskScheduler.class, Tags.TASK_SCHEDULER, work);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        TaskRepo taskRepo = new TaskRepo(this.getApplicationContext());
        taskRepo.getAll().observeForever(tasks -> {
            for (Task task : tasks){
                System.out.println(task.getTitle());
                if(task.isActive())
                    AlarmHandler.scheduleAlarm(this, task);
            }
        });
    }

    @Override
    public boolean onStopCurrentWork() {
        return super.onStopCurrentWork();
    }
}