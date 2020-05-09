package com.adida.hw06;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Handler myHandler = new Handler();

    Button btnDoItAgain;
    TextView txtInfo;
    ProgressBar myBar;
    EditText edtNumTask;

    int totalTask = 1000;
    int numTaskDone = 0;
    int taskStep = 100;
    boolean isDone = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtInfo = findViewById(R.id.txtInfo);
        myBar = findViewById(R.id.myBar);
        edtNumTask = findViewById(R.id.edtNumTask);

        btnDoItAgain = (Button)findViewById(R.id.btnDoItAgain);
        btnDoItAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStart();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();


        txtInfo.setText("0%");
        btnDoItAgain.setEnabled(false);
        try{
            totalTask = Integer.parseInt(edtNumTask.getText().toString());
        }
        catch (Exception ex){}
        // reset and show progress bars
        //accum = 0;
        myBar.setMax(100);
        myBar.setProgress(0);
        myBar.setVisibility(View.VISIBLE);
        // create-start background thread were the busy work will be done
        Thread myBackgroundThread = new Thread( backgroundTask, "backAlias1");
        isDone = false;
        myBackgroundThread.start();

    }

    private Runnable backgroundTask = new Runnable() {
        @Override
        public void run() { // busy work goes here...
            try {
                while(!isDone) {
                    Thread.sleep(10);
//                    doTask();
                    numTaskDone+=taskStep;
                    myHandler.post(foregroundRunnable);
                }
            }
            catch (InterruptedException e) {
                Log.e("<<foregroundTask>>", e.getMessage());
            }
            Log.i("QT", "background end");
        }// run
    };// backgroundTask

    public void doTask(){
        numTaskDone+= taskStep;
    }

    private Runnable foregroundRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                Log.e("QT", "total:" + totalTask);
                Log.e("QT", "" + numTaskDone);
                int percent = (int)((float)100*numTaskDone/(float)totalTask);
                Log.e("QT", "" + percent + "%");
                txtInfo.setText(""+percent+"%");

                //myBarHorizontal.incrementProgressBy(progressStep); accum += progressStep;
                try{
                    myBar.setProgress(percent);
                }
                catch (Exception ex){}
                if (numTaskDone >= totalTask) {
                    isDone = true;
                    txtInfo.setText("done hehehe");
                    myBar.setProgress(100);
//                    myBar.setVisibility(View.INVISIBLE);
                    btnDoItAgain.setEnabled(true);
                }
            }
            catch (Exception e) {
                Log.e("<<foregroundTask>>", e.getMessage());
            }
        }
    }; // foregroundTask

}
