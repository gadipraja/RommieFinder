package com.example.roomiefinder;


public class chat {
    private String msg;
    private String sender;
    private String group;
    private String time;

    public chat()
    {}

    public chat(String msg, String sender, String group, String time) {
        this.msg = msg;
        this.sender = sender;
        this.group = group;
        this.time = time;
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
}
