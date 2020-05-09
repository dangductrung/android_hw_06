package com.adida.hw06;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    EditText editText;
    Button reset;
    ProgressBar progressBar;
    TextView percent;

    int STEP = 1;
    int MAX = 0;
    Handler handler = new Handler();
    boolean isRunning = false;
    int accum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (EditText) findViewById(R.id.editText);
        reset = (Button)findViewById(R.id.btnAgain);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        percent = (TextView) findViewById(R.id.textPercent);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = editText.getText().toString();
                if (editText.getText().toString().equals("")) {
                    Toast toast = Toast. makeText(getApplicationContext(),"Please enter max.",Toast. LENGTH_SHORT);
                    toast.show();
                    return;
                }
                MAX = Integer.parseInt(editText.getText().toString());
                percent.setText("0%");
                reset.setEnabled(false);
                isRunning = true;
                onStart();
            }
        });

        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!isRunning) {
            return;
        }
        progressBar.setMax(MAX);
        progressBar.setProgress(0);
        accum = 0;
        Thread backgroundThread = new Thread(backgroundRunnable, "");
        backgroundThread.start();
    }

    private Runnable foregroundRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                if (accum >= MAX) {
                    percent.setText("100%");
                    reset.setEnabled(true);
                    isRunning = false;
                }

                progressBar.setProgress(accum);
                int per = (int)(((double)accum / (double)MAX) * 100);
                percent.setText(per + "%");
            }catch (Exception e) {
                Log.e("<<foreground>>", e.getMessage());
            }
        }
    };

    private Runnable backgroundRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                for (int i = 0; i < MAX; i++) {
                    accum += STEP;
                    handler.post(foregroundRunnable);
                }
            }catch (Exception e) {
                Log.e("<<foreground>>", e.getMessage());
            }
        }
    };

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
