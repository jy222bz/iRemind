package se.umu.jayo0002.iremind.database;

import android.app.Application;
import android.os.AsyncTask;
import androidx.lifecycle.LiveData;
import java.util.List;
import se.umu.jayo0002.iremind.models.Task;


public class TaskRepo {
    private TaskDao mTaskDao;
    private LiveData<List<Task>>mAllTasks;
    private LiveData<List<Task>> mActiveTasks;
    private LiveData<List<Task>>mInactiveTasks;

    public TaskRepo(Application application) {
       TaskDatabase database = TaskDatabase.getInstance(application);
        mTaskDao = database.taskDao();
        mAllTasks = mTaskDao.getAll();
        mActiveTasks = mTaskDao.getActiveTasks();
        mInactiveTasks = mTaskDao.getInactiveTasks();
    }

    public void insert(Task task) {
        new InsertTaskAsyncTask(mTaskDao).execute(task);
    }

    public void update(Task task) {
        new UpdateTaskAsyncTask(mTaskDao).execute(task);
    }

    public void delete(Task task) {
        new DeleteTaskAsyncTask(mTaskDao).execute(task);
    }

    public void deleteAll() {
        new DeleteAllAsyncTask(mTaskDao).execute();
    }

    public void deleteAllInactiveTasks() {
        new deleteAllInactiveTasksAsyncTask(mTaskDao).execute();
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


    private static class InsertTaskAsyncTask extends AsyncTask<Task, Void, Void> {
        private TaskDao mTaskDao;

        private InsertTaskAsyncTask(TaskDao taskDao) {
            this.mTaskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Task...tasks) {
            mTaskDao.insert(tasks[0]);
            return null;
        }
    }

    private static class UpdateTaskAsyncTask extends AsyncTask<Task, Void, Void> {
        private TaskDao mTaskDao;

        private UpdateTaskAsyncTask(TaskDao taskDao) {
            this.mTaskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            mTaskDao.update(tasks[0]);
            return null;
        }
    }

    private static class DeleteTaskAsyncTask extends AsyncTask<Task, Void, Void> {
        private TaskDao mTaskDao;

        private DeleteTaskAsyncTask(TaskDao taskDao) {
            this.mTaskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            mTaskDao.delete(tasks[0]);
            return null;
        }
    }

    private static class DeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private TaskDao mTaskDao;

        private DeleteAllAsyncTask(TaskDao taskDao) {
            this.mTaskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mTaskDao.deleteAll();
            return null;
        }
    }

    private static class deleteAllInactiveTasksAsyncTask extends AsyncTask<Void, Void, Void> {
        private TaskDao mTaskDao;

        private deleteAllInactiveTasksAsyncTask(TaskDao taskDao) {
            this.mTaskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mTaskDao.deleteAllInactiveTasks();
            return null;
        }
    }
}