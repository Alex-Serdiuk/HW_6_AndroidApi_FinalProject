package com.example.hw_6_androidapi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {
    private Context context;
    private int resource;
    private  List<Note> noteList;
    //private LayoutInflater inflater;
    private OnItemClickListener localListener;

    public NoteAdapter(@NonNull Context context, int resource, @NonNull List<Note> noteList) {
//        super(context, resource, noteList);
        this.context = context;
        this.resource = resource;
        this.noteList = noteList;

//        inflater = LayoutInflater.from(context);
    }

    public NoteAdapter(List<Note> noteList, OnItemClickListener listener) {
        this.noteList = noteList;
        this.localListener = listener;
    }

    //    @NonNull
//    @Override
//    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        View item = inflater.inflate(resource, parent, false);
//
//        TextView tvNoteTitle = item.findViewById(R.id.tvNoteTitle);
//        TextView tvNoteText = item.findViewById(R.id.tvNoteText);
//        TextView tvNoteAuthor = item.findViewById(R.id.tvNoteAuthor);
//
//        Note note = noteList.get(position);
//
//        tvNoteTitle.setText(note.getTitle());
//        tvNoteText.setText(note.getTextValue());
//        tvNoteAuthor.setText(note.getAuthor());
//
//        return item;
//    }

    @NonNull
    @Override
    public NoteAdapter.NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
        NoteViewHolder noteViewHolder = new NoteViewHolder(item);
        return noteViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NoteAdapter.NoteViewHolder holder, int position) {
        Note note = noteList.get(position);
        holder.tvNoteTitle.setText(note.getTitle());
        holder.tvNoteText.setText(note.getTextValue());
        holder.tvNoteAuthor.setText(note.getAuthor());
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    interface OnItemClickListener {
        public void onItemClick(int position);
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView tvNoteTitle;
        private TextView tvNoteText;
        private TextView tvNoteAuthor;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tvNoteTitle = itemView.findViewById(R.id.tvNoteTitle);
            this.tvNoteText = itemView.findViewById(R.id.tvNoteText);
            this.tvNoteAuthor = itemView.findViewById(R.id.tvNoteAuthor);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            localListener.onItemClick(getAdapterPosition());
        }
    }
}
