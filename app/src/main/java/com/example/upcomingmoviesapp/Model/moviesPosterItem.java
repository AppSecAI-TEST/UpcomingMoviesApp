package com.example.upcomingmoviesapp.Model;

/**
 * Created by Vikas.Dasade on 29/07/2017.
 */

public class moviesPosterItem {

    String id;
    String author;
    String content;
    String url;

    public String getId() {
        return id;
    }

    public String setId(String id) {
        this.id = id;
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String setAuthor(String author) {
        this.author = author;
        return author;
    }

    public String getContent() {
        return content;
    }

    public String setContent(String content) {
        this.content = content;
        return content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


}
