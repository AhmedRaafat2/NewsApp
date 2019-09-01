package com.example.newsapp;

public class New {
    private String newTitle;
    private String newTopic;
    private String newDate;
    private String newTime;
    private String new_url;

    public New(String newTitle, String newTopic, String newDate, String newTime, String new_url) {
        this.newTitle = newTitle;
        this.newTopic = newTopic;
        this.newDate = newDate;
        this.newTime = newTime;
        this.new_url = new_url;
    }

    public String getNewTitle() {
        return newTitle;
    }

    public void setNewTitle(String newTitle) {
        this.newTitle = newTitle;
    }

    public String getNewTopic() {
        return newTopic;
    }

    public void setNewTopic(String newTopic) {
        this.newTopic = newTopic;
    }

    public String getNewDate() {
        return newDate;
    }

    public void setNewDate(String newDate) {
        this.newDate = newDate;
    }

    public String getNewTime() {
        return newTime;
    }

    public void setNewTime(String newTime) {
        this.newTime = newTime;
    }

    public String getNew_url() {
        return new_url;
    }

    public void setNew_url(String new_url) {
        this.new_url = new_url;
    }
}