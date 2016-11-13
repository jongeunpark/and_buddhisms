package com.jp.buddhisms.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.jp.buddhisms.MYJPAPP;
import com.jp.buddhisms.MYJPAPP.TrackerName;
import com.jp.buddhisms.R;
import com.jp.buddhisms.data.BookData;
import com.jp.buddhisms.fragments.MainFragment;
import com.jp.buddhisms.utils.animations.ActivityAnimator;
import com.jp.buddhisms.views.adapters.BooklistAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class BookListActivity extends BaseActivity {

	public static final String BOOK_POSITION = "BOOK_POSITION";
	private int RESULT_VIEWERACTIVITY = 101;


	private String mTitle;
	private String[] mNames;
	private BooklistAdapter mAdapter;
	private ArrayList<BookData> mBookDatas;
	private int mBookType;
	private int mBooklist;
	private RecyclerView.LayoutManager mLayoutManager;

	private RecyclerView mRecyclerView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		startTracking();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_booklist);
		initData();
		initActionbar();
		initView();

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
		mBookDatas = new ArrayList<BookData>();
		mBookType = getIntent().getIntExtra(MainFragment.BOOKTYPE, 0);
		mBooklist = getIntent().getIntExtra(MainFragment.BOOKLIST, 0);
		mTitle = getIntent().getStringExtra(MainFragment.TITLE);

	}

	private void initActionbar() {



		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(getIntent().getStringExtra(MainFragment.TITLE));




	}

	private void initView() {
		mRecyclerView =  (RecyclerView) findViewById(R.id.booklist_recycler_book);
		mRecyclerView.setHasFixedSize(true);

		// use a linear layout manager
		mLayoutManager = new LinearLayoutManager(this);
		mRecyclerView.setLayoutManager(mLayoutManager);

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
				intent.putExtra(MainFragment.IS_SCROLL, true);
				intent.putExtra(MainFragment.CURRENT_PAGE, position);
				intent.putExtra(MainFragment.MAX_PAGE, mNames.length);
				startActivityForResult(intent,RESULT_VIEWERACTIVITY);
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
				intent.putExtra(MainFragment.IS_SCROLL, true);
				intent.putExtra(MainFragment.CURRENT_PAGE, position);
				intent.putExtra(MainFragment.MAX_PAGE, mNames.length);
				startActivityForResult(intent,RESULT_VIEWERACTIVITY);
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
				intent.putExtra(MainFragment.IS_SCROLL, true);
				intent.putExtra(MainFragment.CURRENT_PAGE, position);
				intent.putExtra(MainFragment.MAX_PAGE, mNames.length);
				startActivityForResult(intent,RESULT_VIEWERACTIVITY);
				ActivityAnimator.fadeAnimation(this);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	private void setData() {

		mNames = getResources().getStringArray(mBooklist);


		for (int i = 0; i < mNames.length; i++) {

			mBookDatas.add(new BookData(mNames[i], getIntent().getIntExtra(
					MainFragment.BOOKTYPE, 0)));

		}


		mAdapter = new BooklistAdapter(this, mBookDatas);
		mRecyclerView.setAdapter(mAdapter);
		mAdapter.setOnItemClickListener(new BooklistAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(int position) {
				openBook(position);
			}
		});

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
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == RESULT_VIEWERACTIVITY) {
			if(resultCode == RESULT_OK){
				if(intent!=null ){
					int postion = intent.getIntExtra(BOOK_POSITION,0);
					openBook(postion);
				}
			}
		}
	}



}
