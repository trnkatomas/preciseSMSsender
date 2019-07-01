package eu.trnkatomas.precisesmssender;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

/**
 * Created by tomas on 31/05/16.
 */


public class MessageDialogFragment extends DialogFragment {


    private static Bundle b;

    public static MessageDialogFragment newInstance(String param1, String param2, String param3, String param4) {
        MessageDialogFragment fragment = new MessageDialogFragment();
        Bundle args = new Bundle();
        args.putString("message", param1);
        args.putString("name", param2);
        args.putString("scheduled", param3);
        args.putString("sent", param4);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle b = getArguments();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        // Build the dialog and set up the button click handlers

        View view = getActivity().getLayoutInflater().inflate(R.layout.message_dialog , null);

        TextView tm = (TextView)view.findViewById(R.id.message_dailog);
        tm.setText(b.getString("message"));

        TextView tn = (TextView)view.findViewById(R.id.number_dialog);
        tn.setText(b.getString("name"));

        TextView tsc = (TextView)view.findViewById(R.id.scheduled_dialog);
        tsc.setText(b.getString("scheduled"));

        TextView ts = (TextView)view.findViewById(R.id.sent_dialog);
        ts.setText(b.getString("sent"));


        builder.setView(view);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Send the positive button event back to the host activity
               dialog.dismiss();
            }
        });
        //builder.
        //builder.setView(inflater.inflate(R.layout.custom_dialog, null));
        return builder.create();
    }

    public MessageDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}