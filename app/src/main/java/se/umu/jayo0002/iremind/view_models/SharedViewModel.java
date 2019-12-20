package se.umu.jayo0002.iremind.view_models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.List;
import se.umu.jayo0002.iremind.models.Task;

public class SharedViewModel extends ViewModel {
    private MutableLiveData<Task> mTask =new MutableLiveData<>();
    private MutableLiveData<Task> mTask2 =new MutableLiveData<>();
    private MutableLiveData<LiveData<List<Task>>> inActive = new MutableLiveData<>();

    public void sendInactiveTasks (LiveData<List<Task>> tasks){
        inActive.setValue(tasks);
    }

    public MutableLiveData<LiveData<List<Task>>> getInactiveTasks (){
        return inActive;
    }
    public void sendTaskToBeUpdated(Task task) {
        mTask.setValue(task);
    }

    public void sendTaskTobeDeleted(Task task) {
        mTask2.setValue(task);
    }

    public LiveData<Task> getTaskToBeUpdated() {
        return mTask;
    }

    public LiveData<Task> getTaskToBeDeleted() {
        return mTask2;
    }
}
