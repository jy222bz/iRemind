package se.umu.jayo0002.iremind.database;

import android.content.Context;
import android.os.AsyncTask;
import androidx.lifecycle.LiveData;
import java.util.List;
import se.umu.jayo0002.iremind.models.Task;


/**
 * This object provide a single source of data from the database to the TaskViewModel.
 * It communicates between the TaskViewModel and the database.
 *
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
     * @param context
     */
    public TaskRepo(Context context) {
       TaskDatabase database = TaskDatabase.getInstance(context);
        mTaskDao = database.taskDao();
        mAllTasks = mTaskDao.getAll();
        mActiveTasks = mTaskDao.getActiveTasks();
        mInactiveTasks = mTaskDao.getInactiveTasks();
    }

    /**
     * It adds the Task in the database.
     * @param task
     */
    public void add(Task task) {
        new AddTask(mTaskDao).execute(task);
    }

    /**
     * It updates the Task in the database.
     * @param task
     */
    public void update(Task task) {
        new UpdateTask(mTaskDao).execute(task);
    }

    /**
     * It deletes the Task from the database.
     * @param task
     */
    public void delete(Task task) {
        new DeleteTask(mTaskDao).execute(task);
    }

    /**
     * It deletes all the Tasks in the database.
     */
    public void deleteAll() {
        new DeleteAll(mTaskDao).execute();
    }

    /**
     * It deletes all the inactive Tasks in the database.
     */
    public void deleteAllInactiveTasks() {
        new DeleteAllInactiveTasks(mTaskDao).execute();
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
    private static class AddTask extends AsyncTask<Task, Void, Void> {
        private final TaskDao mTaskDao;

        private AddTask(TaskDao taskDao) {
            this.mTaskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Task...tasks) {
            mTaskDao.insert(tasks[0]);
            return null;
        }
    }

    /**
     * It executes the getTheSavedTask operation in the background thread.
     */
    private static class UpdateTask extends AsyncTask<Task, Void, Void> {
        private final TaskDao mTaskDao;

        private UpdateTask(TaskDao taskDao) {
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
    private static class DeleteTask extends AsyncTask<Task, Void, Void> {
        private final TaskDao mTaskDao;

        private DeleteTask(TaskDao taskDao) {
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
    private static class DeleteAll extends AsyncTask<Void, Void, Void> {
        private final TaskDao mTaskDao;

        private DeleteAll(TaskDao taskDao) {
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
    private static class DeleteAllInactiveTasks extends AsyncTask<Void, Void, Void> {
        private final TaskDao mTaskDao;

        private DeleteAllInactiveTasks(TaskDao taskDao) {
            this.mTaskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mTaskDao.deleteAllInactiveTasks();
            return null;
        }
    }
}