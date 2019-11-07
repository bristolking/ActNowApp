package com.actnow.android.activities;


import android.app.DatePickerDialog;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.abdeveloper.library.MultiSelectDialog;
import com.abdeveloper.library.MultiSelectModel;
import com.actnow.android.ANApplications;
import com.actnow.android.R;
import com.actnow.android.sdk.responses.CheckBoxResponse;
import com.actnow.android.sdk.responses.OrgnUserRecordsCheckBox;
import com.actnow.android.sdk.responses.ProjectListResponse;
import com.actnow.android.sdk.responses.ProjectListResponseRecords;
import com.actnow.android.utils.AndroidUtils;
import com.actnow.android.utils.UserPrefUtils;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AdvancedSearchActivity extends AppCompatActivity {
    Context context;
    View mProgressView, mContentLayout;
    UserPrefUtils session;
    TextView mDateAdvanced, mProjectAdvanced, mAdvancedIndividuals;
    private int mYear, mMonth, mDay;


    ArrayList<com.abdeveloper.library.MultiSelectModel> listOfIndividuval = new ArrayList<com.abdeveloper.library.MultiSelectModel>();
    ArrayList<com.abdeveloper.library.MultiSelectModel> listOfProjectNames = new ArrayList<MultiSelectModel>();

    MultiSelectDialog mIndividuvalDialog, mProjectDialog;
    ArrayList<Integer> individualCheckBox, projectListCheckBox;
    JSONArray individuvalArray;
    JSONArray projectArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        session = new UserPrefUtils( getApplicationContext() );
        setContentView( R.layout.activity_adanced_search );
        advancedSearch();
        initializeViews();

    }

    private void advancedSearch() {
        ImageView mImgBackSearchAdvanced = (ImageView) findViewById( R.id.img_backAdvnaced );
        mImgBackSearchAdvanced.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        } );
        EditText mAdvancedSearchEditText = (EditText) findViewById( R.id.edit_advncedSearch );
       /* mAdvancedSearchEditText.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText( getApplicationContext(),"Work in progress!",Toast.LENGTH_LONG).show();
            }
        } );*/
      /*  ImageView mSearchAdvancedImg =(ImageView)findViewById(R.id.img_advncedSearch);
        mSearchAdvancedImg.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText( getApplicationContext(),"Work in progress!",Toast.LENGTH_LONG).show();

            }
        } );*/

    }

    private void initializeViews() {

        mProgressView = findViewById( R.id.progress_bar );
        mContentLayout = findViewById( R.id.content_layout );
        mProjectDialog = new MultiSelectDialog();
        projectListCheckBox = new ArrayList<>();
        projectListCheckBox.add( 0 );
        requestDynamicProjectList();

        mIndividuvalDialog = new MultiSelectDialog();
        individualCheckBox = new ArrayList<>();
        individualCheckBox.add( 0 );
        requestIndividualDynamicContent();
        mDateAdvanced = (TextView) findViewById( R.id.tv_dateAdvanced );
        mDateAdvanced.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get( Calendar.YEAR );
                mMonth = c.get( Calendar.MONTH );
                mDay = c.get( Calendar.DAY_OF_MONTH );
                DatePickerDialog datePickerDialog = new DatePickerDialog( AdvancedSearchActivity.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // mDateReminderTextView.setText( year + "-" + (monthOfYear + 1) + "-" + dayOfMonth );

                    }
                }, mYear, mMonth, mDay );
                datePickerDialog.show();
            }
        } );
        mProjectAdvanced = (TextView) findViewById( R.id.tv_projectAdvanced );
        mProjectAdvanced.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProjectDialog.show( getSupportFragmentManager(), "mProjectDialog" );

            }
        } );
        mAdvancedIndividuals = (TextView) findViewById( R.id.tv_individualsAdvanced );
        mAdvancedIndividuals.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIndividuvalDialog.show( getSupportFragmentManager(), "mIndividuvalDialog" );

            }
        } );

    }

    private void requestDynamicProjectList() {
        HashMap<String, String> userId = session.getUserDetails();
        String id = userId.get( UserPrefUtils.ID );
        Call<ProjectListResponse> call = ANApplications.getANApi().checkProjectListResponse( id );
        call.enqueue( new Callback<ProjectListResponse>() {
            @Override
            public void onResponse(Call<ProjectListResponse> call, Response<ProjectListResponse> response) {
                //AndroidUtils.showProgress( false, mProgressView, mContentLayout );
                if (response.isSuccessful()) {
                    if (response.body().getSuccess().equals( "true" )) {
                        setProjectFooterList( response.body().getProject_records() );
                    } else {
                        Snackbar.make( mContentLayout, "Data Not Found", Snackbar.LENGTH_SHORT ).show();
                    }
                } else {
                    AndroidUtils.displayToast( getApplicationContext(), "Something Went Wrong!!" );
                }
            }

            @Override
            public void onFailure(Call<ProjectListResponse> call, Throwable t) {
                Log.d( "CallBack", " Throwable is " + t );
            }
        } );

    }

    private void setProjectFooterList(List<ProjectListResponseRecords> project_records) {
        if (project_records.size() > 0) {
            for (int i = 0; project_records.size() > i; i++) {
                ProjectListResponseRecords projectListResponseRecords = project_records.get( i );
                ProjectListResponseRecords projectListResponseRecords1 = new ProjectListResponseRecords();
                projectListResponseRecords1.setProject_code( (projectListResponseRecords.getProject_code()) );
                listOfProjectNames.add( new MultiSelectModel( Integer.parseInt( projectListResponseRecords.getProject_id() ), projectListResponseRecords.getName() ) );
            }
            mProjectDialog = new MultiSelectDialog()
                    .title( "Project" ) //setting title for dialog
                    .titleSize( 25 )
                    .positiveText( "Done" )
                    .negativeText( "Cancel" )
                    .preSelectIDsList( projectListCheckBox )
                    .setMinSelectionLimit( 0 )
                    .setMaxSelectionLimit( listOfProjectNames.size() )
                    .multiSelectList( listOfProjectNames ) // the multi select model list with ids and name
                    .onSubmit( new MultiSelectDialog.SubmitCallbackListener() {
                        @Override
                        public void onSelected(ArrayList<Integer> selectedIds, ArrayList<String> selectedNames, String dataString) {
                            for (int i = 0; i < selectedIds.size(); i++) {
                                // mProjectCheckBox.setText( dataString );
                                //mProjectCheckBox.setText(projectcode);

                            }
                            projectArray = new JSONArray( selectedIds );
                        }

                        @Override
                        public void onCancel() {
                            Log.d( "TAG", "Dialog cancelled" );
                        }
                    } );
        }

    }

    private void requestIndividualDynamicContent() {
        HashMap<String, String> userId = session.getUserDetails();
        String id = userId.get( UserPrefUtils.ID );
        Call<CheckBoxResponse> call = ANApplications.getANApi().checktheSpinnerResponse( id );
        call.enqueue( new Callback<CheckBoxResponse>() {
            @Override
            public void onResponse(Call<CheckBoxResponse> call, Response<CheckBoxResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getSuccess().equals( "true" )) {
                        setLoadCheckBox( response.body().getOrgn_users_records() );
                    } else {
                        Snackbar.make( mContentLayout, "Data Not Found", Snackbar.LENGTH_SHORT ).show();
                    }
                } else {
                    AndroidUtils.displayToast( getApplicationContext(), "Something Went Wrong!!" );
                }
            }

            @Override
            public void onFailure(Call<CheckBoxResponse> call, Throwable t) {
                Log.d( "CallBack", " Throwable is " + t );
            }
        } );

    }

    private void setLoadCheckBox(List<OrgnUserRecordsCheckBox> orgn_users_records) {
        if (orgn_users_records.size() > 0) {
            for (int i = 0; orgn_users_records.size() > i; i++) {
                OrgnUserRecordsCheckBox orgnUserRecordsCheckBox = orgn_users_records.get( i );
                OrgnUserRecordsCheckBox orgnUserRecordsCheckBox1 = new OrgnUserRecordsCheckBox();
                orgnUserRecordsCheckBox1.setEmail( orgnUserRecordsCheckBox.getEmail() );
                listOfIndividuval.add( new MultiSelectModel( Integer.parseInt( orgnUserRecordsCheckBox.getId() ), orgnUserRecordsCheckBox.getName() ) );
            }
            mIndividuvalDialog = new MultiSelectDialog()
                    .title( "Individuval" ) //setting title for dialog
                    .titleSize( 25 )
                    .positiveText( "Done" )
                    .negativeText( "Cancel" )
                    .preSelectIDsList( individualCheckBox )
                    .setMinSelectionLimit( 0 )
                    .setMaxSelectionLimit( listOfIndividuval.size() )
                    .multiSelectList( listOfIndividuval ) // the multi select model list with ids and name
                    .onSubmit( new MultiSelectDialog.SubmitCallbackListener() {
                        @Override
                        public void onSelected(ArrayList<Integer> selectedIds, ArrayList<String> selectedNames, String dataString) {
                            for (int i = 0; i < selectedIds.size(); i++) {
                                //mUserReminder.setText(dataString);
                            }
                            individuvalArray = new JSONArray( selectedIds );
                        }

                        @Override
                        public void onCancel() {
                            Log.d( "TAG", "Dialog cancelled" );
                        }
                    } );
        }
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.search_menu, menu );
        return true;
    }

    public void onBackPressed() {
        super.onBackPressed();

    }

}
