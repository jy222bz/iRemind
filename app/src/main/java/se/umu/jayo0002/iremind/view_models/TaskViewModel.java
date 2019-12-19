package se.umu.jayo0002.iremind.view_models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import se.umu.jayo0002.iremind.database.TaskRepo;
import se.umu.jayo0002.iremind.models.Task;


public class TaskViewModel extends AndroidViewModel {
    private TaskRepo mRepo;
    private LiveData<List<Task>> mAllTasks;
    private LiveData<List<Task>> mInactiveTasks;
    private LiveData<List<Task>> mActiveTasks;

    public TaskViewModel(@NonNull Application application) {
        super(application);
        mRepo = new TaskRepo(application);
        mAllTasks = mRepo.getAll();
        mActiveTasks = mRepo.getActiveTasks();
        mInactiveTasks = mRepo.getInactiveTasks();
    }

    public void insert(Task task) {
        mRepo.insert(task);
    }

    public void update(Task task) {
        mRepo.update(task);
    }

    public void delete(Task task) {
        mRepo.delete(task);
    }

    public void deleteAll() {
        mRepo.deleteAll();
    }

    public void deleteAllInactiveTasks() {
        mRepo.deleteAllInactiveTasks();
    }

    public LiveData<List<Task>> getAll() {
        return mAllTasks;
    }

    public LiveData<List<Task>> getActiveTasks() {
        return mActiveTasks;
    }

    public LiveData<List<Task>> getInactiveTasks() {
        return mInactiveTasks;
    }
}
