package com.example.shaf.ntuhackathon2018.Storage;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;


import java.util.List;
import java.util.concurrent.ExecutionException;

public class ExtractedTextRepository {

    private ExtractedTextDao extractedTextDao;
    private LiveData<List<ExtractedText>> allData;

    public ExtractedTextRepository(Application application) {
        HackathonDatabase db = HackathonDatabase.getDatabase(application);
        extractedTextDao = db.extractedTextDao();
        allData = extractedTextDao.getAllData();
    }

    public LiveData<List<ExtractedText>> getAllAccounts() {
        return allData;
    }

    public void insert(ExtractedText extractedText) {
        new insertAsyncTask(extractedTextDao).execute(extractedText);
    }

    public void delete(ExtractedText extractedText) {
        new deleteAsyncTask(extractedTextDao).execute(extractedText);
    }

    public void update(ExtractedText extractedText) {
        new updateAsyncTask(extractedTextDao).execute(extractedText);
    }

    public String getExtractedText(String filepath) {
        try {
            return new ExtractedTextRepository.getExtractedTextAsyncTask(extractedTextDao).execute(filepath).get();

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }




    private static class getExtractedTextAsyncTask extends AsyncTask<String, Void, String> {

        private ExtractedTextDao mAsyncTaskDao;
        getExtractedTextAsyncTask(ExtractedTextDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected String doInBackground(final String... params) {
            String filepath = params[0];
            String extractedText = mAsyncTaskDao.getExtractedText(filepath);
            return extractedText;
        }
    }


    private static class insertAsyncTask extends AsyncTask<ExtractedText, Void, Void> {

        private ExtractedTextDao mAsyncTaskDao;
        insertAsyncTask(ExtractedTextDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final ExtractedText... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<ExtractedText, Void, Void> {

        private ExtractedTextDao mAsyncTaskDao;
        deleteAsyncTask(ExtractedTextDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final ExtractedText... params) {
            mAsyncTaskDao.delete(params[0]);
            return null;
        }
    }

    private static class updateAsyncTask extends AsyncTask<ExtractedText, Void, Void> {

        private ExtractedTextDao mAsyncTaskDao;
        updateAsyncTask(ExtractedTextDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final ExtractedText... params) {
            String filepath = params[0].getFilepath();
            String extractedText = params[0].getExtractedText();
            mAsyncTaskDao.updateExtractedText(filepath, extractedText);
            return null;
        }
    }
}
