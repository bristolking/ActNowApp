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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EditAccountActivity extends AppCompatActivity {
    EditText mEditName, mEditEmail, mEditPassWord;
    TextView mName, mEmail;
    ImageView mProfilePhoto;
    UserPrefUtils session;
    String id;
    String name;
    String accountEmail;
    String getId;
    Bitmap bitmap;
    String screen = "NFG";

    String email;
    String password;
    private static final int REQUEST_TAKE_PHOTO = 0;
    private static final int REQUEST_PICK_PHOTO = 2;
    private static final int CAMERA_PIC_REQUEST = 1111;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;

    public static final int MEDIA_TYPE_IMAGE = 1;

    private Uri fileUri;
    private String mediaPath;
    private String mImageFileLocation = " ";
    public static final String IMAGE_DIRECTORY_NAME = "Android File Upload";

    private String postPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        session = new UserPrefUtils( getApplicationContext() );
        setContentView( R.layout.activity_edit_account );
        header();
        if (getIntent() != null && getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            if (bundle.containsKey("screen")) {
                screen = bundle.getString( "screen");
            }
        }
        initializeViews();
        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        if (b != null) {
            id = (String) b.get( "id" );
            name = (String) b.get( "name" );
            mName.setText( "" + name );
            accountEmail = (String) b.get( "email" );
            mEmail.setText( "" + accountEmail );
            System.out.println( "passsed" + name + id + accountEmail + accountEmail );
        }

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
        mName = findViewById( R.id.tv_name );
        mEmail = findViewById( R.id.tv_email );
        mProfilePhoto = findViewById( R.id.image_photo );
        mEditName = findViewById( R.id.et_editName );
        mEditEmail = findViewById( R.id.et_editEmail );
        mEditPassWord = findViewById( R.id.et_editPassword );

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
        String name = mEditName.getText().toString();
        mEditEmail.setError( null );
        String email = mEditEmail.getText().toString();
        mEditPassWord.setError( null );
        String password = mEditPassWord.getText().toString();
        boolean cancel = false;
        View focusView = null;
        if (TextUtils.isEmpty( name )) {
            mEditName.setError( getString(R.string.error_required));
            focusView = mEditName;
            cancel = true;
        }
        if (TextUtils.isEmpty( email )) {
            mEditEmail.setError( getString(R.string.error_required));
            focusView = mEditEmail;
            cancel = true;
        }
        if (TextUtils.isEmpty( password )) {
            mEditPassWord.setError( getString(R.string.error_required));
            focusView = mEditPassWord;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
            HashMap<String, String> useId = session.getUserDetails();
            String id = useId.get( UserPrefUtils.ID );
            if (postPath == null || postPath.equals( "" )) {
                Toast.makeText( getApplicationContext(), "Please select an image", Toast.LENGTH_LONG ).show();
                return;
            } else {
                //File file = new File( postPath );
                File mFolder = new File(getFilesDir() + "/sample");
                File imgFile = new File(mFolder.getAbsolutePath() + "/someimage.png");
                if (!mFolder.exists()) {
                    mFolder.mkdir();
                }
                if (!imgFile.exists()) {
                    try {
                        imgFile.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(imgFile);
                   // bitmap.compress(Bitmap.CompressFormat.PNG,70, fos);
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //RequestBody requestBody = RequestBody.create( MediaType.parse( "*/*" ), file );
                RequestBody surveyBody = RequestBody.create(MediaType.parse("image/*"), imgFile);
                System.out.println( "file2"+ surveyBody);
                MultipartBody.Part image_path = MultipartBody.Part.createFormData("image", imgFile.getName(), surveyBody);
                System.out.println( "severUpload" + image_path );
                Call<UpdateProfileResponses> call = ANApplications.getANApi().checkUpdateProfile( id, name, email, password, image_path );
                System.out.println( "nagrjuna" + id + name + email + password + image_path );
                call.enqueue( new Callback<UpdateProfileResponses>() {
                    @Override
                    public void onResponse(Call<UpdateProfileResponses> call, Response<UpdateProfileResponses> response) {
                        System.out.println( "somedata" + response.raw() );
                        if (response.isSuccessful()) {
                            System.out.println( "profile" + response.raw() );
                            if (response.body().getSuccess().equals( "true" )) {
                                System.out.println( "dataReponse" + response.body().getSuccess() );

                            }else {
                                //Snackbar.make(mContentLayout, "Data Not Found", Snackbar.LENGTH_SHORT).show();

                            }
                        }else {

                        }
                    }

                    @Override
                    public void onFailure(Call<UpdateProfileResponses> call, Throwable t) {
                        Log.d("CallBack", " Throwable is " + t);
                    }
                } );
            }
        }

    }


    private void chooseFile() {
        Intent galleryIntent = new Intent( Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI );
        startActivityForResult( galleryIntent, REQUEST_PICK_PHOTO );
    }

    protected void onActivityResult(int requstCode, int resultCode, Intent data) {
        super.onActivityResult( requstCode, resultCode, data );
        if (resultCode == RESULT_OK) {
            if (requstCode == REQUEST_TAKE_PHOTO || requstCode == REQUEST_PICK_PHOTO) {
                if (data != null) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query( selectedImage, filePathColumn, null, null, null );
                    assert cursor != null;
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex( filePathColumn[0] );
                    mediaPath = cursor.getString( columnIndex );
                    mProfilePhoto.setImageBitmap( BitmapFactory.decodeFile( mediaPath ) );
                    cursor.close();
                    postPath = mediaPath;
                    System.out.println( "imge" + mediaPath );
                }
            } else if (requstCode == CAMERA_PIC_REQUEST) {
                if (Build.VERSION.SDK_INT > 21) {
                    Glide.with( this ).load( mImageFileLocation ).into( mProfilePhoto );
                    postPath = mImageFileLocation;
                } else {
                    Glide.with( this ).load( fileUri ).into( mProfilePhoto );
                    postPath = fileUri.getPath();
                }
            }
        } else if (requstCode != RESULT_CANCELED) {
            Toast.makeText( getApplicationContext(), "Sorry, there was on error!", Toast.LENGTH_LONG ).show();
        }
    }
}
