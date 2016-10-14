package com.junerver.myblog.db;

import com.junerver.myblog.AppContext;
import com.junerver.myblog.db.dao.ArticleEntityDao;
import com.junerver.myblog.db.entity.ArticleEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Junerver on 2016/8/10.
 */
public class ArticleUtils {
    private static List<ArticleEntity> articleEntities = new ArrayList<>();

    /**
     * @return 返回数据库的全部对象并按照ID降序排列
     */
    public static List<ArticleEntity> list() {
        articleEntities.clear();
        List<ArticleEntity> temp= AppContext.getArticleEntityDao()
                .queryBuilder()
                .orderDesc(ArticleEntityDao.Properties.Id)
                .list();
        for (int i = 0; i < temp.size(); i++) {
            articleEntities.add(temp.get(i));
        }
        return articleEntities;
    }

    /**
     * @return 获取数据库数据总数
     */
    public static long count() {
        return AppContext.getArticleEntityDao()
                .queryBuilder()
                .orderDesc(ArticleEntityDao.Properties.Id)
                .count();
    }
}
