package com.jp.buddhisms.activities;

import io.github.onivas.promotedactions.PromotedActionsLibrary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.jp.buddhisms.MYJPAPP;
import com.jp.buddhisms.MYJPAPP.TrackerName;
import com.jp.buddhisms.fragments.MainFragment;
import com.jp.buddhisms.R;
import com.jp.buddhisms.utils.animations.ActivityAnimator;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ViewerActivity extends BaseActivity{

	private final String ISHANGLE = "ISHANGLE";
	private final String TEXT_SIZE = "TEXT_SIZE";

	private TextView mContents;
	private TextView mBtnPrev, mBtnNext;
	private View mViewBottom;


	private boolean isScroll = false;
	private int mCurrentPage = 0;
	private int mMaxPage = 0;
	private float mTextSize = 16;
	private int mBookType = 0;


	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		startTracking();
		setContentView(R.layout.activity_viewer);
		mBookType = getIntent().getIntExtra(MainFragment.BOOKTYPE, 0);
		isScroll = getIntent().getBooleanExtra(MainFragment.IS_SCROLL, false);
		mCurrentPage  = getIntent().getIntExtra(MainFragment.CURRENT_PAGE, 0);
		mMaxPage  = getIntent().getIntExtra(MainFragment.MAX_PAGE, 0);

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

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
		toolbar.setTitle(title);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowTitleEnabled(true);



	}

	private void initView() {

		mContents = (TextView) findViewById(R.id.viewer_text_contents);
		mViewBottom = findViewById(R.id.viewer_view_bottom);
		mBtnPrev = (TextView) findViewById(R.id.viewer_btn_prev);
		mBtnNext = (TextView) findViewById(R.id.viewer_btn_next);

		SharedPreferences prefs = getPreferences(MODE_PRIVATE);
		mTextSize = prefs.getFloat(TEXT_SIZE, 16F);
		mContents.setTextSize(mTextSize);
		if(isScroll){
			mViewBottom.setVisibility(View.VISIBLE);
		}else{
			mViewBottom.setVisibility(View.GONE);
		}

		if(mCurrentPage == 0){
			mBtnPrev.setVisibility(View.GONE);
		}
		if(mCurrentPage == (mMaxPage-1)){
			mBtnNext.setVisibility(View.GONE);
		}



	}


	private void textSizeUp(){
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
	private void textSizeDown(){
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
	private void showKoreanText(){
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
	private void showHanMoonText(){
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
	private void showDescText(){
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

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.viewer_menu, menu);
		switch (mBookType) {
			case 0:
				menu.findItem(R.id.menu_viewer_korean).setVisible(false);
				menu.findItem(R.id.menu_viewer_hanmoon).setVisible(false);
				menu.findItem(R.id.menu_viewer_desc).setVisible(false);
				break;
			case 1:
				menu.findItem(R.id.menu_viewer_korean).setVisible(true);
				menu.findItem(R.id.menu_viewer_hanmoon).setVisible(false);
				menu.findItem(R.id.menu_viewer_desc).setVisible(false);
				break;

			case 2:


				menu.findItem(R.id.menu_viewer_korean).setVisible(false);
				menu.findItem(R.id.menu_viewer_hanmoon).setVisible(true);
				menu.findItem(R.id.menu_viewer_desc).setVisible(false);
				break;

			case 3:

				menu.findItem(R.id.menu_viewer_korean).setVisible(true);
				menu.findItem(R.id.menu_viewer_hanmoon).setVisible(true);
				menu.findItem(R.id.menu_viewer_desc).setVisible(false);
				break;

			case 4:

				menu.findItem(R.id.menu_viewer_korean).setVisible(false);
				menu.findItem(R.id.menu_viewer_hanmoon).setVisible(false);
				menu.findItem(R.id.menu_viewer_desc).setVisible(true);
				break;

			case 5:
				menu.findItem(R.id.menu_viewer_korean).setVisible(true);
				menu.findItem(R.id.menu_viewer_hanmoon).setVisible(false);
				menu.findItem(R.id.menu_viewer_desc).setVisible(true);

				break;

			case 6:

				menu.findItem(R.id.menu_viewer_korean).setVisible(false);
				menu.findItem(R.id.menu_viewer_hanmoon).setVisible(true);
				menu.findItem(R.id.menu_viewer_desc).setVisible(true);

				break;
			case 7:

				menu.findItem(R.id.menu_viewer_korean).setVisible(true);
				menu.findItem(R.id.menu_viewer_hanmoon).setVisible(true);
				menu.findItem(R.id.menu_viewer_desc).setVisible(true);
				break;

		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			activityFinish();
		}else if (item.getItemId() == R.id.menu_viewer_plus) {
			textSizeUp();

		}else if (item.getItemId() == R.id.menu_viewer_minus) {
			 textSizeDown();

		}else if (item.getItemId() == R.id.menu_viewer_korean) {
			 showKoreanText();

		 }else if (item.getItemId() == R.id.menu_viewer_hanmoon) {
			 showHanMoonText();

		 }else if (item.getItemId() == R.id.menu_viewer_desc) {
			 showDescText();

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
