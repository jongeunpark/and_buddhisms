package com.jp.buddhisms.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
import com.rankey.android.acm.launcher.Launcher;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    private final int REQUESTCODE_ACM_USAGETERMS = 2580;

    private ActionBarDrawerToggle mDrawerToggle;


    private MainFragment mainFragment;
    private DrawerLayout mDrawerLayout;



    @SuppressLint({"NewApi", "ResourceAsColor"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics.Builder().core(new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build()).build());
        startTracking();
        setContentView(R.layout.activity_main);

        /*******************************************************************************
         * ACM 적용 – 옵션 설정 – 필수 지정 권장 (담당자와 협의된 옵션값 지정)
         *******************************************************************************/
        // 수집자동동의 설정 (기본값 false)
        Launcher.getInstance().setOptionAutoAgreeUsageTerms(this, false);
        // 수집동의 화면 랜덤 출력 (기본값 0 (렌덤 비활성화))
        //Launcher.getInstance().setOptionUsageTermsActivityRandomShowDays(getApplicationContext(), 0);
        // 수집동의 화면 색상 지정 (기본 색상 권장)
        Launcher.getInstance().setOptionUsageTermsActivityBaseColor(this, Color.rgb(85, 108, 222));
        // 수집동의 취소(거절)시 다음 재동의 화면 출력 인터벌 (단위 ms, 기본값 0ms)
        // 수집동의 재입력이 없는 경우 Long.MAX_VALUE 값을 입력한다
        Launcher.getInstance().setOptionUsageTermsActivityUsageTermsRetryCheckInterval(this, 0);

        // 수집 필수 권한 요청 (기본값 false)
        Launcher.getInstance().setOptionUsageTermsActivityUseRequestPermission(getApplicationContext(), true);

        // 프로파일설문 사용여부
        Launcher.getInstance().setOptionUsageTermsActivityUsePanelProfile(this, true);
        // 프로파일설문 취소(거절)시 다음 재입력 화면 출력 인터벌 (단위 ms, 기본값 0ms)
        // 프로파일설문 재입력이 없는 경우 Long.MAX_VALUE 값을 입력한다
        Launcher.getInstance().setOptionUsageTermsActivityPanelProfileRetryCheckInterval(this, 0);

        // 앱사용 통계 권한설정 사용여부 (기본값 fasle, 롤리팝 이상 단말기에 적용)
        Launcher.getInstance().setOptionUsageTermsActivityUsePkgUsageStats(this, true);
        // 앱사용 통계 권한설정 시나리오 선택 (기본값 Launcher.USAGESTATS_PLAN_TYPE_1)
        Launcher.getInstance().setOptionPkgUsageStatsAccessSettingsPlan(this, Launcher.USAGESTATS_PLAN_TYPE_1);

        // DataSave 예외 설정 요청(기본값 false)
        //Launcher.getInstance().setOptionUsageTermsActivityUseRequestDataSaveWhiteList(getApplicationContext(), true);
        /******************************************************************************/

        /*******************************************************************************
         * ACM 적용 – 서비스 시작 - 필수
         *******************************************************************************/
        //서비스를 시작합니다
        Launcher.getInstance().startService(this);
        /******************************************************************************/

        /************************************************************************************
         * ACM 적용 – 수집 동의(수집 동의 / 프로파일 설문 / 앱 사용통계 권한 설정) 화면 시작 - 필수
         *************************************************************************************/
        initData();

        initView();
        initFragment();
        initEvent();
        mDrawerLayout.setVisibility(View.GONE);
        if(Launcher.getInstance().checkNeedUsageTerms (this)){
            Launcher.getInstance().startUsageTerms(this, REQUESTCODE_ACM_USAGETERMS);
        }else{
            mDrawerLayout.setVisibility(View.VISIBLE);
        }





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
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
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
        }else if(id == R.id.main_nav_term){
            openTerm();
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            /*********************************************************************************
             * ACM  적용 – 수집동의 화면 결과 처리
             **********************************************************************************/
            case REQUESTCODE_ACM_USAGETERMS:

                if (resultCode == RESULT_OK) {
                    mDrawerLayout.setVisibility(View.VISIBLE);

                } else if (resultCode == RESULT_CANCELED) {
                    mDrawerLayout.setVisibility(View.VISIBLE);
                }
                break;
            /********************************************************************************/
        }
    }

    private void openTerm(){
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse("http://mf.rankey.com/app/fullagree.php"));
        try {
            startActivity(i);
        } catch (Exception e) {

        }
    }

}
