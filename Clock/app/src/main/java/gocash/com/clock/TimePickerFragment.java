package gocash.com.clock;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

/**
 * Created by la-skhatri on 12/23/2016.
 * this fragment open the dialog fragment for picking alarm_time
 */

public class TimePickerFragment extends DialogFragment {
    //
    private TimePicker timePicker;

    //Creating an interface to handle the return time
    public interface TimeDialogListner {
        void onFinishDialog(String time);
    }

    /**
     * Override to build your own custom Dialog container.  This is typically
     * used to show an AlertDialog instead of a generic Dialog; when doing so,
     * {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)} does not need
     * to be implemented since the AlertDialog takes care of its own content.
     * <p>
     * <p>This method will be called after {@link #onCreate(Bundle)} and
     * before {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.  The
     * default implementation simply instantiates and returns a {@link Dialog}
     * class.
     * <p>
     * <p><em>Note: DialogFragment own the {@link Dialog#setOnCancelListener
     * Dialog.setOnCancelListener} and {@link Dialog#setOnDismissListener
     * Dialog.setOnDismissListener} callbacks.  You must not set them yourself.</em>
     * To find out about these events, override {@link #onCancel(DialogInterface)}
     * and {@link #onDismiss(DialogInterface)}.</p>
     *
     * @param savedInstanceState The last saved instance state of the Fragment,
     *                           or null if this is a freshly created Fragment.
     * @return Return a new Dialog instance to be displayed by the Fragment.
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.time_picker_dialog, null);

        timePicker = (TimePicker) v.findViewById(R.id.timePicker);

        return  new AlertDialog.Builder(getActivity())
                .setView(v)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // checking for version and then implementing the timepicker function

                                // Getting hour value
                                int hour = 0;
                                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    hour = timePicker.getHour();
                                } else {
                                    hour = timePicker.getCurrentHour();
                                }

                                //getting minute value
                                int minute = 0;
                                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    minute = timePicker.getMinute();
                                } else {
                                    minute = timePicker.getCurrentMinute();
                                }

                                TimeDialogListner activity = (TimeDialogListner)getActivity();
                                activity.onFinishDialog(updateTime(hour, minute));
                                dismiss();
                            }
                        })

                .create();
    }

    private String updateTime(int hours, int mins) {
        String timeSet = "";
        if(hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if(hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if(hours == 12) {
            timeSet = "PM";
        } else {
            timeSet = "AM";
        }

        String minutes = "";
        if(mins < 10) {
            minutes = "0" + mins;
        }else {
            minutes = String.valueOf(mins);
        }

        String timeValue = new StringBuilder().append(hours).append(':').append(minutes).append(" ").append(timeSet).toString();

        return timeValue;

    }
}
