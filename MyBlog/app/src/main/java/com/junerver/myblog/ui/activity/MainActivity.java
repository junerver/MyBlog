package com.junerver.myblog.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.view.View;

import com.github.jdsjlzx.interfaces.OnItemClickLitener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.util.RecyclerViewStateUtils;
import com.github.jdsjlzx.view.LoadingFooter;
import com.junerver.myblog.R;
import com.junerver.myblog.adapter.ArticleRecyclerAdapter;
import com.junerver.myblog.db.ArticleUtils;
import com.junerver.myblog.db.entity.ArticleEntity;
import com.junerver.myblog.service.ArticleService;
import com.junerver.myblog.service.LoadDataCompleteEvent;
import com.junerver.myblog.utils.NetUtils;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    private LRecyclerView mRvList;
    private LRecyclerViewAdapter mLRecyclerViewAdapter;
    private ArticleRecyclerAdapter mDataAdapter;
    private LinearLayoutManager mLayoutManager;
    private List<ArticleEntity> mArticleEntities = new ArrayList<>();

    private boolean isStart=false;
    private boolean isRefresh=false;

    /**服务器端一共多少条数据*/
    private static int TOTAL_COUNTER ;

    /**每一页展示多少条数据*/
    private static final int REQUEST_COUNT = 10;

    /**已经获取到多少条数据了*/
    private static int mCurrentCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //注册事件总线
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //注销
        EventBus.getDefault().unregister(this);
    }

    //事件总线
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCompleteEvent(LoadDataCompleteEvent event) {
        //下拉刷新
        if (isRefresh) {
            mRvList.refreshComplete();
            //重新计数
            mCurrentCounter = 0;
        }
        TOTAL_COUNTER = (int) ArticleUtils.count();
        mArticleEntities.clear();
        if (TOTAL_COUNTER <= REQUEST_COUNT) {
            for (int i = 0; i < TOTAL_COUNTER; i++) {
                mArticleEntities.add(ArticleUtils.list().get(i));
            }
        } else {
            for (int i = 0; i < 10; i++) {
                mArticleEntities.add(ArticleUtils.list().get(i));
            }
        }
        mCurrentCounter = mArticleEntities.size();
        mDataAdapter.setDataList(mArticleEntities);
        //启动后第一次获取数据
        if (isStart) {
            closeProgress();
            isStart = false;
        }
    }



    @Override
    protected void initView() {
        //设置配适器
        mDataAdapter = new ArticleRecyclerAdapter(this);
        mDataAdapter.setDataList(mArticleEntities);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(this, mDataAdapter);

        mRvList = (LRecyclerView) findViewById(R.id.rvList);
        mRvList.setAdapter(mLRecyclerViewAdapter);
        //设置固定大小
        mRvList.setHasFixedSize(true);
        //创建线性布局
        mLayoutManager = new LinearLayoutManager(this);
        //垂直方向
        mLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        //给RecyclerView设置布局管理器
        mRvList.setLayoutManager(mLayoutManager);
    }

    @Override
    protected void initData() {
        if (NetUtils.isConnected(this)) {
            //存在网络连接时启动服务
            isStart = true;
            showProgress();
            ArticleService.startMe(this);
        } else {
            showShortToast("您还没有连接网络！");
        }
    }

    @Override
    protected void setListeners() {
        mRvList.setLScrollListener(new LRecyclerView.LScrollListener() {
            @Override
            public void onRefresh() {
                //下拉刷新
                isRefresh = true;
                if (NetUtils.isConnected(mContext)) {
                    //存在网络连接时启动服务
                    ArticleService.startMe(mContext);
                }
            }

            @Override
            public void onScrollUp() {}

            @Override
            public void onScrollDown() {}

            @Override
            public void onBottom() {
                LoadingFooter.State state = RecyclerViewStateUtils.getFooterViewState(mRvList);
                if(state == LoadingFooter.State.Loading) {
                    Logger.d( "the state is Loading, just wait..");
                    return;
                }
                //当前加载小于总数量则加载更多
                if (mCurrentCounter < TOTAL_COUNTER) {
                    // loading more
                    RecyclerViewStateUtils.setFooterViewState(MainActivity.this, mRvList, REQUEST_COUNT, LoadingFooter.State.Loading, null);
                    if (mCurrentCounter + 10 < TOTAL_COUNTER) {
                        for (int i=0;i<10;i++) {
                            mArticleEntities.add(ArticleUtils.list().get(mCurrentCounter + i));
                        }
                    } else {
                        for (int i=0;i<TOTAL_COUNTER-mCurrentCounter;i++) {
                            mArticleEntities.add(ArticleUtils.list().get(mCurrentCounter + i));
                        }
                    }
                    mDataAdapter.setDataList(mArticleEntities);
                    mCurrentCounter = mArticleEntities.size();
                    RecyclerViewStateUtils.setFooterViewState(mRvList, LoadingFooter.State.Normal);
                } else {
                    //the end
                    RecyclerViewStateUtils.setFooterViewState(MainActivity.this, mRvList, REQUEST_COUNT, LoadingFooter.State.TheEnd, null);
                }
            }

            @Override
            public void onScrolled(int i, int i1) {}
        });

        mLRecyclerViewAdapter.setOnItemClickLitener(new OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                //传送实体到下个activity
                ArticleEntity articleEntity = mDataAdapter.getDataList().get(position);
                Intent articleIntent = new Intent(mContext, ArticleActivity.class);
                articleIntent.putExtra("article", articleEntity);
                startActivity(articleIntent);
            }

            @Override
            public void onItemLongClick(View view, int position) {}
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }


}
