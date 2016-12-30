package gocash.com.clock;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.ExpandableListView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by la-skhatri on 12/22/2016.
 */

public class AlarmRepeatSettings extends DialogFragment {

    //public interface to send the value to main activity
    public interface RepeatDialogListner {
        void onFinishRepeatDialog(String value, int groupPosition);
    }

    String alarm_repeat_interval[] ;
    String selected_interval;
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
        // creating an alertDialog object
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //defining the alarm_repeat_interval string
        alarm_repeat_interval = getResources().getStringArray(R.array.alarm_repeat_interval);

        final int groupPosition = getArguments().getInt("position");

        int selected_value = getArguments().getInt("valueIndex", -1);
        if(selected_value > -1) {
            selected_interval = alarm_repeat_interval[selected_value];
        }
        //setting the alert dialog title and the type of items contained in it.
        //First parameter is the list, second is the already checked item, third is the listner object
        builder.setTitle("Choose Repeat Interval").setSingleChoiceItems(alarm_repeat_interval, selected_value, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        selected_interval = (String) alarm_repeat_interval[which];
                        break;
                    case 1:
                        selected_interval = (String) alarm_repeat_interval[which];
                        break;
                    case 2:
                        selected_interval = (String) alarm_repeat_interval[which];
                }
            }
        }).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                RepeatDialogListner activity = (RepeatDialogListner) getActivity();
                activity.onFinishRepeatDialog(selected_interval, groupPosition);
                dismiss();
            }
        });
        return builder.create();
    }



    //Instance provide to access from the ListView click
    public static AlarmRepeatSettings newInstance(int groupPosition, int valueIndex) {
        AlarmRepeatSettings frag = new AlarmRepeatSettings();

        Bundle bundle = new Bundle();
        bundle.putInt("position", groupPosition);
        bundle.putInt("valueIndex", valueIndex);
        frag.setArguments(bundle);
        return frag;
    }
}
