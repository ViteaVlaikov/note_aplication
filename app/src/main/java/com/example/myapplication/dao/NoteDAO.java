package com.example.myapplication.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.myapplication.model.Note;

import java.util.List;

@Dao
public interface NoteDAO {

    @Insert(onConflict = 1)
    void insert(Note note);

    @Query(value = "select * from note order by id desc")
    List<Note> getAll();

    @Query(value = "update note set title = :title, note = :note where id = :id")
    void update(int id, String title, String note);

    @Delete
    void delete(Note note);

    @Query(value = "update note set pinned = :pin where id = :id")
    void pin(int id, boolean pin);
}
