package com.example.shaf.ntuhackathon2018;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shaf.ntuhackathon2018.Storage.ImagesRepo;
import com.example.shaf.ntuhackathon2018.Storage.ImagesRepository;
import com.example.shaf.ntuhackathon2018.Storage.InMemoryImagesRepository;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_IMAGE_IMPORT = 4;
    private static final int RC_HANDLE_CAMERA_PERM = 2;
    private static final int REQUEST_WRITE_PERMISSION = 20;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    ImagesRepo imagesRepo;

    private final String TAG = MainActivity.class.getSimpleName();

    private static Uri currentPhotoPath;
    private String pathString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        getSupportActionBar().setElevation(0);

        imagesRepo = new ImagesRepo(this);

        int rc = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (rc == PackageManager.PERMISSION_GRANTED) {
            //   createCameraSource(autoFocus, useFlash);
        } else {
            requestCameraPermission();
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(MainActivity.this, new
                        String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });

        TextView textView = findViewById(R.id.text_view);
        List<Image> imageslist = imagesRepo.getImages();
        textView.setText("LIST" + imageslist.size());

        ImageView imageView = findViewById(R.id.image_view);
        imageView.setImageBitmap(imageslist.get(0).getImage());
    }



    /**
     * Handles the requesting of the camera permission.  This includes
     * showing a "Snackbar" message of why the permission is needed then
     * sending the request.
     */
    private void requestCameraPermission() {
        final String[] permissions = new String[]{Manifest.permission.CAMERA};
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(this, permissions, RC_HANDLE_CAMERA_PERM);
            return;
        }

//        final Activity thisActivity = this;

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(MainActivity.this, permissions,
                        RC_HANDLE_CAMERA_PERM);
            }
        };

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_WRITE_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    dispatchTakePictureIntent();
                } else {
                    Toast.makeText(MainActivity.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

//            try {
//                photoFile = createImageFile();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            // Continue only if the File was successfully created
//            if (photoFile != null) {
//                Uri photoURI = FileProvider.getUriForFile(this,
//                        "com.example.android.fileprovider",
//                        photoFile);
//                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // Image capture intent result
            if (data.getExtras() != null) {
                Log.e(TAG, data.getExtras().toString());
                onImageCapture(data.getExtras());

            } else
                Toast.makeText(this, "Image Capture: No Data", Toast.LENGTH_SHORT).show();

        } else if (requestCode == REQUEST_IMAGE_IMPORT && resultCode == Activity.RESULT_OK && data != null) {
            // Image import intent result
            // onImageImport(data.getExtras());

            // The ACTION_GET_CONTENT Intent returns a URI pointing to the file.
            // It does not return the file itself. Extract the URI
            // from the Intent and process it.
            Uri uri = data.getData();
        //  importUriImage(uri);

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private File createImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

//        // Get the directory for the user's public pictures directory.
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File photoFile = File.createTempFile(imageFileName, null, storageDir);

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = FileProvider.getUriForFile(this,getString(R.string.provider_authority),
                photoFile);

        pathString = photoFile.getPath();

        Toast.makeText(this, currentPhotoPath.toString() + "\n" + pathString, Toast.LENGTH_LONG).show();
        return photoFile;
    }

    private void onImageCapture(Bundle extras) {
        Bitmap imageBitmap = (Bitmap) extras.get("data");
    //    Log.e(TAG, imageBitmap.toString());
        Log.e(TAG, "Photo successfully taken.");
        String key = imagesRepo.saveImage(imageBitmap);
          Toast.makeText(this, key, Toast.LENGTH_SHORT).show();
        /*update UI!!*/
    }


    public void dispatchImportImageIntent() {
        // Use an ACTION_GET_CONTENT intent to select a file using the system's file browser.
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        // Filter files only to those that can be "opened" and directly accessed as a stream.
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        // Only show images.
        intent.setType("image/*");
        // launch activity
        startActivityForResult(intent, REQUEST_IMAGE_IMPORT);
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
        if (id == R.id.import_image) {
            dispatchImportImageIntent();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



}
