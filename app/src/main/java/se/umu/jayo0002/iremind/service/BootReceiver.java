package se.umu.jayo0002.iremind.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import se.umu.jayo0002.iremind.models.Scheduler;


/**
 * This class is responsible for receiving the intent after reboot to reschedule it again.
 * It extends the BroadcastReceiver.
 *
 * @author Jacob Yousif
 * @version 1.0
 * @since 2019 -12-09
 */
public class BootReceiver extends BroadcastReceiver {

    /**
     * It receives the intent and reschedule the Tasks upon reboot.
     * It deactivate any Task that is currently active but its reminder did not trigger.
     * The Reminder of a Task will not go off when the device is switched off, therefore they have to be
     * handled when the device is switched on.
     *
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction()))
            Scheduler.scheduleTasks(context);
    }
}
