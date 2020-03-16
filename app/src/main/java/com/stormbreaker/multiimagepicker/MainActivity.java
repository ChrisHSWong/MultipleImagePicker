package com.stormbreaker.multiimagepicker;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;
import com.esafirm.imagepicker.model.Image;
import com.fxn.pix.Options;
import com.fxn.pix.Pix;
import com.fxn.utility.ImageQuality;
import com.fxn.utility.PermUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.stormbreaker.multiimagepicker.Adapter.ImageAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.stormbreaker.multiimagepicker.utils.Tools.setSystemBarColor;
import static com.stormbreaker.multiimagepicker.utils.Tools.setSystemBarLight;

public class MainActivity extends AppCompatActivity {

    public final static String IMAGE_PICKER_PIX = "pix";
    public final static String IMAGE_PICKER_ESAFIRM = "esafirm";
    private final static int REQUEST_CODE_PIX = 100;
    private final static int REQUEST_CODE_ESAFIRM = 101;

    private String selectedImagePicker = IMAGE_PICKER_ESAFIRM;

    private ActionBar actionBar;
    private RecyclerView rvImageSelected;
    private FloatingActionButton fabSelectImage;
    private ImageAdapter mImageAdapter;
    private Options options;
    private ArrayList<String> returnImageListPix = new ArrayList<>();
    private ArrayList<Image> returnImageListEsafirm = new ArrayList<>();
    private TextView tvNoImage;
    private ImagePicker imagePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setTitle("Image Picker");
        actionBar.setDisplayHomeAsUpEnabled(false);
        setSystemBarColor(this, R.color.grey_5);
        setSystemBarLight(this);

        initView();
        bindData();
        bindEvent();
    }

    private void initView() {
        rvImageSelected = (RecyclerView) findViewById(R.id.rv_image_selected);
        fabSelectImage = (FloatingActionButton) findViewById(R.id.fab_select_image);
        tvNoImage = (TextView) findViewById(R.id.tv_no_image);
    }

    private void bindData() {
        tvNoImage.setVisibility(View.VISIBLE);
        if (mImageAdapter == null) {
            rvImageSelected.setLayoutManager(new LinearLayoutManager(this));
            mImageAdapter = new ImageAdapter(this);
            mImageAdapter.setPickerType(selectedImagePicker);
            rvImageSelected.setAdapter(mImageAdapter);
        } else {
            mImageAdapter.notifyDataSetChanged();
        }
        inititalPicker();
    }

    private void bindEvent() {
        fabSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFabClicked();
            }
        });

    }

    private void inititalPicker(){
        switch (selectedImagePicker){
            case IMAGE_PICKER_PIX:{
                options = Options.init()
                        .setRequestCode(REQUEST_CODE_PIX)
                        .setCount(20)
                        .setFrontfacing(false)
                        .setImageQuality(ImageQuality.LOW)
                        .setPreSelectedUrls(returnImageListPix)
                        .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT)
                        .setPath("/MultiImagePicker/Images");
            }
            break;

            case IMAGE_PICKER_ESAFIRM:
                if(imagePicker == null){
                    imagePicker = new ImagePicker.ImagePickerWithActivity(this);
                }
                break;
        }
    }

    private void onFabClicked(){
        switch (selectedImagePicker){
            case IMAGE_PICKER_PIX:{
                options.setPreSelectedUrls(returnImageListPix);
                Pix.start(MainActivity.this, options);
            }
            break;

            case IMAGE_PICKER_ESAFIRM:{
                imagePicker.returnMode(ReturnMode.NONE);// set whether pick and / or camera action should return immediate result or not.
                imagePicker.folderMode(true); // folder mode (false by default)
                imagePicker.toolbarFolderTitle("Folder"); // folder selection title
                imagePicker.toolbarImageTitle("Tap to select"); // image selection title
                imagePicker.toolbarArrowColor(ContextCompat.getColor(this, R.color.grey_60)); // Toolbar 'up' arrow color
                imagePicker.includeVideo(false); // Show video on image picker
//                imagePicker.single(); // single mode
                imagePicker.multi(); // multi mode (default mode)
                imagePicker.limit(20); // max images can be selected (99 by default)
                imagePicker.showCamera(false); // show camera or not (true by default)
                imagePicker.imageDirectory("Camera"); // directory name for captured image  ("Camera" folder by default)
                imagePicker.origin(returnImageListEsafirm); // original selected images, used in multi mode
//                imagePicker.exclude(images); // exclude anything that in image.getPath()
//                imagePicker.excludeFiles(files); // same as exclude but using ArrayList<File>
                imagePicker.theme(R.style.ImagePickerTheme); // must inherit ef_BaseTheme. please refer to sample
                imagePicker.enableLog(false); // disabling log
                imagePicker.imageDirectory("MultiImagePicker");
                imagePicker.start(); // start image picker activity with request code
            }
            break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Log.e("val", "requestCode ->  " + requestCode+"  resultCode "+resultCode);
        switch (requestCode) {
            case (REQUEST_CODE_PIX): {
                if (resultCode == RESULT_OK) {
                    returnImageListPix = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
                    mImageAdapter.addImagePix(returnImageListPix);
                    if(returnImageListPix.size() != 0){
                        tvNoImage.setVisibility(View.GONE);
                    }
                    else{
                        tvNoImage.setVisibility(View.VISIBLE);
                    }

                }
            }
            break;

        }
        if(ImagePicker.shouldHandle(requestCode, resultCode, data)){
            List<Image> images = ImagePicker.getImages(data);
            returnImageListEsafirm.clear();
            returnImageListEsafirm.addAll(images);
            mImageAdapter.addImageEsafirm(returnImageListEsafirm);
            if(returnImageListEsafirm.size() != 0){
                tvNoImage.setVisibility(View.GONE);
            }
            else{
                tvNoImage.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Pix.start(MainActivity.this, options);
                } else {
                    Toast.makeText(MainActivity.this, "Approve permissions to open Pix ImagePicker", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            switch (selectedImagePicker){
                case IMAGE_PICKER_PIX:{
                    returnImageListPix.clear();
                    mImageAdapter.addImagePix(returnImageListPix);
                    tvNoImage.setVisibility(View.VISIBLE);
                }
                break;

                case IMAGE_PICKER_ESAFIRM:{
                    returnImageListEsafirm.clear();
                    mImageAdapter.addImageEsafirm(returnImageListEsafirm);
                    tvNoImage.setVisibility(View.VISIBLE);
                }
                break;
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
