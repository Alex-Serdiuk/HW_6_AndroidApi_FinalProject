package com.example.hw_6_androidapi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Note implements Serializable {

    @Expose
    @SerializedName("id")
    private int id;

    @Expose
    @SerializedName("author")
    private String author;

    @Expose
    @SerializedName("title")
    private String title;

    @Expose
    @SerializedName("textValue")
    private String textValue;

    public Note() {
    }

    public Note(String author, String title, String textValue) {
        this.author = author;
        this.title = title;
        this.textValue = textValue;
    }

    public int getId() {
        return id;
    }


    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTextValue() {
        return textValue;
    }

    public void setTextValue(String textValue) {
        this.textValue = textValue;
    }
}
