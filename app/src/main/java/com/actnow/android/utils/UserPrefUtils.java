package com.actnow.android.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.actnow.android.activities.SignInActivity;

import java.util.HashMap;

/**
 * Created by aryan7 on 28/05/19.
 */

public class UserPrefUtils {
    SharedPreferences pref;
    Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "ANPref";
    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String EMAIL = "email";
    public static final String MOBILE = "mobileNumber";
    public static final String PASSWORD= "password";
    public static final String TYPE = "type";
    public static final String PROVIDERID = "provider_id";
    public static final String PROVIDERNAME = "provider_name";
    public  final  static String ORGANIZATIONNAME ="orgn_code";
    public static final String DUE_DATE="due_date";
    public static final String CREATED_DATE="created_date";
    public static final String UPDATED_DATE="updated_date";

    public UserPrefUtils(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSession(String id, String name, String email, String mobile, String orgn_code,String type, String provider_id, String provider_name) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(ID, id);
        editor.putString(NAME, name);
        editor.putString(EMAIL, email);
        editor.putString(MOBILE, mobile);
        editor.putString(ORGANIZATIONNAME,orgn_code);
        editor.putString(TYPE, type);
        editor.putString(PROVIDERID, provider_id);
        editor.putString(PROVIDERNAME, provider_name);
        editor.commit();
    }
    public void checkLogin() {
        if (!this.isLoggedIn()) {
            Intent i = new Intent(_context, SignInActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(i);
        }
    }
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(ID, pref.getString(ID, null));
        user.put(NAME, pref.getString(NAME, null));
        user.put(EMAIL, pref.getString(EMAIL, null));
        user.put(MOBILE, pref.getString(MOBILE, null));
        user.put(TYPE, pref.getString(TYPE, null));
        user.put(PROVIDERID,pref.getString(PROVIDERID,null));
        user.put(PROVIDERNAME,pref.getString(PROVIDERNAME,null));
        user.put(ORGANIZATIONNAME,pref.getString(ORGANIZATIONNAME,null));
        return user;
    }
    public void logoutUser() {
        editor.clear();
        editor.commit();
        Intent i = new Intent(_context, SignInActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(i);
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }


}
