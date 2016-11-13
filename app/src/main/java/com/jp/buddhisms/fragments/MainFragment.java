package com.jp.buddhisms.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jp.buddhisms.R;
import com.jp.buddhisms.activities.BookListActivity;
import com.jp.buddhisms.activities.ViewerActivity;
import com.jp.buddhisms.data.BookData;
import com.jp.buddhisms.utils.animations.ActivityAnimator;
import com.jp.buddhisms.views.adapters.BooklistAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class MainFragment extends Fragment {


	public static final String TITLE = "TITLE";
	public static final String BOOK = "BOOK";
	public static final String BOOK_KO = "BOOK_KO";
	public static final String BOOK_DE = "BOOK_DE";
	public static final String ISTEXT = "ISTEXT";
	public static final String BOOKLIST = "BOOKLIST";
	public static final String BOOKTYPE = "BOOKTYPE";
	public static final String IS_SCROLL = "IS_SCROLL";
	public static final String CURRENT_PAGE = "CURRENT_PAGE";
	public static final String MAX_PAGE = "MAX_PAGE";




	private ArrayList<BookData> mBookDatas;
	private RecyclerView.LayoutManager mLayoutManager;

	private RecyclerView mRecyclerView;
	private BooklistAdapter mAdapter;
	private String[] mNames;

	public static MainFragment newInstance() {
		return new MainFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_main, container,
				false);
		initData();
		initView(rootView);

		setData();
		return rootView;

	}

	private void initData() {
		mBookDatas = new ArrayList<BookData>();
	}

	private void initView(View rootView) {
		mRecyclerView =  (RecyclerView) rootView.findViewById(R.id.main_recycler_book);
		mRecyclerView.setHasFixedSize(true);

		// use a linear layout manager
		mLayoutManager = new LinearLayoutManager(getActivity());
		mRecyclerView.setLayoutManager(mLayoutManager);
	}


	private void openBook(int position) {
		boolean bottomUp = true;
		BookData item = mBookDatas.get(position);
		
		Intent intent = new Intent();

		intent.putExtra(MainFragment.TITLE, item.getBookName());
		intent.putExtra(BOOKTYPE, item.getType());
		intent.putExtra(BOOK, item.getMoonFile());
		intent.putExtra(BOOK_KO, item.getKoFile());
		intent.putExtra(BOOK_DE, item.getDeFile());

		if (mNames[position].equals("금강경")) {
			bottomUp = false;
			intent.setClass(getActivity(), BookListActivity.class);
			intent.putExtra(BOOKLIST, R.array.geumgang);

		} else if (mNames[position].equals("지장경")) {
			bottomUp = false;
			intent.setClass(getActivity(), BookListActivity.class);
			intent.putExtra(BOOKLIST, R.array.jijang);

		} else if (mNames[position].equals("능엄경")) {
			bottomUp = false;
			intent.setClass(getActivity(), BookListActivity.class);
			intent.putExtra(BOOKLIST, R.array.neungum);

		} else {
			intent.setClass(getActivity(), ViewerActivity.class);
		}

		startActivity(intent);
		ActivityAnimator.fadeAnimation(getActivity());


	}

	private void setData() {
		mNames = getResources().getStringArray(R.array.list_book);


		for (int i = 0; i < mNames.length; i++) {
			HashMap<String, String> hm = new HashMap<String, String>();
			hm.put("name", mNames[i]);
			if (mNames[i].equals("반야심경")) {
				mBookDatas.add(new BookData(mNames[i], 7, R.raw.ban_ko,
						R.raw.ban, R.raw.ban_de));
			} else if (mNames[i].equals("화엄경 약찬게")) {
				mBookDatas.add(new BookData(mNames[i], 3, R.raw.yackchun_ko,
						R.raw.yackchun));

			} else if (mNames[i].equals("천수경")) {
				mBookDatas.add(new BookData(mNames[i], 3, R.raw.chun_ko,
						R.raw.chun));

			}  else if (mNames[i].equals("법성게")) {
				mBookDatas.add(new BookData(mNames[i], 7, R.raw.bubsung_ko,
						R.raw.bubsung, R.raw.bubsung_de));

			} else if (mNames[i].equals("신묘장구대다라니")) {
				mBookDatas.add(new BookData(mNames[i], 1, R.raw.sinmyo_ko));

			} else if (mNames[i].equals("금강경")) {

				mBookDatas.add(new BookData(mNames[i], 3));

			} else if (mNames[i].equals("아미타경")) {
				mBookDatas.add(new BookData(mNames[i], 3, R.raw.ami_ko,
						R.raw.ami));

			} else if (mNames[i].equals("천지팔양신주경")) {

				mBookDatas.add(new BookData(mNames[i], 3, R.raw.chunji_ko,
						R.raw.chunji));
			} else if (mNames[i].equals("대불정능엄신주")) {
				mBookDatas.add(new BookData(mNames[i], 1, R.raw.debulneungum_ko));

			} else if (mNames[i].equals("백팔대참회문")) {

				mBookDatas.add(new BookData(mNames[i], 4, R.raw.backpal_de,
						R.raw.backpal_de, R.raw.backpal_de));
			} else if (mNames[i].equals("지장경")) {

				mBookDatas.add(new BookData(mNames[i], 4, R.raw.jijang_de,
						R.raw.jijang_de, R.raw.jijang_de));
			} else if (mNames[i].equals("천수천안관세음보살광대원만무애대비심대다라니경")) {

				mBookDatas.add(new BookData(mNames[i], 4,
						R.raw.chunsoochunan_de, R.raw.chunsoochunan_de,
						R.raw.chunsoochunan_de));
			} else if (mNames[i].equals("능엄경")) {

				mBookDatas.add(new BookData(mNames[i], 4,
						R.raw.chunsoochunan_de, R.raw.chunsoochunan_de,
						R.raw.chunsoochunan_de));
			}


		}


		mAdapter = new BooklistAdapter(getActivity(),
				mBookDatas);
		mRecyclerView.setAdapter(mAdapter);
		mAdapter.setOnItemClickListener(new BooklistAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(int position) {
				openBook(position);
			}
		});
	}

}