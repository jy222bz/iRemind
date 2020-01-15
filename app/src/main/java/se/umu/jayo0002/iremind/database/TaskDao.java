package se.umu.jayo0002.iremind.database;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;
import se.umu.jayo0002.iremind.models.Task;

/**
 * A Data Access Object class to communicate with SQLIte database.
 * TaskDao interface for storing data in the database, to provide persistence mechanism.
 * It defines the SQLite data operations, table and columns that will be generated.
 *
 */

@Dao
public interface TaskDao {

    /**
     * It adds the Task to the database.
     * @param task
     */
    @Insert
    void insert(Task task);

    /**
     * It updates the Task in the database.
     * @param task
     */
    @Update
    void update(Task task);

    /**
     * It deletes the Task from database.
     * @param task
     */
    @Delete
    void delete(Task task);

    /**
     * It deletes all the Tasks from the database.
     */
    @Query("DELETE FROM task_table")
    void deleteAll();


    /**
     * It deletes all inactive Tasks from the database.
     */
    @Query("DELETE FROM task_table WHERE status_column = 0")
    void deleteAllInactiveTasks();


    /**
     * It returns All the Tasks in the database.
     * The order is by the id.
     * @return LiveData<List<Task>>
     */
    @Query("SELECT * FROM task_table ORDER BY id DESC")
    LiveData<List<Task>> getAll();


    /**
     * It returns the Active Tasks only.
     * It picks ony the Tasks where their status is active, i.e. is 1.
     * @return LiveData<List<Task>>
     */
    @Query("SELECT * FROM  task_table WHERE status_column = 1")
    LiveData<List<Task>> getActiveTasks();

    /**
     * It returns the inactive Tasks only.
     * It picks ony the Tasks where their status is inactive, i.e. is 0.
     * @return LiveData<List<Task>>
     */
    @Query("SELECT * FROM task_table WHERE status_column = 0")
    LiveData<List<Task>> getInactiveTasks();
}