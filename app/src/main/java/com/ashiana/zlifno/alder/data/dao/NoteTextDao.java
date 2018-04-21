package com.ashiana.zlifno.alder.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.ashiana.zlifno.alder.data.entity.NoteText;

import static android.arch.persistence.room.OnConflictStrategy.IGNORE;
import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface NoteTextDao {

    @Insert(onConflict = IGNORE)
    void insertNote(NoteText noteText);

    @Update(onConflict = REPLACE)
    void updateNote(NoteText noteText);

    @Delete()
    void deleteNote(NoteText noteText);

    @Query("DELETE FROM note_text WHERE noteTextId = (:noteTextId)")
    void deleteNoteById(int noteTextId);

    @Query("SELECT * FROM note_text WHERE noteTextId = (:noteTextId)")
    NoteText getNoteById(int noteTextId);
}
