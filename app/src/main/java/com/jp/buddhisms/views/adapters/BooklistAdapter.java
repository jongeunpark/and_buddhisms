package com.jp.buddhisms.views.adapters;

import java.util.HashMap;
import java.util.List;

import com.jp.buddhisms.R;
import com.jp.buddhisms.data.BookData;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class BooklistAdapter extends SimpleAdapter {
	private int mRandom;
	private List<HashMap<String, String>> mItems;
	private List<BookData> mBookData;


	public BooklistAdapter(Context context,
			List<HashMap<String, String>> items, int resource, String[] from,
			int[] to, List<BookData> data) {

		super(context, items, resource, from, to);

		mRandom = 0;
		mBookData = data;
		mItems = items;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = super.getView(position, convertView, parent);





		BookData item = mBookData.get(position);
	//	switch (item.getType()) {
//		case 0:
//			option1.setVisibility(View.GONE);
//			option2.setVisibility(View.GONE);
//			option3.setVisibility(View.GONE);
//			break;
//		case 1:
//			option1.setVisibility(View.VISIBLE);
//			option2.setVisibility(View.GONE);
//			option3.setVisibility(View.GONE);
//			break;
//		case 2:
//			option1.setVisibility(View.GONE);
//			option2.setVisibility(View.VISIBLE);
//			option3.setVisibility(View.GONE);
//			break;
//		case 3:
//			option1.setVisibility(View.VISIBLE);
//			option2.setVisibility(View.VISIBLE);
//			option3.setVisibility(View.GONE);
//			break;
//		case 4:
//			option1.setVisibility(View.GONE);
//			option2.setVisibility(View.GONE);
//			option3.setVisibility(View.VISIBLE);
//			break;
//		case 5:
//			option1.setVisibility(View.VISIBLE);
//			option2.setVisibility(View.GONE);
//			option3.setVisibility(View.VISIBLE);
//			break;
//		case 6:
//			option1.setVisibility(View.GONE);
//			option2.setVisibility(View.VISIBLE);
//			option3.setVisibility(View.VISIBLE);
//			break;
//		case 7:
//			option1.setVisibility(View.VISIBLE);
//			option2.setVisibility(View.VISIBLE);
//			option3.setVisibility(View.VISIBLE);
//			break;
//		}
		
		return view;
	}
}
