package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.myapplication.adapter.NoteListAdapter;
import com.example.myapplication.clicker.NoteClickListener;
import com.example.myapplication.database.NoteDatabase;
import com.example.myapplication.model.Note;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener{

    private static final int INSERT_REQUEST_CODE = 101;
    private static final int UPDATE_REQUEST_CODE = 102;

    RecyclerView recyclerView;
    FloatingActionButton floatingActionButton;
    NoteListAdapter noteListAdapter;
    NoteDatabase noteDatabase;
    List<Note> notes = new ArrayList<>();
    SearchView searchView;
    Note selectedNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycler_home);
        floatingActionButton = findViewById(R.id.fab_add);
        searchView = findViewById(R.id.search_bar);
        noteDatabase = NoteDatabase.getInstance(this);
        notes = noteDatabase.noteDAO().getAll();
        updateRecycle(notes);
        floatingActionButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this,NoteTakerActivity.class);
            startActivityForResult(intent,101);
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });
    }

    private void filter(String newText) {
        List<Note> notesList = new ArrayList<>();
        for(Note note : notes){
            if(note.getTitle().toLowerCase().contains(newText.toLowerCase())
            ||note.getNote().toLowerCase().contains(newText.toLowerCase())){
                notesList.add(note);
            }
        }
        noteListAdapter.filterList(notesList);
    }

    private void updateRecycle(List<Note> notes){
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        noteListAdapter = new NoteListAdapter(MainActivity.this,notes,noteClickListener);
        recyclerView.setAdapter(noteListAdapter);

    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == INSERT_REQUEST_CODE){
            if(resultCode == Activity.RESULT_OK){
                assert data != null;
                Note newNote = (Note) data.getSerializableExtra("note");
                noteDatabase.noteDAO().insert(newNote);
                notes.clear();
                notes.addAll(noteDatabase.noteDAO().getAll());
                noteListAdapter.notifyDataSetChanged();
            }
        }
        if(requestCode == UPDATE_REQUEST_CODE){
            if(resultCode == Activity.RESULT_OK){
                assert data != null;
                Note newNote = (Note) data.getSerializableExtra("note");
                noteDatabase.noteDAO().update(newNote.getId(), newNote.getTitle(),newNote.getNote());
                notes.clear();
                notes.addAll(noteDatabase.noteDAO().getAll());
                noteListAdapter.notifyDataSetChanged();
            }
        }
    }

    private final NoteClickListener noteClickListener = new NoteClickListener() {
        @Override
        public void onClick(Note note) {
            Intent intent = new Intent(MainActivity.this,NoteTakerActivity.class);
            intent.putExtra("old_note", note);
            startActivityForResult(intent,102);
        }

        @Override
        public void onLongClick(Note note, CardView cardView) {
            selectedNote = note;
            showPopup(cardView);
        }
    };
    private void showPopup(CardView cardView) {
        PopupMenu popupMenu = new PopupMenu(this,cardView);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.show();
    }

    @SuppressLint({"NonConstantResourceId", "NotifyDataSetChanged"})
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.pin :
                if(selectedNote.isPinned()){
                    noteDatabase.noteDAO().pin(selectedNote.getId(),false);
                    Toast.makeText(MainActivity.this,"Unpinned",Toast.LENGTH_SHORT).show();
                }
                else {
                    noteDatabase.noteDAO().pin(selectedNote.getId(),true);
                    Toast.makeText(MainActivity.this,"Pinned",Toast.LENGTH_SHORT).show();
                }
                notes.clear();
                notes.addAll(noteDatabase.noteDAO().getAll());
                noteListAdapter.notifyDataSetChanged();
                return true;
            case R.id.delete:
                noteDatabase.noteDAO().delete(selectedNote);
                notes.remove(selectedNote);
                Toast.makeText(MainActivity.this,"Node removed",Toast.LENGTH_SHORT).show();
                notes.clear();
                notes.addAll(noteDatabase.noteDAO().getAll());
                noteListAdapter.notifyDataSetChanged();
                return true;
            default:
                return false;
        }
    }
}