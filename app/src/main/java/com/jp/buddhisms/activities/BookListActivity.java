package com.jp.buddhisms.activities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.jp.buddhisms.MYJPAPP;
import com.jp.buddhisms.MYJPAPP.TrackerName;
import com.jp.buddhisms.fragments.MainFragment;
import com.jp.buddhisms.R;
import com.jp.buddhisms.data.BookData;
import com.jp.buddhisms.utils.animations.ActivityAnimator;
import com.jp.buddhisms.views.adapters.BooklistAdapter;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class BookListActivity extends BaseActivity implements
		ObservableScrollViewCallbacks {
	private ObservableListView mListView;
	private String mTitle;
	private String[] mNames;
	private BooklistAdapter mAdapter;
	private List<BookData> mBookData;
	private int mBookType;
	private int mBooklist;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		startTracking();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_booklist);
		initData();
		initActionbar();
		initView();
		initEvent();
		setData();

	}

	private void startTracking() {
		Tracker t = ((MYJPAPP) getApplication())
				.getTracker(TrackerName.APP_TRACKER);
		t.setScreenName("리스트화면");

		t.send(new HitBuilders.AppViewBuilder().build());
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

	private void initData() {
		mBookData = new LinkedList<BookData>();
		mBookType = getIntent().getIntExtra(MainFragment.BOOKTYPE, 0);
		mBooklist = getIntent().getIntExtra(MainFragment.BOOKLIST, 0);
		mTitle = getIntent().getStringExtra(MainFragment.TITLE);

	}

	private void initActionbar() {

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
		toolbar.setTitle(getIntent().getStringExtra(MainFragment.TITLE));
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowTitleEnabled(true);





	}

	private void initView() {

		mListView = (ObservableListView) findViewById(R.id.booklist_list_book);
		mListView.setScrollViewCallbacks(this);
	}

	private void initEvent() {

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				openBook(position);
			}
		});
	}

	private void openBook(int position) {
		boolean bottomUp = true;
		Intent intent = new Intent();

		intent.putExtra(MainFragment.TITLE, mNames[position]);

		if (mTitle.equals("금강경")) {

			try {
				JSONObject jsonObject = new JSONObject(
						readRawTextFile(R.raw.geumgang_detail));
				JSONObject jsonBook = jsonObject.getJSONObject("texts"
						+ (position + 1));
				intent.setClass(BookListActivity.this, ViewerActivity.class);
				intent.putExtra(MainFragment.BOOKTYPE, mBookType);
				intent.putExtra(MainFragment.BOOK, jsonBook.getString("text2"));
				intent.putExtra(MainFragment.BOOK_KO,
						jsonBook.getString("text"));
				intent.putExtra(MainFragment.ISTEXT, true);
				startActivity(intent);
				ActivityAnimator.fadeAnimation(this);

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if (mTitle.equals("지장경")) {
			JSONObject jsonObject;
			try {
				jsonObject = new JSONObject(readRawTextFile(R.raw.jijang_de));
				String jsonBook = jsonObject.getString("text" + (position + 1));

				intent.setClass(BookListActivity.this, ViewerActivity.class);
				intent.putExtra(MainFragment.BOOKTYPE, mBookType);

				intent.putExtra(MainFragment.BOOK_DE, jsonBook);
				intent.putExtra(MainFragment.ISTEXT, true);
				startActivity(intent);
				ActivityAnimator.fadeAnimation(this);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if (mTitle.equals("능엄경")) {
			JSONObject jsonObject;
			try {
				jsonObject = new JSONObject(readRawTextFile(R.raw.neungum_de));
				String jsonBook = jsonObject.getString("text" + (position + 1));

				intent.setClass(BookListActivity.this, ViewerActivity.class);
				intent.putExtra(MainFragment.BOOKTYPE, mBookType);

				intent.putExtra(MainFragment.BOOK_DE, jsonBook);
				intent.putExtra(MainFragment.ISTEXT, true);
				startActivity(intent);
				ActivityAnimator.fadeAnimation(this);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	private void setData() {

		mNames = getResources().getStringArray(mBooklist);

		List<HashMap<String, String>> listinfo = new ArrayList<HashMap<String, String>>();
		listinfo.clear();
		for (int i = 0; i < mNames.length; i++) {
			HashMap<String, String> hm = new HashMap<String, String>();
			hm.put("name", mNames[i]);
			mBookData.add(new BookData(mNames[i], getIntent().getIntExtra(
					MainFragment.BOOKTYPE, 0)));
			listinfo.add(hm);
		}

		String[] from = { "name" };
		int[] to = { R.id.row_listview_text_title };
		mAdapter = new BooklistAdapter(getBaseContext(), listinfo,
				R.layout.row_booklist, from, to, mBookData);
		mListView.setAdapter(mAdapter);

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
		// TODO Auto-generated method stub

	}

}
