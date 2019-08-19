package com.actnow.android.databse;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.actnow.android.sdk.responses.Task;

public class TaskDatabaseHelper extends SQLiteOpenHelper {

    public static String DATABASE_NAME = "task.db";
    public static String TABLE_NAME = "actNTask_db";
    public static String TASKID = "task_id";
    public static String  NAME = "name";
    public static String  TASKCODE = "task_code";
    public static String  PROJECTCODE = "project_code";
    public static String  PRIORITY = "priority";
    public static String  DUEDATE = "due_date";
    public static String  TASKMEMBERS = "task_members";
    public static String  STATUS = "status";
    public static String  APPROVALSTATUS = "approval_status";
    public static String  CREATEDBY = "created_by";
    public static String  CREATEDDATE = "created_date";
    public static String  UPDATEDBY ="updated_by";
    public static String  UPDATEDDATE ="updated_date";
    public static String  ORGNCODE  ="orgn_code";
    public static String  REPEATTYPE ="repeat_type";
    public static String  REPEATMONTHS ="repeat_months";
    public static String  REPEATWEEK ="repeat_week";
    public static String  REPEATDAYS ="repeat_days";
    public static String  PARENTTASKCODE ="parenrt_task_code";

    public static String CREATE_TABLE =" CREATE TABLE actNTask_db(task_id bigint(75)UNIQUE,name varchar(150),task_code varchar(150),project_code varchar(150),priority varchar(150),due_date varchar(150),task_members varchar(150),status varchar(150),approval_status varchar(150),created_by varchar(150),created_date varchar(150),updated_by varchar(150), updated_date varchar(150),orgn_code varchar(150),repeat_type varchar(150),repeat_months varchar(150),repeat_week varchar(150),repeat_days varchar(150),parenrt_task_code varchar(150))";


    public TaskDatabaseHelper(Context context) {
        super(context, DATABASE_NAME,null,  1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

  public  long insertData(Task task){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TASKID,task.getId());
        contentValues.put(NAME,task.getName());
        contentValues.put(TASKCODE,task.getTask_code());
        contentValues.put(PROJECTCODE,task.getProject_code());
        contentValues.put(PRIORITY,task.getPriority());
        contentValues.put(DUEDATE,task.getDue_date());
        contentValues.put(TASKMEMBERS,task.getTask_members());
        contentValues.put(STATUS,task.getStatus());
        contentValues.put(APPROVALSTATUS,task.getApproval_status());
        contentValues.put(CREATEDBY,task.getCreated_by());
        contentValues.put(CREATEDDATE,task.getCreated_date());
        contentValues.put(UPDATEDBY,task.getUpdated_by());
        contentValues.put(UPDATEDDATE,task.getUpdated_date());
        contentValues.put(ORGNCODE,task.getOrgn_code());
        contentValues.put(REPEATTYPE,task.getRepeat_type());
        contentValues.put(REPEATMONTHS,task.getRepeat_months());
        contentValues.put(REPEATWEEK,task.getRepeat_week());
        contentValues.put(REPEATDAYS,task.getRepeat_days());
        contentValues.put(PARENTTASKCODE,task.getParenrt_task_code());

        long  id = sqLiteDatabase.insertWithOnConflict(TABLE_NAME,null,contentValues,SQLiteDatabase.CONFLICT_REPLACE);
        return id;
  }
    public Cursor getAllData(){
        SQLiteDatabase db = getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM "+TABLE_NAME,null);
        return res;
    }

    public   Task getSpecificTask(Task task){
        SQLiteDatabase db= this.getWritableDatabase();
        Task task1 = new Task();
        String taskId = task.getId();
        Cursor c= db.rawQuery("SELECT * FROM "+TABLE_NAME +"WHERE" +TASKID +" = '"+ taskId+"'",null  );
        {
            while (c.moveToNext()){
                task1.setId(c.getString(c.getColumnIndex(TASKID)));
                task1.setName(c.getString(c.getColumnIndex(NAME)));
                task1.setTask_code(c.getString(c.getColumnIndex(TASKCODE)));
                task1.setProject_code(c.getString(c.getColumnIndex(PROJECTCODE)));
                task1.setPriority(c.getString(c.getColumnIndex(PRIORITY)));
                task1.setDue_date(c.getString(c.getColumnIndex(DUEDATE)));
                task1.setTask_members(c.getString(c.getColumnIndex(TASKMEMBERS)));
                task1.setStatus(c.getString(c.getColumnIndex(STATUS)));
                task1.setApproval_status(c.getString(c.getColumnIndex(APPROVALSTATUS)));
                task1.setCreated_by(c.getString(c.getColumnIndex(CREATEDBY)));
                task1.setCreated_date(c.getString(c.getColumnIndex(CREATEDDATE)));
                task1.setUpdated_by(c.getString(c.getColumnIndex(UPDATEDBY)));
                task1.setUpdated_date(c.getString(c.getColumnIndex(UPDATEDDATE)));
                task1.setOrgn_code(c.getString(c.getColumnIndex(ORGNCODE)));
                task1.setRepeat_type(c.getString(c.getColumnIndex(REPEATTYPE)));
                task1.setRepeat_months(c.getString(c.getColumnIndex(REPEATMONTHS)));
                task1.setRepeat_days(c.getString(c.getColumnIndex(REPEATDAYS)));
                task1.setRepeat_week(c.getString(c.getColumnIndex(REPEATWEEK)));
                task1.setParenrt_task_code(c.getString(c.getColumnIndex(PARENTTASKCODE)));
            }
            c.close();
        }
        return task1;
    }
}
