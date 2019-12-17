package se.umu.jayo0002.iremind.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import se.umu.jayo0002.iremind.models.Task;


@Dao
public interface TaskDao {

    @Insert
    void insert(Task task);

    @Update
    void update(Task task);

    @Delete
    void delete(Task task);

    @Query("DELETE FROM task_table")
    void deleteAll();


    @Query("DELETE FROM task_table WHERE status_column = 0")
    void deleteAllInactiveTasks();


    @Query("SELECT * FROM task_table ORDER BY id DESC")
    LiveData<List<Task>> getAll();


    @Query("SELECT * FROM  task_table WHERE status_column = 1")
    LiveData<List<Task>> getActiveTasks();


    @Query("SELECT * FROM task_table WHERE status_column = 0")
    LiveData<List<Task>> getInactiveTasks();

}
