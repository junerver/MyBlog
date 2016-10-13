package com.junerver.myblog.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.junerver.myblog.AppContext;
import com.junerver.myblog.Constants;
import com.junerver.myblog.db.dao.ArticleEntityDao;
import com.junerver.myblog.db.entity.ArticleEntity;
import com.junerver.myblog.model.DbVersionModel;
import com.orhanobut.logger.Logger;
import com.zhy.http.okhttp.OkHttpUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Response;

/**
 * 这个服务用语从数据库获取最新的数据
 */
public class ArticleService extends Service {

    public ArticleService() {
    }

    //封装启动\关闭方式为服务自身的静态方法
    public static void startMe(Context context) {
        Intent service = new Intent(context, ArticleService.class);
        context.startService(service);
    }

    public static void stopMe(Context context) {
        context.stopService(new Intent(context, ArticleService.class));
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.d("服务创建！");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Logger.d("服务启动！");
        // TODO: 2016/8/6  判断服务器与本地状态，确定是否需要从服务器获取数据
        if (intent == null) {
            return super.onStartCommand(intent, flags, startId);
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!isLocalEqServer()) {
                    Logger.d("开始从服务器检索！！！！！！！！！！！！！！！！！！！！！！！！！" );
                    getArticleToLocal();
                }
                EventBus.getDefault().post(new LoadDataCompleteEvent("加载数据完成"));
                Logger.d("数据下载完毕！发送事件！");
            }
        }).start();


        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 从服务器获取数据存入本地
     */
    private void getArticleToLocal() {
        try {
            String url = Constants.GET_POSTS_AFTER_ID + getLocaldbLastId();
            Response response = OkHttpUtils
                    .get()
                    .url(url)
                    .build()
                    .execute();
            Gson gson = new Gson();
            //利用反射机制获取实体类的类型
            Type lt = new TypeToken<List<ArticleEntity>>() {
            }.getType();
            List<ArticleEntity> articleEntities = gson.fromJson(response.body().string(), lt);
            if (articleEntities.size() != 0) {
                for (ArticleEntity articleEntity : articleEntities) {
                    AppContext.getArticleEntityDao().insert(articleEntity);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // TODO: 2016/8/10 如果本地数据为空，获取全部，否则获取ID之后
    }

    /**
     * 判断本地数据状态是否与服务器相同
     * @return
     */
    private boolean isLocalEqServer() {
        try {
            //执行同步get方法获取服务器的dbVersionModel
            Response response = OkHttpUtils
                    .get()
                    .url(Constants.GET_SERVER_DB_VERSION)
                    .build()
                    .execute();
            Gson gson = new Gson();
            DbVersionModel serverdbVersionModel = gson.fromJson(response.body().string(), DbVersionModel.class);
            Logger.d("服务器数据库版本信息为：" + serverdbVersionModel.toString());
            Logger.d("获取本地数据库信息：" + getLocaldbVersionModel().toString());
            Logger.d("获取本地数据库与服务器数据库是否匹配：" + serverdbVersionModel.equals(getLocaldbVersionModel()));
            return serverdbVersionModel.equals(getLocaldbVersionModel());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    //获取本地数据库版本模型
    private DbVersionModel getLocaldbVersionModel() {
        //查询本地数据库
        //倒序排列
        List<ArticleEntity> articleEntities = AppContext.getArticleEntityDao()
                .queryBuilder()
                .orderDesc(ArticleEntityDao.Properties.Id)
                .list();

        if (articleEntities.size() > 0) {
            long post_date_unix = articleEntities.get(0).getUnix_time();
            int count = articleEntities.size();
            return new DbVersionModel(post_date_unix, count);
        } else {
            return new DbVersionModel(0, 0);
        }

    }

    private long getLocaldbLastId(){
        List<ArticleEntity> articleEntities = AppContext.getArticleEntityDao()
                .queryBuilder()
                .orderDesc(ArticleEntityDao.Properties.Id)
                .list();
        if (articleEntities.size() > 0) {

            return articleEntities.get(0).getId();
        } else {
            return 0;
        }
    }
}
