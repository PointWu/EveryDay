package com.example.everydayis.activity;
/**
 * DESC:
 * Author:七月无雨
 * Data:实体类,日记本
 **/
public class Diary {
    private int id;
    private String title;
    private String content;
    private String pubdate;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getPubdate() {
        return pubdate;
    }
    public void setPubdate(String pubdate) {
        this.pubdate = pubdate;
    }
    public Diary(String title, String content, String pubdate) {
        super();
        this.title = title;
        this.content = content;
        this.pubdate = pubdate;
    }
    public Diary(int id, String title, String content, String pubdate) {
        super();
        this.id = id;
        this.title = title;
        this.content = content;
        this.pubdate = pubdate;
    }
    public Diary(int id, String title, String content) {
        super();
        this.id = id;
        this.title = title;
        this.content = content;
    }
}
