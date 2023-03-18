package com.example.hw_6_androidapi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NoteDataModel implements Serializable {
    private String authorName;

    private List<Note> noteList = new ArrayList<>();

    private int noteId;

    public NoteDataModel(){

    }

    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public List<Note> getNoteList() {
        return noteList;
    }

    public void setNoteList(List<Note> noteList) {
        this.noteList = noteList;
    }
}
