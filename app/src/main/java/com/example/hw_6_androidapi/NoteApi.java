package com.example.hw_6_androidapi;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface NoteApi {

    @GET("/notes")
    Call<List<Note>>getAllNotes();

    @GET("/notes/{id}")
    Call<Note>getNoteById(@Path("id") int id);

    @POST("/notes")
    Call<Note>saveNote(@Body Note note);

    @PUT("/notes")
    Call<Note>updateNote(@Body Note note);

    @DELETE("/notes/{id}")
    Call<Void>deleteNoteById(@Path("id") int id);
}
