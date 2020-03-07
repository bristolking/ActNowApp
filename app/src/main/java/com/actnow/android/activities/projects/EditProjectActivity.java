package com.actnow.android.activities.projects;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.actnow.android.ANApplications;
import com.actnow.android.R;
import com.actnow.android.activities.ThisWeekActivity;
import com.actnow.android.activities.TimeLineActivity;
import com.actnow.android.activities.TodayTaskActivity;
import com.actnow.android.activities.ideas.ViewIdeasActivity;
import com.actnow.android.activities.individuals.ViewIndividualsActivity;
import com.actnow.android.activities.insights.DailyTaskChartActivity;
import com.actnow.android.activities.settings.AccountSettingActivity;
import com.actnow.android.activities.settings.EditAccountActivity;
import com.actnow.android.activities.settings.PremiumActivity;
import com.actnow.android.activities.tasks.TaskAddListActivity;
import com.actnow.android.databse.ProjectDBHelper;
import com.actnow.android.sdk.responses.ProjectEditResponse;
import com.actnow.android.sdk.responses.ProjectListResponseRecords;
import com.actnow.android.utils.AndroidUtils;
import com.actnow.android.utils.UserPrefUtils;
import com.bumptech.glide.Glide;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import java.util.HashMap;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;

public class EditProjectActivity extends AppCompatActivity {
    EditText mEditProjectName, mEditProjectOwnerName;
    View  mProjectColorEdit;
    UserPrefUtils session;
    View mProgressView, mContentLayout;
    final Context context = this;
    String projectOwnerName;
    String projectName;
    String projectcode;
    String color;
    TextView mColorCodePojectEdit;
    ImageView mImgePojectEditColor;
    private int currentBackgroundColor = 0xffffffff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new UserPrefUtils(getApplicationContext());
        setContentView(R.layout.activity_edit_project);
        initializeViews();
        header();
        footer();
        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        if (b != null) {
            projectcode =(String)b.get("projectcode");
            projectOwnerName = (String) b.get("projectOwnerName");
            mEditProjectOwnerName.setText(" " + projectOwnerName);
            projectName = (String) b.get("projectName");
            mEditProjectName.setText("" + projectName);
            color = (String)b.get( "color");
            mColorCodePojectEdit.setText( " " + color );

        }
    }

    private void header() {
        ImageView imgeBack = (ImageView) findViewById(R.id.image_back);
        imgeBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        TextView tv_title = (TextView) findViewById(R.id.txt_title);
        tv_title.setText("Project Edit Name");
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setVisibility(View.GONE);
        ImageView imageMenu = (ImageView) findViewById(R.id.image_menu);
        imageMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final NavigationView navigationView = (NavigationView) findViewById( R.id.nav_view );
                HashMap<String, String> userId = session.getUserDetails();
                String id = userId.get( UserPrefUtils.ID );
                String taskOwnerName = userId.get( UserPrefUtils.NAME );
                String email = userId.get( UserPrefUtils.EMAIL);
                ImageView mImageProfile = (ImageView) findViewById( R.id.img_profile );
                String img = userId.get( UserPrefUtils.IMAGEPATH);
                System.out.println( "img"+ img );
                Glide.with(getApplicationContext())
                        .load(img)
                        .centerCrop()
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .into(mImageProfile);
                mImageProfile.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent( getApplicationContext(), EditAccountActivity.class );
                        startActivity( i );
                    }
                } );

                TextView mTextName = (TextView) findViewById( R.id.tv_nameProfile );
                mTextName.setText( taskOwnerName );
                TextView mTextEmail =(TextView)findViewById( R.id.tv_emailProfile);
                mTextEmail.setText( email );
                navigationView.setNavigationItemSelectedListener( new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.nav_today:
                                Intent iToday = new Intent(getApplicationContext(),TodayTaskActivity.class);
                                startActivity(iToday);
                                break;
                            case R.id.nav_idea:
                                Intent iIdea = new Intent(getApplicationContext(), ViewIdeasActivity.class);
                                startActivity(iIdea);
                                break;
                            case R.id.nav_thisweek:
                                Intent ithisweek = new Intent(getApplicationContext(), ThisWeekActivity.class);
                                startActivity(ithisweek);
                                break;
                            case R.id.nav_taskfilter:
                                Intent iTaskfilter = new Intent(getApplicationContext(), TaskAddListActivity.class);
                                startActivity(iTaskfilter);
                                break;
                            case R.id.nav_project:
                                Intent iProjects = new Intent( getApplicationContext(),ProjectFooterActivity.class);
                                startActivity( iProjects);
                                break;
                            case R.id.nav_individuals:
                                Intent iIndividuals = new Intent(getApplicationContext(), ViewIndividualsActivity.class);
                                startActivity(iIndividuals);
                                break;
                            case R.id.nav_insights:
                                Intent iInsights = new Intent(getApplicationContext(), DailyTaskChartActivity.class);
                                startActivity(iInsights);
                                break;
                            case R.id.nav_timeLine:
                                Intent iTimeLine = new Intent(getApplicationContext(), TimeLineActivity.class);
                                startActivity(iTimeLine);
                                break;
                            case R.id.nav_profile:
                                Intent iprofile = new Intent(getApplicationContext(), AccountSettingActivity.class);
                                startActivity(iprofile);
                                break;
                            case R.id.nav_premium:
                                Intent ipremium = new Intent(getApplicationContext(), PremiumActivity.class);
                                startActivity(ipremium);
                                break;
                            case R.id.nav_logout:
                                session.logoutUser();
                                break;

                        }
                        return false;
                    }
                });
                final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layoutProjectEditView);
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
        mEditProjectName = findViewById(R.id.et_newProjectEditName);
        mEditProjectOwnerName = findViewById(R.id.et_EditProjectOwnerName);
        mColorCodePojectEdit =(TextView)findViewById( R.id.tv_projectEditTextView);
        mProjectColorEdit = findViewById(R.id.liner_projectEditColor);
        mImgePojectEditColor = findViewById(R.id.image_projectColorEdit);
        mProjectColorEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorPickerDialogBuilder
                        .with(context)
                        .setTitle("Choose colorWhite")
                        .initialColor(currentBackgroundColor)
                        .wheelType( ColorPickerView.WHEEL_TYPE.FLOWER)
                        .density(12)
                        .setOnColorSelectedListener(new OnColorSelectedListener() {
                            @Override
                            public void onColorSelected(int selectedColor) {
                                toast("onColorSelected: 0x" + Integer.toHexString(selectedColor));
                                String color= Integer.toHexString(selectedColor);
                                mColorCodePojectEdit.setText( color );
                                System.out.println( "color"+color );
                            }
                        })
                        .setPositiveButton("ok", new ColorPickerClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                                changeBackgroundColor(selectedColor);
                                if (allColors != null) {
                                    StringBuilder sb = null;

                                    for (Integer color : allColors) {
                                        if (color == null)
                                            continue;
                                        if (sb == null)
                                            sb = new StringBuilder("Color List:");
                                        sb.append("\r\n#" + Integer.toHexString(color).toUpperCase());
                                    }

                                    if (sb != null)
                                        Toast.makeText(getApplicationContext(), sb.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .showColorEdit(true)
                        .setColorEditTextColor( ContextCompat.getColor(EditProjectActivity.this, android.R.color.black))
                        .build()
                        .show();
            }
        });

    }
    private void changeBackgroundColor(int selectedColor) {
        currentBackgroundColor = selectedColor;
        mImgePojectEditColor.setBackgroundColor(selectedColor);
    }
    private void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void attemptUpdateTask() {
        HashMap<String, String> userId = session.getUserDetails();
        String id = userId.get(UserPrefUtils.ID);
        String orgn_code= userId.get(UserPrefUtils.ORGANIZATIONNAME);
        String projectEditName= mEditProjectName.getText().toString();
        String color = mColorCodePojectEdit.getText().toString();
        String mColor= color.substring(2);
        System.out.println( "mColor"+mColor);
        requestCrateTask(id, projectcode,projectEditName,mColor,orgn_code);
        System.out.println("daa"+ id+projectcode+projectEditName+mColor+orgn_code);
    }
    private void requestCrateTask(String id, String project_code,String name,String color,String orgn_code) {
        Call<ProjectEditResponse> call = ANApplications.getANApi().checkProjectEditResponse(id,project_code,name,color,orgn_code);
        call.enqueue(new Callback<ProjectEditResponse>() {
            @Override
            public void onResponse(Call<ProjectEditResponse> call, Response<ProjectEditResponse> response) {
                AndroidUtils.showProgress(false, mProgressView, mContentLayout);
                System.out.println("severReponse"+ response.raw());
                if (response.isSuccessful()) {
                    System.out.println("severReponse1"+ response.raw());
                    if (response.body().getSuccess().equals("true")) {
                        System.out.println("severReponse2"+response.body().getMessage());
                        Intent i = new Intent(EditProjectActivity.this, ProjectFooterActivity.class);
                        startActivity(i);
                    } else {
                        Snackbar.make(mContentLayout, "Data Not Found", Snackbar.LENGTH_SHORT).show();
                    }
                } else {
                    AndroidUtils.displayToast(getApplicationContext(), "Something Went Wrong!!");
                }
            }

            @Override
            public void onFailure(Call<ProjectEditResponse> call, Throwable t) {
                Log.d("CallBack", " Throwable is " + t);
            }
        });
    }

    private void footer() {
        ImageView imageGallery = (ImageView) findViewById(R.id.image_gallery);
        imageGallery.setVisibility(GONE);
        ImageView imageAttachament = (ImageView) findViewById(R.id.image_attachament);
        imageAttachament.setVisibility(GONE);

        ImageView imageCamera = (ImageView) findViewById(R.id.image_camera);
        imageCamera.setVisibility(GONE);
        ImageView imageProfile = (ImageView) findViewById(R.id.image_profile);
        imageProfile.setVisibility(GONE);
        TextView tv_create = (TextView) findViewById(R.id.tv_create);
        tv_create.setText("Update");
        tv_create.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (AndroidUtils.isNetworkAvailable( getApplicationContext() )) {
                    attemptUpdateTask();

                } else {
                    attemptUpdateOfflineProjects();
                }
            }
        });
    }

    private void attemptUpdateOfflineProjects() {
        HashMap<String, String> userId = session.getUserDetails();
        String id = userId.get(UserPrefUtils.ID);
        String orgn_code= userId.get(UserPrefUtils.ORGANIZATIONNAME);
        String projectEditName= mEditProjectName.getText().toString();
        String color = mColorCodePojectEdit.getText().toString();
        String mColor= color.substring(2);
        ProjectListResponseRecords projectListResponseRecords1 = new ProjectListResponseRecords();
        projectListResponseRecords1.setProject_id( id );
        projectListResponseRecords1.setName(projectEditName);
        projectListResponseRecords1.setColor(mColor);
        System.out.println("felids" + mColor + projectEditName );
        ProjectDBHelper projectDBHelper = new ProjectDBHelper(EditProjectActivity.this);
        projectDBHelper.insertUserDetails(projectListResponseRecords1);
        Intent i = new Intent(EditProjectActivity.this, ProjectFooterActivity.class);
        Toast.makeText( getApplicationContext(), "Project Offline Updated Sucessfully", Toast.LENGTH_SHORT ).show();
        startActivity(i);

    }


    public void onBackPressed() {
        super.onBackPressed();
    }

}


