package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.model.Note;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NoteTakerActivity extends AppCompatActivity {

    EditText edit_note, edit_title;
    ImageView image_save;
    Note note;
    boolean isOldNote = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_taker);
        edit_note = findViewById(R.id.edit_note);
        edit_title = findViewById(R.id.edit_title);
        image_save = findViewById(R.id.image_save);
        note = new Note();
        try {
            note = (Note) getIntent().getSerializableExtra("old_note");
            edit_title.setText(note.getTitle());
            edit_note.setText(note.getNote());
            isOldNote = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        image_save.setOnClickListener(view -> {
            String title = edit_title.getText().toString();
            String note_context = edit_note.getText().toString();
            if (note_context.isEmpty()) {
                Toast.makeText(NoteTakerActivity.this, "Please enter context", Toast.LENGTH_SHORT).show();
                return;
            }
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
            Date date = new Date();
            if (!isOldNote)
                note = new Note();
            note.setTitle(title);
            note.setNote(note_context);
            note.setDate(simpleDateFormat.format(date));

            Intent intent = new Intent();
            intent.putExtra("note", note);
            setResult(Activity.RESULT_OK, intent);
            finish();
        });
    }
}