package com.tiaoshei.ganhuo.model;

/**
 * Created by ronnie on 15/5/20.
 */
public class Article {
    private String __LINK__;

    public String get__LINK__() {
        return __LINK__;
    }

    public void set__LINK__(String __LINK__) {
        this.__LINK__ = __LINK__;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTranslator() {
        return translator;
    }

    public void setTranslator(String translator) {
        this.translator = translator;
    }

    public String getTranslatorUrl() {
        return translatorUrl;
    }

    public void setTranslatorUrl(String translatorUrl) {
        this.translatorUrl = translatorUrl;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private String title;
    private String author;

    public String getAuthorUrl() {
        return authorUrl;
    }

    public void setAuthorUrl(String authorUrl) {
        this.authorUrl = authorUrl;
    }

    private String authorUrl;
    private String translator;
    private String translatorUrl;
    private String summary;
    private String url;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getReply() {
        return reply;
    }

    public void setReply(int reply) {
        this.reply = reply;
    }

    private String time;
    private int reply;

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    private String cover;
}
