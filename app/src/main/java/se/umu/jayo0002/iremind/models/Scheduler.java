package se.umu.jayo0002.iremind.models;

import android.content.Context;
import se.umu.jayo0002.iremind.database.TaskRepo;
import se.umu.jayo0002.iremind.service.AlarmHandler;

/**
 * A class that handles the re-scheduling of the tasks after reboot.
 */
public class Scheduler {

    /**
     * A private constructor.
     */
    private Scheduler() {}


    /**
     * It re-schedules the tasks.
     * It deactivate any Task that is currently active but its reminder did not trigger.
     * The Reminder of a Task will not go off when the device is switched off, therefore they have to be
     * handled when the device is switched on.
     * @param context
     */
    public static void scheduleTasks(Context context){
        TaskRepo taskRepo = new TaskRepo(context.getApplicationContext());
        taskRepo.getAll().observeForever(tasks -> {
            if (!tasks.isEmpty()) {
                for (Task task : tasks) {
                    if (task.isActive() && task.isAlarmValid())
                        AlarmHandler.scheduleAlarm(context, task);
                    else if (task.isActive() && !task.isAlarmValid()) {
                        task.setInactive();
                        taskRepo.update(task);
                    }
                }
            }
        });
    }
}
