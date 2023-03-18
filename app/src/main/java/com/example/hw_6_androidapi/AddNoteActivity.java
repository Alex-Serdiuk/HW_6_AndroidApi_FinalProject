package com.example.hw_6_androidapi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddNoteActivity extends AppCompatActivity {
    private NoteDataModel noteDataModel;
    private Note note;
    private TextView tvAuthorName;
    private EditText etNoteTitle;
    private EditText etmNoteText;
    private Button btnSaveNote;
    private Button btnClear;
    private Button btnAllNotes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        initView();
        setListener();
        initData();
    }

    private void initView() {
        tvAuthorName = findViewById(R.id.tvAuthorName);
        etNoteTitle = findViewById(R.id.etNoteTitle);
        etmNoteText = findViewById(R.id.etmNoteText);
        btnSaveNote = findViewById(R.id.btnSaveNote);
        btnClear = findViewById(R.id.btnClear);
        btnAllNotes = findViewById(R.id.btnAllNotes2);

    }

    private void setListener() {
        btnSaveNote.setOnClickListener(v->{
            String title = etNoteTitle.getText().toString().length() > 0 ? etNoteTitle.getText().toString() : etNoteTitle.getHint().toString();
            String text = etmNoteText.getText().toString().length() > 0 ? etmNoteText.getText().toString() : etmNoteText.getHint().toString();

            note.setTitle(title);
            note.setTextValue(text);
            note.setAuthor(noteDataModel.getAuthorName());

            NetworkService
                    .getInstance()
                    .getNoteApi()
                    .saveNote(note)
                    .enqueue(new Callback<Note>() {
                        @Override
                        public void onResponse(Call<Note> call, Response<Note> response) {
                            note = response.body();

                            Toast.makeText(AddNoteActivity.this, "Замітка збережена!", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<Note> call, Throwable t) {
                            Toast.makeText(AddNoteActivity.this, "ERROR: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        btnClear.setOnClickListener(v -> {
            note = new Note();
            etNoteTitle.setText("");
            etmNoteText.setText("");
        });

        btnAllNotes.setOnClickListener(v->{
            NetworkService
                    .getInstance()
                    .getNoteApi()
                    .getAllNotes()
                    .enqueue(new Callback<List<Note>>() {
                        @Override
                        public void onResponse(Call<List<Note>> call, Response<List<Note>> response) {
                            noteDataModel.setNoteList(response.body());
                            Collections.reverse(noteDataModel.getNoteList());

                            if (noteDataModel.getNoteList().size() == 0) {
                                Toast.makeText(AddNoteActivity.this, "There is no data at your request", Toast.LENGTH_SHORT).show();
                            } else {


                                Intent intent = new Intent(AddNoteActivity.this, ListNotesActivity.class);
                                intent.putExtra(MainActivity.KEY_ALL_NOTES, noteDataModel);

                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onFailure(Call<List<Note>> call, Throwable t) {
                            Toast.makeText(AddNoteActivity.this, "ERROR: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("xxx", t.getMessage());
                        }
                    });
        });
    }

    private void initData() {
        note = new Note();

        Intent intent = getIntent();
        noteDataModel = (NoteDataModel) intent.getSerializableExtra(MainActivity.KEY_ALL_NOTES);

        tvAuthorName.setText(noteDataModel.getAuthorName());

        if(!(noteDataModel.getNoteId() < 0)){
            note = noteDataModel.getNoteList().get(noteDataModel.getNoteId());
            etNoteTitle.setText(note.getTitle());
            etmNoteText.setText(note.getTextValue());
        }
        noteDataModel.setNoteId(-1); // обнуляем id
    }
}