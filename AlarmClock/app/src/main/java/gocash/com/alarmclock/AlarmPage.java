package gocash.com.alarmclock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class AlarmPage extends AppCompatActivity {

    //Alarm manager variables declaration
    AlarmManager alarm_manager;
    TimePicker alarm_timepicker;    // for the time_picker layout
    TextView alarm_status;          // for textview corresponding to alarm status
    PendingIntent pending_intent;   // pending intent for the alarm reciever class
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.context = this;

        //initialise our alarm_manager
        alarm_manager = (AlarmManager)getSystemService(ALARM_SERVICE);

        // initialise our time picker
        alarm_timepicker = (TimePicker) findViewById(R.id.timePicker);

        //initialise our text update
        alarm_status = (TextView) findViewById(R.id.alarm_status);

        //create and instance of a calendar
        final Calendar calendar  = Calendar.getInstance();

        //initialise set_alarm button
        Button set_alarm = (Button) findViewById(R.id.set_alarm);

        //create an intent to the AlarmReceiver class
        final Intent alarm_receiver_intent = new Intent(this.context, AlarmReceiver.class);


        //create an onClick listener to set an alarm
        set_alarm.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View v) {

                //set the timepicker input to calendar
                calendar.set(Calendar.HOUR_OF_DAY, alarm_timepicker.getHour());
                calendar.set(Calendar.MINUTE, alarm_timepicker.getMinute());

                int hour = alarm_timepicker.getHour();
                int minute = alarm_timepicker.getMinute();

                //convert the int values to string
                String hour_string = String.valueOf(hour);
                String minute_String = String.valueOf(minute);

                //convert 24 hour time to 12 hour time
                if(hour > 12) {
                    hour_string = String.valueOf(hour - 12);
                }

                if(minute < 10) {
                    //10:2 -> 10:02
                    minute_String = "0" + String.valueOf(minute);
                }
                //method that changes the status text
                set_alarm_text("Alarm set to " + hour_string + ':' + minute_String);

                //put in extra string into my intent. Tells the clock then we presses the alarm on button
                alarm_receiver_intent.putExtra("extra", "alarm on");
                //create a pending intent that delays the intent until the specified calendar time
                pending_intent = PendingIntent.getBroadcast(AlarmPage.this, 0,
                                    alarm_receiver_intent, PendingIntent.FLAG_UPDATE_CURRENT);


                //set the alarm manager
                alarm_manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pending_intent);
            }
        });

        //initialise cancel_alarm button
        Button cancel_alarm = (Button) findViewById(R.id.cancel_alarm);
        //create an onClick listener to turnoff the alarm
        cancel_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //method that changes the status text
                set_alarm_text("Alarm Off");

                //cancel the alarm
                alarm_manager.cancel(pending_intent);

                //put extra string into intent that tells the clock that we pressed the alarm off button
                alarm_receiver_intent.putExtra("extra", "alarm off");
                //stop the ringtone
                sendBroadcast(alarm_receiver_intent);
            }
        });


    }

    private void set_alarm_text(String output) {
        alarm_status.setText(output);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_alarm_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
