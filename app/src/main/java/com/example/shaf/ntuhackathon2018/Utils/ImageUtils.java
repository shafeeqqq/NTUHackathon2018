package com.example.shaf.ntuhackathon2018.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.FileNotFoundException;


/**
 * Various handy image utility methods.
 */
public class ImageUtils {

    /**
     * Calculates the sample size to load an image, described by its
     * {@link android.graphics.BitmapFactory.Options}
     * into a given width and height.
     * <p>
     * Source: https://developer.android.com/topic/performance/graphics/load-bitmap.html
     */
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    /**
     * Loads an image with a maximum width and height.
     * <p>
     * Source based on: https://developer.android.com/topic/performance/graphics/load-bitmap.html
     */
    public static Bitmap decodeSampledBitmapFromFile(String file,
                                                     int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(file, options);
    }


    public static Bitmap decodeBitmap(Context context, Uri uri) throws FileNotFoundException {
        int targetW = 600; int targetH = 600;

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        //  BitmapFactory.decodeFile(uri.getPath(), bmOptions);
        BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        return BitmapFactory.decodeStream(context.getContentResolver()
                .openInputStream(uri), null, bmOptions);
    }
}
