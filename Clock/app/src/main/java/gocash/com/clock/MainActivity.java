package gocash.com.clock;

import android.app.AlarmManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity  implements TimePickerFragment.TimeDialogListner, AlarmRepeatSettings.RepeatDialogListner, CustomDayRepeatFragment.DayRepeatListner{

    // Defining the Alarm manager variables
    AlarmManager alarm_manager;      /// An alarm instance
    Context context;
    ExpandableListView expandableListView; //
    List<String> headings = new ArrayList<String>();
    HashMap<String, HashMap<String, List<String> > > childItems = new HashMap<String, HashMap<String, List<String>>>();
    AlarmList alarmList;
    private static final String DIALOG_TIME = "MainActivity.TimeDialog";
    private static final String DIALOG_REPEAT_SETTING = "MainActivity.RepeatDialog";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //Initialising the expandable view
        expandableListView = (ExpandableListView) findViewById(R.id.alarm_list);

        alarmList = new AlarmList(this, headings, childItems);
        expandableListView.setAdapter(alarmList);

        // OnClick listener on the FAB icon
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_alarm);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TimePickerFragment dialog = new TimePickerFragment();
                dialog.show(getSupportFragmentManager(), DIALOG_TIME);
            }
        });



        this.context = this;        // setting the context variable


    }

    /**
     * Dispatch onPause() to fragments.
     */
    @Override
    protected void onPause() {
        super.onPause();
        //saving the expanded view headings
        File headerFile = new File(getDir("ClockMap", MODE_PRIVATE), "list");
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(headerFile));
            outputStream.writeObject(headings);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //saving the expanded view childitems
        File file = new File(getDir("ClockMap", MODE_PRIVATE), "map");

        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file));
            outputStream.writeObject(childItems);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Dispatch onResume() to fragments.  Note that for better inter-operation
     * with older versions of the platform, at the point of this call the
     * fragments attached to the activity are <em>not</em> resumed.  This means
     * that in some cases the previous state may still be saved, not allowing
     * fragment transactions that modify the state.  To correctly interact
     * with fragments in their proper state, you should instead override
     * {@link #onResumeFragments()}.
     */
    @Override
    protected void onResume() {
        super.onResume();
        //loading the expanded view headings
        File headerFile = new File(getDir("ClockMap", MODE_PRIVATE), "list");
        File childFile = new File(getDir("ClockMap", MODE_PRIVATE), "map");
       try {
           ObjectInputStream inputStream1  = new ObjectInputStream(new FileInputStream(headerFile));
           headings = (List<String>) inputStream1.readObject();
           inputStream1.close();

           ObjectInputStream inputStream2 = new ObjectInputStream(new FileInputStream(childFile));
           childItems = (HashMap<String, HashMap<String, List<String> > >) inputStream2.readObject();
           inputStream2.close();
           Log.d("Headings", "Childs");
           Log.d( String.valueOf(headings.size()), String.valueOf(childItems.size()));
           if(headings != null || childItems != null ) {
               Log.d("Headings", "Childs");
               Log.d( String.valueOf(headings.size()), String.valueOf(childItems.size()));
                expandableListView = (ExpandableListView) findViewById(R.id.alarm_list);

                alarmList = new AlarmList(this, headings, childItems);
                expandableListView.setAdapter(alarmList);
           }

       } catch (IOException e) {
            e.printStackTrace();
       } catch (ClassNotFoundException e) {
           e.printStackTrace();
       } catch (NullPointerException e) {
           e.printStackTrace();
       }

//        if(headings != null && childItems != null) {
//            expandableListView = (ExpandableListView) findViewById(R.id.alarm_list);
//
//            alarmList = new AlarmList(this, headings, childItems);
//            expandableListView.setAdapter(alarmList);
//        }
    }

    //Creating an expandable List view
    @Override
    public void onFinishDialog(String Time) {
        Log.d("Time: ", Time);
        headings.add(Time);
        HashMap<String, List<String>> innerList = new HashMap<String, List<String>>();
        List<String> repeatValue = new ArrayList<String>();
        repeatValue.add("false");
        innerList.put("Repeat", repeatValue);
        childItems.put(headings.get(headings.size() - 1), innerList);
        alarmList.setAlarm_headers(headings);
        alarmList.setAlarm_list_children(childItems);
        expandableListView.setAdapter(alarmList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    //Method to get the repeat alarm setting value
    @Override
    public void onFinishRepeatDialog(String value, int groupPosition) {

        List<String> repeatSetting = new ArrayList<String>();           //Arraylist storing options like CUSTOM, WEEKDAY, WEEKEND
        List<String> repeatDOW = new ArrayList<>();
        List<Integer> selectedDOW = new ArrayList<>();

        //Initialising all days of week to be unselected
        for(int i = 0; i < 7; i++) {
            repeatDOW.add("false");
        }
        if(value.equals("Custom")) {
            repeatSetting.add("Custom");
            //checking the days selected
            if(childItems.get(headings.get(groupPosition)).get("RepeatDOW") != null) {
                for(int i = 0; i < 7; i++) {
                    if(childItems.get(headings.get(groupPosition)).get("RepeatDOW").get(i).equals("true")) {
                        selectedDOW.add(i);
                    }

                }
            }

            CustomDayRepeatFragment dialog = CustomDayRepeatFragment.newInstance(groupPosition, selectedDOW);
            dialog.show(getSupportFragmentManager(), DIALOG_REPEAT_SETTING);
        } else if(value.equals("Weekday(Mon-Fri)")) {

            //Loop to update the days status
            for(int i = 0; i < 5; i++) {
                repeatDOW.set(i, "true");
            }
            repeatSetting.add("Weekday(Mon-Fri)");
        } else if(value.equals("Weekend(Sat,Sun)")) {
            //Loop to update the days status
            for(int i = 5; i < 7; i++) {
                repeatDOW.set(i, "true");
            }
            repeatSetting.add("Weekend(Sat,Sun)");
        }

        //checking to update state if it already exits else create a new one for RepeatDay
        if(childItems.get(headings.get(groupPosition)).get("RepeatDay") == null) {
            childItems.get(headings.get(groupPosition)).put("RepeatDay", repeatSetting);
        } else {
            childItems.get(headings.get(groupPosition)).get("RepeatDay").set(0, value);
        }

        //checking to update state if it already exits else create a new one for RepeatDOW
        childItems.get(headings.get(groupPosition)).put("RepeatDOW", repeatDOW);



    }

    //callback interface to get the days selected value in case of custom select
    @Override
    public void onFinishDayRepeatListner(List<Integer> days, int groupPosition) {
        for(int i = 0; i < 7; i++) {
            childItems.get(headings.get(groupPosition)).get("RepeatDOW").set(i, "false");
        }
        for(int i = 0; i < days.size(); i++) {
             childItems.get(headings.get(groupPosition)).get("RepeatDOW").set(days.get(i), "true");

        }


    }

//    // A  function accessible by AlarmPopupActivity to send the value
//    public void onConfirmAlarm(String value) {
//        headings.add(value);
//        HashMap<String, List<String>> innerList = new HashMap<String, List<String>>();
//        List<String> repeatValue = new ArrayList<String>();
//        repeatValue.add("false");
//        innerList.put("Repeat", repeatValue);
//        childItems.put(headings.get(headings.size() - 1), innerList);
//        alarmList.setAlarm_headers(headings);
//        alarmList.setAlarm_list_children(childItems);
//        expandableListView.setAdapter(alarmList);
//    }
}
