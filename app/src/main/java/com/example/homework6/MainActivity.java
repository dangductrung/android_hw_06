package com.example.homework6;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ProgressBar prgBarRun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prgBarRun = findViewById(R.id.prgBarRunning);

        final Button btnStart= findViewById(R.id.btnStart);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView txtSpeed= (TextView)findViewById(R.id.txtSpeed);
                String speedString=txtSpeed.getText().toString();

                if(!speedString.matches("")){
                    btnStart.setEnabled(false);

                    final int speed=Integer.parseInt(speedString);

                    Thread thread = new Thread() {
                        @Override
                        public void run() {
                            prgBarRun.setProgress(0);

                            for(int i=0;i<100;i++){
                                prgBarRun.setProgress(i);
                                try {
                                    if(speed<1000){
                                        Thread.sleep(1000-speed);
                                    }
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    btnStart.setEnabled(true);
                                }
                            });
                        }
                    };
                    thread.start();
                }

            }
        });
    }
}
