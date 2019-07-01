package eu.trnkatomas.precisesmssender;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Date;

/**
 * Created by tomas on 31/05/16.
 */

public class AlarmsTable extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String TABLE_NAME = "events";
    private static final String DATABASE_NAME = "events";


    private static final String DICTIONARY_TABLE_CREATE =
            "create table "+ TABLE_NAME + " (id INTEGER PRIMARY KEY, number TEXT, message TEXT, date INTEGER, created INTEGER, sent INTEGER)";
    private SQLiteDatabase db;

    AlarmsTable(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public String insertDummyData(){
        return "insert into events (number, message, date, created) values('773208003','Jsi kokot', "+new Date().getTime()+", "+new Date().getTime()+");";
    }

    public void resetDB(){
        if (db == null) {
            db = this.getWritableDatabase();
        }
        db.execSQL("drop table events");
        db.execSQL(DICTIONARY_TABLE_CREATE);
    }

    public long insertData(Date d, String number, String message, Date cDate){
        if (db == null) {
            db = this.getWritableDatabase();
        }
        ContentValues cv = new ContentValues();
        cv.put("message", message);
        cv.put("number", number);
        cv.put("date", d.getTime());
        cv.put("created", cDate.getTime());
        cv.put("id",cDate.getTime());
        return db.insert(TABLE_NAME, null, cv);

    }

    public void setSentTime(Long id, Date sentTime){
        if (db == null) {
            db = this.getWritableDatabase();
        }
        ContentValues cv = new ContentValues();
        cv.put("sent", sentTime.getTime());
        int gh = db.update(TABLE_NAME, cv, "id = ?", new String[]{id.toString()});
        System.out.println(gh);
    }


    @Override
    public SQLiteDatabase getWritableDatabase() {
        if(db != null){
            return db;
        }
        return super.getWritableDatabase();
    }



    @Override
    public void onCreate(SQLiteDatabase db_new) {
        db_new.execSQL(DICTIONARY_TABLE_CREATE);
        db = db_new;//`this.getWritableDatabase();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void deleteItem(long itemId) {
        String whereClause = "id" + "=?";
        String[] whereArgs = new String[] { String.valueOf(itemId) };
        db = this.getWritableDatabase();
        db.delete("events",whereClause,whereArgs);
    }

    public Cursor getAllData() {
        db = this.getWritableDatabase();
        Cursor c = db.query(TABLE_NAME, new String[]{"id", "number", "message", "date", "sent"},
                null, null, null, null, null);
        return c;
    }
}