package se.umu.jayo0002.iremind.view_models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import se.umu.jayo0002.iremind.database.TaskRepo;
import se.umu.jayo0002.iremind.models.Task;


/**
 * TaskViewModel prepares and provides data for the UI.
 * It communicates with the TaskRepo.
 * It extends the AndroidViewModel.
 *
 * @author Jacob Yousif
 * @version 1.0
 * @since 2019 -12-09
 */
public class TaskViewModel extends AndroidViewModel {

    /**
     * it represents the repository of the Tasks.
     */
    private final TaskRepo mRepo;

    /**
     * It represents all the Tasks.
     */
    private final LiveData<List<Task>> mAllTasks;

    /**
     * It represents all the inactive Tasks.
     */
    private final LiveData<List<Task>> mInactiveTasks;

    /**
     * It represents all the active Tasks.
     */
    private final LiveData<List<Task>> mActiveTasks;

    /**
     * A Constructor, to construct the fields.
     * Takes the application object as parameter.
     * @param application
     */
    public TaskViewModel(@NonNull Application application) {
        super(application);
        mRepo = new TaskRepo(application);
        mAllTasks = mRepo.getAll();
        mActiveTasks = mRepo.getActiveTasks();
        mInactiveTasks = mRepo.getInactiveTasks();
    }

    /**
     * It add the Task in the repository.
     * @param task
     */
    public void insert(Task task) {
        mRepo.insert(task);
    }

    /**
     * It updates the Task in the repository
     * @param task
     */
    public void update(Task task) {
        mRepo.update(task);
    }

    /**
     * It deletes the Task from the repository.
     * @param task
     */
    public void delete(Task task) {
        mRepo.delete(task);
    }

    /**
     * It deletes all the Tasks from repository.
     */
    public void deleteAll() {
        mRepo.deleteAll();
    }

    /**
     * It deletes all the inactive Tasks from the repository.
     */
    public void deleteAllInactiveTasks() {
        mRepo.deleteAllInactiveTasks();
    }

    /**
     * It returns all the data from the repository
     * @return LiveData<List<Task>>
     */
    public LiveData<List<Task>> getAll() {
        return mAllTasks;
    }

    /**
     * It returns the active Tasks.
     * @return LiveData<List<Task>>
     */
    public LiveData<List<Task>> getActiveTasks() {
        return mActiveTasks;
    }

    /**
     * It returns the inactive Tasks.
     * @return LiveData<List<Task>>
     */
    public LiveData<List<Task>> getInactiveTasks() {
        return mInactiveTasks;
    }
}
