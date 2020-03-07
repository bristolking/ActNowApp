package com.actnow.android.databse;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import com.actnow.android.sdk.responses.ProjectListResponseRecords;

public class ProjectDBHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "projectListDB";
    public static final String TABLE_PROJECTS = "projectoffline";
    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_DUEDATE = "due_date";
    public static final String KEY_PROJECTCODE = "project_code";

    public ProjectDBHelper(@Nullable Context context) {
        super( context, DB_NAME, null, DB_VERSION );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_PROJECTS + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_NAME + " TEXT," + KEY_DUEDATE + " TEXT," + KEY_PROJECTCODE + " TEXT" + ")";
        db.execSQL( CREATE_TABLE );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROJECTS + ";");
        onCreate(db);
    }

    public void insertUserDetails(ProjectListResponseRecords projectListRecords) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cValues = new ContentValues();
        cValues.put( KEY_NAME, projectListRecords.getName() );
        cValues.put( KEY_DUEDATE, projectListRecords.getDue_date() );
        cValues.put( KEY_PROJECTCODE, projectListRecords.getProject_code() );
        long newRowId = db.insert( TABLE_PROJECTS, null, cValues );
        db.close();
    }

    public Cursor getProjectAllData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery( "SELECT * FROM " + TABLE_PROJECTS, null );
        return res;
    }
}
