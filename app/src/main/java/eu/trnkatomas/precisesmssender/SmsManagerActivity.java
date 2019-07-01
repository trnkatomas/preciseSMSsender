
package eu.trnkatomas.precisesmssender;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SmsManagerActivity extends FragmentActivity {

    private AlarmManagerBroadcastReceiver alarm;
    private AlarmsTable at;
    private DateFormat formatter = new SimpleDateFormat("MM/dd/yy HH:mm:ss.SSS");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_manager);
        alarm = new AlarmManagerBroadcastReceiver();

        final ListView listView = (ListView) findViewById(R.id.listView);

        if (at == null){
            at = new AlarmsTable(this);
            //at.resetDB();
        }

        // Assign adapter to ListView
        final EventsList adapter = new EventsList(this, at);
        listView.setAdapter(adapter);

        Bundle b = getIntent().getExtras();
        if (b != null && b.containsKey("timer")){// && b.get("timer")){
            Toast.makeText(this, "Message sent " + formatter.format(Calendar.getInstance()), Toast.LENGTH_LONG).show();
        }


        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String stringText;
                //show selected
                Long id_2 = parent.getItemIdAtPosition(position);
                Log.i("mrdka", id_2+"");
                AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                // Add the buttons
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                    }
                });

                // Create the AlertDialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });*/
    }

    public void showMessageDialog(View view){
        ListView listView = (ListView) findViewById(R.id.listView);
        int pos = listView.getPositionForView(view);
        System.out.println(pos);
        MessageWrapper mw = (MessageWrapper) listView.getItemAtPosition(pos);

        DialogFragment dialog = MessageDialogFragment.newInstance(mw.getMessage(), mw.getNumber(),
                                                                mw.getDateAsString(), mw.getSentAsString());
        //DialogFragment dialog = new MessageDialogFragment();
        dialog.show(getSupportFragmentManager(),"tag");

    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    public void startRepeatingTimer(View view) {
        Context context = this.getApplicationContext();
        if(alarm != null){
            alarm.SetAlarm(context);
        }else{
            Toast.makeText(context, "Alarm is null", Toast.LENGTH_SHORT).show();
        }
    }

    public void cancelRepeatingTimer(View view){
        Context context = this.getApplicationContext();
        if(alarm != null){
            alarm.CancelAlarm(context);
        }else{
            Toast.makeText(context, "Alarm is null", Toast.LENGTH_SHORT).show();
        }
    }

    public void onetimeTimer(View view){
        Intent i = new Intent(this, eu.trnkatomas.precisesmssender.OneTimeActivity.class);
        //i.setClassName(getBaseContext(),"OneTimeActivity");
        //i.setClassName()
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_widget_alarm_manager, menu);
        return true;
    }


}
