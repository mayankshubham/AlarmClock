package gocash.com.alarmclock;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.security.Provider;

/**
 * Created by la-skhatri on 12/6/2016.
 */

public class RingtonePlayingService extends Service{

    MediaPlayer media_player;
    boolean is_music_playing;
    int startId;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int flags,  int startId) {

        Log.e("LocalService", "Received start id" + startId + ":" + intent);

        //fetch the extra string values
        String states = intent.getExtras().getString("extra");

        Log.e("Ringtone state extra" , states);



        //this converts the extra strings from intent to StartIds 0 or 1
        assert states != null;
        switch (states) {
            case "alarm on":
                startId = 1;
                break;
            case "alarm off":
                startId = 0;
                break;
            default:
                startId = 0;
                break;
        }


        //if-else statements
        if (!this.is_music_playing && startId == 1) {   // no music playing and alarm on
            //create an instance of media player
            media_player = MediaPlayer.create(this, R.raw.force);
            media_player.start();

            this.is_music_playing = true;
            this.startId = 0;

            //notifications
            //set up the notification service

            NotificationManager notifyManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            //set up an intent that goes to the main activity
            Intent intent_main_activity = new Intent(this.getApplicationContext(), AlarmPage.class);

            //set up a pending intent
            PendingIntent pending_intent_main_activity = PendingIntent.getActivity(this, 0, intent_main_activity, 0);

            // make the notification parameters
            Notification notification_popup = new Notification.Builder(this)
                    .setContentTitle("An alarm is going off")
                    .setContentText("Click me")
                    .setContentIntent(pending_intent_main_activity)
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.clock)
                    .build();


            //set up the notification  call command
            notifyManager.notify(0, notification_popup);
        }else if(this.is_music_playing && startId == 0) {   //music playing and alarm off
            //end ringtone
            media_player.stop();
            media_player.reset();

            this.is_music_playing = false;
            this.startId = 0;

        } else if(!this.is_music_playing && startId == 0) { //musix not playing and alarm off

            this.is_music_playing = false;
            this.startId = 0;

        }else if(this.is_music_playing && startId == 1) {

            this.is_music_playing = true;
            this.startId = 1;

        } else {

        }


        return START_NOT_STICKY;   // if service stops it shouldnt automatically restart
    }

    @Override
    public void onDestroy() {
        //Tell the user we stopped
        super.onDestroy();
        this.is_music_playing = false;
        Toast.makeText(this, "On Destroy called", Toast.LENGTH_SHORT).show();
    }
}
