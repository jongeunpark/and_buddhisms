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
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ViewerActivity extends BaseActivity implements
		ObservableScrollViewCallbacks {

	private final String ISHANGLE = "ISHANGLE";
	private final String TEXT_SIZE = "TEXT_SIZE";

	private TextView mContents;
	private PromotedActionsLibrary mPromotedActionsLibrary;
	private float mTextSize = 16;
	private int mBookType = 0;
	private ObservableScrollView mScrollView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		startTracking();
		setContentView(R.layout.activity_viewer);
		mBookType = getIntent().getIntExtra(MainFragment.BOOKTYPE, 0);
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
		SharedPreferences prefs = getPreferences(MODE_PRIVATE);
		mTextSize = prefs.getFloat(TEXT_SIZE, 16F);
		mContents.setTextSize(mTextSize);
		mScrollView = (ObservableScrollView) findViewById(R.id.viewer_scroll);
		mScrollView.setScrollViewCallbacks(this);
		initMenu();

	}

	private void addPlusButton() {
		mPromotedActionsLibrary.addItem(
				getResources().getDrawable(R.drawable.icon_plus),
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
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

				});
	}

	private void addMinusButton() {
		mPromotedActionsLibrary.addItem(
				getResources().getDrawable(R.drawable.icon_minus),
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
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

				});

	}

	private void addHanButton() {
		mPromotedActionsLibrary.addItem(
				getResources().getDrawable(R.drawable.hangle),
				new View.OnClickListener(){
					@Override
					public void onClick(View v) {
						if (getIntent().getBooleanExtra(
									MainFragment.ISTEXT, false)) {
								mContents.setText(getIntent().getStringExtra(
										MainFragment.BOOK_KO));

							} else {
								mContents.setText(readRawTextFile(getIntent()
										.getIntExtra(MainFragment.BOOK_KO, 0)));
							}

							if (mPromotedActionsLibrary.isMenuOpened()) {
								mPromotedActionsLibrary.closePromotedActions()
										.start();
								mPromotedActionsLibrary.setMenuOpened(false);
							}
							initActionbar(
									getIntent().getStringExtra(
											MainFragment.TITLE)
											+ " - 한글");
					}

				});
	}

	private void addMoonButton() {
		mPromotedActionsLibrary.addItem(
				getResources().getDrawable(R.drawable.icon_hanja),
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						if (getIntent().getBooleanExtra(
								MainFragment.ISTEXT, false)) {
							mContents.setText(getIntent().getStringExtra(
									MainFragment.BOOK));

						} else {
							mContents.setText(readRawTextFile(getIntent()
									.getIntExtra(MainFragment.BOOK, 0)));
						}

						if (mPromotedActionsLibrary.isMenuOpened()) {
							mPromotedActionsLibrary.closePromotedActions()
									.start();
							mPromotedActionsLibrary.setMenuOpened(false);
						}
						initActionbar(
								getIntent().getStringExtra(
										MainFragment.TITLE)
										+ " - 한자");
					}


				});
	}

	private void addDeButton() {
		mPromotedActionsLibrary.addItem(
				getResources().getDrawable(R.drawable.icon_de),
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						if (getIntent().getBooleanExtra(
								MainFragment.ISTEXT, false)) {
							mContents.setText(getIntent().getStringExtra(
									MainFragment.BOOK_DE));

						} else {
							mContents.setText(readRawTextFile(getIntent()
									.getIntExtra(MainFragment.BOOK_DE, 0)));
						}
						if (mPromotedActionsLibrary.isMenuOpened()) {
							mPromotedActionsLibrary.closePromotedActions()
									.start();
							mPromotedActionsLibrary.setMenuOpened(false);
						}
						initActionbar(
								getIntent().getStringExtra(
										MainFragment.TITLE)
										+ " - 풀이");
					}


				});
	}

	private void initMenu() {
		FrameLayout frameLayout = (FrameLayout) findViewById(R.id.viewer_container);

		mPromotedActionsLibrary = new PromotedActionsLibrary();

		mPromotedActionsLibrary.setup(getApplicationContext(), frameLayout);


		addPlusButton();
		addMinusButton();

		switch (mBookType) {
		case 0:
			break;
		case 1:
			addHanButton();
			break;

		case 2:
			addMoonButton();
			break;

		case 3:
			addMoonButton();
			addHanButton();

			break;

		case 4:
			addDeButton();
			break;

		case 5:
			addDeButton();
			addHanButton();
			break;

		case 6:
			addDeButton();
			addMoonButton();

			break;
		case 7:
			addDeButton();
			addMoonButton();
			addHanButton();
			break;

		}

		mPromotedActionsLibrary.addMainItem(getResources().getDrawable(
				R.drawable.menu_icon));
	}

	private void initEvent() {
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
								+ " - 한자");
			} else {
				mContents.setText(readRawTextFile(getIntent().getIntExtra(
						MainFragment.BOOK, 0)));
				initActionbar(
						getIntent().getStringExtra(MainFragment.TITLE)
								+ " - 한자");
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
								+ " - 한자");
			} else {
				mContents.setText(readRawTextFile(getIntent().getIntExtra(
						MainFragment.BOOK, 0)));
				initActionbar(
						getIntent().getStringExtra(MainFragment.TITLE)
								+ " - 한자");
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

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			activityFinish();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		if (mPromotedActionsLibrary.isMenuOpened()) {
			mPromotedActionsLibrary.closePromotedActions().start();
			mPromotedActionsLibrary.setMenuOpened(false);
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

	@Override
	public void onScrollChanged(int scrollY, boolean firstScroll,
			boolean dragging) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDownMotionEvent() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpOrCancelMotionEvent(ScrollState scrollState) {


	}

}
