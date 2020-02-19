package com.actnow.android.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.actnow.android.R;
import com.actnow.android.adapter.ImageAdapter;
import com.actnow.android.sdk.ItemOffsetDecoration;
import com.actnow.android.utils.DirectoryCleaner;

import java.io.File;
import java.util.ArrayList;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class GalleryActivity extends AppCompatActivity {

    private ImageAdapter imageAdapter;
    private static final int REQUEST_FOR_STORAGE_PERMISSION = 123;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        initializeView();
    }
    private void initializeView() {
        toolBar();
        populateImagesFromGallery();

        File dir = new File(getApplicationContext().getExternalFilesDir( Environment.DIRECTORY_PICTURES), "SignaturePad");
        new DirectoryCleaner(dir).clean();
        dir.delete();
    }
    public void toolBar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //mTitle.setText(getIntent().getExtras().getString("title").toUpperCase());
        ImageView mNav = (ImageView) toolbar.findViewById(R.id.img_menu);
        mNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentBack();
            }
        });
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
    private void populateImagesFromGallery() {
        if (!mayRequestGalleryImages()) {
            return;
        }

        ArrayList<String> imageUrls = loadPhotosFromNativeGallery();
        initializeRecyclerView(imageUrls);
    }
    private boolean mayRequestGalleryImages() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        if (checkSelfPermission(READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }

        if (shouldShowRequestPermissionRationale(READ_EXTERNAL_STORAGE)) {
            //promptStoragePermission();
            showPermissionRationaleSnackBar();
        } else {
            requestPermissions(new String[]{READ_EXTERNAL_STORAGE}, REQUEST_FOR_STORAGE_PERMISSION);
        }

        return false;
    }
    private ArrayList<String> loadPhotosFromNativeGallery() {
        final String[] columns = { MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID };
        final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
        Cursor imagecursor = managedQuery(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
                null, orderBy + " DESC");

        ArrayList<String> imageUrls = new ArrayList<String>();

        for (int i = 0; i < imagecursor.getCount(); i++) {
            imagecursor.moveToPosition(i);
            int dataColumnIndex = imagecursor.getColumnIndex(MediaStore.Images.Media.DATA);
            imageUrls.add(imagecursor.getString(dataColumnIndex));

            //System.out.println("=====> Array path => "+imageUrls.get(i));
        }

        return imageUrls;
    }
    private void initializeRecyclerView(ArrayList<String> imageUrls) {
        imageAdapter = new ImageAdapter(this, imageUrls);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),3);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new ItemOffsetDecoration(this, R.dimen.item_offset));
        recyclerView.setAdapter(imageAdapter);
    }
    private void showPermissionRationaleSnackBar() {
        Snackbar.make(findViewById(R.id.button1), getString(R.string.permission_rationale),
                Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Request the permission
                ActivityCompat.requestPermissions(GalleryActivity.this,
                        new String[]{READ_EXTERNAL_STORAGE},
                        REQUEST_FOR_STORAGE_PERMISSION);
            }
        }).show();

    }

    public void btnChoosePhotosClick(View view) {
        ArrayList<String> selectedItems = imageAdapter.getCheckedItems();

        if (selectedItems!= null && selectedItems.size() > 0) {
            //Toast.makeText(GalleryActivity.this, "Total photos selected: " + selectedItems.size(), Toast.LENGTH_SHORT).show();
            Log.d(GalleryActivity.class.getSimpleName(), "Selected Items: " + selectedItems.toString());
            Intent i = new Intent(getApplicationContext(),CommentsActivity.class);
            i.putExtra("screen","gallery");
            i.putStringArrayListExtra("images",selectedItems);
            i.putExtra("growerid",getIntent().getExtras().getString("growerid"));
            startActivity(i);
        }
    }

    public void intentBack(){
        Intent i = new Intent(getApplicationContext(),CommentsActivity.class);
        //i.putExtra("growerid",getIntent().getExtras().getString("growerid"));
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        intentBack();
    }
    /*protected void onRestart() {
        super.onRestart();
        session.checkLogin();
    }*/
}