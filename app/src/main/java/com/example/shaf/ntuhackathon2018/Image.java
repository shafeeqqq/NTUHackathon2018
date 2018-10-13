package com.example.shaf.ntuhackathon2018;

import android.graphics.Bitmap;

/**
 * An image conists of a {@link Bitmap} and a filename.
 */
public class Image {
    private Bitmap bitmap;
    private String source;


    public Image(String source, Bitmap bitmap) {
        this.source = source;
        this.bitmap = bitmap;
    }

    public Bitmap getImage() {
        return bitmap;
    }

    public String getSource() {
        return source;
    }
}


