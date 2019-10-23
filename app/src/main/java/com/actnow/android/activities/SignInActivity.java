package com.actnow.android.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.actnow.android.ANApplications;
import com.actnow.android.R;
import com.actnow.android.sdk.responses.SignInResponse;;
import com.actnow.android.utils.AndroidUtils;
import com.actnow.android.utils.UserPrefUtils;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity {
    UserPrefUtils session;
    View mProgressView,mContentLayout;
    EditText mEmail,mPass;
    Button mLogin,mSignUp,mFacebookButton,mGoogleButton;
    TextView mForgotPassWord;
    CheckBox mDisclaimer;

    private GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;
    private SignInButton btnSignIn;
    private static final int RC_SIGN_IN = 007;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new UserPrefUtils(getApplicationContext());
        if(session.isLoggedIn()){
            activityMe();
        }
        setContentView(R.layout.activity_sign_in);
        initializeViews();
    }
    private void initializeViews() {
        mProgressView = findViewById(R.id.progress_bar);
        mContentLayout = findViewById(R.id.content_layout);
        mEmail = findViewById(R.id.et_loginEmail);
        mPass = findViewById(R.id.et_loginPassword);
      //  mDisclaimer = (CheckBox)findViewById(R.id.chk_disclaimer);

        //btnSignIn = (SignInButton) findViewById(R.id.btn_sign_in);
        mGoogleButton =(Button)findViewById( R.id.bt_google);
        mGoogleButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText( getApplicationContext(),"Work in progress!",Toast.LENGTH_SHORT).show();
            }
        } );
        mFacebookButton =(Button)findViewById( R.id.bt_facebook);
        mFacebookButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText( getApplicationContext(),"Work in progress!",Toast.LENGTH_SHORT).show();

            }
        } );
        mForgotPassWord= findViewById(R.id.forgot_password);
        mForgotPassWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityForgot();
            }
        });
        mLogin = findViewById(R.id.bt_login);
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });
        mSignUp = findViewById(R.id.bt_signup);
        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activitySignUp();
            }
        });
       /* GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi( Auth.GOOGLE_SIGN_IN_API, gso)
                .build();*/

       // btnSignIn.setOnClickListener(this);
    }

    private void attemptLogin(){
        mEmail.setError(null);
        String email = mEmail.getText().toString();
        mPass.setError(null);
        String pass  = mPass.getText().toString();
        boolean cancel = false;
        View focusView = null;
        if(TextUtils.isEmpty(email)){
            mEmail.setError(getString(R.string.error_required));
            focusView = mEmail;
            cancel = true;
        } else if (!AndroidUtils.isEmailorMobileValid(email)){
            mEmail.setError("Invalid Mobile/Email field");
            focusView = mEmail;
            cancel = true;
        }
        if(TextUtils.isEmpty(pass)){
            mPass.setError(getString(R.string.error_required));
            focusView = mPass;
            cancel = true;
        }
        if(cancel){
            focusView.requestFocus();
        } else {
            requestLogin(email, pass);
            System.out.println( "logindata"+email+pass);

           /* if (mDisclaimer.isChecked()) {
                AndroidUtils.showProgress(true,mProgressView,mContentLayout);
                requestLogin(email, pass);
            } else {
                Snackbar.make(mContentLayout, "Accept Terms of Service & Privacy Policy", Snackbar.LENGTH_SHORT).show();
            }*/
        }
    }
    private void requestLogin(String username, String password){
        System.out.println( "userDeatails"+username+password);

        Call<SignInResponse> call = ANApplications.getANApi().userSignIn(username,password);
        call.enqueue(new Callback<SignInResponse>() {
            @Override
            public void onResponse(Call<SignInResponse> call, Response<SignInResponse> response) {
                System.out.println( "reponse"+ response.raw());
                AndroidUtils.showProgress(false,mProgressView,mContentLayout);
                if (response.isSuccessful()){
                    if (response.body().getSuccess().equals("true")){
                        SignInResponse response1 = response.body();
                        session.createLoginSession(response1.getId(),response1.getName(),response1.getEmail(),response1.getMobile_number(),response1.getOrgn_code(),response1.getUser_type(),response1.getProvider_id(),response1.getProvider_name(),response1.getImage_path());
                        System.out.println("response1"+getTaskId());
                        //AndroidUtils.displayToast(getApplicationContext(),"Your account has been successfully created.");
                       activityMe();
                    } else {
                        Snackbar.make(mContentLayout, "Invalid credentials", Snackbar.LENGTH_SHORT).show();
                    }
                }else {
                    AndroidUtils.displayToast(getApplicationContext(), "Something Went Wrong!!");
                }
            }
            @Override
            public void onFailure(Call<SignInResponse> call, Throwable t) {
                Log.d("CallBack", " Throwable is " + t);
            }
        });
    }
 /*   public void onClick(View v) {
        int id = v.getId();
*//*
        *//**//*switch (id) {
            case R.id.btn_sign_in:
                signIn();
                break;*//**//*
        }*//*
    }
*/
    /*private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }
    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            //Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    private void handleSignInResult(GoogleSignInResult result) {
        //Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            String personName = acct.getDisplayName();
            String personPhotoUrl = acct.getPhotoUrl().toString();
            String email = acct.getEmail();
            Log.e("googlelogin", "Name: " + personName + ", email: " + email + ", Image: " + personPhotoUrl);

       *//*     Glide.with(getApplicationContext()).load(personPhotoUrl)
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy( DiskCacheStrategy.ALL)
                    .into(imgProfilePic);*//*
            updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false);
        }
    }



    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    private void updateUI(boolean isSignedIn) {
        if (isSignedIn) {
            btnSignIn.setVisibility(View.GONE);
        } else {
            btnSignIn.setVisibility(View.VISIBLE);
        }
    }*/
    private void activityMe(){
        Intent i =new Intent(SignInActivity.this, TodayTaskActivity.class);
        startActivity(i);
        finish();
    }

    private void activitySignUp(){
        Intent me = new Intent(getApplicationContext(),SignUpActivity.class);
        startActivity(me);
        overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out);
        finish();
    }
    private void activityForgot() {
        Intent forgot = new Intent(getApplicationContext(),ForgotPasswordActivity.class);
        startActivity(forgot);
        overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out);
        finish();
    }
    private static long back_pressed;
    @Override
    public void onBackPressed() {
        if (back_pressed + 2000 > System.currentTimeMillis()){
            moveTaskToBack(true);
            overridePendingTransition(R.anim.from_left_in, R.anim.from_right_out);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        } else {
            Snackbar.make(mContentLayout, "Press once again to exit", Snackbar.LENGTH_SHORT).show();
            back_pressed = System.currentTimeMillis();
        }
    }

}
