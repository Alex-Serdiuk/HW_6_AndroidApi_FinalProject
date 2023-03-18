package com.example.hw_6_androidapi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListNotesActivity extends AppCompatActivity implements NoteAdapter.OnItemClickListener{
    private NoteDataModel noteDataModel;
    private NoteAdapter adapter;

    //private ListView lvNotesList;

    private RecyclerView rvNotesList;

    private FloatingActionButton fabAddNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_notes);

        initView();
        setListener();
//        initData();
//        initAdapter();


    }

    private void initView() {
        rvNotesList = findViewById(R.id.rvNotesList);
        fabAddNote = findViewById(R.id.fabAddNote);
    }

    private void setListener() {
        fabAddNote.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddNoteActivity.class);
            noteDataModel.setNoteId(-1); // обнуление id
            intent.putExtra(MainActivity.KEY_ALL_NOTES, noteDataModel);

            startActivity(intent);
        });

        LinearLayoutManager linearLayoutManager =  //вертикальная прокрутка
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        //разделитель
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, linearLayoutManager.getOrientation());
        Intent intent = getIntent();
        noteDataModel = (NoteDataModel) intent.getSerializableExtra(MainActivity.KEY_ALL_NOTES);
        adapter = new NoteAdapter(noteDataModel.getNoteList(), this);

        rvNotesList.setLayoutManager(linearLayoutManager);

        rvNotesList.addItemDecoration(itemDecoration);

        rvNotesList.setAdapter(adapter);

        rvNotesList.setHasFixedSize(true);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
        itemTouchHelper.attachToRecyclerView(rvNotesList);

//        lvNotesList.setOnItemClickListener((parent, view, position, id) -> {
//
//            noteDataModel.setNoteId(position); // выбор заметки для редактирования
//
//            Intent intent = new Intent(this, AddNoteActivity.class);
//            intent.putExtra(MainActivity.KEY_ALL_NOTES, noteDataModel);
//            startActivity(intent);
//        });

//        lvNotesList.setOnItemLongClickListener((parent, view, position, id) -> {
//
//            int noteId = getClickedItemId(position);
//
//            AlertDialog.Builder builder = new AlertDialog.Builder(this)
//                    .setTitle("Видалити замітку?")
//                    .setMessage("Після видалення елемент неможливо буде відновити.")
//                    .setPositiveButton("ТАК", (dialog, which) -> {
//                        deleteNoteById(noteId, position);
//                    })
//                    .setNeutralButton("НІ", (dialog, which) -> {
//                        Toast.makeText(this, "Відміна!", Toast.LENGTH_SHORT).show();
//                    });
//            builder.show();
//
//            return true;
//        });



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
                        adapter.notifyItemRemoved(position);
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
//
//    private void initData() {
//        Intent intent = getIntent();
//        noteDataModel = (NoteDataModel) intent.getSerializableExtra(MainActivity.KEY_ALL_NOTES);
//    }
//
//    private void initAdapter() {
//        adapter = new NoteAdapter(
//                this,
//                R.layout.item_note,
//                noteDataModel.getNoteList()
//        );
//
//        lvNotesList.setAdapter(adapter);
//    }



    @Override
    public void onItemClick(int position) {
            noteDataModel.setNoteId(position); // выбор заметки для редактирования

            Intent intent = new Intent(this, AddNoteActivity.class);
            intent.putExtra(MainActivity.KEY_ALL_NOTES, noteDataModel);
            startActivity(intent);
    }

    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            int noteId = getClickedItemId(position);

            AlertDialog.Builder builder = new AlertDialog.Builder(ListNotesActivity.this)
                    .setTitle("Видалити замітку?")
                    .setMessage("Після видалення елемент неможливо буде відновити.")
                    .setPositiveButton("ТАК", (dialog, which) -> {
                        deleteNoteById(noteId, position);
                    })
                    .setNeutralButton("НІ", (dialog, which) -> {
                        Toast.makeText(ListNotesActivity.this, "Відміна!", Toast.LENGTH_SHORT).show();
                        adapter.notifyItemChanged(position);
                    });
            builder.show();
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

            if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                View itemView = viewHolder.itemView;

                // Рассчитываем координаты для анимации свайпа
                float itemHeight = (float) itemView.getHeight();
                float itemWidth = (float) itemView.getWidth();
                float alpha = 1.0f - Math.abs(dX) / itemWidth;

                itemView.setTranslationX(dX);
                itemView.setAlpha(alpha);

                // Рисуем иконку удаления
                Drawable icon = ContextCompat.getDrawable(getApplicationContext(), R.drawable.delete);
                int iconHeight = icon.getIntrinsicHeight();
                int iconWidth = icon.getIntrinsicWidth();
                int iconTop = (int) (itemView.getTop() + (itemHeight - iconHeight) / 2);
                int iconBottom = iconTop + iconHeight;
                int iconMargin = (int) ((itemHeight - iconHeight) / 2);
                int iconLeft = (int) (itemView.getRight() - iconMargin - iconWidth);
                int iconRight = itemView.getRight() - iconMargin;

                icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                icon.draw(c);
            }
        }
    };




}