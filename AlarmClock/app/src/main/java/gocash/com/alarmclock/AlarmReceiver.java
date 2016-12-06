package gocash.com.alarmclock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by skhatri on 12/6/2016.
 */

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.e("We are in the receiver", "Yay!");
        //fetch extra string from reciver
        String getextra_string = intent.getExtras().getString("extra");

        Log.e("The key is ", getextra_string);
        //create an intent to the rintoneService
        Intent service_intent = new Intent(context, RingtonePlayingService.class);

        //pass the extra string from AlarmPage to ringtone playing service.
        service_intent.putExtra("extra", getextra_string);

        //start the ringtone service
        context.startService(service_intent);
    }
}
