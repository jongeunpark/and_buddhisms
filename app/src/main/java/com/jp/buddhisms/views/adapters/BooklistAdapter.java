package com.jp.buddhisms.views.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jp.buddhisms.R;
import com.jp.buddhisms.data.BookData;

import java.util.ArrayList;

public class BooklistAdapter extends RecyclerView.Adapter<BooklistAdapter.ViewHolder> {
    private int mRandom;
    private Context mContext;
    private OnItemClickListener mItemClickListener;

    private ArrayList<BookData> mBookDatas;


    public BooklistAdapter(Context context, ArrayList<BookData> bookDatas) {

        this.mContext = context;
        this.mBookDatas = bookDatas;

    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_booklist, parent, false);

        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        BookData item = mBookDatas.get(position);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemClickListener.onItemClick(position);
            }
        });
        holder.mTextView.setText(item.getBookName());
        switch (item.getType()) {
            case 0:
                holder.mImageHan.setImageResource(R.drawable.non_check);
                holder.mImageMoon.setImageResource(R.drawable.non_check);
                holder.mImageDesc.setImageResource(R.drawable.non_check);
                break;
            case 1:
                holder.mImageHan.setImageResource(R.drawable.check);
                holder.mImageMoon.setImageResource(R.drawable.non_check);
                holder.mImageDesc.setImageResource(R.drawable.non_check);
                break;
            case 2:
                holder.mImageHan.setImageResource(R.drawable.non_check);
                holder.mImageMoon.setImageResource(R.drawable.check);
                holder.mImageDesc.setImageResource(R.drawable.non_check);
                break;
            case 3:
                holder.mImageHan.setImageResource(R.drawable.check);
                holder.mImageMoon.setImageResource(R.drawable.check);
                holder.mImageDesc.setImageResource(R.drawable.non_check);
                break;
            case 4:
                holder.mImageHan.setImageResource(R.drawable.non_check);
                holder.mImageMoon.setImageResource(R.drawable.non_check);
                holder.mImageDesc.setImageResource(R.drawable.check);
                break;
            case 5:
                holder.mImageHan.setImageResource(R.drawable.check);
                holder.mImageMoon.setImageResource(R.drawable.non_check);
                holder.mImageDesc.setImageResource(R.drawable.check);
                break;
            case 6:
                holder.mImageHan.setImageResource(R.drawable.non_check);
                holder.mImageMoon.setImageResource(R.drawable.check);
                holder.mImageDesc.setImageResource(R.drawable.check);
                break;
            case 7:
                holder.mImageHan.setImageResource(R.drawable.check);
                holder.mImageMoon.setImageResource(R.drawable.check);
                holder.mImageDesc.setImageResource(R.drawable.check);
                break;
        }


    }

    @Override
    public int getItemCount() {
        return mBookDatas.size();
    }


    public interface OnItemClickListener {
        public void onItemClick(int position);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        public View mView;
        public ImageView mImageHan, mImageMoon, mImageDesc;

        public ViewHolder(View v) {
            super(v);
            mView = v.findViewById(R.id.row_listview_card_view);
            mTextView = (TextView) v.findViewById(R.id.row_listview_text_title);
            mImageHan = (ImageView) v.findViewById(R.id.row_listview_image_han);
            mImageMoon = (ImageView) v.findViewById(R.id.row_listview_image_moon);
            mImageDesc = (ImageView) v.findViewById(R.id.row_listview_image_desc);
        }
    }
}
