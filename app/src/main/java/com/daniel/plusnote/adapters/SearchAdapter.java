package com.daniel.plusnote.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.daniel.plusnote.entities.Note;
import com.daniel.plusnote.listeners.NotesListener;
import com.daniel.plusnote.R;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.NoteViewHolder>{

    private List<Note> notes;
    private NotesListener notesListener;
    private Context context;

    public SearchAdapter(List<Note> notes, NotesListener notesListener,Context context) {
        this.notes = notes;
        this.context = context;
        this.notesListener = notesListener;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.search_recycler_view_item,
                        parent,
                        false
                )
        );
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        holder.setNote(notes.get(position));
        holder.search_container.setOnClickListener(v -> {
            notesListener.onNoteClicked(notes.get(position), position);
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView search_note_title, search_note_date;
        ImageButton search_note_icon;
        ConstraintLayout search_container;

        NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            search_note_title = itemView.findViewById(R.id.search_note_title);
            search_note_date = itemView.findViewById(R.id.search_note_date);
            search_note_icon = itemView.findViewById(R.id.search_note_icon);
            search_container = itemView.findViewById(R.id.search_container);
        }

        void setNote(Note note) {
            search_note_title.setText(note.getTextNoteTitle());
            final DateTimeFormatter noteDay = DateTimeFormatter.ofPattern("yyyy_MM_dd");
            LocalDate ld = LocalDate.from(noteDay.parse(note.getDate()));
            final DateTimeFormatter noteDay1 = DateTimeFormatter.ofPattern("dd MMM yyyy");
            String str1 = noteDay1.format(ld);
            search_note_date.setText(str1);
            if (note.isIs_list()) {
                search_note_icon.setBackgroundResource(R.drawable.ic_listnote);
            } else if (note.isIs_image()) {
                if (note.isIs_video()) {
                    search_note_icon.setBackgroundResource(R.drawable.ic_video);
                } else if (note.isFrom_gallery()) {
                    search_note_icon.setBackgroundResource(R.drawable.ic_image);
                } else {
                    search_note_icon.setBackgroundResource(R.drawable.ic_camera);
                }
            } else if (note.isIs_voice()) {
                search_note_icon.setBackgroundResource(R.drawable.ic_microphone);
            } else {
                search_note_icon.setBackgroundResource(R.drawable.ic_textnote);
            }
        }
    }
}
