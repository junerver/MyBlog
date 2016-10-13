package com.junerver.myblog.db.entity;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Generated;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Junerver on 2016/8/5.
 * 这是文章类的实体
 */
@Entity
public class ArticleEntity implements Parcelable {

    /**
     * id : 5
     * post_content : null
     * post_title : 随便测试一
     * post_date : 2016-08-03 18:45:56
     */

    @Id
    private long id;
    @Property(nameInDb = "POST_CONTENT")
    private String post_content;
    @Property(nameInDb = "POST_TITLE")
    private String post_title;
    @Property(nameInDb = "POST_DATE")
    private String post_date;
    @Transient
    private long unix_time=0L;

    public long getUnix_time() {
        if (this.unix_time == 0L) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date date = simpleDateFormat.parse(getPost_date());
                long time = date.getTime();
                this.unix_time = time/1000;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return unix_time;
    }

    public String getPost_date() {
        return this.post_date;
    }
    public void setPost_date(String post_date) {
        this.post_date = post_date;
    }
    public String getPost_title() {
        return this.post_title;
    }
    public void setPost_title(String post_title) {
        this.post_title = post_title;
    }
    public String getPost_content() {
        return this.post_content;
    }
    public void setPost_content(String post_content) {
        this.post_content = post_content;
    }
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }

    @Generated(hash = 897393802)
    public ArticleEntity(long id, String post_content, String post_title,
            String post_date) {
        this.id = id;
        this.post_content = post_content;
        this.post_title = post_title;
        this.post_date = post_date;
    }
    @Generated(hash = 1301498493)
    public ArticleEntity() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.post_content);
        dest.writeString(this.post_title);
        dest.writeString(this.post_date);
        dest.writeLong(this.unix_time);
    }

    protected ArticleEntity(Parcel in) {
        this.id = in.readLong();
        this.post_content = in.readString();
        this.post_title = in.readString();
        this.post_date = in.readString();
        this.unix_time = in.readLong();
    }

    @Transient
    public static final Parcelable.Creator<ArticleEntity> CREATOR = new Parcelable.Creator<ArticleEntity>() {
        @Override
        public ArticleEntity createFromParcel(Parcel source) {
            return new ArticleEntity(source);
        }

        @Override
        public ArticleEntity[] newArray(int size) {
            return new ArticleEntity[size];
        }
    };
}
