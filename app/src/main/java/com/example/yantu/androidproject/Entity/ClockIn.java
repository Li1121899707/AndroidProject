package com.example.yantu.androidproject.Entity;

public class ClockIn {
    private Hobby hobby;
    private int ciId;
    private String ciDate;
    public Hobby getHobby() {
        return hobby;
    }

    public void setHobby(Hobby hobby) {
        this.hobby = hobby;
    }

    public int getCiId() {
        return ciId;
    }

    public void setCiId(int ciId) {
        this.ciId = ciId;
    }

    public String getCiDate() {
        return ciDate;
    }

    public void setCiDate(String ciDate) {
        this.ciDate = ciDate;
    }



}
