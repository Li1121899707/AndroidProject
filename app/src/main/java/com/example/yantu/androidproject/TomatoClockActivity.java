package com.example.yantu.androidproject;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.yantu.androidproject.Util.timer.Timer;
import com.example.yantu.androidproject.Util.timer.TimerCallback;

public class TomatoClockActivity extends AppCompatActivity {
    static final private String TIME_FORMAT = "%02d:%02d";
    static final private String TOMATO_STAGE_FORMAT = "番茄周期 %d/%d (当前番茄/总番茄数)";
    static final private String BREAK_STAGE_FORMAT = "休息周期 %d/%d (已完成番茄/总番茄数)";
    static final private String FINISH_HIT = "所有番茄周期已完成，请退出～";
    static final private String INTERRUPT_HIT = "当前周期被打断，请退出～";

    final private Timer tomatoTimer = new Timer(0, 25, 0);
    final private Timer breakTimer = new Timer(0, 5, 0);

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
                updateTime(0, 0);
                changeState();
            }
        });

        breakTimer.registerPostCallback(new TimerCallback() {
            @Override
            public void run(long hours, long minutes, long seconds) {
                updateTime(0, 0);
                changeState();
            }
        });

        tomatoTimer.registerInterruptCallback(new TimerCallback() {
            @Override
            public void run(long hours, long minutes, long seconds) {
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
                breakTimer.stop(); // ensure the break timer is stopped
                tomatoTimer.start();
            }
        };

        tomatoStopListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tomatoTimer.stop()) {
                    prepareResults();
                    finish();
                }
            }
        };

        breakStartListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startBtn.setEnabled(false);
                tomatoTimer.stop(); // ensure the tomato timer is stopped
                breakTimer.start();
            }
        };

        breakStopListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!breakTimer.stop()) {
                    prepareResults();
                    finish();
                }
            }
        };

        exitListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prepareResults();
                finish();
            }
        };
    }

    private void updateTime(long minutes, long seconds) {
        long progress = 100 * (totalTime - (minutes * 60 + seconds)) / totalTime;
        progressBar.setProgress((int) progress);
        timeRemainingTv.setText(String.format(TIME_FORMAT, minutes, seconds));
    }

    private void changeIntoTomato() {
        stages++;

        startBtn.setEnabled(true);

        startBtn.setOnClickListener(tomatoStartListener);
        stopBtn.setOnClickListener(tomatoStopListener);

        totalTime = 25 * 3600;
        timeRemainingTv.setText(String.format(TIME_FORMAT, 25, 0));

        stageTv.setText(String.format(TOMATO_STAGE_FORMAT, stages, totalStage));
    }

    private void changeIntoBreak() {
        startBtn.setEnabled(true);

        startBtn.setOnClickListener(breakStartListener);
        stopBtn.setOnClickListener(breakStopListener);

        totalTime = 5 * 3600;
        timeRemainingTv.setText(String.format(TIME_FORMAT, 5, 0));

        stageTv.setText(String.format(BREAK_STAGE_FORMAT, stages, totalStage));
    }

    private void prepareResults() {
        Bundle ret = new Bundle();
        ret.putString("task", taskName);
        ret.putInt("finished_stages", stages);

        Intent intent = new Intent();
        intent.putExtras(ret);
        setResult(MainActivity.ACTIVITY_ID, intent);
    }

    private void changeIntoEnd() {
        stageTv.setText(FINISH_HIT);

        updateTime(0, 0);

        stopBtn.setOnClickListener(exitListener);
    }

    // MUST BE called by the interrupt event
    private void changeIntoInterrupt() {
        state = 2;

        stageTv.setText(INTERRUPT_HIT);

        stopBtn.setOnClickListener(exitListener);
    }

    private void changeState() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (state == 1) {
                    state = 0;
                    changeIntoTomato();
                } else if (state == 0) {
                    if (stages >= totalStage) {
                        state = 2;
                        changeIntoEnd();
                    } else {
                        state = 1;
                        changeIntoBreak();
                    }
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tomato_clock);

        init();
        changeState();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK)
            return true;
        return super.onKeyDown(keyCode, event);
    }
}
