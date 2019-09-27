package com.actnow.android.activities.settings;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
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
import com.actnow.android.BuildConfig;
import com.actnow.android.R;
import com.actnow.android.sdk.responses.UpdateProfileResponses;
import com.actnow.android.utils.UserPrefUtils;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;

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
    private  static  final int REQUEST_TAKE_PHOTO = 0;
    private static  final  int REQUEST_PICK_PHOTO = 2;
    private static  final int CAMERA_PIC_REQUEST =1111;
    private  static  final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE =100;

    public  static  final int MEDIA_TYPE_IMAGE=1;

    private  Uri fileUri;
    private String  mediaPath;
    private  String mImageFileLocation= " ";
    public static  final String IMAGE_DIRECTORY_NAME= "Android File Upload";

    private String postPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        session = new UserPrefUtils( getApplicationContext() );
        setContentView( R.layout.activity_edit_account );
        header();
        if (getIntent() != null && getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            if (bundle.containsKey( "screen" )) {
                screen = bundle.getString( "screen" );
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
                //Toast.makeText( getApplicationContext(), "account Update", Toast.LENGTH_SHORT ).show();
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
            mEditName.setError( getString( R.string.error_required ) );
            focusView = mEditName;
            cancel = true;
        }
        if (TextUtils.isEmpty( email )) {
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
            String id = useId.get( UserPrefUtils.ID );
            uploadFile( id, name, email, password );

        }
    }
        private void uploadFile(String id,String name,String email,String password) {

        System.out.println( "values"+id+name+email+password);

        if (postPath == null || postPath.equals( "" )){
            Toast.makeText( getApplicationContext(),"Please select an image" ,Toast.LENGTH_LONG).show();
            return;
        }else {
            File file = new File( postPath );
            RequestBody requestBody = RequestBody.create( MediaType.parse( "*/*" ), file );
            MultipartBody.Part body = MultipartBody.Part.createFormData( "image", "image.jpg", requestBody );
            System.out.println( "file"+ body);
            Call<UpdateProfileResponses> call = ANApplications.getANApi().checkUpdateProfile( id, name, email,password, body );
            System.out.println( "nagrjuna" + id + name + email + password + body );

            call.enqueue( new Callback<UpdateProfileResponses>() {
                @Override
                public void onResponse(Call<UpdateProfileResponses> call, Response<UpdateProfileResponses> response) {
                    if (response.isSuccessful()) {
                        System.out.println( "profile" + response.raw() );
                        if (response.body().getSuccess().equals( "true" )) {
                            System.out.println( "dataReponse" + response.body().getSuccess() );

                        }
                    }
                }

                @Override
                public void onFailure(Call<UpdateProfileResponses> call, Throwable t) {

                }
            } );

        }

    }
    private  void captureImage(){
        if (Build.VERSION.SDK_INT>21){
            Intent  callCameraAppicationIntent = new Intent(  );
            callCameraAppicationIntent.setAction( MediaStore.ACTION_IMAGE_CAPTURE);
            File photofile = null;
            try {
                photofile = createImagrFile();

            }catch (IOException e){
                Logger.getAnonymousLogger().info( "Exception error in generating the file" );
                e.printStackTrace();
            }
            Uri outputUri= FileProvider.getUriForFile( this, BuildConfig.APPLICATION_ID +".provider",photofile);
            callCameraAppicationIntent.putExtra( MediaStore.EXTRA_OUTPUT,outputUri);
            callCameraAppicationIntent.addFlags( Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION );
            Logger.getAnonymousLogger().info( "Calling the Camera App by Intent" );
            startActivityForResult( callCameraAppicationIntent,CAMERA_PIC_REQUEST );
        }else {
            Intent intent  = new Intent( MediaStore.ACTION_IMAGE_CAPTURE );
            //fileUri =  getOutPutMediaFileUri(MEDIA_TYPE_IMAGE);
            intent.putExtra( MediaStore.EXTRA_OUTPUT,fileUri);
            startActivityForResult( intent,CAMERA_PIC_REQUEST );
        }
    }

 /*   private Uri getOutPutMediaFileUri(int mediaTypeImage) {

    }*/

    File createImagrFile() throws IOException {
        Logger.getAnonymousLogger().info( "Generating the Image -method started" );
        String timeStamp = new SimpleDateFormat("yyyyyMMdd_HHmmSS").format( new Date(  ) );
        String  imagFileName = "IMAGE_"+timeStamp;

        File storgeDirectory = Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_PICTURES +"/photo_saving_app");
        Logger.getAnonymousLogger().info( "Stroge directory set" );
        if (!storgeDirectory.exists()) storgeDirectory.mkdir();
        File image = new File( storgeDirectory,imagFileName + "jpg" );
        Logger.getAnonymousLogger().info( "File name and path set" );
        mImageFileLocation = image.getAbsolutePath();
        return image;


     }

    private void chooseFile() {
        Intent galleryIntent = new Intent( Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult( galleryIntent,REQUEST_PICK_PHOTO);
    }
    protected  void onActivityResult(int requstCode,int resultCode,Intent data){
        super.onActivityResult( requstCode,resultCode,data );
        if (resultCode == RESULT_OK){
            if (requstCode == REQUEST_TAKE_PHOTO || requstCode == REQUEST_PICK_PHOTO){
                if (data != null){
                    Uri selectedImage = data.getData();
                    String[] filePathColumn ={MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query( selectedImage,filePathColumn,null,null,null );
                    assert cursor  != null;
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    mediaPath = cursor.getString(columnIndex);
                    mProfilePhoto.setImageBitmap(BitmapFactory.decodeFile(mediaPath));
                    cursor.close();
                    postPath = mediaPath;
                    System.out.println( "imge"+ mediaPath );
                }
            }else if (requstCode == CAMERA_PIC_REQUEST){
                if (Build.VERSION.SDK_INT > 21){
                    Glide.with( this).load(mImageFileLocation).into(mProfilePhoto);
                    postPath = mImageFileLocation;
                }else {
                    Glide.with( this).load(fileUri).into(mProfilePhoto);
                    postPath = fileUri.getPath();
                }
            }
        }
        else if (requstCode !=  RESULT_CANCELED){
            Toast.makeText( getApplicationContext(),"Sorry, there was on error!",Toast.LENGTH_LONG).show();
        }
    }
}
