package com.actnow.android.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.actnow.android.ANApplications;
import com.actnow.android.R;
import com.actnow.android.sdk.responses.SignInResponse;;
import com.actnow.android.sdk.responses.SignUpResponse;
import com.actnow.android.utils.AndroidUtils;
import com.actnow.android.utils.UserPrefUtils;
import com.facebook.FacebookSdk;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity implements
        View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener{
    UserPrefUtils session;
    View mProgressView,mContentLayout;
    EditText mEmail,mPass;
    Button mLogin,mSignUp /*mFacebookButton,mGoogleButton*/;
    TextView mForgotPassWord;


    private GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;
    private SignInButton btnSignIn;
    private static final int RC_SIGN_IN = 007;
    String fullName;
    String email;
    String providerId;
    String providerName = "actNowapp";
    ImageView imgLogo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        session = new UserPrefUtils(getApplicationContext());
        if(session.isLoggedIn()){
            activityMe();
        }
        setContentView(R.layout.activity_sign_in);
        initializeViews();
    }
    private void initializeViews() {
        imgLogo =(ImageView)findViewById(R.id.img_logo);
        mProgressView = findViewById(R.id.progress_bar);
        mContentLayout = findViewById(R.id.content_layout);
        mEmail = findViewById(R.id.et_loginEmail);
        mPass = findViewById(R.id.et_loginPassword);
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
        btnSignIn = (SignInButton) findViewById(R.id.sign_in_button);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi( Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        btnSignIn.setOnClickListener(this);


    }
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.sign_in_button:
                providerName = ("Google");
                signIn();
                break;
        }
    }
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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

            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
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
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            fullName = acct.getDisplayName();
            email = acct.getEmail();
            providerId= acct.getId();
            //String personPhotoUrl = acct.getPhotoUrl().toString();
            Log.e("googlelogin", "Name: " +fullName + ", email: " + email + "id:" + providerId  +",providerName :" + providerName );
           /* Glide.with(getApplicationContext()).load()
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy( DiskCacheStrategy.ALL)
                    .into(imgLogo);*/
          /*  Glide.with(getApplicationContext())
                    .load(personPhotoUrl)
                    .centerCrop()
                    .into(imgLogo);*/
            updateUI(true);
        } else {
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
            Call<SignUpResponse> call = ANApplications.getANApi().userSignUp(fullName,email,providerId,providerName," "," " );
            System.out.println( "goolgeSignup" + fullName + email +providerId + providerName );
            call.enqueue( new Callback<SignUpResponse>() {
                @Override
                public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response) {
                    System.out.println( "goolgereponse"+ response.raw());
                    AndroidUtils.showProgress(false,mProgressView,mContentLayout);
                    if (response.isSuccessful()){
                        System.out.println( "googleReonse" + response.raw() );
                        if (response.body().getSuccess().equals("true")){
                            SignUpResponse response2= response.body();
                            session.createLoginSession(response2.getId(),response2.getName(),response2.getEmail(),response2.getMobile_number(),response2.getOrgn_code(),response2.getUser_type(),response2.getProvider_id(),response2.getProvider_name(),response2.getImage_path());

                            System.out.println( "imge" + response2.getImage_path());
                            activityMe();
                            AndroidUtils.displayToast(getApplicationContext(),"Your account has been Login");

                        } else {
                            Snackbar.make(mContentLayout, "Invalid credentials", Snackbar.LENGTH_SHORT).show();
                        }
                    }else {
                        AndroidUtils.displayToast(getApplicationContext(), "Something Went Wrong!!");
                    }
                }
                @Override
                public void onFailure(Call<SignUpResponse> call, Throwable t) {
                    Log.d("CallBack", " Throwable is " + t);
                }
            });

        } else {
            btnSignIn.setVisibility(View.VISIBLE);
        }
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
