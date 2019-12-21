package se.umu.jayo0002.iremind.database;

import android.app.Application;
import android.os.AsyncTask;
import androidx.lifecycle.LiveData;
import java.util.List;
import se.umu.jayo0002.iremind.models.Task;


/**
 * This object provide a single source of data from the database to the TaskViewModel.
 * It communicates between the TaskViewModel and the database.
 *
 * @author Jacob Yousif
 * @version 1.0
 * @since 2019 -12-09
 */
public class TaskRepo {
    /**
     * Ir represents the TaskDao Object.
     */
    private final TaskDao mTaskDao;

    /**
     * It represents all Tasks.
     */
    private final LiveData<List<Task>>mAllTasks;

    /**
     * It represents all active Tasks.
     */
    private final LiveData<List<Task>> mActiveTasks;

    /**
     * It represents the inactive Tasks.
     */
    private final LiveData<List<Task>>mInactiveTasks;

    /**
     * Constructor, to construct the object and assign the values.
     * @param application
     */
    public TaskRepo(Application application) {
       TaskDatabase database = TaskDatabase.getInstance(application);
        mTaskDao = database.taskDao();
        mAllTasks = mTaskDao.getAll();
        mActiveTasks = mTaskDao.getActiveTasks();
        mInactiveTasks = mTaskDao.getInactiveTasks();
    }

    /**
     * It adds the Task in the database.
     * @param task
     */
    public void insert(Task task) {
        new InsertTaskAsyncTask(mTaskDao).execute(task);
    }

    /**
     * It updates the Task in the database.
     * @param task
     */
    public void update(Task task) {
        new UpdateTaskAsyncTask(mTaskDao).execute(task);
    }

    /**
     * It deletes the Task from the database.
     * @param task
     */
    public void delete(Task task) {
        new DeleteTaskAsyncTask(mTaskDao).execute(task);
    }

    /**
     * It deletes all the Tasks in the database.
     */
    public void deleteAll() {
        new DeleteAllAsyncTask(mTaskDao).execute();
    }

    /**
     * It deletes all the inactive Tasks in the database.
     */
    public void deleteAllInactiveTasks() {
        new deleteAllInactiveTasksAsyncTask(mTaskDao).execute();
    }


    /**
     * It return all the active Tasks.
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


    /**
     * It executes the insertion operation in the background thread.
     */
    private static class InsertTaskAsyncTask extends AsyncTask<Task, Void, Void> {
        private final TaskDao mTaskDao;

        private InsertTaskAsyncTask(TaskDao taskDao) {
            this.mTaskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Task...tasks) {
            mTaskDao.insert(tasks[0]);
            return null;
        }
    }

    /**
     * It executes the update operation in the background thread.
     */
    private static class UpdateTaskAsyncTask extends AsyncTask<Task, Void, Void> {
        private final TaskDao mTaskDao;

        private UpdateTaskAsyncTask(TaskDao taskDao) {
            this.mTaskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            mTaskDao.update(tasks[0]);
            return null;
        }
    }

    /**
     * It executes the insertion operation in the background thread.
     */
    private static class DeleteTaskAsyncTask extends AsyncTask<Task, Void, Void> {
        private final TaskDao mTaskDao;

        private DeleteTaskAsyncTask(TaskDao taskDao) {
            this.mTaskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            mTaskDao.delete(tasks[0]);
            return null;
        }
    }

    /**
     * It executes the delete all operation in the background thread.
     */
    private static class DeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private final TaskDao mTaskDao;

        private DeleteAllAsyncTask(TaskDao taskDao) {
            this.mTaskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mTaskDao.deleteAll();
            return null;
        }
    }

    /**
     * It executes the delete all inactive Tasks operation in the background thread.
     */
    private static class deleteAllInactiveTasksAsyncTask extends AsyncTask<Void, Void, Void> {
        private final TaskDao mTaskDao;

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