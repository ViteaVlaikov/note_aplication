package com.example.myapplication.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;


public class NoteViewHolder extends RecyclerView.ViewHolder {
    CardView note_container;
    TextView note_title, note_context, note_date;
    ImageView pin_button_image;
    public NoteViewHolder(@NonNull View itemView) {
        super(itemView);
        note_container = itemView.findViewById(R.id.note_container);
        note_title = itemView.findViewById(R.id.note_title);
        note_context = itemView.findViewById(R.id.note_context);
        note_date = itemView.findViewById(R.id.note_date);
        pin_button_image = itemView.findViewById(R.id.pin_button_image);
    }
}
