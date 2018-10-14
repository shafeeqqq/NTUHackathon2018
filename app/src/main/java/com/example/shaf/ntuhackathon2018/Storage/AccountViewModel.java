package com.example.shaf.ntuhackathon2018.Storage;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;


import java.util.List;

public class AccountViewModel extends AndroidViewModel {

    private ExtractedTextRepository extractedTextRepository;

    private LiveData<List<ExtractedText>> mAllData;

    public AccountViewModel(@NonNull Application application) {
        super(application);

        extractedTextRepository = new ExtractedTextRepository(application);
        mAllData = extractedTextRepository.getAllAccounts();
    }

    public LiveData<List<ExtractedText>> getAllData() {
        return mAllData;
    }

    public void insert(ExtractedText extractedText) {
        extractedTextRepository.insert(extractedText);
    }

    public void delete(ExtractedText extractedText) {
        extractedTextRepository.delete(extractedText);
    }

    public void update(ExtractedText extractedText) {
        extractedTextRepository.update(extractedText);
    }

    public String getExtractedText(String filepath) {
        return extractedTextRepository.getExtractedText(filepath);
    }

}
