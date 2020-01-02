package com.actnow.android.activities;


import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
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
import com.actnow.android.adapter.FileAdapter;
import com.actnow.android.adapter.GalleryAdapter;
import com.actnow.android.adapter.ProjectCommentListAdapter;
import com.actnow.android.adapter.TaskCommentListAdapter;
import com.actnow.android.sdk.responses.ProjectCommentRecordsList;
import com.actnow.android.sdk.responses.TaskCommentListResponse;
import com.actnow.android.sdk.responses.TaskComplete;
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
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.Gravity.NO_GRAVITY;
import static android.view.View.GONE;


public class CommentsActivity extends AppCompatActivity {
    final Context context = this;
    View mProgressView, mContentLayout;
    UserPrefUtils session;
    RecyclerView mCommentRecylcerView;
    RecyclerView.LayoutManager mLayoutManager;
    ProjectCommentListAdapter mProjectCommentListAdapter;
    TaskCommentListAdapter mTaskCommentListAdapter;
    private FileAdapter fileAdapter;
    ArrayList<String> fileArray;
    int location[] = new int[2];


    ArrayList<ProjectCommentRecordsList> projectCommentRecordsListArrayList = new ArrayList<>();

    ArrayList<TaskCommentListResponse> taskCommentListResponseArrayList = new ArrayList<>();

    private final int requestCode = 20;

    private int PICK_IMAGE_REQUEST = 1;
    private static final int STORAGE_PERMISSION_CODE = 123;
    private Bitmap bitmap;
    private Uri filePath;

    EditText mEditAddComment;
    ImageView mImgAttachament;
    ImageView imageGallery;
    int clickCounter = 0;
    ImageView mImgCommentAdd;

    String project_code;
    String projectId;
    String task_code;

    MultipartBody.Part[] surveyImagesParts;

    TextView mCommentMeassgeTask;
    TextView mCommentId;
    ImageView imgComment;

    TextView mCommentMeassgeProject;
    TextView mCommentProjectId;


    private static final int REQUEST_TAKE_PHOTO = 0;
    private static final int REQUEST_PICK_PHOTO = 2;
    private static final int CAMERA_PIC_REQUEST = 1111;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;

    public static final int MEDIA_TYPE_IMAGE = 1;

    int PICK_IMAGE_MULTIPLE = 1;

    private Uri fileUri;
    private String mediaPath;
    private String mImageFileLocation = " ";


    String postPath;
    EditText commentUpdate;
    View view1;
    String comment;
    String strImage;
    String type = "ActNowApp";

    String imageEncoded;
    List<String> imagesEncodedList;
    private GridView gvGallery;
    private GalleryAdapter galleryAdapter;

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
            task_code = (String) b.get( "TaskCode" );
            System.out.println( "values" + projectId + task_code + project_code );

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
        ImageView btnCalendar = (ImageView) findViewById( R.id.btn_calendarAppHeaderTwo );
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
        gvGallery = (GridView)findViewById(R.id.gv);
        mProgressView = findViewById( R.id.progress_bar );
        mContentLayout = findViewById( R.id.content_layout );
        mEditAddComment = (EditText) findViewById( R.id.et_commentEdit );
        mImgCommentAdd = (ImageView) findViewById( R.id.img_commnetAdd );
        mCommentRecylcerView = (RecyclerView) findViewById( R.id.rv_recyclerViewComment );
        mLayoutManager = new LinearLayoutManager( getApplicationContext() );
        mCommentRecylcerView.setLayoutManager( mLayoutManager );
        mCommentRecylcerView.setItemAnimator( new DefaultItemAnimator() );
        mProjectCommentListAdapter = new ProjectCommentListAdapter( projectCommentRecordsListArrayList, R.layout.comment_custom_list, getApplicationContext() );
        mCommentRecylcerView.setAdapter( mProjectCommentListAdapter );
        mTaskCommentListAdapter = new TaskCommentListAdapter( taskCommentListResponseArrayList, R.layout.comment_custom_list, getApplicationContext() );
        projectCommentListReponse();
        taskCommentListReponse();

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    private void showPopupProject(View view) {
        PopupMenu popupMenu = new PopupMenu( this, view, Gravity.RIGHT | NO_GRAVITY, R.attr.actionOverflowMenuStyle, 0 );
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate( R.menu.task_comment_menu, popupMenu.getMenu() );
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener( new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.editCommentTask:
                        final String userProjectComment = mCommentMeassgeProject.getText().toString();
                        System.out.println( "commentUpdate" + commentUpdate );
                        AlertDialog.Builder builder = new AlertDialog.Builder( CommentsActivity.this );
                        builder.setTitle( "UPDATE COMMENT" );
                        final EditText commentProjetUpdate = new EditText( CommentsActivity.this );
                        final String updateProjectComment = commentProjetUpdate.getText().toString();
                        commentProjetUpdate.setText( userProjectComment );
                        mCommentMeassgeProject.setText( updateProjectComment );
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT );
                        commentProjetUpdate.setLayoutParams( lp );
                        builder.setView( commentProjetUpdate );
                        builder.setPositiveButton( "OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                HashMap<String, String> userId = session.getUserDetails();
                                String id = userId.get( UserPrefUtils.ID );
                                String orgn_code = userId.get( UserPrefUtils.ORGANIZATIONNAME );
                                String comment_id = mCommentProjectId.getText().toString();
                                String editComment = commentProjetUpdate.getText().toString();
                                Call<TaskComplete> taskEditCall = ANApplications.getANApi().checkTheTaskEdit( id, comment_id, orgn_code, editComment, task_code, project_code );
                                System.out.println( "editTaskFelis" + id + comment_id + orgn_code + editComment + task_code + project_code );
                                taskEditCall.enqueue( new Callback<TaskComplete>() {
                                    @Override
                                    public void onResponse(Call<TaskComplete> call, Response<TaskComplete> response) {
                                        if (response.isSuccessful()) {
                                            System.out.println( "editReponse" + response.raw() );
                                            if (response.body().getSuccess().equals( "true" )) {
                                                Snackbar.make( mContentLayout, "Comment edited successfully", Snackbar.LENGTH_SHORT ).show();
                                                Intent i = new Intent( getApplicationContext(), CommentsActivity.class );
                                                i.putExtra( "projectcode", project_code );
                                                startActivity( i );
                                            } else {
                                                Snackbar.make( mContentLayout, "Data Not Found", Snackbar.LENGTH_SHORT ).show();
                                            }
                                        } else {
                                            AndroidUtils.displayToast( getApplicationContext(), "Something Went Wrong!!" );
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<TaskComplete> call, Throwable t) {
                                        Log.d( "CallBack", " Throwable is " + t );

                                    }
                                } );

                            }
                        } );
                        builder.setNegativeButton( "CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        } );
                        builder.show();
                        return true;
                    case R.id.deleteCommentTask:
                        type =("text");
                        HashMap<String, String> userId = session.getUserDetails();
                        String id = userId.get( UserPrefUtils.ID );
                        String orgn_code = userId.get( UserPrefUtils.ORGANIZATIONNAME );
                        String comment_id = mCommentProjectId.getText().toString();
                        Call<TaskComplete> deleteApiCall = ANApplications.getANApi().checkTheTaskDelete( id, orgn_code, comment_id, type );
                        System.out.println( "commentValues" + id + orgn_code + comment_id + comment );
                        deleteApiCall.enqueue( new Callback<TaskComplete>() {
                            @Override
                            public void onResponse(Call<TaskComplete> call, Response<TaskComplete> response) {
                                System.out.println( "commentDelete" + response.raw() );
                                if (response.isSuccessful()) {
                                    System.out.println( "deleteComment" + response.raw());
                                    if (response.body().getSuccess().equals( "true")){
                                        Intent i = new Intent( getApplicationContext(), CommentsActivity.class );
                                        i.putExtra( "projectcode", project_code );
                                        startActivity( i );
                                        //Snackbar.make( mContentLayout, "Comment deleted successfully", Snackbar.LENGTH_SHORT ).show();
                                        Snackbar.make( mContentLayout, "Comment deleted successfully", Snackbar.LENGTH_SHORT ).show();

                                        //Toast.makeText( getApplicationContext(), "", Toast.LENGTH_SHORT ).show();
                                    } else {
                                        Snackbar.make( mContentLayout, "Data Not Found", Snackbar.LENGTH_SHORT ).show();
                                    }
                                } else {
                                    AndroidUtils.displayToast( getApplicationContext(), "Something Went Wrong!!" );

                                }
                            }
                            @Override
                            public void onFailure(Call<TaskComplete> call, Throwable t) {
                                Log.d( "CallBack", " Throwable is " + t );

                            }
                        } );

                        return true;
                    default:
                        return false;
                }
            }
        } );


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
        mCommentRecylcerView.addOnItemTouchListener( new CommentsActivity.RecyclerTouchListener( this, mCommentRecylcerView, new ClickListener() {
            @Override
            public void onClick(final View view, int position) {
                View view1 = (View) view.findViewById( R.id.liner_projectList );
                ImageView mMenuComment = (ImageView) view.findViewById( R.id.img_menuCommentProject );
                mMenuComment.setOnClickListener( new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(View v) {
                        showPopupProject( view );
                    }
                } );
                TextView mCommentProjectUserName = (TextView) view.findViewById( R.id.tv_projectuserNameComment );
                TextView mCommenProjecttDate = (TextView) view.findViewById( R.id.tv_projectcommentDate );
                mCommentMeassgeProject = (TextView) view.findViewById( R.id.tv_projectcommentText );
                mCommentProjectId = (TextView) view.findViewById( R.id.tv_projectcommentId );

                ImageView imgProjectDelete =(ImageView)view.findViewById(R.id.img_attachamentProjectDelete);
                imgProjectDelete.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        type = (strImage);
                        HashMap<String, String> userId = session.getUserDetails();
                        String id = userId.get( UserPrefUtils.ID );
                        String orgn_code = userId.get( UserPrefUtils.ORGANIZATIONNAME );
                        String commentId = mCommentId.getText().toString();
                        Call<TaskComplete> deleteApiCall = ANApplications.getANApi().checkTheTaskDelete( id, orgn_code, commentId,type );
                        System.out.println( "commentImageValues" + id + orgn_code + commentId + type );
                        deleteApiCall.enqueue( new Callback<TaskComplete>() {
                            @Override
                            public void onResponse(Call<TaskComplete> call, Response<TaskComplete> response) {
                                //System.out.println( "commentImageDelete" + response.raw() );
                                if (response.isSuccessful()) {
                                    //System.out.println( "Imagedelete" + response.raw() );
                                    if (response.body().getSuccess().equals( "true" )) {
                                        Intent i = new Intent( getApplicationContext(), CommentsActivity.class );
                                        i.putExtra( "projectcode", project_code );
                                        startActivity( i );
                                        Snackbar.make( mContentLayout, "Image deleted successfully", Snackbar.LENGTH_SHORT ).show();
                                    } else {
                                        Snackbar.make( mContentLayout, "Data Not Found", Snackbar.LENGTH_SHORT ).show();
                                    }
                                } else {
                                    AndroidUtils.displayToast( getApplicationContext(), "Something Went Wrong!!" );

                                }
                            }

                            @Override
                            public void onFailure(Call<TaskComplete> call, Throwable t) {
                                Log.d( "CallBack", " Throwable is " + t );

                            }
                        } );

                    }
                } );
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        } ) );
    }

    public interface ClickListenerProject {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    class RecyclerTouchListenerProject implements RecyclerView.OnItemTouchListener {

        private ClickListenerProject clicklistener;
        private GestureDetector gestureDetector;

        public RecyclerTouchListenerProject(CommentsActivity context, final RecyclerView mRecylerViewSingleSub, ClickListenerProject clickListener) {
            this.clicklistener = clickListener;

            gestureDetector = new GestureDetector( context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = mRecylerViewSingleSub.findChildViewUnder( e.getX(), e.getY() );
                    if (child != null && clicklistener != null) {
                        clicklistener.onLongClick( child, mRecylerViewSingleSub.getChildAdapterPosition( child ) );
                    }
                }
            } );
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder( e.getX(), e.getY() );
            if (child != null && clicklistener != null && gestureDetector.onTouchEvent( e )) {
                clicklistener.onClick( child, rv.getChildAdapterPosition( child ) );
            }

            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        }

    }


    /*TaskOffline Comment List*/
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void showPopupTask(View view) {
        PopupMenu popupMenu = new PopupMenu( this, view, Gravity.RIGHT | NO_GRAVITY, R.attr.actionOverflowMenuStyle, 0 );
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate( R.menu.task_comment_menu, popupMenu.getMenu() );
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener( new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.editCommentTask:
                        final String userComment = mCommentMeassgeTask.getText().toString();
                        System.out.println( "commentUpdate" + commentUpdate );
                        AlertDialog.Builder builder = new AlertDialog.Builder( CommentsActivity.this );
                        commentUpdate = new EditText( CommentsActivity.this );
                        String updateComment = commentUpdate.getText().toString();
                        commentUpdate.setText( userComment );
                        mCommentMeassgeTask.setText( updateComment );
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT );
                        commentUpdate.setLayoutParams( lp );
                        builder.setView( commentUpdate );
                        builder.setPositiveButton( "OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                HashMap<String, String> userId = session.getUserDetails();
                                String id = userId.get( UserPrefUtils.ID );
                                String orgn_code = userId.get( UserPrefUtils.ORGANIZATIONNAME );
                                String comment_id = mCommentId.getText().toString();
                                String editComment = commentUpdate.getText().toString();
                                Call<TaskComplete> taskEditCall = ANApplications.getANApi().checkTheTaskEdit( id, comment_id, orgn_code, editComment, task_code, project_code );
                                taskEditCall.enqueue( new Callback<TaskComplete>() {
                                    @Override
                                    public void onResponse(Call<TaskComplete> call, Response<TaskComplete> response) {
                                        if (response.isSuccessful()) {
                                            if (response.body().getSuccess().equals( "true" )) {
                                                Snackbar.make( mContentLayout, "Comment edited successfully", Snackbar.LENGTH_SHORT ).show();
                                                Intent i = new Intent( getApplicationContext(), CommentsActivity.class );
                                                i.putExtra( "TaskCode", task_code );
                                                startActivity( i );
                                            } else {
                                                Snackbar.make( mContentLayout, "Data Not Found", Snackbar.LENGTH_SHORT ).show();
                                            }
                                        } else {
                                            AndroidUtils.displayToast( getApplicationContext(), "Something Went Wrong!!" );
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<TaskComplete> call, Throwable t) {
                                        Log.d( "CallBack", " Throwable is " + t );

                                    }
                                } );

                            }
                        } );
                        builder.setNegativeButton( "CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        } );
                        builder.show();
                        return true;
                    case R.id.deleteCommentTask:
                        type =("text");
                        HashMap<String, String> userId = session.getUserDetails();
                        String id = userId.get( UserPrefUtils.ID );
                        String orgn_code = userId.get( UserPrefUtils.ORGANIZATIONNAME );
                        String commentId = mCommentId.getText().toString();
                        String commentMsg = mCommentMeassgeTask.getText().toString();
                        Call<TaskComplete> deleteApiCall = ANApplications.getANApi().checkTheTaskDelete( id, orgn_code, commentId, type );
                        System.out.println( "commentValues" + id + orgn_code + commentId + comment );
                        deleteApiCall.enqueue( new Callback<TaskComplete>() {
                            @Override
                            public void onResponse(Call<TaskComplete> call, Response<TaskComplete> response) {
                                System.out.println( "commentDelete" + response.raw() );
                                if (response.isSuccessful()) {
                                    System.out.println( "deleteComment" + response.raw());
                                    if (response.body().getSuccess().equals( "true")){
                                        Intent i = new Intent( getApplicationContext(), CommentsActivity.class );
                                        i.putExtra( "TaskCode", task_code );
                                        startActivity( i );
                                        Snackbar.make( mContentLayout, "Comment deleted successfully", Snackbar.LENGTH_SHORT ).show();
                                        //Toast.makeText( getApplicationContext(), "", Toast.LENGTH_SHORT ).show();
                                    } else {
                                        Snackbar.make( mContentLayout, "Data Not Found", Snackbar.LENGTH_SHORT ).show();
                                    }
                                } else {
                                    AndroidUtils.displayToast( getApplicationContext(), "Something Went Wrong!!" );

                                }
                            }
                            @Override
                            public void onFailure(Call<TaskComplete> call, Throwable t) {
                                Log.d( "CallBack", " Throwable is " + t );

                            }
                        } );

                        return true;
                    default:
                        return false;
                }
            }
        } );
    }

    private void taskCommentListReponse() {
        HashMap<String, String> userId = session.getUserDetails();
        String id = userId.get( UserPrefUtils.ID );
        String orgn_code = userId.get( UserPrefUtils.ORGANIZATIONNAME );
        System.out.println( "data" + id + task_code + orgn_code );
        Call<ResponseBody> callTask = ANApplications.getANApi().checkTheTaskCommentList( id, task_code, orgn_code );
        callTask.enqueue( new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                AndroidUtils.showProgress( false, mProgressView, mContentLayout );
                if (response.isSuccessful()) {
                    try {
                        try {
                            JSONObject jsonObject = new JSONObject( response.body().string() );
                            if (jsonObject.getString( "success" ).equals( "true" )) {
                                JSONArray commentTask = jsonObject.getJSONArray( "comment_records" );
                                setTaskComment( commentTask );
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
    private void setTaskComment(JSONArray commentTask) {
        for (int i = 0; commentTask.length() > i; i++) {
            TaskCommentListResponse taskCommentListResponse = new TaskCommentListResponse();
            try {
                JSONObject values = commentTask.getJSONObject( i );
                comment = values.getString( "comment" );
                String name = values.getString( "user_name" );
                String date = values.getString( "created_date" );
                String commentId = values.getString( "comment_id" );

                String imge = values.getString( "files" );

                taskCommentListResponse.setComment_id( commentId );
                taskCommentListResponse.setComment( comment );
                taskCommentListResponse.setUser_name( name );
                taskCommentListResponse.setCreated_date( date );
                 strImage = imge.replace( "\\", "" );
                taskCommentListResponse.setFiles( strImage );
                //System.out.println("myUri"+ strImage);


            } catch (JSONException e) {
                e.printStackTrace();
            }
            taskCommentListResponseArrayList.add( taskCommentListResponse );
        }
        mCommentRecylcerView.setAdapter( new TaskCommentListAdapter( taskCommentListResponseArrayList, R.layout.comment_custom_list, getApplicationContext() ) );
        mCommentRecylcerView.addOnItemTouchListener( new CommentsActivity.RecyclerTouchListener( this, mCommentRecylcerView, new ClickListener() {
            @Override
            public void onClick(final View view, int position) {
                view1 = (View) view.findViewById( R.id.re_imagComment );
                ImageView mMenuComment = (ImageView) view.findViewById( R.id.img_menuComment );
                mMenuComment.setOnClickListener( new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(View v) {
                        showPopupTask( view );
                    }
                } );
                TextView mCommentUserName = (TextView) view.findViewById( R.id.tv_userNameComment );
                TextView mCommentDate = (TextView) view.findViewById( R.id.tv_commentDate );
                mCommentMeassgeTask = (TextView) view.findViewById( R.id.tv_commentText );
                mCommentId = (TextView) view.findViewById( R.id.tv_commentId );
                imgComment = (ImageView)view. findViewById( R.id.img_attachamentComment );
                ImageView imgDelete = (ImageView)view.findViewById(R.id.img_attachamentDelete);
                imgDelete.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        type = strImage;
                        HashMap<String, String> userId = session.getUserDetails();
                        String id = userId.get( UserPrefUtils.ID );
                        String orgn_code = userId.get( UserPrefUtils.ORGANIZATIONNAME );
                        String commentId = mCommentId.getText().toString();
                        Call<TaskComplete> deleteApiCall = ANApplications.getANApi().checkTheTaskDelete( id, orgn_code, commentId,type );
                        System.out.println( "commentImageValues" + id + orgn_code + commentId + type );
                        deleteApiCall.enqueue( new Callback<TaskComplete>() {
                            @Override
                            public void onResponse(Call<TaskComplete> call, Response<TaskComplete> response) {
                                System.out.println( "commentImageDelete" + response.raw() );
                                if (response.isSuccessful()) {
                                    System.out.println( "Imagedelete" + response.raw() );
                                    if (response.body().getSuccess().equals( "true" )) {
                                        Intent i = new Intent( getApplicationContext(), CommentsActivity.class );
                                        i.putExtra( "TaskCode", task_code );
                                        startActivity( i );
                                        Snackbar.make( mContentLayout, "Image deleted successfully", Snackbar.LENGTH_SHORT ).show();
                                        //Toast.makeText( getApplicationContext(), "", Toast.LENGTH_SHORT ).show();
                                    } else {
                                        Snackbar.make( mContentLayout, "Data Not Found", Snackbar.LENGTH_SHORT ).show();
                                    }
                                } else {
                                    AndroidUtils.displayToast( getApplicationContext(), "Something Went Wrong!!" );

                                }
                            }

                            @Override
                            public void onFailure(Call<TaskComplete> call, Throwable t) {
                                Log.d( "CallBack", " Throwable is " + t );

                            }
                        } );
                    }
                } );

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        } ) );
    }


    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private ClickListener clicklistener;
        private GestureDetector gestureDetector;

        public RecyclerTouchListener(CommentsActivity context, final RecyclerView mRecylerViewSingleSub, ClickListener clickListener) {
            this.clicklistener = clickListener;

            gestureDetector = new GestureDetector( context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = mRecylerViewSingleSub.findChildViewUnder( e.getX(), e.getY() );
                    if (child != null && clicklistener != null) {
                        clicklistener.onLongClick( child, mRecylerViewSingleSub.getChildAdapterPosition( child ) );
                    }
                }
            } );
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder( e.getX(), e.getY() );
            if (child != null && clicklistener != null && gestureDetector.onTouchEvent( e )) {
                clicklistener.onClick( child, rv.getChildAdapterPosition( child ) );
            }

            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        }

    }

    private void footer() {
        imageGallery = (ImageView) findViewById( R.id.image_gallery );
        imageGallery.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* chooseFile();*/
               /* Intent i =new Intent(getApplicationContext(),GalleryActivity.class);
                startActivity(i);*/

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGE_MULTIPLE);
            }
        } );
        ImageView imageAttachament = (ImageView) findViewById( R.id.image_attachament );
        imageAttachament.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // chooseFile();
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGE_MULTIPLE);
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
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onClick(View v) {
                Toast.makeText( getApplicationContext(), "Work in progress!", Toast.LENGTH_LONG ).show();
                String userComment = mEditAddComment.getText().toString();
                HashMap<String, String> userId = session.getUserDetails();
                String uid = userId.get( UserPrefUtils.ID );
                String userorng_code = userId.get( UserPrefUtils.ORGANIZATIONNAME );
                    File file = new File( postPath );
                    RequestBody requestBody = RequestBody.create( MediaType.parse( "*/*" ), file );
                    //System.out.println( "requsetBody" + file );
                    RequestBody id = RequestBody.create( MediaType.parse( "multipart/form-data" ), uid );
                    RequestBody orng_code = RequestBody.create( MediaType.parse( "multipart/form-data" ), userorng_code );
                    RequestBody comment = RequestBody.create( MediaType.parse( "multipart/form-data" ), userComment );
                    RequestBody userTask_code = RequestBody.create( MediaType.parse( "multipart/form-data" ), task_code );
                    RequestBody userProject_code = RequestBody.create( MediaType.parse( "multipart/form-data" ), project_code );
                    RequestBody path = RequestBody.create( MediaType.parse( "multipart/form-data" ), postPath );
                    MultipartBody.Part attachment = MultipartBody.Part.createFormData( "image", "image.jpg", requestBody );
                    System.out.println( "body" + attachment );
                    Call<TaskComplete> taskAddResponseCall = ANApplications.getANApi().checkTheCommentAdd( id, orng_code, comment, userTask_code, userProject_code,path, attachment );
                    System.out.println( "taskAdd" + id + orng_code + comment + userTask_code + userProject_code + attachment );
                    taskAddResponseCall.enqueue( new Callback<TaskComplete>() {

                       public void onResponse(Call<TaskComplete> call, Response<TaskComplete> response) {
                            if (response.isSuccessful()) {
                                System.out.println( "addReponse" + response.raw() );
                                if (response.body().getSuccess().equals( "true" )) {

                                } else {
                                    Snackbar.make( mContentLayout, "Data Not Found", Snackbar.LENGTH_SHORT ).show();
                                }
                            } else {
                                //AndroidUtils.displayToast( getApplicationContext(), "Something Went Wrong!!" );
                            }
                        }

                       @Override
                        public void onFailure(Call<TaskComplete> call, Throwable t) {

                       }

                    } );


            }
        } );

    }


    private void chooseFile() {
        Intent galleryIntent = new Intent( Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI );
        startActivityForResult( galleryIntent, REQUEST_PICK_PHOTO );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            // When an Image is picked
            if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                imagesEncodedList = new ArrayList<String>();
                if(data.getData()!=null){

                    Uri mImageUri=data.getData();

                    // Get the cursor
                    Cursor cursor = getContentResolver().query(mImageUri,
                            filePathColumn, null, null, null);
                    // Move to first row
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    imageEncoded  = cursor.getString(columnIndex);
                    cursor.close();

                    ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
                    mArrayUri.add(mImageUri);
                    galleryAdapter = new GalleryAdapter(getApplicationContext(),mArrayUri);
                    gvGallery.setAdapter(galleryAdapter);
                    gvGallery.setVerticalSpacing(gvGallery.getHorizontalSpacing());
                    ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) gvGallery
                            .getLayoutParams();
                    mlp.setMargins(0, gvGallery.getHorizontalSpacing(), 0, 0);

                } else {
                    if (data.getClipData() != null) {
                        ClipData mClipData = data.getClipData();
                        ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
                        for (int i = 0; i < mClipData.getItemCount(); i++) {

                            ClipData.Item item = mClipData.getItemAt(i);
                            Uri uri = item.getUri();
                            mArrayUri.add(uri);
                            // Get the cursor
                            Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
                            // Move to first row
                            cursor.moveToFirst();

                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                            imageEncoded  = cursor.getString(columnIndex);
                            imagesEncodedList.add(imageEncoded);
                            cursor.close();

                            galleryAdapter = new GalleryAdapter(getApplicationContext(),mArrayUri);
                            gvGallery.setAdapter(galleryAdapter);
                            gvGallery.setVerticalSpacing(gvGallery.getHorizontalSpacing());
                            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) gvGallery
                                    .getLayoutParams();
                            mlp.setMargins(0, gvGallery.getHorizontalSpacing(), 0, 0);

                        }
                        Log.v("LOG_TAG", "Selected Images" + mArrayUri.size());
                    }
                }
            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }
/*

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
                    mImgCommentAdd.setImageBitmap( BitmapFactory.decodeFile( mediaPath ) );
                    cursor.close();
                    postPath = mediaPath;
                    System.out.println( "imge" + mediaPath );
                }
            } else if (requstCode == CAMERA_PIC_REQUEST) {
                if (Build.VERSION.SDK_INT > 21) {
                    Glide.with( this ).load( mImageFileLocation ).into( mImgCommentAdd );
                    postPath = mImageFileLocation;
                } else {
                    Glide.with( this ).load( fileUri ).into( mImgCommentAdd );
                    postPath = fileUri.getPath();
                }
            }
        } else if (requstCode != RESULT_CANCELED) {
            Toast.makeText( getApplicationContext(), "Sorry, there was on error!", Toast.LENGTH_LONG ).show();
        }
    }
*/
    }

    public void onBackPressed() {
        super.onBackPressed();
    }
}
