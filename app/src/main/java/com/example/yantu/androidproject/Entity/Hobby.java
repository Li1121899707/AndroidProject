package com.example.yantu.androidproject.Entity;

import java.io.Serializable;

public class Hobby implements Serializable {

    private int hbId;
    private String hbName;
    private String hbTime;

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

    public String getHbCycle() {
        return hbCycle;
    }

    public void setHbCycle(String hbCycle) {
        this.hbCycle = hbCycle;
    }

    private String hbCycle;
}
