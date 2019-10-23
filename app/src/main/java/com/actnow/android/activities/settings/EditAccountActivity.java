package com.actnow.android.activities.settings;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;


import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EditAccountActivity extends AppCompatActivity {
    EditText mEditName, mEditEmail, mEditPassWord;
    TextView mName, mEmail;
    UserPrefUtils session;
    ImageView mProfilePhoto;
    String picturePath;

    int RESULT_LOAD_IMAGE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        session = new UserPrefUtils( getApplicationContext() );
        setContentView( R.layout.activity_edit_account );
        header();
        initializeViews();

    }

    private void header() {
        ImageView imgeBack = (ImageView) findViewById( R.id.image_backsetting );
        imgeBack.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        } );
        TextView tv_title = (TextView) findViewById( R.id.txt_titlesetting );
        tv_title.setText( "EDIT ACCOUNT" );
        TextView tv_settingDone = (TextView) findViewById( R.id.tv_settingDone );
        tv_settingDone.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accountUpadte();
            }
        } );

    }

    private void initializeViews() {
        HashMap<String,String> userId = session.getUserDetails();
        String id= userId.get( UserPrefUtils.ID );
        String name = userId.get( UserPrefUtils.NAME);
        String email= userId.get( UserPrefUtils.EMAIL);
        mName = findViewById( R.id.tv_name );
        mName.setText( name );
        mEmail = findViewById( R.id.tv_email );
        mEmail.setText( email);
        mProfilePhoto = findViewById( R.id.image_photo );
        mEditName = findViewById( R.id.et_editName );
        mEditEmail = findViewById( R.id.et_editEmail );
        mEditPassWord = findViewById( R.id.et_editPassword );
        String img = userId.get( UserPrefUtils.IMAGEPATH);
        System.out.println( "img"+ img );
        Glide.with(getApplicationContext())
                .load(img)
                .centerCrop()
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(mProfilePhoto);

        mProfilePhoto.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFile();
                //uploadFile();
            }
        } );
    }
    private void accountUpadte() {
        mEditName.setError( null );
        String userName = mEditName.getText().toString();
        mEditEmail.setError( null );
        String accountEmail = mEditEmail.getText().toString();
        mEditPassWord.setError( null );
        String password = mEditPassWord.getText().toString();
        boolean cancel = false;
        View focusView = null;
        if (TextUtils.isEmpty( userName )) {
            mEditName.setError( getString( R.string.error_required ) );
            focusView = mEditName;
            cancel = true;
        }
        if (TextUtils.isEmpty( accountEmail )) {
            mEditEmail.setError( getString( R.string.error_required ) );
            focusView = mEditEmail;
            cancel = true;
        }
        if (TextUtils.isEmpty( password )) {
            mEditPassWord.setError( getString( R.string.error_required ) );
            focusView = mEditPassWord;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
            HashMap<String, String> useId = session.getUserDetails();
            String uid = useId.get( UserPrefUtils.ID );
            File file = new File( picturePath );
            RequestBody requestFile = RequestBody.create( MediaType.parse( "multipart/form-data" ), file );
            MultipartBody.Part body = MultipartBody.Part.createFormData( "image", file.getName(), requestFile );

            RequestBody id = RequestBody.create( MediaType.parse( "multipart/form-data" ), uid );
            RequestBody name = RequestBody.create( MediaType.parse( "multipart/form-data" ), userName );
            RequestBody mail = RequestBody.create( MediaType.parse( "multipart/form-data" ), accountEmail );
            RequestBody pass = RequestBody.create( MediaType.parse( "multipart/form-data" ), password );
            RequestBody path = RequestBody.create( MediaType.parse( "multipart/form-data" ), picturePath );

            Call<ResponseBody> call = ANApplications.getANApi().profileUpdate( id, name, mail, pass, path, body );
            call.enqueue( new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            System.out.println( "Server Response:1 " + response.body().string() );
                            Intent i = new Intent( getApplicationContext(),AccountSettingActivity.class);
                            startActivity( i );
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        System.out.println( "Server Response:2 " + response.errorBody() );
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    System.out.println( "Server Response:3 " + t.getMessage() );
                }
            } );
        }


    }

    private void chooseFile() {
        Intent i = new Intent( Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI );
        startActivityForResult( i, RESULT_LOAD_IMAGE );
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult( requestCode, resultCode, data );
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            System.out.println( "Image Data: " + filePathColumn );
            Cursor cursor = getContentResolver().query( selectedImage, filePathColumn, null, null, null );
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex( filePathColumn[0] );
            picturePath = cursor.getString( columnIndex );
            cursor.close();

            File file = new File( picturePath );
            Uri imageUri = Uri.fromFile( file );
            System.out.println( "Image Data:pic " + imageUri );

            Glide.with( getApplicationContext() )
                    .load( imageUri )
                    .error( R.drawable.ic_close )
                    .override( 150, 150 ) // Can be 2000, 2000
                    .into( mProfilePhoto );

            //ImageView imageView = (ImageView) findViewById(R.id.user_image);
            //imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }
    }

}
