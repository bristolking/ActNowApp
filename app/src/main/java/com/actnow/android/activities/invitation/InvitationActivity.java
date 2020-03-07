package com.actnow.android.activities.invitation;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.actnow.android.ANApplications;
import com.actnow.android.R;
import com.actnow.android.adapter.ShareAdapter;
import com.actnow.android.sdk.responses.CheckBoxResponse;
import com.actnow.android.sdk.responses.OrgnUserRecordsCheckBox;
import com.actnow.android.sdk.responses.UserDeleted;
import com.actnow.android.utils.AndroidUtils;
import com.actnow.android.utils.UserPrefUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class InvitationActivity extends AppCompatActivity {

    RecyclerView mRecyclerViewIntivitattion;
    RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<OrgnUserRecordsCheckBox> orgnUserRecordsCheckBoxList = new ArrayList<OrgnUserRecordsCheckBox>();
    UserPrefUtils session;
    View mProgressView, mContentLayout;
    final Context context = this;
    String task_code;
    String projectcode;
    String orncode;
    String id;
    String invitee_id;
    String sendInvitaionprojectCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        session = new UserPrefUtils( getApplicationContext() );
        setContentView( R.layout.activity_invitation );
        appSharing();
        initializeViews();
        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        if (b != null) {
            task_code = (String) b.get( "TaskCode" );
            projectcode = (String) b.get( "projectCode" );
            sendInvitaionprojectCode = (String)b.get("SenIvitaionprojectCode");
            System.out.println( "severValues" + task_code + projectcode  + sendInvitaionprojectCode );

        }
    }

    private void appSharing() {
        ImageView imgeBackSharing = (ImageView) findViewById( R.id.image_backsharing );
        imgeBackSharing.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        } );
        TextView tv_titleSharing = (TextView) findViewById( R.id.txt_sharing );
        tv_titleSharing.setText( "sharing" );
        ImageView imgeUserAddIntivitation = (ImageView) findViewById( R.id.img_shareUserIntivite );
        imgeUserAddIntivitation.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent( getApplicationContext(), SendInvitationActivity.class );
                i.putExtra( "TaskCode", task_code );
                i.putExtra( "SenIvitaionprojectCode",sendInvitaionprojectCode);
                startActivity( i );
            }
        } );
        /*ImageView imgInvititationMenu = (ImageView) findViewById( R.id.img_appHeaderMenuSharing );
        imgInvititationMenu.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(view);
            }
        } );*/
    }

    private void initializeViews() {
        mProgressView = findViewById( R.id.progress_bar );
        mContentLayout = findViewById( R.id.content_layout );
        mRecyclerViewIntivitattion = (RecyclerView) findViewById( R.id.intivitation_recyclerView );
        mLayoutManager = new LinearLayoutManager( getApplicationContext() );
        mRecyclerViewIntivitattion.setLayoutManager( mLayoutManager );
        mRecyclerViewIntivitattion.setItemAnimator( new DefaultItemAnimator() );
        ShareAdapter shareAdapter = new ShareAdapter( orgnUserRecordsCheckBoxList, R.layout.share_intivitaion_list, getApplicationContext() );
        mRecyclerViewIntivitattion.setAdapter( shareAdapter );
        HashMap<String, String> userId = session.getUserDetails();
        String id = userId.get( UserPrefUtils.ID );
        System.out.println( "id" + id );
        Call<CheckBoxResponse> call = ANApplications.getANApi().checktheSpinnerResponse( id );
        call.enqueue( new Callback<CheckBoxResponse>() {
            @Override
            public void onResponse(Call<CheckBoxResponse> call, Response<CheckBoxResponse> response) {
                AndroidUtils.showProgress( false, mProgressView, mContentLayout );
                System.out.println( "response" + response.raw() );
                if (response.isSuccessful()) {
                    System.out.println( "r" + response.raw() );
                    if (response.body().getSuccess().equals( "true" )) {
                        System.out.println( "data" + response.body().getSuccess() );
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
        System.out.println( "output" + orgn_users_records );
        if (orgn_users_records.size() > 0) {
            for (int i = 0; orgn_users_records.size() > i; i++) {
                OrgnUserRecordsCheckBox orgnUserRecordsCheckBox = orgn_users_records.get( i );
                OrgnUserRecordsCheckBox orgnUserRecordsCheckBox1 = new OrgnUserRecordsCheckBox();
                orgnUserRecordsCheckBox1.setId( orgnUserRecordsCheckBox.getId() );
                orgnUserRecordsCheckBox1.setName( orgnUserRecordsCheckBox.getName() );
                orgnUserRecordsCheckBox1.setEmail( orgnUserRecordsCheckBox.getEmail() );
                orgnUserRecordsCheckBox1.setId( orgnUserRecordsCheckBox.getId() );
                orgnUserRecordsCheckBoxList.add( orgnUserRecordsCheckBox1 );

            }
            mRecyclerViewIntivitattion.setAdapter( new ShareAdapter( orgnUserRecordsCheckBoxList, R.layout.share_intivitaion_list, getApplicationContext() ) );
            mRecyclerViewIntivitattion.addOnItemTouchListener( new InvitationActivity.RecyclerTouchListener( this, mRecyclerViewIntivitattion, new ClickListener() {
                @Override
                public void onClick(final View view, int position) {
                    View view1 = (View) findViewById( R.id.re_viewIntivitation );
                    TextView mShareNameIntivition = (TextView) view.findViewById( R.id.tv_shareName );
                    TextView mShareEamilIntivitation = (TextView) view.findViewById( R.id.tv_shareEmail );
                    final TextView mShereId = (TextView) view.findViewById( R.id.tv_shareId );
                    HashMap<String, String> userId = session.getUserDetails();
                    id = userId.get( UserPrefUtils.ID );
                    orncode = userId.get( UserPrefUtils.ORGANIZATIONNAME );
                    String email = userId.get( UserPrefUtils.EMAIL );
                    invitee_id = mShereId.getText().toString();
                    ImageView mImgDelete = (ImageView) view.findViewById( R.id.img_sharedelete );
                    mImgDelete.setOnClickListener( new View.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.M)
                        @Override
                        public void onClick(View view) {
                            Call<UserDeleted> userDeletedCall = ANApplications.getANApi().checkTheUserDelete( id, orncode, projectcode, task_code, invitee_id );
                            System.out.println( "severReponse1" + id + orncode + projectcode + task_code + invitee_id);

                            userDeletedCall.enqueue( new Callback<UserDeleted>() {
                                @Override
                                public void onResponse(Call<UserDeleted> call, Response<UserDeleted> response) {
                                    if (response.isSuccessful()) {
                                        if (response.body().getSuccess().equals( "true" )) {
                                            Toast.makeText( getApplicationContext(), "Sussfully deleted ", Toast.LENGTH_SHORT ).show();
                                        } else {
                                            Snackbar.make( mContentLayout, "Data Not Found", Snackbar.LENGTH_SHORT ).show();
                                        }
                                    } else {
                                        AndroidUtils.displayToast( getApplicationContext(), "Something Went Wrong!!" );
                                    }
                                }
                                @Override
                                public void onFailure(Call<UserDeleted> call, Throwable t) {
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
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    /**
     * RecyclerView: Implementing single item click and long press (Part-II)
     * <p>
     * - creating an innerclass implementing RevyvlerView.OnItemTouchListener
     * - Pass clickListener interface as parameter
     */
    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private ClickListener clicklistener;
        private GestureDetector gestureDetector;

        public RecyclerTouchListener(InvitationActivity context, final RecyclerView mRecylerViewSingleSub, ClickListener clickListener) {
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

}



