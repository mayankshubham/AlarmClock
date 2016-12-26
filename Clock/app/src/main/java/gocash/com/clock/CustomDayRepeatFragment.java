package gocash.com.clock;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by la-skhatri on 12/23/2016.
 */

public class CustomDayRepeatFragment extends DialogFragment {

    public interface DayRepeatListner {
        void onFinishDayRepeatListner(List<String> days);
    }
    //List to save the result
    private ArrayList<String> selectedDays;
    String weekdays[];
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
        selectedDays = new ArrayList<>();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        weekdays = getResources().getStringArray(R.array.weekdays);
        Log.d("In dialog", "Custom");
        builder.setTitle("Select Days to Repeat")
                .setMultiChoiceItems(weekdays, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if(isChecked) {
                            selectedDays.add(weekdays[which]);
                        } else if (selectedDays.contains(weekdays[which])) {
                            selectedDays.remove(String.valueOf(weekdays[which]));
                        }
                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for(int i = 0; i < selectedDays.size(); i++) {
                            Log.d("Selected Days", selectedDays.get(i));
                        }

                    }
                });

        return builder.create();
    }
}
