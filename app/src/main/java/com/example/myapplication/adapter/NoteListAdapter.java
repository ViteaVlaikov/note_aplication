package com.example.myapplication.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.clicker.NoteClickListener;
import com.example.myapplication.model.Note;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NoteListAdapter extends RecyclerView.Adapter<NoteViewHolder> {

    Context context;
    List<Note> notes;
    NoteClickListener noteClickListener;

    public NoteListAdapter(Context context, List<Note> notes, NoteClickListener noteClickListener) {
        this.context = context;
        this.notes = notes;
        this.noteClickListener = noteClickListener;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteViewHolder(LayoutInflater.from(context).inflate(R.layout.note_list,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        holder.note_title.setText(notes.get(position).getTitle());
        holder.note_title.setSelected(true);
        holder.note_context.setText(notes.get(position).getNote());

        holder.note_date.setText(notes.get(position).getDate());
        holder.note_date.setSelected(true);
        if(notes.get(position).isPinned()){
            holder.pin_button_image.setImageResource(R.drawable.pin_icon);
        }
        else {
            holder.pin_button_image.setImageResource(0);
        }
        int colorCode = getRandomColor();
        holder.note_container.setCardBackgroundColor(ContextCompat.getColor(context,colorCode));
        holder.note_container.setOnClickListener(view -> noteClickListener.onClick(notes.get(holder.getAdapterPosition())));
        holder.note_container.setOnLongClickListener(view -> {
            noteClickListener.onLongClick(notes.get(holder.getAdapterPosition()), holder.note_container);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filterList(List<Note> filteredList){
        notes = filteredList;
        notifyDataSetChanged();
    }

    private int getRandomColor(){
        List<Integer> colorList = new ArrayList<>();
        colorList.add(R.color.color1);
        colorList.add(R.color.color2);
        colorList.add(R.color.color3);
        colorList.add(R.color.color4);
        colorList.add(R.color.color5);
        Random random = new Random();
        int randomNumber = random.nextInt(colorList.size());
        return colorList.get(randomNumber);
    }
}


