package com.ashiana.zlifno.alder.data.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.ashiana.zlifno.alder.data.entity.NoteType;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.IGNORE;
import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

// Adds functionality for the database
@Dao
public interface NoteTypeDao {

    @Insert(onConflict = IGNORE)
    void insertNote(NoteType noteType);

    @Update(onConflict = REPLACE)
    void updateNote(NoteType noteType);

    @Query("DELETE FROM note_type WHERE noteTypeId = (:noteTypeId)")
    void deleteNoteById(int noteTypeId);

    @Query("SELECT * FROM note_type ORDER BY position ASC")
    LiveData<List<NoteType>> getAllNotes();

    @Query("SELECT * FROM note_type WHERE position = :pos")
    NoteType getNoteByPos(int pos);

    @Query("SELECT * FROM note_type WHERE noteTypeId= :id")
    NoteType getNoteById(int id);

    
}
