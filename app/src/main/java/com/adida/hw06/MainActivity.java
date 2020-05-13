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

    public static String MYTAG = "QT";

    Handler myHandler = new Handler();

    Button btnDoItAgain;
    TextView txtInfo;
    ProgressBar myBar;
    EditText edtNumTask;

    int totalTask = 100;
    int numTaskDone = 0;
    int taskStep = 1;
    boolean isDone = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtInfo = findViewById(R.id.txtInfo);
        myBar = findViewById(R.id.myBar);
        edtNumTask = findViewById(R.id.edtNumTask);

        btnDoItAgain = (Button) findViewById(R.id.btnDoItAgain);
        btnDoItAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSlowTask();
            }
        });

    }

    private void doSlowTask() {

        Log.i(MYTAG, "start do slow task");
        btnDoItAgain.setEnabled(false);
        resetTask();

        try {
            totalTask = Integer.parseInt(edtNumTask.getText().toString());
        } catch (Exception ex) {
            Log.e(MYTAG, "parse error");
        }
        Log.i(MYTAG, "total task: " + totalTask);

        // create-start background thread were the busy work will be done
        Thread myBackgroundThread = new Thread(backgroundTask, "backAlias1");
        myBackgroundThread.start();
    }

    private void resetTask() {
        // reset data
        numTaskDone = 0;
        taskStep = 1;
        totalTask = 100;
        isDone = false;

        // reset ui
        txtInfo.setText("0%");
        myBar.setMax(100);
        myBar.setProgress(0);
        myBar.setVisibility(View.VISIBLE);
    }


    @Override
    protected void onStart() {
        super.onStart();

    }


    private Runnable backgroundTask = new Runnable() {
        @Override
        public void run() { // busy work goes here...
            try {
                while (!isDone) {
                    Thread.sleep(1);
                    updateTask();
                    myHandler.post(foregroundRunnable);
                }
            } catch (InterruptedException e) {
                Log.e(MYTAG, e.getMessage());
            }
        }// run

        //    public void doTask(){
        public void updateTask() {
            numTaskDone += taskStep;
        }

    };


    private Runnable foregroundRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                int numDone = numTaskDone;
                int numTotal = totalTask;
                int percent = (int) ((float) 100 * numDone / (float) numTotal);
                txtInfo.setText("" + numDone + "-" + percent + "%");
                Log.e(MYTAG, "" + numDone + "-" + percent + "%");

                if (numDone < numTotal) {
                    try {
                        myBar.setProgress(percent);
                    } catch (Exception ex) {
                    }
                }
                else{
                    isDone = true;
                    txtInfo.setText("done 100%");
                    myBar.setProgress(100);
                    btnDoItAgain.setEnabled(true);
                }
            } catch (Exception e) {
                Log.e(MYTAG, e.getMessage());
            }

        }
    }; // foregroundTask

}
