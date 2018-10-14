package com.example.shaf.ntuhackathon2018;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.util.SparseArray;

import com.example.shaf.ntuhackathon2018.Utils.ImageUtils;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.File;
import java.io.FileNotFoundException;

public class TextExtractor {

    private static Uri mCurrentPhotoPath;
    private TextRecognizer mExtractor;
    private Context mContext;

    TextExtractor(Context context) {
        mContext = context;
        mExtractor = new TextRecognizer.Builder(context).build();
    }

    public String extractText(File file) throws FileNotFoundException {
        StringBuilder finalString = new StringBuilder();
        Bitmap bitmap = ImageUtils.decodeSampledBitmapFromFile(file.getAbsolutePath(), 600, 600);
      //  Bitmap bitmap = ImageUtils.decodeBitmap(mContext, path);
//        Log.e("WW", "Height " + bitmap.getHeight() + "x" + bitmap.getWidth());

        if (mExtractor.isOperational() && bitmap != null) {
            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
            SparseArray<TextBlock> textBlocks = mExtractor.detect(frame);

            for (int index = 0; index < textBlocks.size(); index++) {
                //extract scanned text blocks here
                TextBlock tBlock = textBlocks.valueAt(index);
                String blk = tBlock.getValue() + "\n" + "\n";
                finalString.append(blk);
            }
            if (textBlocks.size() == 0)
                return  "Scan Failed: Found nothing to scan";

        } else {
            return "Could not set up the detector!";
        }
        return finalString.toString();
    }
}
