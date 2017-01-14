package gocash.com.clock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

/**
 * Created by la-skhatri on 12/30/2016.
 */

public class AlarmReceiver extends BroadcastReceiver {
    /*
     * @param context The Context in which the receiver is running.
     * @param intent  The Intent being received.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("In AlarmReceiver class", "alarmReceiver");
    }
}
