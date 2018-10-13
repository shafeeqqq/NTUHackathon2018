package com.example.shaf.ntuhackathon2018.Storage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.shaf.ntuhackathon2018.Image;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ImagesRepo {

    private static final String TAG = "LocalImagesRepository";
    private static final String PATH = "secureimages/";

    private static File mStorage;

    private static HashMap<String, Bitmap> mImageMap = new HashMap<>();


    public ImagesRepo(Context context) {

        File internalStorage = context.getFilesDir();
        mStorage = new File(internalStorage, PATH);

        if (!mStorage.exists()) {
            if (!mStorage.mkdirs()) {
                Log.e(TAG, "Could not create storage directory: " + mStorage.getAbsolutePath());

            }
        }
    }


    public static String saveImage(Bitmap image) {
        final String imageFile = UUID.randomUUID().toString() + ".png";
        mImageMap.put(imageFile, image);

        File filename = new File(mStorage, imageFile);
        try (FileOutputStream out = new FileOutputStream(filename)) {
            image.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
        } catch (IOException e) {
            e.printStackTrace();
        }

        return imageFile;
    }

    public List<Image> getImages() {
        File[] files = mStorage.listFiles();
        if (files == null) {
            Log.e(TAG, "Could not list files.");
            return null;
        }
        ArrayList<Image> list = new ArrayList<>(files.length);
        for (File f : files) {
            Bitmap bitmap = BitmapFactory.decodeFile(f.getAbsolutePath());
            list.add(new Image(f.getAbsolutePath(), bitmap));
        }
        return list;
    }
}
