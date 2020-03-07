package com.actnow.android.databse;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import com.actnow.android.sdk.responses.OrgnUserRecordsCheckBox;

public class IndividualDBHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "individvalDB";
    private static final String TABLE_ORNG = "orgnUseroffline";
    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_MOBILE_NUMBER = "mobile_number";

    public IndividualDBHelper(@Nullable Context context) {
        super( context, DB_NAME, null, DB_VERSION );

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_ORNG + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_NAME + " TEXT," + KEY_EMAIL + " TEXT," +  KEY_MOBILE_NUMBER + " TEXT" + ")";
        sqLiteDatabase.execSQL( CREATE_TABLE );

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_ORNG + ";");
        onCreate(sqLiteDatabase);
    }
    public void insertOrngDetails(OrgnUserRecordsCheckBox orgnUserRecordsCheckBox) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cValues = new ContentValues();
        cValues.put( KEY_NAME, orgnUserRecordsCheckBox.getName() );
        cValues.put( KEY_EMAIL, orgnUserRecordsCheckBox.getEmail());
        cValues.put(KEY_MOBILE_NUMBER,orgnUserRecordsCheckBox.getMobile_number());
        long newRowId = db.insert( TABLE_ORNG, null, cValues );
        db.close();
    }
    public Cursor getAllData( ){
        SQLiteDatabase db = getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM "+TABLE_ORNG,null);
        return res;
    }
}

