package com.actnow.android.activities.projects;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import android.view.Display;
import android.view.MenuItem;

import android.view.View;

import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;

import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.abdeveloper.library.MultiSelectDialog;
import com.abdeveloper.library.MultiSelectModel;
import com.actnow.android.ANApplications;
import com.actnow.android.R;
import com.actnow.android.activities.ColorPicker;
import com.actnow.android.activities.ThisWeekActivity;
import com.actnow.android.activities.TodayTaskActivity;
import com.actnow.android.activities.settings.EditAccountActivity;
import com.actnow.android.activities.settings.PremiumActivity;
import com.actnow.android.sdk.responses.CheckBoxResponse;
import com.actnow.android.sdk.responses.OrgnUserRecordsCheckBox;
import com.actnow.android.sdk.responses.ProjectAddResponse;
import com.actnow.android.utils.AndroidUtils;
import com.actnow.android.utils.UserPrefUtils;
import org.json.JSONArray;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static android.view.View.GONE;


public class ViewProjectsActivity extends AppCompatActivity {
    EditText  mProjectName,mProjectOwnerName,mDateProject, mWriteComment;
    View mProjectAddIndividual,mProjectColor;
   // MultiSelectionSpinner multiSelectionSpinner;
    UserPrefUtils session;
    View mProgressView, mContentLayout;
    MultiSelectDialog mIndividuvalDialog;
    private int mPickedColor = Color.WHITE;

    final Context context = this;
    String id;
    String projectName;
    String projectOwnerName;
    ArrayList<MultiSelectModel> listOfIndividuval = new ArrayList<MultiSelectModel>();
    ArrayList<Integer> individualCheckBox;
    JSONArray individuvalArray;
    TextView mIndividualCheckBox;

    ImageView mImgeCircleColor;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new UserPrefUtils(getApplicationContext());
        setContentView(R.layout.activity_view_projects);
        initializeViews();
        header();
        footer();
        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        if (b != null) {
            id = (String) b.get("id");
            projectOwnerName = (String) b.get("projectOwnerName");
            mProjectOwnerName.setText(" " + projectOwnerName);
            System.out.println("passsed" + projectOwnerName + id);
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
        tv_title.setText("New Project Name");
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setVisibility(GONE);
        ImageView imageMenu = (ImageView) findViewById(R.id.image_menu);
        imageMenu.setOnClickListener(new View.OnClickListener() {
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
                        Intent i = new Intent(getApplicationContext(),EditAccountActivity.class);
                        startActivity(i);
                    }
                });
                TextView mTextName =(TextView)findViewById(R.id.tv_nameProfile);
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
                                Toast.makeText(getApplicationContext(), "Wrok in progress", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.nav_filter:
                                Toast.makeText(getApplicationContext(), "Wrok in progress", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.nav_profile:
                                HashMap<String, String> userId = session.getUserDetails();
                                String id = userId.get(UserPrefUtils.ID);
                                String name = userId.get(UserPrefUtils.NAME);
                                String accountEmail = userId.get(UserPrefUtils.EMAIL);
                                Intent iprofile=new Intent(ViewProjectsActivity.this, EditAccountActivity.class);
                                iprofile.putExtra("id", id);
                                iprofile.putExtra("name", name);
                                iprofile.putExtra("email", accountEmail);
                                startActivity(iprofile);
                                break;
                            case R.id.nav_premium:
                                Intent ipremium=new Intent(ViewProjectsActivity.this, PremiumActivity.class);
                                startActivity(ipremium);
                                break;
                            case R.id.nav_thisweek:
                                Intent ithisweek= new Intent(ViewProjectsActivity.this, ThisWeekActivity.class);
                                startActivity(ithisweek);
                                break;
                        }
                        return false;
                    }
                });
                final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layoutProjectView);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
                ImageView imgeClose =(ImageView)findViewById(R.id.nav_close);
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
        mProjectName = findViewById(R.id.et_newProjectName);
        mProjectOwnerName= findViewById(R.id.et_newProjectOwnerName);
        /*mWriteComment = findViewById(R.id.et_commentNewProject);*/

        mProjectAddIndividual = (View) findViewById(R.id.liner_IndividualProject);
        mIndividualCheckBox = (TextView) findViewById(R.id.tv_indivdualProjectText);
        mIndividuvalDialog = new MultiSelectDialog();
        individualCheckBox = new ArrayList<>();
        individualCheckBox.add(0);
        mProjectAddIndividual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIndividuvalDialog.show(getSupportFragmentManager(), "mIndividuvalDialog");
            }
        });

        HashMap<String, String> userId = session.getUserDetails();
        id = userId.get(UserPrefUtils.ID);
        requestDynamicContent(id);
        mProjectColor = findViewById(R.id.liner_newPojectColor);
        mImgeCircleColor = findViewById(R.id.img_newProjectcolor);

        mProjectColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GridView gv = (GridView) ColorPicker.getColorPicker(ViewProjectsActivity.this);

                // Initialize a new AlertDialog.Builder object
                AlertDialog.Builder builder = new AlertDialog.Builder(ViewProjectsActivity.this);

                // Set the alert dialog content to GridView (color picker)
                builder.setView(gv);

                // Initialize a new AlertDialog object
                final AlertDialog dialog = builder.create();

                // Show the color picker window
                dialog.show();

                // Set the color picker dialog size
                dialog.getWindow().setLayout(
                        getScreenSize().x - mProjectColor.getPaddingLeft() - mProjectColor.getPaddingRight(),
                        getScreenSize().y - getStatusBarHeight() - mProjectColor.getPaddingTop() - mProjectColor.getPaddingBottom());

                // Set an item click listener for GridView widget
                gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // Get the pickedColor from AdapterView
                        mPickedColor = (int) parent.getItemAtPosition(position);

                        // Set the layout background color as picked color
                        mImgeCircleColor.setBackgroundColor(mPickedColor);

                        // close the color picker
                        dialog.dismiss();
                    }
                });
            }
        });
    }
    private Point getScreenSize(){
        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        //Display dimensions in pixels
        display.getSize(size);
        return size;
    }
    public int getStatusBarHeight() {
        int height = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            height = getResources().getDimensionPixelSize(resourceId);
        }
        return height;
    }
    private void requestDynamicContent(String id) {
        System.out.println("id"+id);
        Call<CheckBoxResponse> call = ANApplications.getANApi().checktheSpinnerResponse(id);
        call.enqueue(new Callback<CheckBoxResponse>() {
            @Override
            public void onResponse(Call<CheckBoxResponse> call, Response<CheckBoxResponse> response) {
                System.out.println("naga"+ response.raw());
                if (response.isSuccessful()) {
                    System.out.println("raw"+ response.raw());
                    if (response.body().getSuccess().equals("true")) {
                        setLoadCheckBox(response.body().getOrgn_users_records());
                    } else {
                        Snackbar.make(mContentLayout, "Data Not Found", Snackbar.LENGTH_SHORT).show();
                    }
                } else {
                    AndroidUtils.displayToast(getApplicationContext(), "Something Went Wrong!!");
                }
            }

            @Override
            public void onFailure(Call<CheckBoxResponse> call, Throwable t) {
                Log.d("CallBack", " Throwable is " + t);
            }
        });
    }
    private void setLoadCheckBox(List<OrgnUserRecordsCheckBox> orgn_users_records) {
        if (orgn_users_records.size() > 0) {
            for (int i = 0; orgn_users_records.size() > i; i++) {
                OrgnUserRecordsCheckBox orgnUserRecordsCheckBox = orgn_users_records.get(i);
                listOfIndividuval.add(new MultiSelectModel(Integer.parseInt(orgnUserRecordsCheckBox.getId()), orgnUserRecordsCheckBox.getName()));
                System.out.println("dta" + orgn_users_records);
            }
            mIndividuvalDialog = new MultiSelectDialog()
                    .title("Individuval") //setting title for dialog
                    .titleSize(25)
                    .positiveText("Done")
                    .negativeText("Cancel")
                    .preSelectIDsList(individualCheckBox)
                    .setMinSelectionLimit(0)
                    .setMaxSelectionLimit(listOfIndividuval.size())
                    .multiSelectList(listOfIndividuval) // the multi select model list with ids and name
                    .onSubmit(new MultiSelectDialog.SubmitCallbackListener() {
                        @Override
                        public void onSelected(ArrayList<Integer> selectedIds, ArrayList<String> selectedNames, String dataString) {
                            for (int i = 0; i < selectedIds.size(); i++) {
                                mIndividualCheckBox.setText(dataString);
                            }
                            individuvalArray = new JSONArray(selectedIds);
                        }

                        @Override
                        public void onCancel() {
                            Log.d("TAG", "Dialog cancelled");
                        }
                    });
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void attemptCreateProject() {
        mProjectName.setError(null);
        projectName = mProjectName.getText().toString();
        String mIndividualCheckBox = String.valueOf(individuvalArray);
        //individuvalArray.remove(0);
        System.out.println("aray"+individuvalArray);
        boolean cancel = false;
        View focusView = null;
        if (TextUtils.isEmpty(projectName)) {
            mProjectName.setError(getString(R.string.error_required));
            focusView = mProjectName;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
            HashMap<String, String> userId = session.getUserDetails();
            String id = userId.get(UserPrefUtils.ID);
            requestCrateTask(id, projectName, String.valueOf(individuvalArray).replace("[", "").replace("]", ""));
        }
    }
    private void requestCrateTask(String a, String b, String c) {
        Call<ProjectAddResponse> call = ANApplications.getANApi().checkProjectAddReponse(a, b, c);
        call.enqueue(new Callback<ProjectAddResponse>() {
            @Override
            public void onResponse(Call<ProjectAddResponse> call, Response<ProjectAddResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getSuccess().equals("true")) {
                        Intent i = new Intent(ViewProjectsActivity.this, ProjectFooterActivity.class);
                        startActivity(i);
                    } else {
                        Snackbar.make(mContentLayout, "Data Not Found", Snackbar.LENGTH_SHORT).show();
                    }
                } else {
                    AndroidUtils.displayToast(getApplicationContext(), "Something Went Wrong!!");
                }
            }
            @Override
            public void onFailure(Call<ProjectAddResponse> call, Throwable t) {
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
        tv_create.setText("Create");
        tv_create.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                attemptCreateProject();
            }
        });

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
