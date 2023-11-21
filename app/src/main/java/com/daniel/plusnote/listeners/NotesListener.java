package com.daniel.plusnote.listeners;

import com.daniel.plusnote.entities.Note;

public interface NotesListener {
    void onNoteClicked(Note note, int position);
    void onNoteClicked(Note note, int position, boolean deleteNote);
    void onNoteClicked(Note note, int position, boolean deleteNote, boolean shareNote);
    void onNoteClicked(Note note, int position, boolean deleteNote, boolean shareNote, boolean setReminder);
}
