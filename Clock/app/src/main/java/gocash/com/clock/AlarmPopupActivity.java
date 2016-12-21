package gocash.com.clock;

import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;

/**
 * Created by la-skhatri on 12/17/2016.
 */

public class AlarmPopupActivity extends android.support.v4.app.DialogFragment implements View.OnClickListener {

    View view;
    Button cancel_alarm;
    Button confirm_alarm;
    TimePicker timePicker;          // get the time

    @Override
    public View onCreateView (LayoutInflater inflater,
                              ViewGroup container,
                              Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.alarm_pop_up,container, false);
        cancel_alarm = (Button) view.findViewById(R.id.cancel_alarm);
        confirm_alarm = (Button) view.findViewById(R.id.confirm_alarm);
        cancel_alarm.setOnClickListener(this);
        confirm_alarm.setOnClickListener(this);
        return view;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel_alarm:
                this.dismiss();
                break;
            case R.id.confirm_alarm:
                //Time picker instance
                timePicker = (TimePicker) view.findViewById(R.id.timePicker);

                //get timepicker values
                int hour = timePicker.getHour();
                int minute = timePicker.getMinute();

                //Stringify time values
                String hour_string = String.valueOf(hour);
                String minute_string = String.valueOf(minute);
                String TimeString;
                String noonVariable = "AM";

                if(hour > 12) {
                    hour_string = String.valueOf(hour - 12);
                    noonVariable = "PM";
                }
                if(minute < 10) {
                    minute_string = "0" + String.valueOf(minute);
                }

                TimeString = hour_string + ":" + minute_string + "  " + noonVariable;

                //MainActivity instance and passing value
                MainActivity mainActivity = (MainActivity)getActivity();
                mainActivity.onConfirmAlarm(TimeString);

                //Dismissing the popup window
                this.dismiss();
                break;
        }
    }



}
