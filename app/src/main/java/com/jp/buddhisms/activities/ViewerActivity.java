package com.jp.buddhisms.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.jp.buddhisms.MYJPAPP;
import com.jp.buddhisms.MYJPAPP.TrackerName;
import com.jp.buddhisms.R;
import com.jp.buddhisms.fragments.MainFragment;
import com.jp.buddhisms.utils.animations.ActivityAnimator;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;

public class ViewerActivity extends BaseActivity {

    private final String ISHANGLE = "ISHANGLE";
    private final String TEXT_SIZE = "TEXT_SIZE";

    private TextView mContents;
    private View mBtnPrev, mBtnNext;
    private View mViewBottom;


    private boolean isScroll = false;
    private int mCurrentPage = 0;
    private int mMaxPage = 0;
    private float mTextSize = 16;
    private int mBookType = 0;
    private FloatingActionMenu mFab;
    private FloatingActionButton mMenuSizeUp, mMenuSizeDown, mMenuShowHan, mMenuShowMonn, mMenuShowDesc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        startTracking();
        setContentView(R.layout.activity_viewer);
        mBookType = getIntent().getIntExtra(MainFragment.BOOKTYPE, 0);
        isScroll = getIntent().getBooleanExtra(MainFragment.IS_SCROLL, false);
        mCurrentPage = getIntent().getIntExtra(MainFragment.CURRENT_PAGE, 0);
        mMaxPage = getIntent().getIntExtra(MainFragment.MAX_PAGE, 0);

        initActionbar(getIntent().getStringExtra(MainFragment.TITLE));
        initView();
        initEvent();
        setData();

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
        t.setScreenName("불경화면");

        t.send(new HitBuilders.AppViewBuilder().build());
    }


    private void initActionbar(String title) {


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(title);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            activityFinish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {

        mContents = (TextView) findViewById(R.id.viewer_text_contents);
        mViewBottom = findViewById(R.id.viewer_view_bottom);
        mBtnPrev =  findViewById(R.id.viewer_btn_prev);
        mBtnNext = findViewById(R.id.viewer_btn_next);
        mFab = (FloatingActionMenu) findViewById(R.id.viewer_fab);
        mMenuSizeUp = (FloatingActionButton) findViewById(R.id.fab_menu_size_up);
        mMenuSizeDown = (FloatingActionButton) findViewById(R.id.fab_menu_size_down);
        mMenuShowHan = (FloatingActionButton) findViewById(R.id.fab_menu_show_han);
        mMenuShowMonn = (FloatingActionButton) findViewById(R.id.fab_menu_show_moon);
        mMenuShowDesc = (FloatingActionButton) findViewById(R.id.fab_menu_show_desc);

        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        mTextSize = prefs.getFloat(TEXT_SIZE, 16F);
        mContents.setTextSize(mTextSize);
        if (isScroll) {
           // mViewBottom.setVisibility(View.VISIBLE);
            mBtnPrev.setVisibility(View.VISIBLE);
            mBtnNext.setVisibility(View.VISIBLE);
        } else {
           // mViewBottom.setVisibility(View.GONE);
            mBtnPrev.setVisibility(View.GONE);
            mBtnNext.setVisibility(View.GONE);

        }

        if (mCurrentPage == 0) {
            mBtnPrev.setVisibility(View.GONE);
        }
        if (mCurrentPage == (mMaxPage - 1)) {
            mBtnNext.setVisibility(View.GONE);
        }


    }


    private void textSizeUp() {
        if (mTextSize < 30) {
            mTextSize += 2;
        } else {
            Toast.makeText(ViewerActivity.this,
                    "이미 충분히 글자가 큽니다.", Toast.LENGTH_SHORT)
                    .show();
        }
        mContents.setTextSize(mTextSize);
        saveTextSize();
    }

    private void textSizeDown() {
        if (mTextSize > 10) {
            mTextSize -= 2;
        } else {
            Toast.makeText(ViewerActivity.this,
                    "더 이상 글자크기를 줄이면, 글이 보이지 않아요.",
                    Toast.LENGTH_SHORT).show();
        }

        mContents.setTextSize(mTextSize);
        saveTextSize();
    }

    private void showKoreanText() {
        if (getIntent().getBooleanExtra(
                MainFragment.ISTEXT, false)) {
            mContents.setText(getIntent().getStringExtra(
                    MainFragment.BOOK_KO));

        } else {
            mContents.setText(readRawTextFile(getIntent()
                    .getIntExtra(MainFragment.BOOK_KO, 0)));
        }


        initActionbar(
                getIntent().getStringExtra(
                        MainFragment.TITLE)
                        + " - 한글");
    }

    private void showHanMoonText() {
        if (getIntent().getBooleanExtra(
                MainFragment.ISTEXT, false)) {
            mContents.setText(getIntent().getStringExtra(
                    MainFragment.BOOK));

        } else {
            mContents.setText(readRawTextFile(getIntent()
                    .getIntExtra(MainFragment.BOOK, 0)));
        }


        initActionbar(
                getIntent().getStringExtra(
                        MainFragment.TITLE)
                        + " - 한문");
    }

    private void showDescText() {
        if (getIntent().getBooleanExtra(
                MainFragment.ISTEXT, false)) {
            mContents.setText(getIntent().getStringExtra(
                    MainFragment.BOOK_DE));

        } else {
            mContents.setText(readRawTextFile(getIntent()
                    .getIntExtra(MainFragment.BOOK_DE, 0)));
        }

        initActionbar(
                getIntent().getStringExtra(
                        MainFragment.TITLE)
                        + " - 풀이");
    }


    private void initEvent() {
        mBtnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(BookListActivity.BOOK_POSITION, (mCurrentPage - 1));
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        mBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(BookListActivity.BOOK_POSITION, (mCurrentPage + 1));
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        mMenuShowHan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showKoreanText();
            }
        });

        mMenuShowMonn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showHanMoonText();
            }
        });
        mMenuShowDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDescText();
            }
        });
        mMenuSizeUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textSizeUp();
            }
        });
        mMenuSizeDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textSizeDown();
            }
        });
        mContents.setMovementMethod(new ScrollingMovementMethod());
    }

    private void setData() {
        switch (mBookType) {
            case 1:
                if (getIntent().getBooleanExtra(MainFragment.ISTEXT, false)) {
                    mContents.setText(getIntent().getStringExtra(
                            MainFragment.BOOK_KO));
                    initActionbar(
                            getIntent().getStringExtra(MainFragment.TITLE)
                                    + " - 한글");
                } else {
                    mContents.setText(readRawTextFile(getIntent().getIntExtra(
                            MainFragment.BOOK_KO, 0)));
                    initActionbar(
                            getIntent().getStringExtra(MainFragment.TITLE)
                                    + " - 한글");
                }
                mFab.removeMenuButton(mMenuShowMonn);
                mFab.removeMenuButton(mMenuShowDesc);

                break;
            case 2:
                if (getIntent().getBooleanExtra(MainFragment.ISTEXT, false)) {
                    mContents
                            .setText(getIntent().getStringExtra(MainFragment.BOOK));
                    initActionbar(
                            getIntent().getStringExtra(MainFragment.TITLE)
                                    + " - 한문");
                } else {
                    mContents.setText(readRawTextFile(getIntent().getIntExtra(
                            MainFragment.BOOK, 0)));
                    initActionbar(
                            getIntent().getStringExtra(MainFragment.TITLE)
                                    + " - 한문");
                }
                mFab.removeMenuButton(mMenuShowHan);
                mFab.removeMenuButton(mMenuShowDesc);

                break;
            case 3:
                if (getIntent().getBooleanExtra(MainFragment.ISTEXT, false)) {
                    mContents.setText(getIntent().getStringExtra(
                            MainFragment.BOOK_KO));
                    initActionbar(
                            getIntent().getStringExtra(MainFragment.TITLE)
                                    + " - 한글");
                } else {
                    mContents.setText(readRawTextFile(getIntent().getIntExtra(
                            MainFragment.BOOK_KO, 0)));
                    initActionbar(
                            getIntent().getStringExtra(MainFragment.TITLE)
                                    + " - 한글");
                }

                mFab.removeMenuButton(mMenuShowDesc);

                break;
            case 4:
                if (getIntent().getBooleanExtra(MainFragment.ISTEXT, false)) {
                    mContents.setText(getIntent().getStringExtra(
                            MainFragment.BOOK_DE));
                    initActionbar(
                            getIntent().getStringExtra(MainFragment.TITLE)
                                    + " - 풀이");
                } else {
                    mContents.setText(readRawTextFile(getIntent().getIntExtra(
                            MainFragment.BOOK_DE, 0)));
                    initActionbar(
                            getIntent().getStringExtra(MainFragment.TITLE)
                                    + " - 풀이");
                }
                mFab.removeMenuButton(mMenuShowHan);
                mFab.removeMenuButton(mMenuShowMonn);


                break;
            case 5:
                if (getIntent().getBooleanExtra(MainFragment.ISTEXT, false)) {
                    mContents.setText(getIntent().getStringExtra(
                            MainFragment.BOOK_KO));
                    initActionbar(
                            getIntent().getStringExtra(MainFragment.TITLE)
                                    + " - 한글");
                } else {
                    mContents.setText(readRawTextFile(getIntent().getIntExtra(
                            MainFragment.BOOK_KO, 0)));
                    initActionbar(
                            getIntent().getStringExtra(MainFragment.TITLE)
                                    + " - 한글");
                }

                mFab.removeMenuButton(mMenuShowMonn);
                break;
            case 6:
                if (getIntent().getBooleanExtra(MainFragment.ISTEXT, false)) {
                    mContents
                            .setText(getIntent().getIntExtra(MainFragment.BOOK, 0));
                    initActionbar(
                            getIntent().getStringExtra(MainFragment.TITLE)
                                    + " - 한문");
                } else {
                    mContents.setText(readRawTextFile(getIntent().getIntExtra(
                            MainFragment.BOOK, 0)));
                    initActionbar(
                            getIntent().getStringExtra(MainFragment.TITLE)
                                    + " - 한문");
                }
                mFab.removeMenuButton(mMenuShowHan);

                break;
            case 7:
                if (getIntent().getBooleanExtra(MainFragment.ISTEXT, false)) {
                    mContents.setText(getIntent().getStringExtra(
                            MainFragment.BOOK_KO));
                    initActionbar(getIntent().getStringExtra(MainFragment.TITLE)
                            + " - 한글");

                } else {
                    mContents.setText(readRawTextFile(getIntent().getIntExtra(
                            MainFragment.BOOK_KO, 0)));
                    initActionbar(
                            getIntent().getStringExtra(MainFragment.TITLE)
                                    + " - 한글");
                }
                break;

        }

    }

    private void saveTextSize() {
        SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
        editor.putFloat(TEXT_SIZE, mTextSize);

        editor.apply();
    }


    @Override
    public void onBackPressed() {
        if (mFab.isOpened()) {
            mFab.close(true);
        } else {
            activityFinish();
        }

    }

    private void activityFinish() {

        finish();
        ActivityAnimator.PullLeftPushRight(this);

    }

    private String readRawTextFile(int resId) {
        if (resId == 0) {
            return null;
        }
        InputStream inputStream = getResources().openRawResource(resId);

        InputStreamReader inputreader = new InputStreamReader(inputStream);
        BufferedReader buffreader = new BufferedReader(inputreader);
        String line;
        StringBuilder text = new StringBuilder();

        try {
            while ((line = buffreader.readLine()) != null) {
                text.append(line);
                text.append('\n');

            }
        } catch (IOException e) {
            return null;
        }

        return text.toString();
    }


}
