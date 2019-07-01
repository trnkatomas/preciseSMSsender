package eu.trnkatomas.precisesmssender;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by tomas on 31/05/16.
 */
public class EventsList extends BaseAdapter{

    private AlarmsTable at;
    private LayoutInflater inflater;
    private Cursor c;
    private Context context;
    private ArrayList<MessageWrapper> messages = new ArrayList<>();

    public EventsList(Context context, AlarmsTable data) {
        at = data;
        inflater = LayoutInflater.from(context);
        c = data.getAllData();
        updateMessages(c);
        this.context = context;
    }

    @Override
    public int getCount() {
        return c.getCount();
    }

    @Override
    public Object getItem(int position) {
        c = at.getAllData();
        updateMessages(c);
        if (position >= messages.size()) {
            return null;
        }else{
            return messages.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        c.moveToPosition(position);
        return c.getLong(0);
    }

    public void updateList(String message){
        if (!message.isEmpty()) {
            Toast.makeText(this.context, message, Toast.LENGTH_SHORT).show();
        }
        c = at.getAllData();
        updateMessages(c);
        notifyDataSetInvalidated();
        notifyDataSetChanged();
    }

    private void updateMessages(Cursor c) {
        messages.clear();
        if (c.moveToFirst()){
            do {
                String name = c.getString(1);
                String message = c.getString(2);
                Long id = c.getLong(0);
                Long time = c.getLong(3);
                Long sent = null;
                if (!c.isNull(4)) {
                    sent = c.getLong(4);
                }
                MessageWrapper mw = new MessageWrapper(message,name,time, sent, id);
                messages.add(mw);
            }while(c.moveToNext());
        }
    }


    //public MessageWrapper getItemAt

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        //c.moveToPosition(position);
        MessageWrapper mw = messages.get(position);

        convertView = inflater.inflate(R.layout.list_item_event, null);
        TextView twNumber = (TextView) convertView.findViewById(R.id.number);
        String name = mw.getNumber();
        //String name = c.getString(1);
        name = (name.indexOf("<") > 0) ? name.substring(0,name.indexOf("<")) : name;
        twNumber.setText(name);

        TextView twMessage = (TextView) convertView.findViewById(R.id.message);
        String text = mw.getMessage();
        text = (text.length() > 10) ? text.substring(0,10)+"..." : text;
        twMessage.setText(text);
        TextView twDate = (TextView) convertView.findViewById(R.id.scheduleTime);
        //DateFormat formatter = new SimpleDateFormat("MM/dd/yy HH:mm");
        twDate.setText(mw.getDateAsString());
        //twDate.setText(formatter.format(new Date(c.getLong(3))));

        ImageButton ib = (ImageButton)convertView.findViewById(R.id.delete_button);
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                at.deleteItem(getItemId(position));
                updateList("Event deleted");
            }
        });

        return convertView;
    }



}

