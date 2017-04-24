package com.hele.hardware.analyser.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2017/4/20.
 */
@Entity
public class ResultInfo {
    @Id
    private Long id;
    private long identity;
    private String name;
    private String picturePath;
    private String value;
    private Long dateTime;

    public Long getDateTime() {
        return this.dateTime;
    }

    public void setDateTime(Long dateTime) {
        this.dateTime = dateTime;
    }

    public String getPicturePath() {
        return this.picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getIdentity() {
        return this.identity;
    }

    public void setIdentity(long identity) {
        this.identity = identity;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Generated(hash = 129481143)
    public ResultInfo(Long id, long identity, String name, String picturePath,
            String value, Long dateTime) {
        this.id = id;
        this.identity = identity;
        this.name = name;
        this.picturePath = picturePath;
        this.value = value;
        this.dateTime = dateTime;
    }

    @Generated(hash = 1829158754)
    public ResultInfo() {
    }
}
