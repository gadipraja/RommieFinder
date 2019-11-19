package com.example.roomiefinder;


import com.google.firebase.Timestamp;

public class chat {
    private String msg;
    private String sender;
    private String group;
    private String time;
    private Timestamp timestamp;
    private String senderName;

    public chat() {
    }

    public chat(String msg, String sender, String group, String time, Timestamp timestamp, String senderName) {
        this.msg = msg;
        this.sender = sender;
        this.group = group;
        this.time = time;
        this.timestamp = timestamp;
        this.senderName = senderName;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getMsg() {
        return msg;
    }

    public String getSender() {
        return sender;
    }

    public String getGroup() {
        return group;
    }

    public String getTime() {
        return time;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }
}