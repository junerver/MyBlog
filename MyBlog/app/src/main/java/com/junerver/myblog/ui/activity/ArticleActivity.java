package com.junerver.myblog.ui.activity;

import android.os.Build;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.junerver.myblog.R;
import com.junerver.myblog.db.entity.ArticleEntity;

public class ArticleActivity extends BaseActivity {
    private TextView mTvTitle;
    private WebView mWvContent;
    private ArticleEntity mArticleEntity;

    @Override
    protected void initView() {

        mTvTitle = (TextView) findViewById(R.id.tvTitle);
        mWvContent = (WebView) findViewById(R.id.wvContent);

        mTvTitle.setText(mArticleEntity.getPost_title());

        mWvContent.getSettings().setDefaultTextEncodingName("utf-8");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mWvContent.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        } else {
            mWvContent.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        }
        mWvContent.loadData(getHtmlData(mArticleEntity.getPost_content()), "text/html; charset=utf-8", "utf-8");

    }

    @Override
    protected void initData() {
        //从inten中获取实体类
        mArticleEntity = getIntent().getParcelableExtra("article");
    }

    @Override
    protected void setListeners() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_article;
    }

    private String getHtmlData(String bodyHTML) {
        String head = "<head>" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\"> " +
                "<style>img{max-width: 100%; width:auto; height:auto;}</style>" +
                "</head>";
        return "<html>" + head + "<body>" + bodyHTML + "</body></html>";
    }
}
