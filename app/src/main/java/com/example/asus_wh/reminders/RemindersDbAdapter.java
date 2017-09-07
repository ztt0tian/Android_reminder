package com.example.asus_wh.reminders;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by asus-wh on 2017/9/7.
 */

public class RemindersDbAdapter {
    public static final String COL_ID = "_id";
    public static final String COL_CONTENT = "content";
    public static final String COL_IMPORTANT = "important";

    public static final int INDEX_ID = 0;
    public static final int INDEX_CONTENT = INDEX_ID + 1;
    public static final int INDEX_IMPORTANT = INDEX_ID + 2;

    public static final String TAG = "RemindersDbAdapter";

    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String DATABASE_NAME = "dba_remdrs";
    private static final String TABLE_NAME = "tbl_remdrs";
    private static final int VERSION = 1;

    private final Context mCtx;
    //SQL statement to create the database
    private static final String DATABASE_CREATE ="create table if not exists" + TABLE_NAME + "(" + COL_ID + " integer primary key autoincrement, " + COL_CONTENT + " text, " + COL_IMPORTANT + " integer );";

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.w(TAG, DATABASE_CREATE);
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrding database from version" + oldVersion + "to" + newVersion + ",which will destory all old data");
            db.execSQL("drop table if exists " + TABLE_NAME);
            onCreate(db);

        }
    }
    public RemindersDbAdapter(Context ctx){
        this.mCtx=ctx;
    }
    //open
    public void open() throws SQLException
    {
        mDbHelper=new DatabaseHelper(mCtx);
        mDb=mDbHelper.getWritableDatabase();
    }
    //close
    public void close(){
        if(mDbHelper!=null){
            mDbHelper.close();
        }
    }
    //CURD
    //create
    public void createReminder(String name,boolean important){
        ContentValues values=new ContentValues();
        values.put(COL_CONTENT,name);
        values.put(COL_IMPORTANT,important?1:0);
        mDb.insert(TABLE_NAME,null,values);
    }
    //overload to take a reminder
    public long createReminder(Reminder reminder){
        ContentValues values=new ContentValues();
        values.put(COL_CONTENT,reminder.getmContent());
        values.put(COL_IMPORTANT,reminder.getmImportant());
        return mDb.insert(TABLE_NAME,null,values);
    }
    //read
    public Reminder fetchReminderByID(int id){
        Cursor cursor=mDb.query(TABLE_NAME,new String[]{
                COL_ID,COL_CONTENT,COL_IMPORTANT
        },COL_ID+"=?",new String[]{
                String.valueOf(id)
        },null,null,null,null);
        if(cursor!=null) {
            cursor.moveToFirst();
        }
            return new Reminder(cursor.getInt(INDEX_ID),cursor.getString(INDEX_CONTENT),cursor.getInt(INDEX_IMPORTANT));

    }

    public Cursor ftchAllReminders(){
        Cursor mCursor=mDb.query(TABLE_NAME,new String[]{COL_ID,COL_CONTENT,COL_IMPORTANT},null,null,null,null,null);
        if(mCursor!=null){
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    //update
    public void updateReminder(Reminder reminder){
        ContentValues values=new ContentValues();
        values.put(COL_CONTENT,reminder.getmContent());
        values.put(COL_IMPORTANT,reminder.getmImportant());
        mDb.update(TABLE_NAME,values,COL_ID+"=?",new String[]{String.valueOf(reminder.getmId())});
    }
    //delete
    public void deleteReminderById(int nID){
        mDb.delete(TABLE_NAME,COL_ID+"=?",new String[]{String.valueOf(nID)});
    }
    public void deleteAll(){
        mDb.delete(TABLE_NAME,null,null);
    }
}
