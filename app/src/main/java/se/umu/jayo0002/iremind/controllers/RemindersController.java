package se.umu.jayo0002.iremind.controllers;

import android.content.Context;
import se.umu.jayo0002.iremind.database.TaskRepo;
import se.umu.jayo0002.iremind.models.Task;


/**
 * A class that checks the Task that their reminders did not trigger.
 */
public class RemindersController {

    /**
     * It checks the Tasks that their reminders did not trigger because
     * the device was switched off when they alarm became due time.
     *
     * @param context
     */
    public static void doCheckTasks(Context context){
        TaskRepo taskRepo = new TaskRepo(context.getApplicationContext());
        taskRepo.getAll().observeForever(tasks -> {
            if (tasks.size() > 0){
                for (Task task : tasks){
                    if(task.isActive() && !task.isAlarmValid()){
                        task.setInactive();
                        taskRepo.update(task);
                    }
                }
            }
        });
    }
}
