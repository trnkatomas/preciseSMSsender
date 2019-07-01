package eu.trnkatomas.precisesmssender;

import android.support.v4.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by tomas on 31/05/16.
 */

public class OneTimeActivity extends FragmentActivity implements CustomDialogFragment.NoticeDialogListener{


    private AlarmManagerBroadcastReceiver alarm;
    private AlarmsTable at;
    private SQLiteDatabase db;
    private DateFormat formatter = new SimpleDateFormat("MM/dd/yy HH:mm");
    private Date sendingData = new Date();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_timer);
        //TimePicker tp = (TimePicker)findViewById(R.id.timePicker);
        //tp.setIs24HourView(true);
        alarm = new AlarmManagerBroadcastReceiver();

        if (at == null){
            at = new AlarmsTable(this);
            db = at.getWritableDatabase();
        }

        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null);
        String[] contacts = new String[phones.getCount()];
        int counter = 0;
        while (phones.moveToNext())
        {
            String name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            contacts[counter++] = name + "<" + phoneNumber + ">";
        }
        phones.close();

        // Get a reference to the AutoCompleteTextView in the layout
        AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.autocomplete_number);
        // Get the string array
        // Create the adapter and set it to the AutoCompleteTextView
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, contacts);
        textView.setAdapter(adapter);
    }


    public void showNoticeDialog(View view) {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new CustomDialogFragment();
        dialog.show(getSupportFragmentManager(),"tag");
    }

    // The dialog fragment receives a reference to this Activity through the
    // Fragment.onAttach() callback, which it uses to call the following methods
    // defined by the NoticeDialogFragment.NoticeDialogListener interface
    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        TimePicker tp = (TimePicker) dialog.getDialog().findViewById(R.id.timePicker);
        DatePicker dp = (DatePicker) dialog.getDialog().findViewById(R.id.datePicker);
        int hour = tp.getCurrentHour();
        int minute = tp.getCurrentMinute();
        int year = dp.getYear();
        int day = dp.getDayOfMonth();
        int month = dp.getMonth()+1;
        try {
            sendingData = formatter.parse(month + "/" + day + "/" + year%100 + " " + hour + ":" + minute);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        TextView ed = (TextView) findViewById(R.id.dateText);
        ed.setText(formatter.format(sendingData));
        dialog.dismiss();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        // User touched the dialog's negative button
        dialog.dismiss();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_one_timer, menu);
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

    public void onetimeTimer(View view){
        Context context = this.getApplicationContext();
        AutoCompleteTextView number = (AutoCompleteTextView) findViewById(R.id.autocomplete_number);
        EditText message = (EditText) findViewById(R.id.message);
        String numberS = number.getText().toString();
        String messageS = message.getText().toString();
        Date cDate = new Date();

        if(alarm != null){
            Toast.makeText(context, "Alarm is to " + formatter.format(sendingData), Toast.LENGTH_SHORT).show();
            Date d = new Date(sendingData.getTime()-100);
            //alarm.setOnetimeTimer(context, sendingData, messageS, numberS, cDate.getTime());
            alarm.setOnetimeTimer(context, d, messageS, numberS, cDate.getTime());
            at.insertData(sendingData, numberS, messageS, cDate);
            Intent i = new Intent(this, SmsManagerActivity.class);
            startActivity(i);
            finish();
        }else{
            Toast.makeText(context, "Alarm is null", Toast.LENGTH_SHORT).show();
        }
    }
}
