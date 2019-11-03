package com.example.roomiefinder;

public class lookup {
    private String title;
    private String desc;
    private String addr;
    private String poster;
    private String price;

    public lookup()
    {}

    public lookup(String title, String desc, String addr, String poster, String price) {
        this.title = title;
        this.desc = desc;
        this.addr = addr;
        this.price = price;
        this.poster = poster;
    }

    public String getTitle() {
        return title;
    }

    public String getPoster() {
        return poster;
    }

    public String getDesc() {
        return desc;
    }

    public String getAddr() {
        return addr;
    }

    public String getPrice() {
        return price;
    }
}
