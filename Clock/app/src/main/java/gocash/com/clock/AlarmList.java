package gocash.com.clock;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by la-skhatri on 12/19/2016.
 */

public class AlarmList extends BaseExpandableListAdapter {

    private List<String> alarm_headers;
    private HashMap<String, HashMap<String, List<String> > > alarm_list_children;
    private Context context;

    private static final String DIALOG_SINGLE_CHOICE_LIST = "MainActivity.AlarmRepeatSettings";

    public List<String> getAlarm_headers() {
        return alarm_headers;
    }

    public void setAlarm_headers(List<String> alarm_headers) {
        this.alarm_headers = alarm_headers;
    }

    public HashMap<String, HashMap<String, List<String>>> getAlarm_list_children() {
        return alarm_list_children;
    }

    public void setAlarm_list_children(HashMap<String, HashMap<String, List<String>>> alarm_list_children) {
        this.alarm_list_children = alarm_list_children;
    }

    //Constructor for initialization
    AlarmList(Context context, List<String> alarm_headers,  HashMap<String, HashMap<String, List<String> > > alarm_list_children) {
        this.context = context;
        this.alarm_list_children = alarm_list_children;
        this.alarm_headers = alarm_headers;
    }
    /**
     * Gets the number of groups.
     *
     * @return the number of groups
     */
    @Override
    public int getGroupCount() {
        return alarm_headers.size();
    }

    /**
     * Gets the number of children in a specified group.
     *
     * @param groupPosition the position of the group for which the children
     *                      count should be returned
     * @return the children count in the specified group
     */
    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
//        return alarm_list_children.get(alarm_headers.get(groupPosition)).size();
    }

    /**
     * Gets the data associated with the given group.
     *
     * @param groupPosition the position of the group
     * @return the data child for the specified group
     */
    @Override
    public Object getGroup(int groupPosition) {
        return alarm_headers.get(groupPosition);
    }

    /**
     * Gets the data associated with the given child within the given group.
     *
     * @param groupPosition the position of the group that the child resides in
     * @param childPosition the position of the child with respect to other
     *                      children in the group
     * @return the data of the child
     */
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return alarm_list_children.get(alarm_headers.get(groupPosition));
    }

    /**
     * Gets the ID for the group at the given position. This group ID must be
     * unique across groups. The combined ID (see
     * {@link #getCombinedGroupId(long)}) must be unique across ALL items
     * (groups and all children).
     *
     * @param groupPosition the position of the group for which the ID is wanted
     * @return the ID associated with the group
     */
    @Override
    public long getGroupId(int groupPosition) {
        return  groupPosition;
    }

    /**
     * Gets the ID for the given child within the given group. This ID must be
     * unique across all children within the group. The combined ID (see
     * {@link #getCombinedChildId(long, long)}) must be unique across ALL items
     * (groups and all children).
     *
     * @param groupPosition the position of the group that contains the child
     * @param childPosition the position of the child within the group for which
     *                      the ID is wanted
     * @return the ID associated with the child
     */
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    /**
     * Indicates whether the child and group IDs are stable across changes to the
     * underlying data.
     *
     * @return whether or not the same ID always refers to the same object
     * @see Adapter#hasStableIds()
     */
    @Override
    public boolean hasStableIds() {
        return false;
    }

    /**
     * Gets a View that displays the given group. This View is only for the
     * group--the Views for the group's children will be fetched using
     * {@link #getChildView(int, int, boolean, View, ViewGroup)}.
     *
     * @param groupPosition the position of the group for which the View is
     *                      returned
     * @param isExpanded    whether the group is expanded or collapsed
     * @param convertView   the old view to reuse, if possible. You should check
     *                      that this view is non-null and of an appropriate type before
     *                      using. If it is not possible to convert this view to display
     *                      the correct data, this method can create a new view. It is not
     *                      guaranteed that the convertView will have been previously
     *                      created by
     *                      {@link #getGroupView(int, boolean, View, ViewGroup)}.
     * @param parent        the parent that this view will eventually be attached to
     * @return the View corresponding to the group at the specified position
     */
    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String title = (String) this.getGroup(groupPosition);
        if(convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.alarm_list_header, null);
        }
        TextView textView = (TextView) convertView.findViewById(R.id.alarmHeader);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setText(title);

        //Set alarm status based on
        Switch alarm_toggle_switch = (Switch) convertView.findViewById(R.id.alarm_toggle);
        boolean switchState;
        if(alarm_list_children != null || alarm_headers != null) {
            if(alarm_toggle_switch != null) {
                //default setchecked for switch
                List<String> temp1 = null;
                if(alarm_list_children.get(alarm_headers.get(groupPosition)) != null) {
                    temp1 = (List<String>) alarm_list_children.get(alarm_headers.get(groupPosition)).get("alarmState");
                }
                if (temp1 != null) {
                    if (temp1.get(0).equals("false")) {
                        switchState = false;
                    } else {
                        switchState = true;
                    }
                } else {
                    switchState = false;
                }
                alarm_toggle_switch.setChecked(switchState);

                //having an onChange listner
                alarm_toggle_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            List<String> temp = (List<String>) alarm_list_children.get(alarm_headers.get(groupPosition)).get("alarmState");
                            if (temp != null) {
                                temp.set(0, "true");
                            } else {
                                List<String> alarm_toggle = new ArrayList<String>();
                                alarm_toggle.add("true");
                                alarm_list_children.get(alarm_headers.get(groupPosition)).put("alarmState", alarm_toggle);

                            }
                        } else {
                            List<String> temp = (List<String>) alarm_list_children.get(alarm_headers.get(groupPosition)).get("alarmState");
                            if (temp != null) {
                                temp.set(0, "false");
                            } else {
                                List<String> alarm_toggle = new ArrayList<String>();
                                alarm_toggle.add("false");
                                alarm_list_children.get(alarm_headers.get(groupPosition)).put("alarmState", alarm_toggle);
                            }
                        }
                        //updating the original data and notifying it to change the list view
                        notifyDataSetChanged();
                        //calling the main activity method
                        ((MainActivity)context).onAlarmToggleStateChange(groupPosition, isChecked);
                    }
                });
            }
        }
        return convertView;
    }

    /**
     * Gets a View that displays the data for the given child within the given
     * group.
     *
     * @param groupPosition the position of the group that contains the child
     * @param childPosition the position of the child (for which the View is
     *                      returned) within the group
     * @param isLastChild   Whether the child is the last child within the group
     * @param convertView   the old view to reuse, if possible. You should check
     *                      that this view is non-null and of an appropriate type before
     *                      using. If it is not possible to convert this view to display
     *                      the correct data, this method can create a new view. It is not
     *                      guaranteed that the convertView will have been previously
     *                      created by
     *                      {@link #getChildView(int, int, boolean, View, ViewGroup)}.
     * @param parent        the parent that this view will eventually be attached to
     * @return the View corresponding to the child at the specified position
     */
    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        HashMap<String, List<String>> listHashMap = (HashMap<String, List<String>>) this.getChild(groupPosition,childPosition);
        if(convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.alarm_list_child, null);
        }


        TextView alarm_repeat_btn = (TextView) convertView.findViewById(R.id.alarm_repeat_btn);
        alarm_repeat_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> repeatInteval = alarm_list_children.get(alarm_headers.get(groupPosition)).get("RepeatDay");
                String Value;
                int valueIndex = -1;
                if(repeatInteval != null) {
                   Value = repeatInteval.get(0);
                    Log.d("Interval value", Value);
                   switch(Value) {
                        case "Custom": valueIndex = 0;
                            break;
                       case "Weekday(Mon-Fri)": valueIndex = 1;
                           break;
                       case "Weekend(Sat,Sun)": valueIndex = 2;
                           break;
                    }
                }

                AlarmRepeatSettings alarmRepeatSettings = AlarmRepeatSettings.newInstance(groupPosition, valueIndex);
                alarmRepeatSettings.show(((MainActivity)context).getSupportFragmentManager(), DIALOG_SINGLE_CHOICE_LIST);
            }
        });
        TextView alarm_delete_btn = (TextView) convertView.findViewById(R.id.delete_alarm_icon);
        alarm_delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete Alert")
                        .setMessage("Are you sure to delete alarm")
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                alarm_list_children.remove(alarm_headers.get(groupPosition));
                                alarm_headers.remove(groupPosition);
                                notifyDataSetChanged();
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("Reject", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        return convertView;
    }

    /**
     * Whether the child at the specified position is selectable.
     *
     * @param groupPosition the position of the group that contains the child
     * @param childPosition the position of the child within the group
     * @return whether the child is selectable.
     */
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }



}
