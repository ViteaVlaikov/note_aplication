package com.example.myapplication.clicker;

import androidx.cardview.widget.CardView;

import com.example.myapplication.model.Note;

public interface NoteClickListener {
    void onClick(Note note);
    void onLongClick(Note note, CardView cardView);
}
