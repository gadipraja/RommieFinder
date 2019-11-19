package com.example.roomiefinder;

import com.google.firebase.Timestamp;

public class errand {
    private String errand;
    private Timestamp time;

    public errand()
    {}

    public errand(String errand, Timestamp time) {
        this.errand = errand;
        this.time = time;
    }

    public Timestamp getTime() {
        return time;
    }


    public String getErrand() {
        return errand;
    }
}
