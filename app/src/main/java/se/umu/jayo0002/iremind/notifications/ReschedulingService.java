package se.umu.jayo0002.iremind.notifications;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import se.umu.jayo0002.iremind.Tags;
import se.umu.jayo0002.iremind.database.TaskRepo;
import se.umu.jayo0002.iremind.models.Task;
import se.umu.jayo0002.iremind.view_models.TaskViewModel;

public class ReschedulingService extends Service {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        TaskRepo taskRepo = new TaskRepo(this.getApplication());
        List<Task> list = new ArrayList<>(Objects.requireNonNull(taskRepo.getActiveTasks().getValue()));
        Toast.makeText(this, " we got here " + list.size(), Toast.LENGTH_LONG).show();
        if (list != null) {
            for (Task task : list)
                AlarmHandler.scheduleAlarm(this.getApplicationContext(), task);
        }
        stopSelf();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
