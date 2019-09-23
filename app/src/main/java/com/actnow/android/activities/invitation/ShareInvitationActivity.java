package com.actnow.android.activities.invitation;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.actnow.android.ANApplications;
import com.actnow.android.R;
import com.actnow.android.adapter.CheckBoxAdapter;
import com.actnow.android.sdk.responses.CheckBoxResponse;
import com.actnow.android.sdk.responses.OrgnUserRecordsCheckBox;
import com.actnow.android.utils.AndroidUtils;
import com.actnow.android.utils.UserPrefUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShareInvitationActivity extends AppCompatActivity {
    RecyclerView mRecyclerViewIntivitattion;
    RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<OrgnUserRecordsCheckBox> orgnUserRecordsCheckBoxList = new ArrayList<OrgnUserRecordsCheckBox>();
    UserPrefUtils session;
    View mProgressView, mContentLayout;
    private SearchView searchView;

    CheckBoxAdapter checkBoxAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        session = new UserPrefUtils( getApplicationContext() );
        setContentView( R.layout.activity_share_invitation );
        initializeViews();
    }

    private void initializeViews() {
       /* mProgressView = findViewById( R.id.progress_bar );
        mContentLayout = findViewById( R.id.content_layout );*/
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // toolbar fancy stuff
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.toolbar_title);

        // white background notification bar
        mRecyclerViewIntivitattion = (RecyclerView) findViewById( R.id.intivitation_recyclerView );
        mLayoutManager = new LinearLayoutManager( getApplicationContext() );
        mRecyclerViewIntivitattion.setLayoutManager( mLayoutManager );
        mRecyclerViewIntivitattion.setItemAnimator( new DefaultItemAnimator() );
        checkBoxAdapter = new CheckBoxAdapter( orgnUserRecordsCheckBoxList, R.layout.individual_check, getApplicationContext() );
        mRecyclerViewIntivitattion.setAdapter( checkBoxAdapter );
        whiteNotificationBar(mRecyclerViewIntivitattion);
        fetchContacts();

    }

    private void fetchContacts() {
        HashMap<String, String> userId = session.getUserDetails();
        String id = userId.get(UserPrefUtils.ID);
        System.out.println("id" + id);

        Call<CheckBoxResponse> call = ANApplications.getANApi().checktheSpinnerResponse(id);
        call.enqueue(new Callback<CheckBoxResponse>() {
            @Override
            public void onResponse(Call<CheckBoxResponse> call, Response<CheckBoxResponse> response) {
                System.out.println("response" + response.raw());
                if (response.isSuccessful()) {
                    System.out.println("r" + response.raw());
                    if (response.body().getSuccess().equals("true")) {
                        System.out.println("data" + response.body().getSuccess());
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
        System.out.println( "output" + orgn_users_records );
        if (orgn_users_records.size() > 0) {
            for (int i = 0; orgn_users_records.size() > i; i++) {
                OrgnUserRecordsCheckBox orgnUserRecordsCheckBox = orgn_users_records.get( i );
                OrgnUserRecordsCheckBox orgnUserRecordsCheckBox1 = new OrgnUserRecordsCheckBox();
                orgnUserRecordsCheckBox1.setName( orgnUserRecordsCheckBox.getName() );
                orgnUserRecordsCheckBoxList.add( orgnUserRecordsCheckBox1 );
            }
            mRecyclerViewIntivitattion.setAdapter( new CheckBoxAdapter( orgnUserRecordsCheckBoxList, R.layout.individual_check, getApplicationContext() ) );
        }
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_sharing, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService( Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted

               // checkBoxAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                //checkBoxAdapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // close search view on back button pressed
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }

    private void whiteNotificationBar(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            getWindow().setStatusBarColor( Color.WHITE);
        }
    }

    public void onContactSelected(OrgnUserRecordsCheckBox orgnUserRecordsCheckBox) {
        Toast.makeText(getApplicationContext(), "Selected: " + orgnUserRecordsCheckBox.getName(), Toast.LENGTH_LONG).show();
    }
}
