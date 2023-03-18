package com.example.hw_6_androidapi;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListNotesActivity extends AppCompatActivity {
    private NoteDataModel noteDataModel;
    private NoteAdapter adapter;

    private ListView lvNotesList;

    private FloatingActionButton fabAddNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_notes);

        initView();
        setListener();
        initData();
        initAdapter();
    }

    private void initView() {
        lvNotesList = findViewById(R.id.lvNotesList);
        fabAddNote = findViewById(R.id.fabAddNote);
    }

    private void setListener() {
        fabAddNote.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddNoteActivity.class);
            noteDataModel.setNoteId(-1); // обнуление id
            intent.putExtra(MainActivity.KEY_ALL_NOTES, noteDataModel);

            startActivity(intent);
        });

        lvNotesList.setOnItemClickListener((parent, view, position, id) -> {

            noteDataModel.setNoteId(position); // выбор заметки для редактирования

            Intent intent = new Intent(this, AddNoteActivity.class);
            intent.putExtra(MainActivity.KEY_ALL_NOTES, noteDataModel);
            startActivity(intent);
        });

        lvNotesList.setOnItemLongClickListener((parent, view, position, id) -> {

            int noteId = getClickedItemId(position);

            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setTitle("Видалити замітку?")
                    .setMessage("Після видалення елемент неможливо буде відновити.")
                    .setPositiveButton("ТАК", (dialog, which) -> {
                        deleteNoteById(noteId, position);
                    })
                    .setNeutralButton("НІ", (dialog, which) -> {
                        Toast.makeText(this, "Відміна!", Toast.LENGTH_SHORT).show();
                    });
            builder.show();

            return true;
        });

        

    }

    private void deleteNoteById(int noteId, int position) {
        NetworkService
                .getInstance()
                .getNoteApi()
                .deleteNoteById(noteId)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Toast.makeText(ListNotesActivity.this, "Замітка видалена!", Toast.LENGTH_SHORT).show();
                        noteDataModel.getNoteList().remove(position);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(ListNotesActivity.this, "ERROR: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private int getClickedItemId(int position) {
        return noteDataModel.getNoteList().get(position).getId();
    }

    private void initData() {
        Intent intent = getIntent();
        noteDataModel = (NoteDataModel) intent.getSerializableExtra(MainActivity.KEY_ALL_NOTES);
    }

    private void initAdapter() {
        adapter = new NoteAdapter(
                this,
                R.layout.item_note,
                noteDataModel.getNoteList()
        );

        lvNotesList.setAdapter(adapter);
    }
}