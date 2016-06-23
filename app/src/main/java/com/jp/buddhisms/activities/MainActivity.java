package com.jp.buddhisms.activities;

import com.afollestad.materialdialogs.MaterialDialog;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.jp.buddhisms.fragments.MainFragment;
import com.jp.buddhisms.R;
import com.jp.buddhisms.MYJPAPP;
import com.jp.buddhisms.MYJPAPP.TrackerName;
import com.jp.buddhisms.utils.animations.ActivityAnimator;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends BaseActivity implements
        View.OnClickListener {

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;

    private MainFragment mainFragment;
    private Toolbar mToolbar;

    private TextView mTitle, mSubTitle;

    @SuppressLint({"NewApi", "ResourceAsColor"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startTracking();
        setContentView(R.layout.activity_main);
        initData();
        initActionbar();
        initView();
        initEvent();

        initFragment(savedInstanceState);

    }

    private void startTracking() {
        Tracker t = ((MYJPAPP) getApplication())
                .getTracker(TrackerName.APP_TRACKER);
        t.setScreenName("메인화면");

        t.send(new HitBuilders.AppViewBuilder().build());
    }

    private void initFragment(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            mainFragment = new MainFragment();

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.main_view, mainFragment).commit();
        }
    }

    private void initData() {

    }

    private void initView() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setStatusBarBackgroundColor(getResources().getColor(
                R.color.primary_dark));
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                mToolbar, R.string.app_name, R.string.app_name);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mTitle = (TextView) findViewById(R.id.navi_text_title);
        mSubTitle = (TextView) findViewById(R.id.navi_text_subtitle);

        try {
            mTitle.setText(getString(getApplicationInfo().labelRes)
                    + " "
                    + getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
            mSubTitle.setText(getString(R.string.powered));
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void initEvent() {
        findViewById(R.id.navi_btn_info).setOnClickListener(this);
        findViewById(R.id.navi_btn_life).setOnClickListener(this);
        findViewById(R.id.navi_btn_review).setOnClickListener(this);
        findViewById(R.id.navi_btn_share).setOnClickListener(this);
        findViewById(R.id.navi_btn_send).setOnClickListener(this);
        findViewById(R.id.navi_btn_bitcoin).setOnClickListener(this);
        findViewById(R.id.navi_btn_download).setOnClickListener(this);


    }

    private void initActionbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);

    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.START | Gravity.LEFT)) {
            mDrawerLayout.closeDrawers();
            return;
        }
        super.onBackPressed();
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

    public boolean onCreateOptionsMenu(Menu menu) {

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openInfo() {
        startActivity(new Intent(MainActivity.this, InfomationActivity.class));
        ActivityAnimator.fadeAnimation(this);
    }

    private void openLife() {
        startActivity(new Intent(MainActivity.this, LifeActivity.class));
        ActivityAnimator.fadeAnimation(this);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
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
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        sendIntent.setType("text/plain");
        startActivity(sendIntent);

    }

    private void openMarket() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        try {
            intent.setData(Uri
                    .parse("market://details?id="
                            + getPackageManager().getPackageInfo(
                            getPackageName(), 0).packageName));
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        startActivity(intent);

    }

    private void openMarketToJP() {
        Intent intent = new Intent(Intent.ACTION_VIEW);

        intent.setData(Uri.parse("market://search?q=pub:Korean+JP"));

        startActivity(intent);
    }

    private void openAddress() {
        final String btcAddress = "1N11nqjLtYmpbYMetvegBKEjgkzNvJc8Vp";
        LayoutInflater inflater = getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.dialog_qrcode, null);

        TextView addressText = (TextView) dialoglayout
                .findViewById(R.id.qrcode_text_address);

        ImageView addressImage = (ImageView) dialoglayout
                .findViewById(R.id.qrcode_image_address);

        addressText.setText(btcAddress);
        addressImage.setImageResource(R.drawable.mybitcoin);
        boolean wrapInScrollView = true;

        MaterialDialog.Builder dialogBuilder = new MaterialDialog.Builder(this);
        dialogBuilder.title(getString(R.string.donation));

        dialogBuilder.customView(dialoglayout, wrapInScrollView);
        dialogBuilder.positiveText(getString(R.string.donation_copy));


        dialogBuilder.negativeText(getString(R.string.donation_execute));
        dialogBuilder.callback(new MaterialDialog.ButtonCallback() {
            @Override
            public void onPositive(MaterialDialog dialog) {
                int currentapiVersion = android.os.Build.VERSION.SDK_INT;

                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("JP", btcAddress);
                clipboard.setPrimaryClip(clip);

                Toast.makeText(MainActivity.this,
                        getString(R.string.donation_copies), Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onNegative(MaterialDialog dialog) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_DEFAULT);

                intent.setData(Uri.parse("bitcoin:" + btcAddress
                        + "?amount=0.01"));

                try {
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "비트코인 지갑을 설치하세요.", Toast.LENGTH_SHORT).show();
                }
            }

        });

        dialogBuilder.show();

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

        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        intent.putExtra(Intent.EXTRA_EMAIL,
                new String[]{"jp.jongeun.park@gmail.com.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "");
        intent.putExtra(Intent.EXTRA_TEXT, body);
        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent, "Send Email"));
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (mDrawerLayout.isDrawerOpen(Gravity.START | Gravity.LEFT)) {
            mDrawerLayout.closeDrawers();
        }
        Tracker t = ((MYJPAPP) getApplication())
                .getTracker(TrackerName.APP_TRACKER);

        switch (v.getId()) {
            case R.id.navi_btn_info:
                openInfo();
                break;
            case R.id.navi_btn_life:
                openLife();
                break;
            case R.id.navi_btn_share:
                t.send(new HitBuilders.EventBuilder().setCategory("네비게이션")
                        .setAction("버튼 선택").setLabel("공유").build());
                openShareApp();
                break;
            case R.id.navi_btn_review:
                t.send(new HitBuilders.EventBuilder().setCategory("네비게이션")
                        .setAction("버튼 선택").setLabel("마켓").build());
                openMarket();
                break;
            case R.id.navi_btn_send:
                t.send(new HitBuilders.EventBuilder().setCategory("네비게이션")
                        .setAction("버튼 선택").setLabel("메일").build());
                openEmailApp();
                break;
            case R.id.navi_btn_download:
                t.send(new HitBuilders.EventBuilder().setCategory("네비게이션")
                        .setAction("버튼 선택").setLabel("다운로드").build());
                openMarketToJP();
                break;
            case R.id.navi_btn_bitcoin:
                t.send(new HitBuilders.EventBuilder().setCategory("네비게이션")
                        .setAction("버튼 선택").setLabel("기부").build());
                openAddress();
                break;

        }
    }

}
