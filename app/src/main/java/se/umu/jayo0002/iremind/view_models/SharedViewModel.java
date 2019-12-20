package se.umu.jayo0002.iremind.view_models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import se.umu.jayo0002.iremind.models.Task;

public class SharedViewModel extends ViewModel {
    private MutableLiveData<Task> mTask =new MutableLiveData<>();
    private MutableLiveData<Task> mTask2 =new MutableLiveData<>();


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
