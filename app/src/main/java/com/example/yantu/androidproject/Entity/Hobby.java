package com.example.yantu.androidproject.Entity;

import java.io.Serializable;

public class Hobby implements Serializable {

    private int hbId;
    private String hbName;
    private String hbTime;
    private Integer hbCycle;
    private String hbImg = "";

    public int getHbId() {
        return hbId;
    }

    public void setHbId(int hbId) {
        this.hbId = hbId;
    }

    public String getHbName() {
        return hbName;
    }

    public void setHbName(String hbName) {
        this.hbName = hbName;
    }

    public String getHbTime() {
        return hbTime;
    }

    public void setHbTime(String hbTime) {
        this.hbTime = hbTime;
    }

    public Integer getHbCycle() {
        return hbCycle;
    }

    public void setHbCycle(Integer hbCycle) {
        this.hbCycle = hbCycle;
    }

    public String getHbImg() {
        return hbImg;
    }

    public void setHbImg(String hbImg) {
        this.hbImg = hbImg;
    }
}
