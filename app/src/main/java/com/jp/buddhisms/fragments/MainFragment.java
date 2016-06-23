package com.jp.buddhisms.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.jp.buddhisms.R;
import com.jp.buddhisms.activities.BookListActivity;
import com.jp.buddhisms.activities.ViewerActivity;
import com.jp.buddhisms.data.BookData;
import com.jp.buddhisms.utils.animations.ActivityAnimator;
import com.jp.buddhisms.views.adapters.BooklistAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

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




	private List<BookData> mBookData;
	private ListView mListView;
	private BooklistAdapter mAdapter;
	private String[] mNames;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_main, container,
				false);
		initData();
		initView(rootView);
		initEvent();
		setData();
		return rootView;

	}

	private void initData() {
		mBookData = new LinkedList<BookData>();
	}

	private void initView(View rootView) {
		mListView = (ListView) rootView.findViewById(R.id.main_list_book);
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
		BookData item = mBookData.get(position);
		
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

		List<HashMap<String, String>> listinfo = new ArrayList<HashMap<String, String>>();
		listinfo.clear();
		for (int i = 0; i < mNames.length; i++) {
			HashMap<String, String> hm = new HashMap<String, String>();
			hm.put("name", mNames[i]);
			if (mNames[i].equals("반야심경")) {
				mBookData.add(new BookData(mNames[i], 7, R.raw.ban_ko,
						R.raw.ban, R.raw.ban_de));
			} else if (mNames[i].equals("화엄경 약찬게")) {
				mBookData.add(new BookData(mNames[i], 3, R.raw.yackchun_ko,
						R.raw.yackchun));

			} else if (mNames[i].equals("천수경")) {
				mBookData.add(new BookData(mNames[i], 3, R.raw.chun_ko,
						R.raw.chun));

			}  else if (mNames[i].equals("법성게")) {
				mBookData.add(new BookData(mNames[i], 7, R.raw.bubsung_ko,
						R.raw.bubsung, R.raw.bubsung_de));

			} else if (mNames[i].equals("신묘장구대다라니")) {
				mBookData.add(new BookData(mNames[i], 1, R.raw.sinmyo_ko));

			} else if (mNames[i].equals("금강경")) {

				mBookData.add(new BookData(mNames[i], 3));

			} else if (mNames[i].equals("아미타경")) {
				mBookData.add(new BookData(mNames[i], 3, R.raw.ami_ko,
						R.raw.ami));

			} else if (mNames[i].equals("천지팔양신주경")) {

				mBookData.add(new BookData(mNames[i], 3, R.raw.chunji_ko,
						R.raw.chunji));
			} else if (mNames[i].equals("대불정능엄신주")) {
				mBookData.add(new BookData(mNames[i], 1, R.raw.debulneungum_ko));

			} else if (mNames[i].equals("백팔대참회문")) {

				mBookData.add(new BookData(mNames[i], 4, R.raw.backpal_de,
						R.raw.backpal_de, R.raw.backpal_de));
			} else if (mNames[i].equals("지장경")) {

				mBookData.add(new BookData(mNames[i], 4, R.raw.jijang_de,
						R.raw.jijang_de, R.raw.jijang_de));
			} else if (mNames[i].equals("천수천안관세음보살광대원만무애대비심대다라니경")) {

				mBookData.add(new BookData(mNames[i], 4,
						R.raw.chunsoochunan_de, R.raw.chunsoochunan_de,
						R.raw.chunsoochunan_de));
			} else if (mNames[i].equals("능엄경")) {

				mBookData.add(new BookData(mNames[i], 4,
						R.raw.chunsoochunan_de, R.raw.chunsoochunan_de,
						R.raw.chunsoochunan_de));
			}

			listinfo.add(hm);
		}

		String[] from = { "name" };
		int[] to = { R.id.row_listview_text_title };
		mAdapter = new BooklistAdapter(getActivity().getBaseContext(),
				listinfo, R.layout.row_booklist, from, to, mBookData);
		mListView.setAdapter(mAdapter);
	}

}