package com.assignment;

import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private final String TAG = MainActivity.class.toString();
    private EditText mUserDataEdT;
    private TextView mFileDataTv;
    private Button mSavaBtn, mDeleteBtn;
    private File file;
    private final String FILE_NAME = "Assignment16.txt";
    private String userData, fileData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // initialise layout
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: ");
        // init all views
        inItViews();
        // create file with 'Assignment16.txt' name
        file = new File(Environment.getExternalStorageDirectory(), FILE_NAME);
        // set onclick listener on save Button
        mSavaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userData = mUserDataEdT.getText().toString(); // get data from EditText
                mUserDataEdT.setText("");
                FileManager rf = new FileManager(file);
                rf.execute(userData); // start Async Task
            }
        });

        // set onclick listener on Delete Button
        mDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (file.exists()) {   // Check if file is exist or not
                    if (file.delete()) {
                        Toast.makeText(MainActivity.this, "File Deleted", Toast.LENGTH_SHORT).show();
                        mFileDataTv.setText("");
                    }
                } else {
                    Toast.makeText(MainActivity.this, "File not Found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // this method will init all views
    private void inItViews() {
        mUserDataEdT = (EditText) findViewById(R.id.edit_text_user_data);
        mFileDataTv = (TextView) findViewById(R.id.text_view_file_data);
        mSavaBtn = (Button) findViewById(R.id.button_save);
        mDeleteBtn = (Button) findViewById(R.id.button_delete);
    }

    private class FileManager extends AsyncTask <String, Integer, String> {
        private final String TAG = FileManager.class.toString();
        File userfile;

        public FileManager(File userfile) {
            super();
            this.userfile = userfile;
        }

        //write all data enterd by user to file
        @Override
        protected String doInBackground(String... str) {
            Log.d(TAG, "doInBackground: ");
            FileWriter writer = null;
            try {
                writer = new FileWriter(userfile, true);
                writer.append(str[0].toString());
                writer.append("\n");
                writer.flush();
            } catch (IOException e) {
                Log.e(TAG, "doInBackground: " + e.getMessage());
            } finally {
                try {
                    writer.close();
                } catch (IOException e) {
                    Log.e(TAG, "doInBackground: " + e.getMessage());
                }
            }
            return null;
        }

        //read saved data from file
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            StringBuilder fileTextData = new StringBuilder();
            FileReader fileReader = null;

            try {
                fileReader = new FileReader(userfile);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                while ((fileData = bufferedReader.readLine()) != null) {
                    fileTextData.append(fileData);
                    fileTextData.append("\n");

                }
                Log.d(TAG, "onPostExecute: file data" + fileTextData.toString());
                bufferedReader.close();
                fileReader.close();
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            mFileDataTv.setText(fileTextData.toString());
        }
    }
}
