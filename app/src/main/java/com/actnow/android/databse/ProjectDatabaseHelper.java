package com.actnow.android.databse;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ProjectDatabaseHelper extends SQLiteOpenHelper {
    public static String DATABASE_NAME = "project.db";
    public static String  TABLE_NAME = "actNProject_db";

    public static String PROJECTID ="project_id";
    public static String NAME ="name";
    public static String PROJECTCODE ="project_code";
    public static String COLOR ="color";
    public static String PRIORITY ="priority";
    public static String DUEDATE ="due_date";
    public static String PROJECTMEMBERS ="project_members";
    public static String CREATEDBY ="created_by";
    public static String CREATEDDATE ="created_date";
    public static String UPDATEDBY ="updated_by";
    public static String UPDATEDDATE ="updated_date";
    public static String ORNGCODE ="orng_code";
    public static String PARENTPROJECTCODE ="parenrt_project_code";

    public static String CREATE_TABLE = "CREATE TABLE actNProject_db(project_id bigint(75)UNIQUE,name varchar(150),project_code varchar(150),color varchar(150),priority varchar(150),due_date varchar(150),project_members varchar(150),created_by varchar(150),created_date varchar(150),updated_by varchar(150), updated_date varchar(150),orgn_code varchar(150),parenrt_task_code varchar(150))";

    public ProjectDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, null, 1);
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
}
