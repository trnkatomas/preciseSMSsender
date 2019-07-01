package eu.trnkatomas.precisesmssender;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tomas on 31/05/16.
 */

public class PhoneNumbers extends ArrayAdapter {

    private List<String> mObjects; //the "strange Strings"
    private MyFilter mFilter; // my personal filter: this is very important!!
    private final Object mLock=new Object();

    public PhoneNumbers(Context context, int resource) {
        super(context, resource);
    }


    //functions very similar to the ArrayAdapter implementation
    @Override
    public int getCount() {
        return mObjects.size();
    }

    @Override
    public Filter getFilter() {
        if (mFilter==null) {
            mFilter=new MyFilter();
        }
        return mFilter;
    }

    @Override
    public String getItem(int position) {
        return mObjects.get(position);
    }

    public int getPosition(String item) {
        return mObjects.indexOf(item);
    }

    //the trick is here!
    private class MyFilter extends Filter {
        //"constraint" is the string written by the user!
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            //no constraint => nothing to return
            if ((constraint==null)||(constraint.length()==0)) {
                synchronized (mLock) {
                    ArrayList<String> list=new ArrayList<String>();
                    results.values=list;
                    results.count=list.size();
                }
            }
            else {
                String constr=constraint.toString();
                mObjects = new ArrayList<>();
                mObjects.add("nic tu není");
                results.values=mObjects;
                results.count=mObjects.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.count>0) {
                notifyDataSetChanged();
            }
            else {
                notifyDataSetInvalidated();
            }
        }
    }
}