package com.example.hw_6_androidapi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    public static final String KEY_ALL_NOTES = "keyAllNotes";
    public static final String KEY_ADD_NOTE = "keyAddNote";
    public static final String KEY_AUTHOR_NAME = "keyAuthorName";


    private SharedPreferences sharedPreferences;

    private SharedPreferences.Editor editor;

    private String authorName;

    private NoteDataModel noteDataModel;
    private EditText etName;
    private Button btnSaveAuthor;
    private Button btnAddNote;
    private Button btnAllNotes;
    private ProgressBar pbDownload;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        setListener();
        initData();
    }




    private void initView() {
        sharedPreferences = getSharedPreferences("settingsAuthor", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        etName = findViewById(R.id.etName);
        btnSaveAuthor=findViewById(R.id.btnSaveAuthor);
        btnAddNote = findViewById(R.id.btnAddNote);
        btnAllNotes=findViewById(R.id.btnAllNotes);
        pbDownload=findViewById(R.id.pbDownload);

    }

    private void setListener() {
        btnSaveAuthor.setOnClickListener(v->{
            editor.putString(KEY_AUTHOR_NAME, etName.getText().toString());

            editor.apply();

            authorName = etName.getText().toString();

            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);

        });

        btnAddNote.setOnClickListener(v ->{
            if(authorName.equals("")){
                Toast.makeText(this, "Введіть імʼя автора замітки", Toast.LENGTH_SHORT).show();
            }else {
                Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
                noteDataModel.setAuthorName(authorName);
                noteDataModel.setNoteId(-1); // обнуляем Id
                intent.putExtra(KEY_ALL_NOTES,noteDataModel);

                startActivity(intent);
            }
        });

        btnAllNotes.setOnClickListener(v->{
            if(authorName.equals("")){
                Toast.makeText(this, "Введіть імʼя автора замітки", Toast.LENGTH_SHORT).show();
            }else {
                noteDataModel.setAuthorName(authorName);
                showAllNotesActivity();
            }
        });
    }

    private void showAllNotesActivity() {
        pbDownload.setVisibility(View.VISIBLE);

        NetworkService
                .getInstance()
                .getNoteApi()
                .getAllNotes()
                .enqueue(new Callback<List<Note>>() {
                    @Override
                    public void onResponse(Call<List<Note>> call, Response<List<Note>> response) {
                        noteDataModel.setNoteList(response.body());
                        Collections.reverse(noteDataModel.getNoteList());

                        pbDownload.setVisibility(View.INVISIBLE);

                        if(noteDataModel.getNoteList().size() == 0){
                            Toast.makeText(MainActivity.this,"Немає даних для вашого запиту", Toast.LENGTH_SHORT).show();
                        }else {
                            Intent intent = new Intent(MainActivity.this, ListNotesActivity.class);
                            intent.putExtra(KEY_ALL_NOTES, noteDataModel);

                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Note>> call, Throwable t) {
                        pbDownload.setVisibility(View.INVISIBLE);
                        Toast.makeText(MainActivity.this, "ERROR: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("xxx", t.getMessage());
                    }
                });
    }

    private void initData() {
        authorName=sharedPreferences.getString(KEY_AUTHOR_NAME,"");

        etName.setText(authorName);

        noteDataModel = new NoteDataModel();

    }
}