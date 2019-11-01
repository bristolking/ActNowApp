package com.actnow.android.activities.invitation;


import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.abdeveloper.library.MultiSelectDialog;
import com.abdeveloper.library.MultiSelectModel;
import com.actnow.android.ANApplications;
import com.actnow.android.R;
import com.actnow.android.adapter.SendInvitationAdapter;

import com.actnow.android.sdk.responses.CheckBoxResponse;
import com.actnow.android.sdk.responses.OrgnUserRecordsCheckBox;
import com.actnow.android.sdk.responses.UserSendInvitations;
import com.actnow.android.utils.AndroidUtils;
import com.actnow.android.utils.UserPrefUtils;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.activeandroid.Cache.getContext;


public class SendInvitationActivity extends AppCompatActivity {
    View mProgressView, mContentLayout;
    UserPrefUtils session;
    private RecyclerView mRecyclerView;
    private SendInvitationAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private ArrayList<OrgnUserRecordsCheckBox> orgnUserRecordsCheckBoxList = new ArrayList<OrgnUserRecordsCheckBox>();

    ArrayList<MultiSelectModel> listOfIndividuval = new ArrayList<MultiSelectModel>();
    ArrayList<MultiSelectModel> listOfProjectNames = new ArrayList<MultiSelectModel>();

    MultiSelectDialog mIndividuvalDialog, mProjectDialog;
    ArrayList<Integer> individualCheckBox, projectListCheckBox;
    JSONArray individuvalArray;
    JSONArray projectArray;
    TextView mTextShareIndividual;

    String task_code;
    String sendInvitaionprojectCode;
    String orncode;
    String id;
    EditText editText;
    TextView mTvName;
    TextView mTvEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_send_invitation );
        session = new UserPrefUtils( getApplicationContext() );
        buildRecyclerView();
        appheaderSend();
        requestIndividualDynamicContent();
        initializeViews();
        HashMap<String, String> userId = session.getUserDetails();
        id = userId.get( UserPrefUtils.ID );
        orncode = userId.get( UserPrefUtils.ORGANIZATIONNAME );
        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        if (b != null) {
            task_code = (String) b.get( "TaskCode" );
            sendInvitaionprojectCode = (String) b.get( "SenIvitaionprojectCode" );

        }

    }

    private void appheaderSend() {
        ImageView imgBackSend = (ImageView) findViewById( R.id.image_back_send );
        imgBackSend.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        } );

        TextView mTextTileSend = (TextView) findViewById( R.id.txt_title_send );
        mTextTileSend.setText( "Invitations" );
      /*  ImageView imageMenuSend = (ImageView) findViewById( R.id.image_menu_send );
        imageMenuSend.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup( view );
            }
        } );*/
    }

    private void initializeViews() {
        mProgressView = findViewById( R.id.progress_bar );
        mContentLayout = findViewById( R.id.content_layout );
        View view = (View) findViewById( R.id.rv_recylerViewShareIndividual );
        view.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIndividuvalDialog.show( getSupportFragmentManager(), "mIndividuvalDialog" );
            }
        } );
        mTextShareIndividual = (TextView) findViewById( R.id.tv_allIndividuals );
        mIndividuvalDialog = new MultiSelectDialog();
        individualCheckBox = new ArrayList<>();
        individualCheckBox.add( 0 );
        editText = findViewById( R.id.editTextRecylerView );
        editText.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter( s.toString() );
            }
        } );
        ImageView imgSendInvitations = (ImageView) findViewById( R.id.img_search_send );
        imgSendInvitations.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendInvitationCall();
            }
        } );
    }

    private void sendInvitationCall() {

        String invite_emails = editText.getText().toString();
        Call<UserSendInvitations> userSendInvitationsCall = ANApplications.getANApi().
                cheTheUserSend( id, orncode, sendInvitaionprojectCode, task_code, invite_emails );
        System.out.println( "severReponse" + id + orncode + sendInvitaionprojectCode + task_code + invite_emails );
        userSendInvitationsCall.enqueue( new Callback<UserSendInvitations>() {
            @Override
            public void onResponse(Call<UserSendInvitations> call, Response<UserSendInvitations> response) {
                if (response.isSuccessful()) {
                    System.out.println( "severReponse1" + response.raw() );
                    if (response.body().getSuccess().equals( "true" )) {
                        System.out.println( "severReponse2" + response.body().getSuccess() );
                        Intent i = new Intent( getApplicationContext(), InvitationActivity.class );
                        startActivity( i );
                        finish();

                    } else {
                        Snackbar.make( mContentLayout, "Data Not Found", Snackbar.LENGTH_SHORT ).show();

                    }
                } else {
                    AndroidUtils.displayToast( getApplicationContext(), "Something Went Wrong!!" );

                }
            }

            @Override
            public void onFailure(Call<UserSendInvitations> call, Throwable t) {
                Log.d( "CALLBACk", "THORBALE" + t );


            }
        } );

    }

    private void filter(String text) {
        ArrayList<OrgnUserRecordsCheckBox> exampleItemfilteredList = new ArrayList<>();

        for (OrgnUserRecordsCheckBox item : orgnUserRecordsCheckBoxList) {
            if (item.getEmail().toLowerCase().contains( text.toLowerCase() ) && (item.getEmail().toLowerCase().contains( text.toLowerCase() ))) {
                exampleItemfilteredList.add( item );
            }
        }

        mAdapter.filterList( exampleItemfilteredList );
    }

    private void buildRecyclerView() {
        mRecyclerView = findViewById( R.id.recyclerView_share );
        mRecyclerView.setHasFixedSize( true );
        mLayoutManager = new LinearLayoutManager( this );
        mAdapter = new SendInvitationAdapter( orgnUserRecordsCheckBoxList );
        mRecyclerView.setLayoutManager( mLayoutManager );
        mRecyclerView.setAdapter( mAdapter );
        HashMap<String, String> userId = session.getUserDetails();
        String id = userId.get( UserPrefUtils.ID );
        Call<CheckBoxResponse> call = ANApplications.getANApi().checktheSpinnerResponse( id );
        call.enqueue( new Callback<CheckBoxResponse>() {
            @Override
            public void onResponse(Call<CheckBoxResponse> call, Response<CheckBoxResponse> response) {
                AndroidUtils.showProgress( false, mProgressView, mContentLayout );
                if (response.isSuccessful()) {
                    if (response.body().getSuccess().equals( "true" )) {
                        setTasskList( response.body().getOrgn_users_records() );
                    } else {
                        Snackbar.make( mContentLayout, "Data Not Found", Snackbar.LENGTH_SHORT ).show();
                    }
                } else {
                    AndroidUtils.displayToast( getApplicationContext(), "Something Went Wrong!!" );
                }
            }

            @Override
            public void onFailure(Call<CheckBoxResponse> call, Throwable t) {
                Log.d( "CALLBACk", "THORBALE" + t );

            }
        } );
    }

    private void setTasskList(List<OrgnUserRecordsCheckBox> orgn_users_records) {
        if (orgn_users_records.size() > 0) {
            for (int i = 0; orgn_users_records.size() > i; i++) {
                OrgnUserRecordsCheckBox orgnUserRecordsCheckBox = orgn_users_records.get( i );
                OrgnUserRecordsCheckBox orgnUserRecordsCheckBox1 = new OrgnUserRecordsCheckBox();
                orgnUserRecordsCheckBox1.setId( orgnUserRecordsCheckBox.getId() );
                orgnUserRecordsCheckBox1.setName( orgnUserRecordsCheckBox.getName() );
                orgnUserRecordsCheckBox1.setEmail( orgnUserRecordsCheckBox.getEmail() );
                orgnUserRecordsCheckBoxList.add( orgnUserRecordsCheckBox1 );
            }
        }
        mRecyclerView.setAdapter( mAdapter );
        mRecyclerView.addOnItemTouchListener( new SendInvitationActivity.RecyclerTouchListener( this, mRecyclerView, new SendInvitationActivity.ClickListener() {
            @Override
            public void onClick(final View view, int position) {
                final View view1 = view.findViewById( R.id.sendCustom);
                 mTvName = findViewById(R.id.textView);
                 mTvEmail = findViewById(R.id.textView2);
                 final String textEmail = mTvEmail.getText().toString();
                view1.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Call<UserSendInvitations> userSendInvitationsCall = ANApplications.getANApi().
                                cheTheUserSend( id, orncode, sendInvitaionprojectCode, task_code, textEmail );
                        System.out.println( "severReponse" + id + orncode + sendInvitaionprojectCode + task_code + textEmail );
                        userSendInvitationsCall.enqueue( new Callback<UserSendInvitations>() {
                            @Override
                            public void onResponse(Call<UserSendInvitations> call, Response<UserSendInvitations> response) {
                                if (response.isSuccessful()) {
                                    System.out.println( "severReponse1" + response.raw() );
                                    if (response.body().getSuccess().equals( "true" )) {
                                        System.out.println( "severReponse2" + response.body().getSuccess() );
                                        Intent i = new Intent( getApplicationContext(), InvitationActivity.class );
                                        startActivity( i );
                                        finish();

                                    } else {
                                        Snackbar.make( mContentLayout, "Data Not Found", Snackbar.LENGTH_SHORT ).show();

                                    }
                                } else {
                                    AndroidUtils.displayToast( getApplicationContext(), "Something Went Wrong!!" );

                                }
                            }

                            @Override
                            public void onFailure(Call<UserSendInvitations> call, Throwable t) {
                                Log.d( "CALLBACk", "THORBALE" + t );


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

    public class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private SendInvitationActivity.ClickListener clicklistener;
        private GestureDetector gestureDetector;

        public RecyclerTouchListener(SendInvitationActivity context, final RecyclerView mRecylerViewSingleSub, SendInvitationActivity.ClickListener clickListener) {
            this.clicklistener = clickListener;
            gestureDetector = new GestureDetector( getContext(), new GestureDetector.SimpleOnGestureListener() {

                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

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
                                mTextShareIndividual.setText( dataString );
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

}

