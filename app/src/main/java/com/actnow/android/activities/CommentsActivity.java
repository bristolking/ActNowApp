package com.actnow.android.activities;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.actnow.android.ANApplications;
import com.actnow.android.R;
import com.actnow.android.activities.settings.EditAccountActivity;
import com.actnow.android.activities.settings.PremiumActivity;
import com.actnow.android.activities.settings.SettingsActivity;
import com.actnow.android.adapter.FileAdapter;
import com.actnow.android.adapter.ProjectCommentListAdapter;
import com.actnow.android.sdk.responses.ProjectCommentListResponse;
import com.actnow.android.sdk.responses.ProjectCommentRecordsList;
import com.actnow.android.sdk.responses.ProjectListResponseRecords;
import com.actnow.android.utils.UserPrefUtils;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.MultipartBody;
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
    private FileAdapter fileAdapter;
    ArrayList<String> fileArray;
    //private ArrayList<CommentModel> commentModelArrayList = new ArrayList<CommentModel>();

    ArrayList<ProjectCommentRecordsList> projectCommentRecordsListArrayList = new ArrayList<>();
    //ArrayList<String> fiveImgUris = new ArrayList<String>();


    private final int requestCode = 20;

    private int PICK_IMAGE_REQUEST = 1;
    private static final int STORAGE_PERMISSION_CODE = 123;
    private Bitmap bitmap;
    private Uri filePath;

    EditText mEditAddComment;
    ImageView mImgAttachament;
    ImageView imageGallery;
    int clickCounter = 0;

    String project_code;
    String projectId;

    MultipartBody.Part[] surveyImagesParts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new UserPrefUtils(getApplicationContext());
        setContentView(R.layout.activity_comments);
        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        if (b != null) {
            project_code = (String) b.get("projectcode");
            projectId = (String) b.get("projectid");
            System.out.println("values" + projectId + project_code);

        }
        appHeaderTwo();
        initializeViews();
        footer();


    }

    private void appHeaderTwo() {
        ImageView imgeBack = (ImageView) findViewById(R.id.image_back_two);
        imgeBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        TextView btnLink1 = (TextView) findViewById(R.id.btn_link_1_two);
        TextView btnLink2 = (TextView) findViewById(R.id.btn_link_2_two);
        btnLink2.setVisibility(GONE);
        btnLink1.setText("Comments");
        btnLink1.setTextColor(getResources().getColor(R.color.colorAccent));
        ImageView btnCalendar = (ImageView) findViewById(R.id.btn_calendarAppHeaderTwo);
        btnCalendar.setVisibility(GONE);
        ImageView btnNotifications = (ImageView) findViewById(R.id.btn_notificationsAppHeaderTwo);
        btnNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Work in progress!", Toast.LENGTH_SHORT).show();
            }
        });
        ImageView btnSettings = (ImageView) findViewById(R.id.btn_settingsAppHeaderTwo);
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> userId = session.getUserDetails();
                String accountEmail = userId.get(UserPrefUtils.EMAIL);
                Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
                i.putExtra("email", accountEmail);
                startActivity(i);
                finish();
            }
        });
        ImageView btnMenu = (ImageView) findViewById(R.id.img_menuTopTwo);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                HashMap<String, String> userId = session.getUserDetails();
                String id = userId.get(UserPrefUtils.ID);
                String taskOwnerName = userId.get(UserPrefUtils.NAME);
                ImageView mImageProfile = (ImageView) findViewById(R.id.img_profile);
                mImageProfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getApplicationContext(), EditAccountActivity.class);
                        startActivity(i);
                    }
                });
                TextView mTextName = (TextView) findViewById(R.id.tv_nameProfile);
                mTextName.setText(taskOwnerName);
                navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.nav_today:
                                Intent iToady = new Intent(getApplicationContext(), TodayTaskActivity.class);
                                startActivity(iToady);
                                finish();
                                break;
                            case R.id.nav_timeLine:
                                Intent iTimeLine = new Intent(getApplicationContext(), TimeLineActivity.class);
                                startActivity(iTimeLine);
                                break;
                            case R.id.nav_filter:
                                Toast.makeText(getApplicationContext(), "Wrok in progress", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.nav_profile:
                                HashMap<String, String> userId = session.getUserDetails();
                                String id = userId.get(UserPrefUtils.ID);
                                String name = userId.get(UserPrefUtils.NAME);
                                String accountEmail = userId.get(UserPrefUtils.EMAIL);
                                Intent iprofile = new Intent(getApplicationContext(), EditAccountActivity.class);
                                iprofile.putExtra("id", id);
                                iprofile.putExtra("name", name);
                                iprofile.putExtra("email", accountEmail);
                                startActivity(iprofile);
                                break;
                            case R.id.nav_premium:
                                Intent ipremium = new Intent(getApplicationContext(), PremiumActivity.class);
                                startActivity(ipremium);
                                break;
                            case R.id.nav_thisweek:
                                Intent ithisweek = new Intent(getApplicationContext(), ThisWeekActivity.class);
                                startActivity(ithisweek);
                                break;
                        }
                        return false;
                    }
                });
                final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_comments);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
                ImageView imgeClose = (ImageView) findViewById(R.id.nav_close);
                imgeClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (drawer.isDrawerOpen(GravityCompat.START)) {
                            drawer.closeDrawer(GravityCompat.START);
                        } else {
                            drawer.openDrawer(GravityCompat.START);
                        }
                    }
                });
            }
        });
    }

    private void initializeViews() {
        mProgressView = findViewById(R.id.progress_bar);
        mContentLayout = findViewById(R.id.content_layout);
        mEditAddComment = (EditText) findViewById(R.id.et_commentEdit);
        mCommentRecylcerView = (RecyclerView) findViewById(R.id.rv_recyclerViewComment);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mCommentRecylcerView.setLayoutManager(mLayoutManager);
        mCommentRecylcerView.setItemAnimator(new DefaultItemAnimator());
        mProjectCommentListAdapter = new ProjectCommentListAdapter(projectCommentRecordsListArrayList, R.layout.comment_custom_list, getApplicationContext());
        mCommentRecylcerView.setAdapter(mProjectCommentListAdapter);
        HashMap<String, String> userId = session.getUserDetails();
        String id = userId.get(UserPrefUtils.ID);
        String orgn_code = userId.get(UserPrefUtils.ORGANIZATIONNAME);
        System.out.println("data"+ id+project_code+ orgn_code);
        Call<ResponseBody> call2 = ANApplications.getANApi().checkProjectCommentList(id,project_code,orgn_code);
        call2.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    System.out.println("sucess"+response.raw());
                    try {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().string());
                            if (jsonObject.getString("success").equals("true")) {
                                System.out.println( "nul"+ response.body().toString());
                               JSONArray comment = jsonObject.getJSONArray( "comment_records");
/*
                                String files = details.getString("images");
                                JSONArray jsonArray = new JSONArray(files);
                                fileArray = new ArrayList<String>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    fileArray.add(jsonArray.getString(i));
                                }
                                populateImagesFromGallery(fileArray);*/
                                setLoad(comment);
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
                Log.d("CallBack", " Throwable is " + t);

            }
        });


    }

    private void setLoad(JSONArray details) {

        for (int i=0; details.length()>i;i++){
            ProjectCommentRecordsList projectCommentRecordsList = new ProjectCommentRecordsList();

            try {
                JSONObject values = details.getJSONObject( i );
                String comment= values.getString( "comment" );
                 /*String files = values.getString("files");
                JSONArray jsonArray = new JSONArray(files);
                fileArray = new ArrayList<String>();
                for (int j = 0; j < jsonArray.length(); j++) {
                    fileArray.add(jsonArray.getString(j));
                }*/

                projectCommentRecordsList.setComment( comment );
                 System.out.println( "www"+ comment );


            } catch (JSONException e) {
                e.printStackTrace();
            }
            projectCommentRecordsListArrayList.add(projectCommentRecordsList);
        }
        mCommentRecylcerView.setAdapter(new ProjectCommentListAdapter(projectCommentRecordsListArrayList, R.layout.comment_custom_list, getApplicationContext()));
    }


    private void populateImagesFromGallery(ArrayList<String> imageUrls) {
        initializeRecyclerView(imageUrls);
    }

    private void initializeRecyclerView(ArrayList<String> imageUrls) {
        fileAdapter = new FileAdapter(this, imageUrls);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 4);
        mCommentRecylcerView = findViewById(R.id.rv_recyclerViewComment);
        mCommentRecylcerView.setLayoutManager(layoutManager);
        mCommentRecylcerView.setItemAnimator(new DefaultItemAnimator());
        mCommentRecylcerView.setAdapter(fileAdapter);
    }


    private void footer() {
        imageGallery = (ImageView) findViewById(R.id.image_gallery);
        imageGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showFileChooser();
            }
        });
        ImageView imageAttachament = (ImageView) findViewById(R.id.image_attachament);
        imageAttachament.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showFileChooser();

            }
        });
        ImageView imageCamera = (ImageView) findViewById(R.id.image_camera);
        imageCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(photoCaptureIntent, requestCode);

            }
        });
        ImageView imageProfile = (ImageView) findViewById(R.id.image_profile);
        imageProfile.setVisibility(GONE);
        TextView tv_create = (TextView) findViewById(R.id.tv_create);
        tv_create.setText("Add Comment");
        tv_create.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onClick(View v) {
                addTheComment();
                // Toast.makeText(getApplicationContext(), "selte", Toast.LENGTH_LONG).show();

            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void addTheComment() {
        mEditAddComment.setError(null);
        String comment = mEditAddComment.getText().toString();
        //String file = mImgAttachament.getDisplay().toString();
        boolean cancel = false;
        View focusView = null;
        if (TextUtils.isEmpty(comment)) {
            mEditAddComment.setError(getString(R.string.error_required));
            focusView = mEditAddComment;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {

            HashMap<String, String> userId = session.getUserDetails();
            String id = userId.get(UserPrefUtils.ID);
            String orng_code = userId.get(UserPrefUtils.ORGANIZATIONNAME);

        }
    }




    public void onBackPressed() {
        super.onBackPressed();
    }
}
