package com.example.shaf.ntuhackathon2018.Storage;


import android.accounts.Account;
import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "extractedtext_table")
public class ExtractedText {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "filepath")
    private String filepath;

    @ColumnInfo(name = "extractedText")
    private String extractedText;


    public ExtractedText(String filepath, String extractedText) {
        this.filepath = filepath;
        this.extractedText= extractedText;
    }

    @NonNull
    public String getFilepath() {
        return filepath;
    }

    public String getExtractedText() {
        return extractedText;
    }
}
