package com.example.yantu.androidproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.yantu.androidproject.Entity.Log;
import com.example.yantu.androidproject.Util.timer.Timer;
import com.example.yantu.androidproject.Util.timer.TimerCallback;

public class TomatoClockActivity extends AppCompatActivity {
    static final private String TIME_FORMAT = "%02d:%02d";
    static final private String TOMATO_STAGE_FORMAT = "番茄周期 %d/%d (当前番茄/总番茄数)";
    static final private String BREAK_STAGE_FORMAT = "休息周期 %d/%d (已完成番茄/总番茄数)";
    static final private String FINISH_HIT = "所有番茄周期已完成，请退出～";
    static final private String INTERRUPT_HIT = "当前周期被打断，请退出～";

    final private Timer tomatoTimer = new Timer(0, 0, 5);
    final private Timer breakTimer = new Timer(0, 0, 5);

    private int stages = 0; // start from 1
    private String taskName;
    private int totalStage;
    private int totalTime;

    // tomato states
    // 0 -> tomato
    // 1 -> break
    // 2 -> end state
    private int state = 1;

    // UI Components
    private View.OnClickListener tomatoStopListener, breakStopListener;
    private View.OnClickListener tomatoStartListener, breakStartListener;
    private View.OnClickListener exitListener;
    private TextView timeRemainingTv, taskNameTv, stageTv;
    private ProgressBar progressBar;
    private Button startBtn, stopBtn;

    private void init() {
        Bundle bundle = getIntent().getExtras();
        taskName = bundle.getString("task");
        totalStage = bundle.getInt("stages");

        startBtn = findViewById(R.id.startButton);
        stopBtn = findViewById(R.id.stopButton);
        timeRemainingTv = findViewById(R.id.remainingTextView);
        taskNameTv = findViewById(R.id.taskNameTextView);
        stageTv = findViewById(R.id.stageTextView);
        progressBar = findViewById(R.id.remainingProgressBar);

        taskNameTv.setText(taskName);

        tomatoTimer.registerCallback(new TimerCallback() {
            @Override
            public void run(long hours, long minutes, long seconds) {
                updateTime(minutes, seconds);
            }
        });

        breakTimer.registerCallback(new TimerCallback() {
            @Override
            public void run(long hours, long minutes, long seconds) {
                updateTime(minutes, seconds);
            }
        });

        tomatoTimer.registerPostCallback(new TimerCallback() {
            @Override
            public void run(long hours, long minutes, long seconds) {
                changeState();
            }
        });

        breakTimer.registerPostCallback(new TimerCallback() {
            @Override
            public void run(long hours, long minutes, long seconds) {
                changeState();
            }
        });

        tomatoTimer.registerInterruptCallback(new TimerCallback() {
            @Override
            public void run(long hours, long minutes, long seconds) {
                android.util.Log.i("Tomato", "Called here");
                changeIntoInterrupt();
            }
        });

        breakTimer.registerInterruptCallback(new TimerCallback() {
            @Override
            public void run(long hours, long minutes, long seconds) {
                changeIntoInterrupt();
            }
        });

        tomatoStartListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startBtn.setEnabled(false);
                stopBtn.setEnabled(true);
                breakTimer.stop(); // ensure the break timer is stopped
                tomatoTimer.start();
            }
        };

        tomatoStopListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tomatoTimer.stop();
            }
        };

        breakStartListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startBtn.setEnabled(false);
                stopBtn.setEnabled(true);
                tomatoTimer.stop(); // ensure the tomato timer is stopped
                breakTimer.start();
            }
        };

        breakStopListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                breakTimer.stop();
            }
        };

        exitListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        };
    }

    private void updateTime(long minutes, long seconds) {
        long progress = (totalTime - (minutes * 60 + seconds)) / totalTime;
        progressBar.setProgress((int) progress);
        timeRemainingTv.setText(String.format(TIME_FORMAT, minutes, seconds));
    }

    private synchronized void changeIntoTomato() {
        stages++;

        startBtn.setOnClickListener(tomatoStartListener);
        stopBtn.setOnClickListener(tomatoStopListener);

        totalTime = 25 * 3600;

        updateTime(25, 0);
        stageTv.setText(String.format(TOMATO_STAGE_FORMAT, stages, totalStage));
    }

    private synchronized void changeIntoBreak() {

        startBtn.setOnClickListener(breakStartListener);
        stopBtn.setOnClickListener(breakStopListener);

        totalTime = 5 * 3600;

        updateTime(5, 0);
        stageTv.setText(String.format(BREAK_STAGE_FORMAT, stages, totalStage));
    }

    private synchronized void changeIntoEnd() {
        stageTv.setText(FINISH_HIT);

        Bundle ret = new Bundle();
        ret.putString("task", taskName);
        ret.putInt("finished_stages", stages);

        Intent intent = new Intent();
        intent.putExtras(ret);
        setResult(MainActivity.ACTIVITY_ID, intent);

        stopBtn.setOnClickListener(exitListener);
    }

    // MUST BE called by the interrupt event
    private synchronized void changeIntoInterrupt() {
        android.util.Log.i("Tomato", "Interrupt");
        state = 2;

        stageTv.setText(INTERRUPT_HIT);

        stopBtn.setOnClickListener(exitListener);
    }

    private synchronized void changeState() {
        if (state == 1) {
            state = 0;
            changeIntoTomato();
            startBtn.setEnabled(true);
            stopBtn.setEnabled(false);
        } else if (state == 0) {
            if (stages >= totalStage) {
                state = 2;
                changeIntoEnd();
            } else {
                state = 1;
                changeIntoBreak();

                startBtn.setEnabled(true);
                stopBtn.setEnabled(false);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tomato_clock);

        init();
        changeState();
    }
}
