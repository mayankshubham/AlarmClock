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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity  implements TimePickerFragment.TimeDialogListner{

    // Defining the Alarm manager variables
    AlarmManager alarm_manager;      /// An alarm instance
    Context context;
    ExpandableListView expandableListView; //
    final List<String> headings = new ArrayList<String>();
    HashMap<String, HashMap<String, List<String> > > childItems = new HashMap<String, HashMap<String, List<String>>>();
    AlarmList alarmList;
    private static final String DIALOG_TIME = "MainActivity.TimeDialog";

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
