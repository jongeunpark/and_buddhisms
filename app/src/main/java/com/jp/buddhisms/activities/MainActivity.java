package com.jp.buddhisms.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.jp.buddhisms.BuildConfig;
import com.jp.buddhisms.MYJPAPP;
import com.jp.buddhisms.MYJPAPP.TrackerName;
import com.jp.buddhisms.R;
import com.jp.buddhisms.fragments.MainFragment;
import com.jp.buddhisms.utils.activities.ActivityUtils;
import com.jp.buddhisms.utils.animations.ActivityAnimator;
import io.fabric.sdk.android.Fabric;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ActionBarDrawerToggle mDrawerToggle;


    private MainFragment mainFragment;
    private NavigationView mDrawerLayout;

    private TextView mTitle, mSubTitle;

    @SuppressLint({"NewApi", "ResourceAsColor"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics.Builder().core(new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build()).build());
        startTracking();
        setContentView(R.layout.activity_main);
        initData();

        initView();
        initFragment();
        initEvent();



    }

    private void startTracking() {
        Tracker t = ((MYJPAPP) getApplication())
                .getTracker(TrackerName.APP_TRACKER);
        t.setScreenName("메인화면");

        t.send(new HitBuilders.AppViewBuilder().build());
    }

    private void initFragment() {


        if (mainFragment == null) {
            mainFragment = MainFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mainFragment, R.id.contentFrame);
        }
    }

    private void initData() {

    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        TextView title = (TextView) navigationView.getHeaderView(0).findViewById(R.id.nav_text_title);

        try {
            title.setText(getString(getApplicationInfo().labelRes)
                    + " "
                    + getPackageManager().getPackageInfo(getPackageName(), 0).versionName);

        } catch (PackageManager.NameNotFoundException e) {

            title.setText(getString(getApplicationInfo().labelRes));
        }

    }

    private void initEvent() {


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

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.main_nav_review) {
            openMarket();
        } else if (id == R.id.main_nav_share) {
            openShareApp();
        } else if (id == R.id.main_nav_send) {
            openEmailApp();
        }else if(id == R.id.main_nav_info){
            openInfo();
        }
        else if(id == R.id.main_nav_lift){
            openLife();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void openInfo() {
        startActivity(new Intent(MainActivity.this, InfomationActivity.class));
        ActivityAnimator.fadeAnimation(this);
    }

    private void openLife() {
        startActivity(new Intent(MainActivity.this, LifeActivity.class));
        ActivityAnimator.fadeAnimation(this);
    }



    private void openMarket() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        try {
            intent.setData(Uri
                    .parse("market://details?id="
                            + getPackageManager().getPackageInfo(
                            getPackageName(), 0).packageName));
            startActivity(intent);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void openShareApp() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        try {
            sendIntent.putExtra(
                    Intent.EXTRA_TEXT,
                    "https://play.google.com/store/apps/details?id="
                            + getPackageManager().getPackageInfo(
                            getPackageName(), 0).packageName);
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void openEmailApp() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/html");
        String body = "\n\n\n\n\n--------------------\n아래 내용을 지우거나 수정하지 마세요.\n";
        try {
            body += "\nApp Name : " + getString(getApplicationInfo().labelRes);
            body += "\nApp Version : "
                    + getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            body += "\nOS Version : " + android.os.Build.VERSION.RELEASE;
            body += "\nDevice : " + android.os.Build.MODEL;
            intent.putExtra(Intent.EXTRA_EMAIL,
                    new String[]{"jp.jongeun.park@gmail.com.com"});
            intent.putExtra(Intent.EXTRA_SUBJECT, "");
            intent.putExtra(Intent.EXTRA_TEXT, body);
            intent.setType("message/rfc822");
            startActivity(Intent.createChooser(intent, "Send Email"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

//    @Override
//    public void onClick(View v) {
//        // TODO Auto-generated method stub
//        if (mDrawerLayout.isDrawerOpen(Gravity.START | Gravity.LEFT)) {
//            mDrawerLayout.closeDrawers();
//        }
//        Tracker t = ((MYJPAPP) getApplication())
//                .getTracker(TrackerName.APP_TRACKER);
//
//        switch (v.getId()) {
//            case R.id.navi_btn_info:
//                openInfo();
//                break;
//            case R.id.navi_btn_life:
//                openLife();
//                break;
//            case R.id.navi_btn_share:
//                t.send(new HitBuilders.EventBuilder().setCategory("네비게이션")
//                        .setAction("버튼 선택").setLabel("공유").build());
//                openShareApp();
//                break;
//            case R.id.navi_btn_review:
//                t.send(new HitBuilders.EventBuilder().setCategory("네비게이션")
//                        .setAction("버튼 선택").setLabel("마켓").build());
//                openMarket();
//                break;
//            case R.id.navi_btn_send:
//                t.send(new HitBuilders.EventBuilder().setCategory("네비게이션")
//                        .setAction("버튼 선택").setLabel("메일").build());
//                openEmailApp();
//                break;
//            case R.id.navi_btn_download:
//                t.send(new HitBuilders.EventBuilder().setCategory("네비게이션")
//                        .setAction("버튼 선택").setLabel("다운로드").build());
//                openMarketToJP();
//                break;
//            case R.id.navi_btn_bitcoin:
//                t.send(new HitBuilders.EventBuilder().setCategory("네비게이션")
//                        .setAction("버튼 선택").setLabel("기부").build());
//
//                break;
//
//        }
//    }


}
