package com.example.hw_6_androidapi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class NoteAdapter extends ArrayAdapter<Note> {
    private Context context;
    private int resource;
    private  List<Note> noteList;
    private LayoutInflater inflater;

    public NoteAdapter(@NonNull Context context, int resource, @NonNull List<Note> noteList) {
        super(context, resource, noteList);
        this.context = context;
        this.resource = resource;
        this.noteList = noteList;

        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View item = inflater.inflate(resource, parent, false);

        TextView tvNoteTitle = item.findViewById(R.id.tvNoteTitle);
        TextView tvNoteText = item.findViewById(R.id.tvNoteText);
        TextView tvNoteAuthor = item.findViewById(R.id.tvNoteAuthor);

        Note note = noteList.get(position);

        tvNoteTitle.setText(note.getTitle());
        tvNoteText.setText(note.getTextValue());
        tvNoteAuthor.setText(note.getAuthor());

        return item;
    }
}
