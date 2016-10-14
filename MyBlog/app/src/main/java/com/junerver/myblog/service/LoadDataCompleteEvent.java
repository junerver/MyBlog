package com.junerver.myblog.service;

/**
 * Created by Junerver on 2016/8/10.
 * 事件类
 */
public class LoadDataCompleteEvent {

    private String message;

    public LoadDataCompleteEvent(String message) {
        this.message = message;
    }

    public String getMessage() {

        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
