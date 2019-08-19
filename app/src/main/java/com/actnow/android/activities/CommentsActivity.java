package com.actnow.android.activities;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actnow.android.R;
import com.actnow.android.activities.settings.EditAccountActivity;
import com.actnow.android.activities.settings.PremiumActivity;
import com.actnow.android.activities.settings.SettingsActivity;
import com.actnow.android.utils.UserPrefUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

import static android.view.Gravity.TOP;
import static android.view.View.GONE;


public class CommentsActivity extends AppCompatActivity {
    final Context context = this;
    View mProgressView, mContentLayout;
    UserPrefUtils session;

    String messageText;


    private final int requestCode = 20;

    private int PICK_IMAGE_REQUEST = 1;
    private static final int STORAGE_PERMISSION_CODE = 123;
    private Bitmap bitmap;
    private Uri filePath;

    ArrayAdapter<String> adapter;
    ArrayList<String> itemList;
    EditText mEditAddComment;
    ImageView mImgAttachament;
    ListView mListView;
    ImageView imageGallery;
    int clickCounter = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new UserPrefUtils(getApplicationContext());
        setContentView(R.layout.activity_comments);
        appHeaderTwo();
        initializeViews();
        footer();

    }

    private void appHeaderTwo() {
        ImageView imgeBack = ( ImageView) findViewById(R.id.image_back_two);
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
        requestStoragePermission();
        String[] items = {""};
        itemList =new ArrayList<String>(Arrays.asList("GOOD"));
        adapter = new ArrayAdapter<String>(this, R.layout.custom_comment_list,R.id.tv_commentText,itemList);
        mEditAddComment = (EditText) findViewById(R.id.et_commentEdit);
        ImageView imgMenu = (ImageView)findViewById(R.id.img_menuComment);
        mListView = (ListView) findViewById(R.id.listView);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Dialog dialog = new Dialog(context, android.R.style.Theme_DeviceDefault_Dialog_Alert);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.comment_edit_delete);
                int location[]=new int[2];
                mListView.getLocationOnScreen(location);
                Window window = dialog.getWindow();
                TextView tv_Edit =(TextView)dialog.findViewById(R.id.tv_editComment);
                TextView tv_Delete =(TextView)dialog.findViewById(R.id.tv_deleteComment);
                tv_Edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(),"Work in Progress!",Toast.LENGTH_SHORT).show();
                    }
                });
                tv_Delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(),"Work in Progress!",Toast.LENGTH_SHORT).show();
                    }
                });
                WindowManager.LayoutParams wlp = dialog.getWindow().getAttributes();
                wlp.gravity = Gravity.RIGHT|TOP;
                wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
                wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
                wlp.x = 1; // The new position of the X coordinates
                wlp.y = 1; // The new position of the Y coordinates
                wlp.width = 300;// Width
                wlp.height= 300;
                window.setAttributes(wlp);
                dialog.show();
            }
        });

       /* itemList =new ArrayList<String>(Arrays.asList(""));
        mEditAddComment = (EditText) findViewById(R.id.et_commentEdit);

        adapter=new CustomListAdapter(this, itemList, imgid);
        list=(ListView)findViewById(R.id.listView);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                //String Slecteditem= itemname[+position];
                Toast.makeText(getApplicationContext(), Slecteditem, Toast.LENGTH_SHORT).show();

            }
        });*/
    }

    private void footer() {
        imageGallery = (ImageView) findViewById(R.id.image_gallery);
        imageGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });
        ImageView imageAttachament = (ImageView) findViewById(R.id.image_attachament);
        imageAttachament.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();

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
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                submit();
            }
        });

    }

    private void submit() {
        String newItem = mEditAddComment.getText().toString();
        mEditAddComment.setText(" ");
        itemList.add(newItem);
        mImgAttachament=(ImageView)findViewById(R.id.img_userprofileComment);
        //int newImage = imageGallery.getImageAlpha();
        //itemList.add(String.valueOf(newImage));

        adapter.notifyDataSetChanged();
    }


    public void uploadMultipart() {
        //getting name for the image
        // String name = editText.getText().toString().trim();

        //getting the actual path of the image
        String path = getPath(filePath);

        //Uploading code
        try {
            String uploadId = UUID.randomUUID().toString();

          /*  //Creating a multi part request
            new MultipartUploadRequest(this, uploadId, SyncStateContract.Constants.UPLOAD_URL)
                    .addFileToUpload(path, "image") //Adding file
                    .addParameter("name", name) //Adding text parameter to the request
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .startUpload(); //Starting the upload*/

        } catch (Exception exc) {
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
               // mImgAttachament=(ImageView)findViewById(R.id.img_userprofileComment);
                imageGallery.setImageBitmap(bitmap);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //method to get the file path from uri
    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }


    //Requesting permission
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

        }
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == STORAGE_PERMISSION_CODE) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
    }
}
