package com.example.shaf.ntuhackathon2018;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.shaf.ntuhackathon2018.Storage.ImagesRepo;
import com.example.shaf.ntuhackathon2018.Utils.ImageUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_IMAGE_IMPORT = 4;
    private static final int RC_HANDLE_CAMERA_PERM = 2;
    private static final int REQUEST_WRITE_PERMISSION = 20;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_UPDATE_DATA = 800;

    ImagesRepo imagesRepo;
    ImageView imageView;
    EditText textView;
    List<String> imageslist;
    TextExtractor textExtractor;
    private String pathString;
    private static Uri mCurrentPhotoPath;
    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;

    private final String TAG = MainActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imagesRepo = new ImagesRepo(this);
        textExtractor = new TextExtractor(this);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        imageslist = imagesRepo.getImages();


        adapter = new RecyclerAdapter(this);
        adapter.setImageslist(imageslist);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerItemCustomListener(this,
                recyclerView, new RecyclerItemCustomListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                imageslist = imagesRepo.getImages();
                adapter.setImageslist(imageslist);
                loadImage(position);

            }

            @Override
            public void onLongItemClick(View view, int position) {

//                showDeleteDialogBox(position);
            }
        }));



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


//
//        Log.e(TAG, imageslist.size() + "");
//        if (!imageslist.isEmpty()) {
//            textView.setText("LIST" + imageslist.get(imageslist.size() - 1));
//
//            String imagepath = imageslist.get(imageslist.size() - 1);
//            File file = new File(imagepath);
//            pathString = imagepath;
//            Glide.with(this).load(file).into(imageView);
//            updateDisplay();
//        }
    }

    private void loadImage(int position) {

        Intent intent = new Intent(MainActivity.this, OCRActivity.class);
        intent.putExtra("position", position);
        intent.putExtra("filepath", imageslist.get(position));
        startActivityForResult(intent, REQUEST_UPDATE_DATA);

    }


//    private void updateDisplay() {
//        imageslist = imagesRepo.getImages();
//        Log.e(TAG, imageslist.size() + "");
//        String extractedText = "no data";
//        if (!imageslist.isEmpty()) {
//            String imagepath = imageslist.get(imageslist.size() - 1);
//            File file = new File(imagepath);
//            try {
////            imageUri = Uri.fromFil  textExtractor = new TextExtractor(this);e(new File(image.getSource()));
//                extractedText = textExtractor.extractText(file);
//                Log.e(TAG, "SS: " + extractedText);
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
//            textView.setText(extractedText);
//            Glide.with(this).load(file).into(imageView);
//        }
//
//    }

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

            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        getString(R.string.provider_authority),
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//            }
            }
        }
    }

    @Override
    protected void onActivityResult ( int requestCode, int resultCode, Intent data){
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // Image capture intent result

                try {
                    onImageCapture();
                } catch (IOException e) {
                    e.printStackTrace();
                }


        } else if (requestCode == REQUEST_IMAGE_IMPORT && resultCode == Activity.RESULT_OK && data != null) {


            Uri uri = data.getData();
            mCurrentPhotoPath = uri;
            Glide.with(this).load(mCurrentPhotoPath).into(imageView);
            try {
                onImageImport();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private File createImageFile () throws IOException {

        final String imageFileName = UUID.randomUUID().toString();

//        // Get the directory for the user's public pictures directory.
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File photoFile = File.createTempFile(imageFileName, null, storageDir);

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = FileProvider.getUriForFile(this, getString(R.string.provider_authority),
                photoFile);

        pathString = photoFile.getPath();

        return photoFile;
    }

    private void onImageCapture() throws IOException {
        Log.e(TAG, "Photo successfully taken.");

        ContentResolver cr = this.getContentResolver();
        Bitmap bitmap = android.provider.MediaStore.Images.Media.getBitmap(cr, mCurrentPhotoPath);

        String key = ImagesRepo.saveImage(bitmap);
        Toast.makeText(this, key, Toast.LENGTH_SHORT).show();
//        updateDisplay();
        adapter.setImageslist(imagesRepo.getImages());
    }

    private void onImageImport() throws IOException {
        ContentResolver cr = this.getContentResolver();
        Bitmap bitmap = android.provider.MediaStore.Images.Media.getBitmap(cr, mCurrentPhotoPath);

        String key = ImagesRepo.saveImage(bitmap);
        Toast.makeText(this, key, Toast.LENGTH_SHORT).show();
//        updateDisplay();
        adapter.setImageslist(imagesRepo.getImages());
    }


    public void dispatchImportImageIntent () {
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
    public boolean onCreateOptionsMenu (Menu menu){
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item){
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

//
//
//    private void setPic() {
//        // Get the dimensions of the View
//        int targetW = imageView.getWidth();
//        int targetH = imageView.getHeight();
//
//        // Get the dimensions of the bitmap
//        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
//        bmOptions.inJustDecodeBounds = true;
//        BitmapFactory.decodeFile(pathString, bmOptions);
//        int photoW = bmOptions.outWidth;
//        int photoH = bmOptions.outHeight;
//
//        // Determine how much to scale down the image
//        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);
//
//        // Decode the image file into a Bitmap sized to fill the View
//        bmOptions.inJustDecodeBounds = false;
//        bmOptions.inSampleSize = scaleFactor;
//
//        Bitmap bitmap = BitmapFactory.decodeFile(pathString, bmOptions);
//        imageView.setImageBitmap(bitmap);
//    }

}
