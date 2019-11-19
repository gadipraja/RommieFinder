package com.example.roomiefinder;

public class lookup {
    private String title;
    private String desc;
    private String addr;
    private String poster;
    private String price;
    private String url;

    public lookup()
    {}

    public lookup(String title, String desc, String addr, String poster, String price, String url) {
        this.title = title;
        this.desc = desc;
        this.addr = addr;
        this.price = price;
        this.poster = poster;
        this.url = url;
    }

    public String getUrl() {
        return url;
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
