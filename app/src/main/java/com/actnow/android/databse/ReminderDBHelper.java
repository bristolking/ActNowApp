package com.actnow.android.databse;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import com.actnow.android.sdk.responses.ReminderTaskReinders;

public class ReminderDBHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "reminderDB";
    public static final String TABLE_Users = "reminderoffline";
    public static final String KEY_REMINDER_TASK_ID = "reminder_task_id";
    public static final String KEY_REMINDER_TASK_CODE = "task_code";
    public static final String KEY_REMINDER_DATE = "reminder_date";
    public static final String KEY_REMINDER_USER_ID = "user_id";

    public static final String KEY_REMINDER_STATUS = "status";
    public static final String KEY_REMINDER_CREATED_DATE = "created_date";
    public static final String KEY_REMINDER_REMID_TO = "remind_to";
    public static final String KEY_REMINDER_USER_NAME = "user_name";


    public ReminderDBHelper(@Nullable Context context) {
        super( context, DB_NAME, null, DB_VERSION );
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_Users + "(" + KEY_REMINDER_TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_REMINDER_TASK_CODE + " TEXT,"
                + KEY_REMINDER_DATE + " TEXT," + KEY_REMINDER_STATUS + " TEXT ,"
                + KEY_REMINDER_CREATED_DATE + " TEXT ," + KEY_REMINDER_REMID_TO + " TEXT,"
                + KEY_REMINDER_USER_NAME + " TEXT," + KEY_REMINDER_USER_ID + " TEXT " + ")";

        db.execSQL( CREATE_TABLE );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL( " DROP TABLE IF EXISTS  " + TABLE_Users );
        onCreate( db );
    }

    public void insertUserReminder(ReminderTaskReinders reminderTaskReinders){
        SQLiteDatabase  db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues( );
        contentValues.put( KEY_REMINDER_TASK_CODE,reminderTaskReinders.getTask_code());
        contentValues.put(KEY_REMINDER_DATE,reminderTaskReinders.getReminder_date());
        contentValues.put( KEY_REMINDER_STATUS,reminderTaskReinders.getStatus());
        contentValues.put( KEY_REMINDER_CREATED_DATE,reminderTaskReinders.getCreated_date());
        contentValues.put( KEY_REMINDER_REMID_TO,reminderTaskReinders.getRemind_to());
        contentValues.put( KEY_REMINDER_USER_NAME,reminderTaskReinders.getUser_name());
        contentValues.put( KEY_REMINDER_USER_ID,reminderTaskReinders.getUser_id());
        long newReminder = db.insert(TABLE_Users,null,contentValues);
        db.close();
    }
    public Cursor getReminderAllData(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "SELECT * FROM " + TABLE_Users, null);
        return  res;

    }
}
