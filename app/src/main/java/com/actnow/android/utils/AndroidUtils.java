package com.actnow.android.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.actnow.android.R;

/**
 * Created by aryan8 on 28/12/16.
 */

public class AndroidUtils {
    public static boolean isEmailValid(String email){
        //return email.contains("@");
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    public static boolean isMobileValid(String mob){
        return mob.length() == 10;
    }
    public static boolean isPasswordValid(String password) {
        return password.length() > 4;
    }
    public static boolean isEmailorMobileValid(String email){
        String regexStr = "^[0-9]*$";
        if(email.trim().matches(regexStr)){
            return email.length() == 10;
        } else {
            return email.contains("@");
        }
    }
    public static boolean isMatchPassword(String confirmpass, String password){
        return confirmpass.equals(password);
    }
    public static void displayToast(Context context, int messageId) {
        displayToast(context, context.getString(messageId));
    }

    public static void displayToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
    public static void sidebarToast(Context context, String message, Button button){
        int location[]=new int[2];
        button.getLocationOnScreen(location);
        Toast toast= Toast.makeText(context,message, Toast.LENGTH_SHORT);
        View toastView=toast.getView();
        toast.setGravity(Gravity.TOP| Gravity.LEFT,button.getRight()+5,location[1]-70);
        toastView.setBackgroundResource(R.drawable.toast);
        TextView text = toastView.findViewById(android.R.id.message);
        text.setTextColor(Color.WHITE);
        toast.show();
    }
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        // ARE WE CONNECTED TO THE NET
        return conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() && conMgr.getActiveNetworkInfo().isConnected();
    }
    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public static void showProgress(final boolean show, final View mProgressView, final View mContentLayout) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = 200;
            mContentLayout.setVisibility(show ? View.GONE : View.VISIBLE);
            mContentLayout.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mContentLayout.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mContentLayout.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}
