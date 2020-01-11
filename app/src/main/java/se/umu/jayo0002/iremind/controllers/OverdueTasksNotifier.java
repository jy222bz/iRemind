package se.umu.jayo0002.iremind.controllers;


import android.content.Context;
import se.umu.jayo0002.iremind.Tags;
import se.umu.jayo0002.iremind.database.TaskRepo;
import se.umu.jayo0002.iremind.models.Task;
import se.umu.jayo0002.iremind.view.Toaster;

public class OverdueTasksNotifier {

    private boolean mIsOverdue;

    public boolean doControlOverdueTasks(Context context){
        mIsOverdue = false;
        TaskRepo taskRepo = new TaskRepo(context.getApplicationContext());
        taskRepo.getAll().observeForever(tasks -> {
            if (tasks.size() > 0){
                for (Task task : tasks){
                    if(task.isActive() && !task.isAlarmValid()){
                        task.setInactive();
                        taskRepo.update(task);
                        mIsOverdue = true;
                    }
                }
            }
        });
        return mIsOverdue;
    }
}
