package se.umu.jayo0002.iremind.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import se.umu.jayo0002.iremind.models.Task;

@Database(entities = Task.class, version =1, exportSchema = false)
public abstract class TaskDatabase extends RoomDatabase {
    private static TaskDatabase instance;

    public abstract TaskDao noteDao();

    static synchronized TaskDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    TaskDatabase.class, "note_database").fallbackToDestructiveMigration()
                    .addCallback(roomCallback).build();
        }
        return instance;
    }

    private static Callback roomCallback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase sQLiteDatabase) {
            super.onCreate(sQLiteDatabase);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {

        private PopulateDbAsyncTask(TaskDatabase db) {
            TaskDao mTaskDao = db.noteDao();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }
}
