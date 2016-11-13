package com.jp.buddhisms.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.jp.buddhisms.MYJPAPP;
import com.jp.buddhisms.MYJPAPP.TrackerName;
import com.jp.buddhisms.R;
import com.jp.buddhisms.utils.animations.ActivityAnimator;

public class InfomationActivity extends BaseActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        startTracking();
        setContentView(R.layout.activity_infomation);

        initActionbar();
        initView();
        initEvent();

    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
    }

    private void startTracking() {
        Tracker t = ((MYJPAPP) getApplication())
                .getTracker(TrackerName.APP_TRACKER);
        t.setScreenName("불교의식");

        t.send(new HitBuilders.AppViewBuilder().build());
    }

    private void initActionbar() {




        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    private void initView() {


        setTitle("불교의식");

    }

    private void initEvent() {



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            activityFinish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        activityFinish();

    }

    private void activityFinish() {

        finish();
        ActivityAnimator.PullLeftPushRight(this);

    }



}
