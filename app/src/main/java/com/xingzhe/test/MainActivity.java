package com.xingzhe.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    DrawProgressCircleView mDrawProgressCircleView;
    TextView mStart, mRestart, mEnd, mCountinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDrawProgressCircleView = findViewById(R.id.drawProgressCircleView);
        mStart = findViewById(R.id.start);
        mRestart = findViewById(R.id.restart);
        mEnd = findViewById(R.id.stop);
        mCountinue = findViewById(R.id.countinue);
        mStart.setOnClickListener(this);
        mRestart.setOnClickListener(this);
        mEnd.setOnClickListener(this);
        mCountinue.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start:
                mDrawProgressCircleView.start();
                break;
            case R.id.restart:
                mDrawProgressCircleView.restart();
                break;
            case R.id.stop:
                mDrawProgressCircleView.stop();
                break;
            case R.id.countinue:
                mDrawProgressCircleView.progressContinue();
                break;

        }
    }
}
