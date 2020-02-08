package com.actnow.android.databse;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import com.actnow.android.sdk.responses.TaskListRecords;

public class TaskDBHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "taskListdb";
    private static final String TABLE_Users = "tasklistoffline";
    public static final String KEY_ID = "task_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_DUEDATE = "due_date";
    public static final String KEY_PRIORITY = "priority";
    public static final String KEY_PROJECT_CODE = "project_code";
    public static final String KEY_TASK_CODE = "task_code";
    public static final String KEY_REMINDARS_COUNT = "remindars_count";
    public static final String KEY_STATUS = "status";
    public static final String KEY_PROJECT_NAME = "project_name";
    public static final String KEY_REPEAT_TYPE = "repeat_type";


    public TaskDBHelper(@Nullable Context context) {
        super( context, DB_NAME, null, DB_VERSION );
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TABLE = "CREATE TABLE " + TABLE_Users + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_NAME + " TEXT," + KEY_DUEDATE + " TEXT," + KEY_PRIORITY + " TEXT,"
                + KEY_PROJECT_CODE + " TEXT," + KEY_TASK_CODE + " TEXT," + KEY_REMINDARS_COUNT + " TEXT," + KEY_STATUS + " TEXT," + KEY_PROJECT_NAME + " TEXT," + KEY_REPEAT_TYPE + " TEXT" + ")";
        db.execSQL( CREATE_TABLE );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_Users);
        onCreate( db );
    }

    public void insertTaskDetails(TaskListRecords listRecords) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cValues = new ContentValues();
        cValues.put( KEY_NAME, listRecords.getName() );
        cValues.put( KEY_DUEDATE, listRecords.getDue_date() );
        cValues.put( KEY_PRIORITY, listRecords.getPriority() );
        cValues.put( KEY_PROJECT_CODE, listRecords.getProject_code() );
        cValues.put( KEY_TASK_CODE, listRecords.getTask_code() );
        cValues.put( KEY_REMINDARS_COUNT, listRecords.getRemindars_count() );
        cValues.put( KEY_STATUS, listRecords.getStatus() );
        cValues.put( KEY_PROJECT_NAME, listRecords.getProject_name() );
        cValues.put( KEY_REPEAT_TYPE, listRecords.getRepeat_type() );
        long newRowId = db.insert( TABLE_Users, null, cValues );
        db.close();
    }

   /* public TaskListRecords getData(TaskListRecords stdModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        TaskListRecords taskListRecords = new TaskListRecords();
        String recName = stdModel.getName();
        Cursor c = db.rawQuery( "select * from android where name = '" + recName + "'", null );
        {
            while (c.moveToNext()) {
                taskListRecords.setName(c.getString(c.getColumnIndex(KEY_NAME)));
                taskListRecords.setDue_date(c.getString(c.getColumnIndex(KEY_DUEDATE)));
                taskListRecords.setPriority(c.getString(c.getColumnIndex(KEY_PRIORITY)));
                taskListRecords.setProject_code(c.getString(c.getColumnIndex(KEY_PROJECT_CODE)));
                taskListRecords.setTask_code(c.getString(c.getColumnIndex(KEY_TASK_CODE)));
                taskListRecords.setRemindars_count(c.getString(c.getColumnIndex(KEY_REMINDARS_COUNT)));
                taskListRecords.setStatus(c.getString(c.getColumnIndex(KEY_STATUS)));
                taskListRecords.setProject_name(c.getString(c.getColumnIndex(KEY_PROJECT_NAME)));
                taskListRecords.setRepeat_type(c.getString(c.getColumnIndex(KEY_REPEAT_TYPE)));

            }
            c.close();
        }
        return taskListRecords;

    }*/
    public Cursor getAllData( ){
        SQLiteDatabase db = getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM "+TABLE_Users,null);
        return res;
    }
}
