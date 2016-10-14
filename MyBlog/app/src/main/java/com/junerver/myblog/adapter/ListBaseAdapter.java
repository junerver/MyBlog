package com.junerver.myblog.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.junerver.myblog.db.entity.ArticleEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Junerver on 2016/8/10.
 */
public class ListBaseAdapter extends RecyclerView.Adapter {
    protected Context mContext;
    protected int mScreenWidth;

    public void setScreenWidth(int width) {
        mScreenWidth = width;
    }

    protected ArrayList<ArticleEntity> mDataList = new ArrayList<>();

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public List<ArticleEntity> getDataList() {
        return mDataList;
    }

    public void setDataList(Collection<ArticleEntity> list) {
        this.mDataList.clear();
        this.mDataList.addAll(list);
        notifyDataSetChanged();
    }

    public void addAll(Collection<ArticleEntity> list) {
        int lastIndex = this.mDataList.size();
        if (this.mDataList.addAll(list)) {
            notifyItemRangeInserted(lastIndex, list.size());
        }
    }

    public void clear() {
        mDataList.clear();
        notifyDataSetChanged();
    }
}

