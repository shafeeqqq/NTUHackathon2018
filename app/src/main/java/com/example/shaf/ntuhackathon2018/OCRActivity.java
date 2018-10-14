package com.example.shaf.ntuhackathon2018;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.ScrollingMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.xeoh.android.texthighlighter.TextHighlighter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class OCRActivity extends AppCompatActivity {


    Intent mIntent;
    private String imagePath;
    ImageView imageView;
    EditText textView;
    TextExtractor textExtractor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ocr_activity);
        
        textExtractor = new TextExtractor(this);
        textView = findViewById(R.id.ocr_text_view);
        textView.setMovementMethod(new ScrollingMovementMethod());
        imageView = findViewById(R.id.ocr_image_view);
        
        mIntent = getIntent();
        imagePath = mIntent.getStringExtra("filepath");
        File file = new File(imagePath);
        Glide.with(this).load(file).into(imageView);

        String extractedText = "No data";
        try {
            extractedText = textExtractor.extractText(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error Displaying Data", Toast.LENGTH_SHORT).show();
        }



//        List<String> wordList = new ArrayList<>();
//
//        String line;
//        String filename = "words_alpha.txt";
//
//        try {
//            FileReader fileReader = new FileReader(filename);
//
//            BufferedReader bufferedReader = new BufferedReader(fileReader);
//
//            int i = 0;
//
//            while((line = bufferedReader.readLine())!=null) {
//                wordList.add(line);
//                i++;
//            }
//            bufferedReader.close();
//        }
//        catch(FileNotFoundException ex) {
//            System.out.println("Unable to open file '" + filename + "'");
//        }
//        catch(IOException ex) {
//            System.out.println("Error reading file '" + filename + "'");
//            String curWord = null;
//            if ((curWord =SpellChecker.spellCheck(extractedText, wordList))!=null) {

//            }
//
//        }

        final String dataString = extractedText;
        Button submitButton = findViewById(R.id.submit_http_request);

        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                JSONObject postData = new JSONObject();
                try {
                    postData.put("data", dataString);
                    Log.e("JSON", postData.toString());
                    new SendJSONData().execute("http://10.27.186.218:5000/newapp", postData.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        Spannable spannable = new SpannableString(extractedText);
        ForegroundColorSpan fgSpan = new ForegroundColorSpan(Color.RED);
// setting blue background color
        BackgroundColorSpan bgSpan = new BackgroundColorSpan(Color.BLUE);


        spannable.setSpan(CharacterStyle.wrap(fgSpan), 0, 5, 0);
        spannable.setSpan(CharacterStyle.wrap(bgSpan), 0, 5, 0);
        textView.setText(spannable);
    }



    private static class SendJSONData extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String data = "";

            HttpURLConnection httpURLConnection = null;
            try {

                httpURLConnection = (HttpURLConnection) new URL(params[0]).openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setReadTimeout(10000);
                httpURLConnection.setConnectTimeout(15000);
                httpURLConnection.setDoOutput(true);

                httpURLConnection.setRequestProperty("Content-Type","application/json;charset=utf-8");
                httpURLConnection.setRequestProperty("Accept","application/json");


                OutputStreamWriter wr = new OutputStreamWriter(httpURLConnection.getOutputStream());
                wr.write(params[1]);
                wr.flush();
                wr.close();

                Log.e("POST", params[1]);
                Log.e("POST", httpURLConnection.getContentEncoding() + httpURLConnection.getContent().toString());


                InputStream in = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(in);

                int inputStreamData = inputStreamReader.read();
                while (inputStreamData != -1) {
                    char current = (char) inputStreamData;
                    inputStreamData = inputStreamReader.read();
                    data += current;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.e("TAG", result); // this is expecting a response code to be sent from your server upon receiving the POST data
        }
    }
    
  
}
