package se.umu.jayo0002.iremind.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import se.umu.jayo0002.iremind.database.TaskRepo;
import se.umu.jayo0002.iremind.models.Task;


/**
 * This class is responsible for receiving the intent after reboot to reschedule it again.
 * It extends the BroadcastReceiver.
 *
 * @author Jacob Yousif
 * @version 1.0
 * @since 2019 -12-09
 */
public class BootReceiver extends BroadcastReceiver {

    /**
     * It receives the intent and reschedule the Tasks after reboot.
     *
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            TaskRepo taskRepo = new TaskRepo(context.getApplicationContext());
            taskRepo.getAll().observeForever(tasks -> {
                if (tasks.size() > 0){
                    for (Task task : tasks){
                        if(task.isActive())
                            AlarmHandler.scheduleAlarm(context, task);
                    }
                }
            });
        }
    }
}
