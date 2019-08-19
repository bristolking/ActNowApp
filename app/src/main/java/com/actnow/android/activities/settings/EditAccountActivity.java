package com.actnow.android.activities.settings;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.actnow.android.ANApplications;
import com.actnow.android.R;
import com.actnow.android.sdk.responses.UpdateProfileResponses;
import com.actnow.android.utils.UserPrefUtils;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;

public class EditAccountActivity extends AppCompatActivity {
    EditText mEditName,mEditEmail,mEditPassWord;
    TextView mName,mEmail;
    ImageView mProfilePhoto;
    UserPrefUtils session;
    String id;
    String name;
    String accountEmail;
    String getId;
    Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new UserPrefUtils(getApplicationContext());
        setContentView(R.layout.activity_edit_account);
        header();
        initializeViews();
        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        if (b != null) {
            id = (String) b.get("id");
            name = (String) b.get("name");
            mName.setText("" + name);
            accountEmail = (String) b.get("email");
            mEmail.setText("" + accountEmail);
            System.out.println("passsed" + name + id + accountEmail + accountEmail);
        }

    }
    private void header() {
        ImageView imgeBack = (ImageView) findViewById(R.id.image_backsetting);
        imgeBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        TextView tv_title = (TextView) findViewById(R.id.txt_titlesetting);
        tv_title.setText("EDIT ACCOUNT");
        TextView tv_settingDone = (TextView) findViewById(R.id.tv_settingDone);
        tv_settingDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accountUpadte();
            }
        });

    }
    private void initializeViews() {
        mName = findViewById(R.id.tv_name);
        mEmail = findViewById(R.id.tv_email);
        mProfilePhoto = findViewById(R.id.image_photo);
        mEditName = findViewById(R.id.et_editName);
        mEditEmail = findViewById(R.id.et_editEmail);
        mEditPassWord = findViewById(R.id.et_editPassword);

        mProfilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFile();

            }
        });
    }
    private  void  chooseFile(){
        Intent i = new Intent();
         i.setType("image/*");
         i.setAction(Intent.ACTION_GET_CONTENT);
         startActivityForResult(Intent.createChooser(i,"select picture"),1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData()!= null){
            Uri filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
                mProfilePhoto.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
            UploadPicture(getId,getStringImage(bitmap));

        }
    }

    private void UploadPicture(String id, String photo) {

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading......");
        progressDialog.show();

        //StringRequest stringRequest = new StringRequest(Request.Method.POST,urlUpload)



    }
    public String  getStringImage(Bitmap bitmap){

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);

         byte[]  imageByteArray = byteArrayOutputStream.toByteArray();
         String encodeImages = Base64.encodeToString(imageByteArray,Base64.DEFAULT);

        return  encodeImages;

    }
    private void accountUpadte() {
        mEditName.setError(null);
        String name = mEditName.getText().toString();
        mEditEmail.setError(null);
        String email = mEditEmail.getText().toString();
        mEditPassWord.setError(null);
        String password = mEditPassWord.getText().toString();
        boolean cancel = false;
        View focusView = null;
        if (TextUtils.isEmpty(name)) {
            mEditName.setError(getString(R.string.error_required));
            focusView = mEditName;
            cancel = true;
        }
        if (TextUtils.isEmpty(email)) {
            mEditEmail.setError(getString(R.string.error_required));
            focusView = mEditEmail;
            cancel = true;
        }
        if (TextUtils.isEmpty(password)) {
            mEditPassWord.setError(getString(R.string.error_required));
            focusView = mEditPassWord;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
             requstToUpadteProfile(id,name,email,password);
        }
    }

    private void requstToUpadteProfile(String id,String name, String email, String password) {

        Call<UpdateProfileResponses> call = ANApplications.getANApi().checkUpdateProfile(id,name,email,password);
        call.enqueue(new Callback<UpdateProfileResponses>() {
            @Override
            public void onResponse(Call<UpdateProfileResponses> call, Response<UpdateProfileResponses> response) {
                System.out.println("call"+response.raw());
                if (response.isSuccessful()){
                    System.out.println("profile"+ response.raw());
                    if (response.body().getSuccess().equals("true")){
                        System.out.println("data"+ response.body().getSuccess());
                        UpdateProfileResponses responses = response.body();
                        Intent i =new Intent(EditAccountActivity.this,SettingsActivity.class);
                        startActivity(i);
                    }else {

                    }
                }else {

                }
            }
            @Override
            public void onFailure(Call<UpdateProfileResponses> call, Throwable t) {
                Log.d("CallBack", " Throwable is " + t);

            }
        });
    }
    public void onBackPressed() {
        super.onBackPressed();
    }


}
