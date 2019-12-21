package se.umu.jayo0002.iremind.database;

import android.content.Context;
import android.os.AsyncTask;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import se.umu.jayo0002.iremind.models.Task;

/**
 * This class manages the local data.
 * It extends the RoomDatabase class.
 * It creates an instance of the database.
 */
@Database(entities = Task.class, version =5, exportSchema = false)
public abstract class TaskDatabase extends RoomDatabase {
    private static TaskDatabase mTaskDatabase;
    public abstract TaskDao taskDao();

    /**
     * It initializes the instance of the database.
     * It is synchronized to not allow multithreading access to it.
     * It uses a private method populate the database once it is created.
     * @param context
     * @return
     */
    static synchronized TaskDatabase getInstance(Context context) {
        if (mTaskDatabase == null) {
            mTaskDatabase = Room.databaseBuilder(context.getApplicationContext(),
                    TaskDatabase.class, "note_database").fallbackToDestructiveMigration()
                    .addCallback(roomCallback).build();
        }
        return mTaskDatabase;
    }

    /**
     * It populates the database.
     * It uses a private method to carry out the operation.
     */
    private static Callback roomCallback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase sQLiteDatabase) {
            super.onCreate(sQLiteDatabase);
            new onCreatePopulateDatabase (mTaskDatabase).execute();
        }
    };

    /**
     * It asynchronous operation to populate the operation in the background after it is initialized.
     */
    private static class onCreatePopulateDatabase extends AsyncTask<Void, Void, Void> {

        private onCreatePopulateDatabase(TaskDatabase db) {
            TaskDao mTaskDao = db.taskDao();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }
}