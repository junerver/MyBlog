package com.junerver.myblog.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.junerver.myblog.R;
import com.junerver.myblog.db.entity.ArticleEntity;

/**
 * Created by Junerver on 2016/8/5.
 */
public class ArticleRecyclerAdapter extends ListBaseAdapter {


    private LayoutInflater mInflater;

    public ArticleRecyclerAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(mInflater.inflate(R.layout.item_article,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ArticleEntity articleEntity = mDataList.get(position);
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.tvTitle.setText(articleEntity.getPost_title());
        viewHolder.tvPostDate.setText(articleEntity.getPost_date());
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTitle;
        private TextView tvPostDate;


        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvPostDate = (TextView) itemView.findViewById(R.id.tvPostDate);
        }
    }
}
