package com.example.shaf.ntuhackathon2018.Storage;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;


import java.util.List;

@Dao
public interface ExtractedTextDao {

    @Insert
    void insert(ExtractedText extractedText);

    @Query("SELECT * from extractedtext_table ORDER BY filepath ASC")
    LiveData<List<ExtractedText>> getAllData();

    @Delete
    void delete(ExtractedText extractedText);

    @Update
    void update(ExtractedText extractedText);

    @Query("DELETE FROM extractedText_table")
    void deleteAll();

    @Query("UPDATE extractedText_table SET extractedText=:extractedText WHERE filepath=:filepath")
    void updateExtractedText(String filepath, String extractedText);

    @Query("SELECT extractedText from extractedText_table WHERE filepath=:filepath")
    String getExtractedText(String filepath);


}
