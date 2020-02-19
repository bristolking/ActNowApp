package com.actnow.android.activities;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.actnow.android.ANApplications;
import com.actnow.android.R;
import com.actnow.android.activities.ideas.ViewIdeasActivity;
import com.actnow.android.activities.individuals.ViewIndividualsActivity;
import com.actnow.android.activities.insights.DailyTaskChartActivity;
import com.actnow.android.activities.projects.ProjectFooterActivity;
import com.actnow.android.activities.settings.AccountSettingActivity;
import com.actnow.android.activities.settings.EditAccountActivity;
import com.actnow.android.activities.settings.PremiumActivity;
import com.actnow.android.activities.settings.SettingsActivity;
import com.actnow.android.activities.tasks.TaskAddListActivity;
import com.actnow.android.adapter.PreviewImageAdapter;
import com.actnow.android.adapter.ProjectCommentListAdapter;
import com.actnow.android.adapter.TaskCommentListAdapter;
import com.actnow.android.sdk.responses.ProjectCommentRecordsList;
import com.actnow.android.sdk.responses.TaskCommentListResponse;
import com.actnow.android.utils.AndroidUtils;
import com.actnow.android.utils.UserPrefUtils;
import com.bumptech.glide.Glide;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static android.view.View.GONE;

public class CommentsActivity extends AppCompatActivity {
    final Context context = this;
    View mProgressView, mContentLayout;
    UserPrefUtils session;
    RecyclerView mCommentRecylcerView;
    RecyclerView.LayoutManager mLayoutManager;
    ProjectCommentListAdapter mProjectCommentListAdapter;
    TaskCommentListAdapter mTaskCommentListAdapter;

    ArrayList<ProjectCommentRecordsList> projectCommentRecordsListArrayList = new ArrayList<>();

    ArrayList<TaskCommentListResponse> taskCommentListResponseArrayList = new ArrayList<>();

    private final int requestCode = 20;

    EditText mEditAddComment;
    ImageView imageGallery;
    String project_code;
    String projectId;
    String task_code;
    TextView mCommentMeassgeTask;
    TextView mCommentId;
    ImageView imgComment;

    TextView mCommentMeassgeProject;
    TextView mCommentProjectId;


    EditText commentUpdate;
    View view1;
    String comment;
    String strImage;
    String type = "ActNowApp";

    int RESULT_LOAD_IMAGE = 1;
    String picturePath;


    private static final String TAG = CommentsActivity.class.getSimpleName();

    private ListView listView;
    private ProgressBar mProgressBar;
    private Button btnChoose, btnUpload;

    private ArrayList<Uri> arrayList;

    private final int REQUEST_CODE_PERMISSIONS = 1;
    private final int REQUEST_CODE_READ_STORAGE = 2;


    private PreviewImageAdapter imageAdapter;
    ArrayList<String> fiveImgUris = new ArrayList<String>();
    //AddPrescription prescription;
    File imgpath1,imgpath2,imgpath3,imgpath4,imgpath5;
    String screen= "NFG";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        session = new UserPrefUtils( getApplicationContext() );
        setContentView( R.layout.activity_comments );
        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        if (b != null) {
            project_code = (String) b.get( "projectcode" );
            projectId = (String) b.get( "projectid" );
           /// task_code = (String) b.get( "TaskCode" );
            System.out.println( "values" + projectId + task_code + project_code );

        }
        if(getIntent()!=null && getIntent().getExtras()!=null){
            Bundle bundle = getIntent().getExtras();
            if(bundle.containsKey("screen")){
                screen = bundle.getString("screen");
            }
        }
        appHeaderTwo();
        initializeViews();
        footer();
    }

    private void appHeaderTwo() {
        ImageView imgeBack = (ImageView) findViewById( R.id.image_back_two );
        imgeBack.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        } );
        TextView btnLink1 = (TextView) findViewById( R.id.btn_link_1_two );
        TextView btnLink2 = (TextView) findViewById( R.id.btn_link_2_two );
        btnLink2.setVisibility( GONE );
        btnLink1.setText( "Comments" );
        btnLink1.setTextColor( getResources().getColor( R.color.colorAccent ) );
        ImageView btnCalendar = (ImageView) findViewById( R.id.btn_insightsrAppHeaderTwo );
        btnCalendar.setVisibility( GONE );
        ImageView btnNotifications = (ImageView) findViewById( R.id.btn_notificationsAppHeaderTwo );
        btnNotifications.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText( getApplicationContext(), "Work in progress!", Toast.LENGTH_SHORT ).show();
            }
        } );
        ImageView btnSettings = (ImageView) findViewById( R.id.btn_settingsAppHeaderTwo );
        btnSettings.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> userId = session.getUserDetails();
                String accountEmail = userId.get( UserPrefUtils.EMAIL );
                Intent i = new Intent( getApplicationContext(), SettingsActivity.class );
                i.putExtra( "email", accountEmail );
                startActivity( i );
                finish();
            }
        } );
        ImageView btnMenu = (ImageView) findViewById( R.id.img_menuTopTwo );
        btnMenu.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final NavigationView navigationView = (NavigationView) findViewById( R.id.nav_view );
                HashMap<String, String> userId = session.getUserDetails();
                String id = userId.get( UserPrefUtils.ID );
                String taskOwnerName = userId.get( UserPrefUtils.NAME );
                String email = userId.get( UserPrefUtils.EMAIL );
                ImageView mImageProfile = (ImageView) findViewById( R.id.img_profile );
                String img = userId.get( UserPrefUtils.IMAGEPATH );
                System.out.println( "img" + img );
                Glide.with( getApplicationContext() )
                        .load( img )
                        .centerCrop()
                        .placeholder( R.drawable.placeholder )
                        .error( R.drawable.placeholder )
                        .into( mImageProfile );
                mImageProfile.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent( getApplicationContext(), EditAccountActivity.class );
                        startActivity( i );
                    }
                } );
                TextView mTextName = (TextView) findViewById( R.id.tv_nameProfile );
                mTextName.setText( taskOwnerName );
                TextView mTextEmail = (TextView) findViewById( R.id.tv_emailProfile );
                mTextEmail.setText( email );
                navigationView.setNavigationItemSelectedListener( new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.nav_today:
                                Intent iToday = new Intent( getApplicationContext(), TodayTaskActivity.class );
                                startActivity( iToday );
                                break;
                            case R.id.nav_idea:
                                Intent iIdea = new Intent( getApplicationContext(), ViewIdeasActivity.class );
                                startActivity( iIdea );
                                break;
                            case R.id.nav_thisweek:
                                Intent ithisweek = new Intent( getApplicationContext(), ThisWeekActivity.class );
                                startActivity( ithisweek );
                                break;
                            case R.id.nav_taskfilter:
                                Intent iTaskfilter = new Intent( getApplicationContext(), TaskAddListActivity.class );
                                startActivity( iTaskfilter );
                                break;
                            case R.id.nav_project:
                                Intent iProjects = new Intent( getApplicationContext(), ProjectFooterActivity.class );
                                startActivity( iProjects );
                                break;
                            case R.id.nav_individuals:
                                Intent iIndividuals = new Intent( getApplicationContext(), ViewIndividualsActivity.class );
                                startActivity( iIndividuals );
                                break;
                            case R.id.nav_insights:
                                Intent iInsights = new Intent( getApplicationContext(), DailyTaskChartActivity.class );
                                startActivity( iInsights );
                                break;
                            case R.id.nav_timeLine:
                                Intent iTimeLine = new Intent( getApplicationContext(), TimeLineActivity.class );
                                startActivity( iTimeLine );
                                break;
                            case R.id.nav_profile:
                                Intent iprofile = new Intent( getApplicationContext(), AccountSettingActivity.class );
                                startActivity( iprofile );
                                break;
                            case R.id.nav_premium:
                                Intent ipremium = new Intent( getApplicationContext(), PremiumActivity.class );
                                startActivity( ipremium );
                                break;
                            case R.id.nav_logout:
                                session.logoutUser();
                                break;
                        }
                        return false;
                    }
                } );
                final DrawerLayout drawer = (DrawerLayout) findViewById( R.id.drawer_comments );
                if (drawer.isDrawerOpen( GravityCompat.START )) {
                } else {
                    drawer.openDrawer( GravityCompat.START );
                }
                ImageView imgeClose = (ImageView) findViewById( R.id.nav_close );
                imgeClose.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (drawer.isDrawerOpen( GravityCompat.START )) {
                            drawer.closeDrawer( GravityCompat.START );
                        } else {
                            drawer.openDrawer( GravityCompat.START );
                        }
                    }
                } );
            }
        } );
    }

    private void initializeViews() {
        mProgressView = findViewById( R.id.progress_bar );
        mContentLayout = findViewById( R.id.content_layout );
        listView = findViewById( R.id.listView );
        mEditAddComment = (EditText) findViewById( R.id.et_commentEdit );
        mCommentRecylcerView = (RecyclerView) findViewById( R.id.rv_recyclerViewComment );
        mLayoutManager = new LinearLayoutManager( getApplicationContext() );
        mCommentRecylcerView.setLayoutManager( mLayoutManager );
        mCommentRecylcerView.setItemAnimator( new DefaultItemAnimator() );
        mProjectCommentListAdapter = new ProjectCommentListAdapter( projectCommentRecordsListArrayList, R.layout.comment_custom_list, getApplicationContext() );
        mCommentRecylcerView.setAdapter( mProjectCommentListAdapter );
        mTaskCommentListAdapter = new TaskCommentListAdapter( taskCommentListResponseArrayList, R.layout.comment_custom_list, getApplicationContext() );
        arrayList = new ArrayList<>();
        projectCommentListReponse();
    }

    private void projectCommentListReponse() {
        HashMap<String, String> userId = session.getUserDetails();
        String id = userId.get( UserPrefUtils.ID );
        String orgn_code = userId.get( UserPrefUtils.ORGANIZATIONNAME );
        Call<ResponseBody> projectCommentCall = ANApplications.getANApi().checkProjectCommentList( id, project_code, orgn_code );
        projectCommentCall.enqueue( new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                AndroidUtils.showProgress( false, mProgressView, mContentLayout );
                if (response.isSuccessful()) {
                    try {
                        try {
                            JSONObject jsonObject = new JSONObject( response.body().string() );
                            if (jsonObject.getString( "success" ).equals( "true" )) {
                                System.out.println( "nul" + response.body().toString() );
                                JSONArray projectCommentTask = jsonObject.getJSONArray( "comment_records" );
                                setProjectComment( projectCommentTask );
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d( "CallBack", " Throwable is " + t );


            }
        } );
    }

    private void setProjectComment(JSONArray projectCommentTask) {
        for (int i = 0; projectCommentTask.length() > i; i++) {
            ProjectCommentRecordsList projectCommentRecordsList = new ProjectCommentRecordsList();
            try {
                JSONObject values = projectCommentTask.getJSONObject( i );
                String comment = values.getString( "comment" );
                String commentId = values.getString( "comment_id" );
                String dateProject = values.getString( "created_date" );
                String projectuserName = values.getString( "user_name" );
                String imgeFiles = values.getString( "files" );
                projectCommentRecordsList.setComment( comment );
                projectCommentRecordsList.setComment_id( commentId );
                projectCommentRecordsList.setCreated_date( dateProject );
                projectCommentRecordsList.setUser_name( projectuserName );
                String strImage = imgeFiles.replace( "\\", "" );
                projectCommentRecordsList.setFiles( strImage );

            } catch (JSONException e) {
                e.printStackTrace();
            }
            projectCommentRecordsListArrayList.add( projectCommentRecordsList );
        }
        mCommentRecylcerView.setAdapter( new ProjectCommentListAdapter( projectCommentRecordsListArrayList, R.layout.project_comment_list, getApplicationContext() ) );

    }

/*
    private void showChooser() {
        Intent intent = new Intent( Intent.ACTION_GET_CONTENT );
        intent.setType( "image/*" );
        intent.putExtra( Intent.EXTRA_ALLOW_MULTIPLE, true );
        intent.addCategory( Intent.CATEGORY_OPENABLE );
        startActivityForResult( intent, REQUEST_CODE_READ_STORAGE );
    }
*/

    private void footer() {
        imageGallery = (ImageView) findViewById( R.id.image_gallery );
        imageGallery.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showChooser();
                chooseFile();

            }
        } );
        ImageView imageAttachament = (ImageView) findViewById( R.id.image_attachament );
        imageAttachament.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // showChooser();
                chooseFile();

            }
        } );
        ImageView imageCamera = (ImageView) findViewById( R.id.image_camera );
        imageCamera.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoCaptureIntent = new Intent( MediaStore.ACTION_IMAGE_CAPTURE );
                startActivityForResult( photoCaptureIntent, requestCode );

            }
        } );
        ImageView imageProfile = (ImageView) findViewById( R.id.image_profile );
        imageProfile.setVisibility( GONE );
        TextView tv_create = (TextView) findViewById( R.id.tv_create );
        tv_create.setText( "Add Comment" );
        tv_create.setOnClickListener( new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {

                HashMap<String, String> useId = session.getUserDetails();
                String uid = useId.get( UserPrefUtils.ID );
                String userOrng_code = useId.get( UserPrefUtils.ORGANIZATIONNAME );
                String commetAdd = mEditAddComment.getText().toString();

                RequestBody id = RequestBody.create( MediaType.parse( "multipart/form-data" ), uid );
                RequestBody orng_code = RequestBody.create( MediaType.parse( "multipart/form-data" ), userOrng_code );
                RequestBody comment = RequestBody.create( MediaType.parse( "multipart/form-data" ), commetAdd );
                RequestBody userTask_code = RequestBody.create( MediaType.parse( "multipart/form-data" ), "" );
                RequestBody userProject_code = RequestBody.create( MediaType.parse( "multipart/form-data" ), project_code );

                File file = new File( picturePath );
                RequestBody requestFile = RequestBody.create( MediaType.parse( "multipart/form-data" ), file );
                RequestBody path = RequestBody.create( MediaType.parse( "multipart/form-data" ), picturePath );
                MultipartBody.Part body = MultipartBody.Part.createFormData( "image", file.getName(), requestFile );
               Call<ResponseBody> responseBodyCall = ANApplications.getANApi().checkTheCommentAdd(id,orng_code,comment,userTask_code,userProject_code,path,body );
               responseBodyCall.enqueue( new Callback<ResponseBody>() {
                   @Override
                   public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                       System.out.println( "SeverRespone" + response.raw() );
                       if (response.isSuccessful()) {
                           System.out.println( "SeverRespone1" + response.raw() );
                           Intent i = new Intent( CommentsActivity.this, CommentsActivity.class );
                           i.putExtra( "TaskCode", task_code );
                           i.putExtra( "projectcode",project_code );
                           startActivity( i );
                       } else {
                           Snackbar.make( findViewById( android.R.id.content ),
                                   R.string.string_some_thing_wrong, Snackbar.LENGTH_LONG ).show();
                       }
                   }

                   @Override
                   public void onFailure(Call<ResponseBody> call, Throwable t) {
                       Log.d( "TAG", "Throwable" + t );

                   }
               } );

            }
        } );
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

           /* File file = new File( picturePath );
            Uri imageUri = Uri.fromFile( file );
            System.out.println( "Image Data:pic " + imageUri );

            Glide.with( getApplicationContext() )
                    .load( imageUri )
                    .error( R.drawable.ic_close )
                    .override( 150, 150 ) // Can be 2000, 2000
                    .into( mProfilePhoto );*/

            //ImageView imageView = (ImageView) findViewById(R.id.user_image);
            //imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
    }
}
