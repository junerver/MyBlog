package com.junerver.myblog.model;

/**
 * Created by junerver on 2016/8/9.
 * 这个包用于存放一些特殊的模型类，便于使用Gson来序列化与反序列化，
 * 不涉及数据库的操作，设计数据库操作的实体均有greendao实现，
 * 尾缀使用Model与Entity进行区分
 */
public class DbVersionModel {
    /**
     * version : 1464769160
     * count : 3
     */

    private long version;
    private int count;

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public DbVersionModel(long version, int count) {
        this.version = version;
        this.count = count;
    }

    public DbVersionModel() {
    }

    @Override
    public boolean equals(Object o) {
        DbVersionModel temp = (DbVersionModel) o;
        if (this.version == temp.getVersion() && this.count == temp.getCount()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "DbVersionModel{" +
                "version=" + version +
                ", count=" + count +
                '}';
    }
}
